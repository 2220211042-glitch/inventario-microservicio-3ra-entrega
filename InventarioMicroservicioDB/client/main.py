r"""
Cliente de escritorio en Python/Tkinter para consumir el microservicio
Inventario Agrícola V2. Incluye operaciones CRUD y listados con filtros
para Semillas y Proveedores.

Requisitos:
  - Python 3.10+
  - requests (pip install -r requirements.txt)

Ejecución:
  python -m venv .venv
  .\.venv\Scripts\Activate   (Windows PowerShell)
  pip install -r requirements.txt
  python main.py
"""

import json
import tkinter as tk
from tkinter import ttk, messagebox
from datetime import datetime
import requests

# ========================= CONFIG =========================
# *** OJO: puerto 8080 como pediste ***
BASE_URL_SEMILLAS = "http://localhost:8080/api/v1/semillas"
BASE_URL_PROVEEDORES = "http://localhost:8080/api/v1/proveedores"
ISO_FMT = "%Y-%m-%dT%H:%M:%S"
TIMEOUT = 6  # segundos

# ===================== UTILIDADES GENERALES =====================
def show_error(title: str, message: str):
    messagebox.showerror(title, message)

def is_iso_datetime(s: str) -> bool:
    try:
        datetime.strptime(s, ISO_FMT)
        return True
    except ValueError:
        return False

def pretty_out(win: tk.Toplevel | tk.Tk) -> tk.Text:
    txt = tk.Text(win, height=10, width=70, state=tk.DISABLED)
    txt.grid(row=99, column=0, columnspan=2, padx=6, pady=8, sticky="nsew")
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

# ========================= SEMILLAS =========================
def semillas_create_window():
    win = tk.Toplevel()
    win.title("Semillas - Crear")
    win.geometry("500x520")

    labels = [
        "Código", "Nombre", "Precio", "Stock", "Tipo de Semilla",
        "% Germinación", "Proveedor NIT", "Fecha Ingreso (YYYY-MM-DDTHH:MM:SS)"
    ]
    entries = []
    for i, label in enumerate(labels):
        tk.Label(win, text=label).grid(row=i, column=0, sticky="w", padx=6, pady=6)
        e = tk.Entry(win, width=30)
        e.grid(row=i, column=1, padx=6, pady=6)
        entries.append(e)

    out = pretty_out(win)

    def enviar():
        data = {
            "codigo": entries[0].get().strip(),
            "nombre": entries[1].get().strip(),
            "precio": float(entries[2].get() or 0),
            "stock": int(entries[3].get() or 0),
            "tipoSemilla": entries[4].get().strip(),
            "porcentajeGerminacion": float(entries[5].get() or 0),
            "proveedorNit": entries[6].get().strip(),
            "fechaIngreso": entries[7].get().strip()
        }
        if not data["codigo"]:
            return show_error("Dato requerido", "El código es obligatorio")
        if data["fechaIngreso"] and not is_iso_datetime(data["fechaIngreso"]):
            return show_error("Formato fecha", f"La fecha debe ser ISO: {ISO_FMT}")

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
    win.geometry("520x560")

    labels = [
        "Código (ruta)", "Nombre", "Precio", "Stock", "Tipo de Semilla",
        "% Germinación", "Proveedor NIT", "Fecha Ingreso (YYYY-MM-DDTHH:MM:SS)"
    ]
    e = []
    for i, lab in enumerate(labels):
        tk.Label(win, text=lab).grid(row=i, column=0, padx=6, pady=6, sticky="w")
        inp = tk.Entry(win, width=32)
        inp.grid(row=i, column=1, padx=6, pady=6)
        e.append(inp)
    out = pretty_out(win)

    def actualizar():
        codigo = e[0].get().strip()
        if not codigo:
            return show_error("Dato requerido", "Ingrese el código (ruta)")
        body = {
            "codigo": codigo,
            "nombre": e[1].get().strip(),
            "precio": float(e[2].get() or 0),
            "stock": int(e[3].get() or 0),
            "tipoSemilla": e[4].get().strip(),
            "porcentajeGerminacion": float(e[5].get() or 0),
            "proveedorNit": e[6].get().strip(),
            "fechaIngreso": e[7].get().strip()
        }
        if body["fechaIngreso"] and not is_iso_datetime(body["fechaIngreso"]):
            return show_error("Formato fecha", f"La fecha debe ser ISO: {ISO_FMT}")

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
    win.geometry("900x480")

    # Filtros
    tk.Label(win, text="Tipo").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_tipo = tk.Entry(win, width=18); e_tipo.grid(row=0, column=1, padx=6, pady=6)

    tk.Label(win, text="Germinación Min").grid(row=0, column=2, padx=6, pady=6, sticky="w")
    e_germ = tk.Entry(win, width=12); e_germ.grid(row=0, column=3, padx=6, pady=6)

    tk.Label(win, text="Desde (ISO)").grid(row=1, column=0, padx=6, pady=6, sticky="w")
    e_desde = tk.Entry(win, width=18); e_desde.grid(row=1, column=1, padx=6, pady=6)

    tk.Label(win, text="Hasta (ISO)").grid(row=1, column=2, padx=6, pady=6, sticky="w")
    e_hasta = tk.Entry(win, width=18); e_hasta.grid(row=1, column=3, padx=6, pady=6)

    columns = ("codigo", "nombre", "precio", "stock", "tipo", "germinacion", "fechaIngreso", "proveedor")
    tree = ttk.Treeview(win, columns=columns, show="headings", height=12)
    for col in columns:
        tree.heading(col, text=col)
        tree.column(col, width=110, anchor="center")
    tree.grid(row=3, column=0, columnspan=5, padx=6, pady=8, sticky="nsew")

    sb = ttk.Scrollbar(win, orient="vertical", command=tree.yview)
    tree.configure(yscrollcommand=sb.set); sb.grid(row=3, column=5, sticky="ns")

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
            if not is_iso_datetime(e_desde.get().strip()):
                return show_error("Formato", f"'Desde' debe ser ISO: {ISO_FMT}")
            params["desde"] = e_desde.get().strip()
        if e_hasta.get().strip():
            if not is_iso_datetime(e_hasta.get().strip()):
                return show_error("Formato", f"'Hasta' debe ser ISO: {ISO_FMT}")
            params["hasta"] = e_hasta.get().strip()

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

    tk.Button(win, text="Listar", command=listar).grid(row=2, column=0, columnspan=5, pady=6)

