"""
Cliente de escritorio (Semillas + Proveedores) para Inventario Agrícola V2.

Requisitos:
  - Python 3.10+
  - pip install -r requirements.txt  (requests)

Ejecución:
  python -m venv .venv
  .\.venv\Scripts\Activate
  pip install -r requirements.txt
  python main.py
"""

import json
import tkinter as tk
from tkinter import ttk, messagebox
from datetime import datetime
import requests

# ========================= CONFIG =========================
BASE_URL_SEMILLAS = "http://localhost:8080/api/v1/semillas"
BASE_URL_PROVEEDORES = "http://localhost:8080/api/v1/proveedores"
ISO_FMT = "%Y-%m-%dT%H:%M:%S"
TIMEOUT = 6  # segundos

# ===================== UTILIDADES GENERALES =====================
def to_iso(dt: datetime) -> str:
    return dt.strftime(ISO_FMT)

def normalize_datetime_input(s: str) -> str:
    """
    Acepta:
      - "" (vacío) -> ahora (ISO)
      - "YYYY-MM-DD"
      - "YYYY-MM-DD HH:MM"
      - "YYYY-MM-DDTHH:MM"
      - "YYYY-MM-DDTHH:MM:SS"
    y devuelve siempre "YYYY-MM-DDTHH:MM:SS".
    """
    s = (s or "").strip()
    if not s:
        return to_iso(datetime.now())
    s = s.replace(" ", "T")
    if len(s) == 16:  # YYYY-MM-DDTHH:MM
        s = f"{s}:00"
    elif len(s) == 10:  # YYYY-MM-DD
        s = f"{s}T00:00:00"
    datetime.strptime(s, ISO_FMT)  # lanza ValueError si no cumple
    return s

def show_error(title: str, message: str):
    messagebox.showerror(title, message)

def pretty_out(win: tk.Toplevel | tk.Tk) -> tk.Text:
    txt = tk.Text(win, height=10, width=70, state=tk.DISABLED)
    txt.grid(row=99, column=0, columnspan=3, padx=6, pady=8, sticky="nsew")
    return txt

def set_text(widget: tk.Text, payload):
    widget.config(state=tk.NORMAL)
    widget.delete(1.0, tk.END)
    widget.insert(
        tk.END,
        json.dumps(payload, indent=2, ensure_ascii=False)
        if not isinstance(payload, str) else payload
    )
    widget.config(state=tk.DISABLED)

def safe_json(resp: requests.Response):
    try:
        return resp.json()
    except Exception:
        return {"raw": resp.text}

