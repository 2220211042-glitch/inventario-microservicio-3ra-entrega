/*
 * Cliente Web — Proveedores (Vanilla JS)
 * Conectado a Spring Boot en http://localhost:8080
 */

const BASE_PROVEEDORES = "http://localhost:8080/api/v1/proveedores";

/* --------- Utilidades UI --------- */
function toast(msg, ms = 2000){
  const el = document.getElementById("toast");
  el.textContent = msg;
  el.classList.add("show");
  setTimeout(() => el.classList.remove("show"), ms);
}

function showSection(id){
  document.querySelectorAll("main section.card").forEach(s => {
    s.style.display = (s.id === id) ? "block" : "none";
  });
  document.querySelectorAll("nav a[data-section]").forEach(a => {
    a.classList.toggle("active", a.dataset.section === id);
  });
}

/** Devuelve ISO con segundos desde un input datetime-local (o "") */
function getDateTimeValue(id){
  const el = document.getElementById(id);
  if(!el) return "";
  const v = el.value.trim();
  if(!v) return "";
  return v.length === 16 ? `${v}:00` : v; // YYYY-MM-DDTHH:MM -> +:00
}

async function handleResponse(response, outId){
  const out = document.getElementById(outId);
  let data;
  try { data = await response.json(); }
  catch { data = { raw: await response.text() }; }
  out.value = JSON.stringify({ status: response.status, data }, null, 2);
  if(response.ok) toast("Operación exitosa");
}

function handleError(outId, message){
  const out = document.getElementById(outId);
  out.value = JSON.stringify({ error: message }, null, 2);
  toast("Ocurrió un error");
}

/* --------- Navegación --------- */
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll("nav a[data-section]").forEach(link => {
    link.addEventListener("click", () => showSection(link.dataset.section));
  });
  showSection("about"); // sección por defecto
});

/* --------- PROVEEDORES --------- */

async function crearProveedor(){
  const body = {
    nit: document.getElementById("pc_nit").value.trim(),
    nombre: document.getElementById("pc_nombre").value.trim(),
    ciudad: document.getElementById("pc_ciudad").value.trim(),
    telefono: document.getElementById("pc_telefono").value.trim(),
    fechaRegistro: getDateTimeValue("pc_fecha"),
    activo: document.getElementById("pc_activo").checked
  };
  if(!body.nit) return handleError("pc_out", "El NIT es obligatorio");
  try{
    const res = await fetch(BASE_PROVEEDORES, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    await handleResponse(res, "pc_out");
  }catch(err){ handleError("pc_out", err.message) }
}

async function buscarProveedor(){
  const nit = document.getElementById("ps_nit").value.trim();
  if(!nit) return handleError("ps_out", "Debe ingresar el NIT");
  try{
    const res = await fetch(`${BASE_PROVEEDORES}/${encodeURIComponent(nit)}`);
    await handleResponse(res, "ps_out");
  }catch(err){ handleError("ps_out", err.message) }
}

async function actualizarProveedor(){
  const nit = document.getElementById("pu_nit").value.trim();
  if(!nit) return handleError("pu_out", "Debe ingresar el NIT de la ruta");
  const body = {
    nit,
    nombre: document.getElementById("pu_nombre").value.trim(),
    ciudad: document.getElementById("pu_ciudad").value.trim(),
    telefono: document.getElementById("pu_telefono").value.trim(),
    fechaRegistro: getDateTimeValue("pu_fecha"),
    activo: document.getElementById("pu_activo").checked
  };
  try{
    const res = await fetch(`${BASE_PROVEEDORES}/${encodeURIComponent(nit)}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    await handleResponse(res, "pu_out");
  }catch(err){ handleError("pu_out", err.message) }
}

async function eliminarProveedor(){
  const nit = document.getElementById("pd_nit").value.trim();
  if(!nit) return handleError("pd_out", "Debe ingresar el NIT");
  try{
    const res = await fetch(`${BASE_PROVEEDORES}/${encodeURIComponent(nit)}`, { method: "DELETE" });
    const out = document.getElementById("pd_out");
    if(res.headers.get("content-type")?.includes("application/json")){
      await handleResponse(res, "pd_out");
    }else{
      const text = await res.text();
      out.value = JSON.stringify({ status: res.status, data: text || "(sin cuerpo)" }, null, 2);
      if(res.ok) toast("Eliminado");
    }
  }catch(err){ handleError("pd_out", err.message) }
}

async function listarProveedores(){
  const nombre = document.getElementById("pl_nombre").value.trim();
  const ciudad = document.getElementById("pl_ciudad").value.trim();
  const activo = document.getElementById("pl_activo").checked;

  const params = new URLSearchParams();
  if(nombre) params.append("nombre", nombre);
  if(!nombre && ciudad) params.append("ciudad", ciudad);
  if(!nombre && activo) params.append("activo", "true");

  const url = params.toString() ? `${BASE_PROVEEDORES}?${params.toString()}` : BASE_PROVEEDORES;
  try{
    const res = await fetch(url);
    await handleResponse(res, "pl_out");
  }catch(err){ handleError("pl_out", err.message) }
}

/* Exponer funciones a window (para onclick de HTML) */
window.crearProveedor = crearProveedor;
window.buscarProveedor = buscarProveedor;
window.actualizarProveedor = actualizarProveedor;
window.eliminarProveedor = eliminarProveedor;
window.listarProveedores = listarProveedores;