# ========================= PROVEEDORES =========================
def proveedores_create_window():
    win = tk.Toplevel()
    win.title("Proveedores - Crear")
    win.geometry("500x460")

    labels = [
        "NIT", "Nombre", "Ciudad", "Teléfono",
        "Fecha Registro (YYYY-MM-DDTHH:MM:SS)", "Activo (true/false)"
    ]
    e = []
    for i, lab in enumerate(labels):
        tk.Label(win, text=lab).grid(row=i, column=0, padx=6, pady=6, sticky="w")
        inp = tk.Entry(win, width=32)
        inp.grid(row=i, column=1, padx=6, pady=6)
        e.append(inp)
    out = pretty_out(win)

    def enviar():
        body = {
            "nit": e[0].get().strip(),
            "nombre": e[1].get().strip(),
            "ciudad": e[2].get().strip(),
            "telefono": e[3].get().strip(),
            "fechaRegistro": e[4].get().strip(),
            "activo": e[5].get().strip().lower() == "true"
        }
        if not body["nit"]:
            return show_error("Dato requerido", "El NIT es obligatorio")
        if body["fechaRegistro"] and not is_iso_datetime(body["fechaRegistro"]):
            return show_error("Formato fecha", f"La fecha debe ser ISO: {ISO_FMT}")

        try:
            r = requests.post(BASE_URL_PROVEEDORES, json=body, timeout=TIMEOUT)
            if r.status_code in (200, 201):
                messagebox.showinfo("Éxito", "Proveedor creado")
            set_text(out, {"status": r.status_code, "data": safe_json(r)})
        except Exception as ex:
            show_error("Error", str(ex))

    tk.Button(win, text="Crear", command=enviar, bg="#16a34a", fg="white").grid(row=len(labels), column=0, columnspan=2, pady=10)