# ---------- Selector de fecha/hora ----------
def open_datetime_picker(entry: tk.Entry):
    """Pequeño diálogo con SpinBoxes para elegir fecha y hora."""
    now = datetime.now()
    top = tk.Toplevel()
    top.title("Seleccionar fecha y hora")
    top.resizable(False, False)

    # Año-Mes-Día
    tk.Label(top, text="Año").grid(row=0, column=0, padx=6, pady=4, sticky="e")
    sp_year = tk.Spinbox(top, from_=1970, to=2100, width=6)
    sp_year.grid(row=0, column=1, padx=4, pady=4, sticky="w")
    sp_year.delete(0, tk.END); sp_year.insert(0, now.year)

    tk.Label(top, text="Mes").grid(row=0, column=2, padx=6, pady=4, sticky="e")
    sp_mon = tk.Spinbox(top, from_=1, to=12, width=4)
    sp_mon.grid(row=0, column=3, padx=4, pady=4, sticky="w")
    sp_mon.delete(0, tk.END); sp_mon.insert(0, now.month)

    tk.Label(top, text="Día").grid(row=0, column=4, padx=6, pady=4, sticky="e")
    sp_day = tk.Spinbox(top, from_=1, to=31, width=4)
    sp_day.grid(row=0, column=5, padx=4, pady=4, sticky="w")
    sp_day.delete(0, tk.END); sp_day.insert(0, now.day)

    # Hora-Minuto-Segundo
    tk.Label(top, text="Hora").grid(row=1, column=0, padx=6, pady=4, sticky="e")
    sp_h = tk.Spinbox(top, from_=0, to=23, width=4)
    sp_h.grid(row=1, column=1, padx=4, pady=4, sticky="w")
    sp_h.delete(0, tk.END); sp_h.insert(0, now.hour)

    tk.Label(top, text="Min").grid(row=1, column=2, padx=6, pady=4, sticky="e")
    sp_m = tk.Spinbox(top, from_=0, to=59, width=4)
    sp_m.grid(row=1, column=3, padx=4, pady=4, sticky="w")
    sp_m.delete(0, tk.END); sp_m.insert(0, now.minute)

    tk.Label(top, text="Seg").grid(row=1, column=4, padx=6, pady=4, sticky="e")
    sp_s = tk.Spinbox(top, from_=0, to=59, width=4)
    sp_s.grid(row=1, column=5, padx=4, pady=4, sticky="w")
    sp_s.delete(0, tk.END); sp_s.insert(0, now.second)

    def set_hoy():
        d = datetime.now()
        sp_year.delete(0, tk.END); sp_year.insert(0, d.year)
        sp_mon.delete(0, tk.END);  sp_mon.insert(0, d.month)
        sp_day.delete(0, tk.END);  sp_day.insert(0, d.day)

    def set_ahora():
        d = datetime.now()
        set_hoy()
        sp_h.delete(0, tk.END); sp_h.insert(0, d.hour)
        sp_m.delete(0, tk.END); sp_m.insert(0, d.minute)
        sp_s.delete(0, tk.END); sp_s.insert(0, d.second)

    def aceptar():
        try:
            y = int(sp_year.get()); mo = int(sp_mon.get()); da = int(sp_day.get())
            h = int(sp_h.get()); mi = int(sp_m.get()); se = int(sp_s.get())
            val = datetime(y, mo, da, h, mi, se)
            entry.delete(0, tk.END)
            entry.insert(0, to_iso(val))
            top.destroy()
        except Exception as ex:
            messagebox.showerror("Fecha inválida", str(ex))

    frm_btn = tk.Frame(top)
    frm_btn.grid(row=2, column=0, columnspan=6, pady=8)
    tk.Button(frm_btn, text="Hoy", command=set_hoy).pack(side="left", padx=4)
    tk.Button(frm_btn, text="Ahora", command=set_ahora).pack(side="left", padx=4)
    tk.Button(frm_btn, text="Aceptar", command=aceptar).pack(side="left", padx=4)
    tk.Button(frm_btn, text="Cancelar", command=top.destroy).pack(side="left", padx=4)

    top.transient()
    top.grab_set()
    entry.focus_set()

# --------- Auxiliar: validar existencia de proveedor ---------
def proveedor_exists(nit: str) -> bool:
    nit = (nit or "").strip()
    if not nit:
        return False
    try:
        r = requests.get(f"{BASE_URL_PROVEEDORES}/{nit}", timeout=TIMEOUT)
        return r.status_code == 200
    except Exception:
        return False

# ========================= SEMILLAS =========================
def semillas_create_window():
    win = tk.Toplevel()
    win.title("Semillas - Crear")
    win.geometry("560x560")

    labels = [
        "Código", "Nombre", "Precio", "Stock", "Tipo de Semilla",
        "% Germinación", "Proveedor NIT", "Fecha Ingreso"
    ]
    entries = []
    for i, label in enumerate(labels):
        tk.Label(win, text=label).grid(row=i, column=0, sticky="w", padx=6, pady=6)
        if label == "Fecha Ingreso":
            wrap = tk.Frame(win)
            wrap.grid(row=i, column=1, padx=6, pady=6, sticky="w")
            e = tk.Entry(wrap, width=24)
            e.pack(side="left")
            tk.Button(wrap, text="Seleccionar...", command=lambda ent=e: open_datetime_picker(ent)).pack(side="left", padx=6)
        elif label == "% Germinación":
            e = tk.Spinbox(win, from_=0, to=100, increment=0.1, width=10)
            e.grid(row=i, column=1, padx=6, pady=6, sticky="w")
            e.delete(0, tk.END); e.insert(0, "90.0")
        else:
            e = tk.Entry(win, width=30)
            e.grid(row=i, column=1, padx=6, pady=6)
        entries.append(e)

    out = pretty_out(win)

    def enviar():
        try:
            fecha_norm = normalize_datetime_input(entries[7].get())
        except ValueError as ve:
            return show_error("Fecha inválida", str(ve))

        try:
            germ = float(entries[5].get() or 0)
        except ValueError:
            return show_error("Dato inválido", "% Germinación debe ser numérico")
        if not (0 <= germ <= 100):
            return show_error("Dato inválido", "% Germinación debe estar entre 0 y 100")

        nit = entries[6].get().strip()
        if not nit:
            return show_error("Dato requerido", "Proveedor NIT es obligatorio")
        if not proveedor_exists(nit):
            return show_error("Proveedor no existe",
                              f"El NIT {nit} no está registrado.\n\n" +
                              "Primero crea el proveedor (menú Proveedores) o en el cliente web.")

        data = {
            "codigo": entries[0].get().strip(),
            "nombre": entries[1].get().strip(),
            "precio": float(entries[2].get() or 0),
            "stock": int(entries[3].get() or 0),
            "tipoSemilla": entries[4].get().strip(),
            "porcentajeGerminacion": germ,
            "proveedorNit": nit,
            "fechaIngreso": fecha_norm
        }
        if not data["codigo"]:
            return show_error("Dato requerido", "El código es obligatorio")

        try:
            r = requests.post(BASE_URL_SEMILLAS, json=data, timeout=TIMEOUT)
            if r.status_code in (200, 201):
                messagebox.showinfo("Éxito", "Semilla creada")
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as e:
            show_error("Error", str(e))

    tk.Button(win, text="Crear", command=enviar, bg="#16a34a", fg="white").grid(row=len(labels), column=0, columnspan=2, pady=10)

def semillas_search_window():
    win = tk.Toplevel()
    win.title("Semillas - Buscar por código")
    win.geometry("520x280")

    tk.Label(win, text="Código").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_codigo = tk.Entry(win, width=30); e_codigo.grid(row=0, column=1, padx=6, pady=6)
    out = pretty_out(win)

    def buscar():
        codigo = e_codigo.get().strip()
        if not codigo:
            return show_error("Dato requerido", "Ingrese el código")
        try:
            r = requests.get(f"{BASE_URL_SEMILLAS}/{codigo}", timeout=TIMEOUT)
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as e:
            show_error("Error", str(e))

    tk.Button(win, text="Buscar", command=buscar, bg="#2563eb", fg="white").grid(row=1, column=0, columnspan=2, pady=8)

def semillas_update_window():
    win = tk.Toplevel()
    win.title("Semillas - Actualizar")
    win.geometry("560x600")

    labels = [
        "Código (ruta)", "Nombre", "Precio", "Stock", "Tipo de Semilla",
        "% Germinación", "Proveedor NIT", "Fecha Ingreso"
    ]
    e = []
    for i, lab in enumerate(labels):
        tk.Label(win, text=lab).grid(row=i, column=0, padx=6, pady=6, sticky="w")
        if lab == "Fecha Ingreso":
            wrap = tk.Frame(win)
            wrap.grid(row=i, column=1, padx=6, pady=6, sticky="w")
            inp = tk.Entry(wrap, width=24)
            inp.pack(side="left")
            tk.Button(wrap, text="Seleccionar...", command=lambda ent=inp: open_datetime_picker(ent)).pack(side="left", padx=6)
        elif lab == "% Germinación":
            inp = tk.Spinbox(win, from_=0, to=100, increment=0.1, width=10)
            inp.grid(row=i, column=1, padx=6, pady=6, sticky="w")
        else:
            inp = tk.Entry(win, width=32)
            inp.grid(row=i, column=1, padx=6, pady=6)
        e.append(inp)
    out = pretty_out(win)

    def actualizar():
        codigo = e[0].get().strip()
        if not codigo:
            return show_error("Dato requerido", "Ingrese el código (ruta)")

        try:
            fecha_norm = normalize_datetime_input(e[7].get())
        except ValueError as ve:
            return show_error("Fecha inválida", str(ve))

        try:
            germ = float(e[5].get() or 0)
        except ValueError:
            return show_error("Dato inválido", "% Germinación debe ser numérico")
        if not (0 <= germ <= 100):
            return show_error("Dato inválido", "% Germinación debe estar entre 0 y 100")

        nit = e[6].get().strip()
        if not nit:
            return show_error("Dato requerido", "Proveedor NIT es obligatorio")
        if not proveedor_exists(nit):
            return show_error("Proveedor no existe",
                              f"El NIT {nit} no está registrado.\n\n" +
                              "Primero crea el proveedor (menú Proveedores) o en el cliente web.")

        body = {
            "codigo": codigo,
            "nombre": e[1].get().strip(),
            "precio": float(e[2].get() or 0),
            "stock": int(e[3].get() or 0),
            "tipoSemilla": e[4].get().strip(),
            "porcentajeGerminacion": germ,
            "proveedorNit": nit,
            "fechaIngreso": fecha_norm
        }

        try:
            r = requests.put(f"{BASE_URL_SEMILLAS}/{codigo}", json=body, timeout=TIMEOUT)
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as ex:
            show_error("Error", str(ex))

    tk.Button(win, text="Actualizar", command=actualizar, bg="#f59e0b").grid(row=len(labels), column=0, columnspan=2, pady=10)