def proveedores_search_window():
    win = tk.Toplevel()
    win.title("Proveedores - Buscar por NIT")
    win.geometry("520x280")

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
        except Exception as e:
            show_error("Error", str(e))

    tk.Button(win, text="Buscar", command=buscar, bg="#2563eb", fg="white").grid(row=1, column=0, columnspan=2, pady=8)

def proveedores_update_window():
    win = tk.Toplevel()
    win.title("Proveedores - Actualizar")
    win.geometry("520x520")

    labels = [
        "NIT (ruta)", "Nombre", "Ciudad", "Teléfono",
        "Fecha Registro (YYYY-MM-DDTHH:MM:SS)", "Activo (true/false)"
    ]
    e = []
    for i, lab in enumerate(labels):
        tk.Label(win, text=lab).grid(row=i, column=0, padx=6, pady=6, sticky="w")
        inp = tk.Entry(win, width=32)
        inp.grid(row=i, column=1, padx=6, pady=6)
        e.append(inp)
    out = pretty_out(win)

    def actualizar():
        nit = e[0].get().strip()
        if not nit:
            return show_error("Dato requerido", "Ingrese el NIT (ruta)")
        body = {
            "nit": nit,
            "nombre": e[1].get().strip(),
            "ciudad": e[2].get().strip(),
            "telefono": e[3].get().strip(),
            "fechaRegistro": e[4].get().strip(),
            "activo": e[5].get().strip().lower() == "true"
        }
        if body["fechaRegistro"] and not is_iso_datetime(body["fechaRegistro"]):
            return show_error("Formato fecha", f"La fecha debe ser ISO: {ISO_FMT}")

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
            except Exception as e:
                show_error("Error", str(e))

    tk.Button(win, text="Eliminar", command=eliminar, bg="#ef4444", fg="white").grid(row=1, column=0, columnspan=2, pady=8)

def proveedores_list_window():
    win = tk.Toplevel()
    win.title("Proveedores - Listar/Filtrar")
    win.geometry("900x480")

    tk.Label(win, text="Nombre").grid(row=0, column=0, padx=6, pady=6, sticky="w")
    e_nombre = tk.Entry(win, width=18); e_nombre.grid(row=0, column=1, padx=6, pady=6)

    tk.Label(win, text="Ciudad").grid(row=0, column=2, padx=6, pady=6, sticky="w")
    e_ciudad = tk.Entry(win, width=18); e_ciudad.grid(row=0, column=3, padx=6, pady=6)

    tk.Label(win, text="Activo (true/false)").grid(row=1, column=0, padx=6, pady=6, sticky="w")
    e_activo = tk.Entry(win, width=18); e_activo.grid(row=1, column=1, padx=6, pady=6)

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
        if e_nombre.get().strip():
            params["nombre"] = e_nombre.get().strip()
        else:
            if e_ciudad.get().strip():
                params["ciudad"] = e_ciudad.get().strip()
            if e_activo.get().strip():
                val = e_activo.get().strip().lower()
                if val not in ("true", "false"):
                    return show_error("Formato", "Activo debe ser true o false")
                params["activo"] = val

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
        except Exception as e:
            show_error("Error", str(e))

    tk.Button(win, text="Listar", command=listar).grid(row=2, column=0, columnspan=5, pady=6)

# ============================ UI ROOT ============================
def main():
    root = tk.Tk()
    root.title("Inventario Agrícola V2 - Cliente")
    root.geometry("720x460")

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

    # Menú Ayuda
    m_help = tk.Menu(menubar, tearoff=0)
    m_help.add_command(label="Acerca de...", command=lambda: messagebox.showinfo(
        "Acerca de",
        f"Inventario Agrícola V2 - Cliente Tkinter\n\nCRUD de Semillas y Proveedores\nBackend: {BASE_URL_SEMILLAS}"
    ))
    menu_m_help = m_help
    menubar.add_cascade(label="Ayuda", menu=menu_m_help)

    root.config(menu=menubar)

    tk.Label(root, text="Cliente Inventario Agrícola V2", font=("Helvetica", 16)).pack(pady=40)
    tk.Label(root, text=f"Backend: {BASE_URL_SEMILLAS}", fg="#555").pack()

    root.mainloop()

if __name__ == "__main__":
    main()