def semillas_delete_window():
    win = tk.Toplevel()
    win.title("Semillas - Eliminar")
    win.geometry("520x240")

    tk.Label(win, text="Código").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_codigo = tk.Entry(win, width=30); e_codigo.grid(row=0, column=1, padx=6, pady=6)
    out = pretty_out(win)

    def eliminar():
        codigo = e_codigo.get().strip()
        if not codigo:
            return show_error("Dato requerido", "Ingrese el código")
        if messagebox.askyesno("Confirmar", f"¿Eliminar semilla {codigo}?"):
            try:
                r = requests.delete(f"{BASE_URL_SEMILLAS}/{codigo}", timeout=TIMEOUT)
                set_text(out, {"status": r.status_code, "data": r.text if r.text else "(sin cuerpo)"})
            except Exception as e:
                show_error("Error", str(e))

    tk.Button(win, text="Eliminar", command=eliminar, bg="#ef4444", fg="white").grid(row=1, column=0, columnspan=2, pady=8)

def semillas_list_window():
    win = tk.Toplevel()
    win.title("Semillas - Listar/Filtrar")
    win.geometry("980x520")

    # Filtros
    tk.Label(win, text="Tipo").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_tipo = tk.Entry(win, width=18); e_tipo.grid(row=0, column=1, padx=6, pady=6, sticky="w")

    tk.Label(win, text="% Germinación Min").grid(row=0, column=2, padx=6, pady=6, sticky="w")
    e_germ = tk.Entry(win, width=12); e_germ.grid(row=0, column=3, padx=6, pady=6, sticky="w")

    tk.Label(win, text="Desde").grid(row=1, column=0, padx=6, pady=6, sticky="w")
    wrap_desde = tk.Frame(win); wrap_desde.grid(row=1, column=1, padx=6, pady=6, sticky="w")
    e_desde = tk.Entry(wrap_desde, width=20); e_desde.pack(side="left")
    tk.Button(wrap_desde, text="Seleccionar...", command=lambda ent=e_desde: open_datetime_picker(ent)).pack(side="left", padx=6)

    tk.Label(win, text="Hasta").grid(row=1, column=2, padx=6, pady=6, sticky="w")
    wrap_hasta = tk.Frame(win); wrap_hasta.grid(row=1, column=3, padx=6, pady=6, sticky="w")
    e_hasta = tk.Entry(wrap_hasta, width=20); e_hasta.pack(side="left")
    tk.Button(wrap_hasta, text="Seleccionar...", command=lambda ent=e_hasta: open_datetime_picker(ent)).pack(side="left", padx=6)

    columns = ("codigo", "nombre", "precio", "stock", "tipo", "germinacion", "fechaIngreso", "proveedor")
    tree = ttk.Treeview(win, columns=columns, show="headings", height=12)
    for col in columns:
        tree.heading(col, text=col)
        tree.column(col, width=115, anchor="center")
    tree.grid(row=3, column=0, columnspan=6, padx=6, pady=8, sticky="nsew")

    sb = ttk.Scrollbar(win, orient="vertical", command=tree.yview)
    tree.configure(yscrollcommand=sb.set); sb.grid(row=3, column=6, sticky="ns")

    def listar():
        params = {}
        if e_tipo.get().strip():
            params["tipo"] = e_tipo.get().strip()
        if e_germ.get().strip():
            try:
                float(e_germ.get().strip())
                params["germinacionMin"] = e_germ.get().strip()
            except ValueError:
                return show_error("Formato", "Germinación mínima debe ser numérica.")
        if e_desde.get().strip():
            try:
                params["desde"] = normalize_datetime_input(e_desde.get().strip())
            except ValueError as ve:
                return show_error("Fecha 'Desde' inválida", str(ve))
        if e_hasta.get().strip():
            try:
                params["hasta"] = normalize_datetime_input(e_hasta.get().strip())
            except ValueError as ve:
                return show_error("Fecha 'Hasta' inválida", str(ve))

        try:
            r = requests.get(BASE_URL_SEMILLAS, params=params, timeout=TIMEOUT)
            data = safe_json(r) if r.status_code == 200 else []
            for it in tree.get_children():
                tree.delete(it)
            for s in data:
                tree.insert("", tk.END, values=(
                    s.get("codigo"), s.get("nombre"), s.get("precio"), s.get("stock"),
                    s.get("tipoSemilla"), s.get("porcentajeGerminacion"),
                    s.get("fechaIngreso"), s.get("proveedorNit")
                ))
        except Exception as e:
            show_error("Error", str(e))

    tk.Button(win, text="Listar", command=listar).grid(row=2, column=0, columnspan=6, pady=6)

# ========================= PROVEEDORES (CRUD) =========================
def proveedores_create_window():
    win = tk.Toplevel()
    win.title("Proveedores - Crear")
    win.geometry("540x420")

    tk.Label(win, text="NIT").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_nit = tk.Entry(win, width=28); e_nit.grid(row=0, column=1, padx=6, pady=6)

    tk.Label(win, text="Nombre").grid(row=1, column=0, padx=6, pady=6, sticky="w")
    e_nom = tk.Entry(win, width=28); e_nom.grid(row=1, column=1, padx=6, pady=6)

    tk.Label(win, text="Ciudad").grid(row=2, column=0, padx=6, pady=6, sticky="w")
    e_ciu = tk.Entry(win, width=28); e_ciu.grid(row=2, column=1, padx=6, pady=6)

    tk.Label(win, text="Teléfono").grid(row=3, column=0, padx=6, pady=6, sticky="w")
    e_tel = tk.Entry(win, width=28); e_tel.grid(row=3, column=1, padx=6, pady=6)

    tk.Label(win, text="Fecha Registro").grid(row=4, column=0, padx=6, pady=6, sticky="w")
    wrap = tk.Frame(win); wrap.grid(row=4, column=1, padx=6, pady=6, sticky="w")
    e_fecha = tk.Entry(wrap, width=22); e_fecha.pack(side="left")
    tk.Button(wrap, text="Seleccionar...", command=lambda ent=e_fecha: open_datetime_picker(ent)).pack(side="left", padx=6)

    var_act = tk.BooleanVar(value=True)
    tk.Checkbutton(win, text="Activo", variable=var_act).grid(row=5, column=1, padx=6, pady=6, sticky="w")

    out = pretty_out(win)

    def crear():
        nit = e_nit.get().strip()
        if not nit:
            return show_error("Dato requerido", "NIT es obligatorio")

        try:
            fecha = normalize_datetime_input(e_fecha.get())
        except ValueError as ve:
            return show_error("Fecha inválida", str(ve))

        nombre = e_nom.get().strip() or "Proveedor"
        ciudad = e_ciu.get().strip() or "Ciudad"
        telefono = e_tel.get().strip() or "0000000000"

        data = {
            "nit": nit,
            "nombre": nombre,
            "ciudad": ciudad,
            "telefono": telefono,
            "fechaRegistro": fecha,
            "activo": bool(var_act.get())
        }

        try:
            r = requests.post(BASE_URL_PROVEEDORES, json=data, timeout=TIMEOUT)
            if r.status_code in (200, 201):
                messagebox.showinfo("Éxito", "Proveedor creado")
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as ex:
            show_error("Error", str(ex))

    tk.Button(win, text="Crear", command=crear, bg="#16a34a", fg="white").grid(row=6, column=0, columnspan=2, pady=10)

def proveedores_search_window():
    win = tk.Toplevel()
    win.title("Proveedores - Buscar por NIT")
    win.geometry("520x260")

    tk.Label(win, text="NIT").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_nit = tk.Entry(win, width=30); e_nit.grid(row=0, column=1, padx=6, pady=6)

    out = pretty_out(win)

    def buscar():
        nit = e_nit.get().strip()
        if not nit:
            return show_error("Dato requerido", "Ingrese el NIT")
        try:
            r = requests.get(f"{BASE_URL_PROVEEDORES}/{nit}", timeout=TIMEOUT)
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as ex:
            show_error("Error", str(ex))

    tk.Button(win, text="Buscar", command=buscar, bg="#2563eb", fg="white").grid(row=1, column=0, columnspan=2, pady=8)

def proveedores_update_window():
    win = tk.Toplevel()
    win.title("Proveedores - Actualizar")
    win.geometry("560x480")

    labels = ["NIT (ruta)", "Nombre", "Ciudad", "Teléfono", "Fecha Registro", "Activo"]
    e = []
    for i, lab in enumerate(labels):
        tk.Label(win, text=lab).grid(row=i, column=0, padx=6, pady=6, sticky="w")
        if lab == "Fecha Registro":
            wrap = tk.Frame(win); wrap.grid(row=i, column=1, padx=6, pady=6, sticky="w")
            inp = tk.Entry(wrap, width=22); inp.pack(side="left")
            tk.Button(wrap, text="Seleccionar...", command=lambda ent=inp: open_datetime_picker(ent)).pack(side="left", padx=6)
        elif lab == "Activo":
            inp = tk.BooleanVar(value=True)
            tk.Checkbutton(win, text="Activo", variable=inp).grid(row=i, column=1, padx=6, pady=6, sticky="w")
        else:
            inp = tk.Entry(win, width=30)
            inp.grid(row=i, column=1, padx=6, pady=6, sticky="w")
        e.append(inp)

    out = pretty_out(win)

    def actualizar():
        nit = e[0].get().strip()
        if not nit:
            return show_error("Dato requerido", "Ingrese el NIT (ruta)")
        try:
            fecha = normalize_datetime_input(e[4].get())
        except ValueError as ve:
            return show_error("Fecha inválida", str(ve))

        nombre = e[1].get().strip() or "Proveedor"
        ciudad = e[2].get().strip() or "Ciudad"
        telefono = e[3].get().strip() or "0000000000"
        activo = bool(e[5].get()) if isinstance(e[5], tk.BooleanVar) else True

        body = {
            "nit": nit,
            "nombre": nombre,
            "ciudad": ciudad,
            "telefono": telefono,
            "fechaRegistro": fecha,
            "activo": activo
        }
        try:
            r = requests.put(f"{BASE_URL_PROVEEDORES}/{nit}", json=body, timeout=TIMEOUT)
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as ex:
            show_error("Error", str(ex))

    tk.Button(win, text="Actualizar", command=actualizar, bg="#f59e0b").grid(row=len(labels), column=0, columnspan=2, pady=10)

def proveedores_delete_window():
    win = tk.Toplevel()
    win.title("Proveedores - Eliminar")
    win.geometry("520x240")

    tk.Label(win, text="NIT").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_nit = tk.Entry(win, width=30); e_nit.grid(row=0, column=1, padx=6, pady=6)

    out = pretty_out(win)

    def eliminar():
        nit = e_nit.get().strip()
        if not nit:
            return show_error("Dato requerido", "Ingrese el NIT")
        if messagebox.askyesno("Confirmar", f"¿Eliminar proveedor {nit}?"):
            try:
                r = requests.delete(f"{BASE_URL_PROVEEDORES}/{nit}", timeout=TIMEOUT)
                set_text(out, {"status": r.status_code, "data": r.text if r.text else "(sin cuerpo)"})
            except Exception as ex:
                show_error("Error", str(ex))

    tk.Button(win, text="Eliminar", command=eliminar, bg="#ef4444", fg="white").grid(row=1, column=0, columnspan=2, pady=8)

def proveedores_list_window():
    win = tk.Toplevel()
    win.title("Proveedores - Listar/Filtrar")
    win.geometry("900x480")

    tk.Label(win, text="Nombre").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_nombre = tk.Entry(win, width=18); e_nombre.grid(row=0, column=1, padx=6, pady=6)

    tk.Label(win, text="Ciudad").grid(row=0, column=2, padx=6, pady=6, sticky="w")
    e_ciudad = tk.Entry(win, width=18); e_ciudad.grid(row=0, column=3, padx=6, pady=6)

    var_act = tk.BooleanVar(value=False)
    tk.Checkbutton(win, text="Solo activos", variable=var_act).grid(row=1, column=1, padx=6, pady=6, sticky="w")

    columns = ("nit", "nombre", "ciudad", "telefono", "fechaRegistro", "activo")
    tree = ttk.Treeview(win, columns=columns, show="headings", height=12)
    for col in columns:
        tree.heading(col, text=col)
        tree.column(col, width=130, anchor="center")
    tree.grid(row=3, column=0, columnspan=5, padx=6, pady=8, sticky="nsew")

    sb = ttk.Scrollbar(win, orient="vertical", command=tree.yview)
    tree.configure(yscrollcommand=sb.set); sb.grid(row=3, column=5, sticky="ns")

    def listar():
        params = {}
        nombre = e_nombre.get().strip()
        ciudad = e_ciudad.get().strip()
        if nombre:
            params["nombre"] = nombre
        else:
            if ciudad:
                params["ciudad"] = ciudad
            if var_act.get():
                params["activo"] = "true"
        try:
            r = requests.get(BASE_URL_PROVEEDORES, params=params, timeout=TIMEOUT)
            data = safe_json(r) if r.status_code == 200 else []
            for it in tree.get_children():
                tree.delete(it)
            for p in data:
                tree.insert("", tk.END, values=(
                    p.get("nit"), p.get("nombre"), p.get("ciudad"),
                    p.get("telefono"), p.get("fechaRegistro"),
                    p.get("activo")
                ))
        except Exception as ex:
            show_error("Error", str(ex))

    tk.Button(win, text="Listar", command=listar).grid(row=2, column=0, columnspan=5, pady=6)

# ============================ UI ROOT ============================
def main():
    root = tk.Tk()
    root.title("Inventario Agrícola V2 - Cliente de Escritorio")
    root.geometry("820x560")

    menubar = tk.Menu(root)

    # Menú Semillas
    m_sem = tk.Menu(menubar, tearoff=0)
    m_sem.add_command(label="Crear", command=semillas_create_window)
    m_sem.add_command(label="Buscar", command=semillas_search_window)
    m_sem.add_command(label="Actualizar", command=semillas_update_window)
    m_sem.add_command(label="Eliminar", command=semillas_delete_window)
    m_sem.add_command(label="Listar/Filtrar", command=semillas_list_window)
    menubar.add_cascade(label="Semillas", menu=m_sem)

    # Menú Proveedores
    m_prov = tk.Menu(menubar, tearoff=0)
    m_prov.add_command(label="Crear", command=proveedores_create_window)
    m_prov.add_command(label="Buscar", command=proveedores_search_window)
    m_prov.add_command(label="Actualizar", command=proveedores_update_window)
    m_prov.add_command(label="Eliminar", command=proveedores_delete_window)
    m_prov.add_command(label="Listar/Filtrar", command=proveedores_list_window)
    menubar.add_cascade(label="Proveedores", menu=m_prov)

    # Ayuda
    m_help = tk.Menu(menubar, tearoff=0)
    m_help.add_command(label="Acerca de...", command=lambda: messagebox.showinfo(
        "Acerca de",
        "Inventario Agrícola V2 - Cliente Tkinter\n\n"
        "Formato de fecha: YYYY-MM-DD, YYYY-MM-DD HH:MM o completo YYYY-MM-DDTHH:MM:SS\n"
        "Usa 'Seleccionar...' para elegir fecha/hora."
    ))
    menubar.add_cascade(label="Ayuda", menu=m_help)

    root.config(menu=menubar)

    tk.Label(root, text="Cliente Inventario Agrícola V2 — Semillas & Proveedores", font=("Helvetica", 16)).pack(pady=20)
    tk.Label(root, text=f"Semillas: {BASE_URL_SEMILLAS}", fg="#555").pack()
    tk.Label(root, text=f"Proveedores: {BASE_URL_PROVEEDORES}", fg="#555").pack()

    root.mainloop()

if __name__ == "__main__":
    main()
