/*
 * app.js – Funciones para el cliente web del Inventario Agrícola
 *
 * Este script maneja la navegación entre secciones y realiza las
 * solicitudes REST hacia los microservicios de Semillas y Proveedores.
 */

const BASE_SEMILLAS = "http://localhost:8080/api/v1/semillas";
const BASE_PROVEEDORES = "http://localhost:8080/api/v1/proveedores";

/**
 * Muestra únicamente la sección solicitada y oculta el resto. Cada
 * sección se identifica por su ID.
 *
 * @param {string} id - ID de la sección a mostrar
 */
function showSection(id) {
    document.querySelectorAll('main section').forEach(sec => {
        sec.style.display = (sec.id === id) ? 'block' : 'none';
    });
}

// Configurar el menú de navegación al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    // Asignar manejadores a enlaces del menú
    document.querySelectorAll('nav a[data-section]').forEach(link => {
        link.addEventListener('click', () => {
            showSection(link.dataset.section);
        });
    });
    // Mostrar la sección de inicio (About) por defecto
    showSection('about');
});

/**
 * Convierte un valor de input datetime-local en formato ISO con segundos.
 *
 * El formato datetime-local no incluye segundos por defecto (YYYY-MM-DDTHH:MM),
 * por lo que se añade ":00" si faltan.
 *
 * @param {string} id - ID del input
 * @returns {string} Cadena ISO o cadena vacía si no hay valor
 */
function getDateTimeValue(id) {
    const el = document.getElementById(id);
    if (!el) return '';
    const v = el.value.trim();
    if (!v) return '';
    // Si tiene formato YYYY-MM-DDTHH:MM, completar con segundos
    return v.length === 16 ? `${v}:00` : v;
}

/**
 * Procesa una respuesta HTTP, tratando de obtener JSON si es posible.
 * Actualiza el contenido del textarea asociado.
 *
 * @param {Response} response - respuesta de fetch
 * @param {string} outId - ID del textarea donde se mostrará el resultado
 */
async function handleResponse(response, outId) {
    const out = document.getElementById(outId);
    let data;
    try {
        data = await response.json();
    } catch (err) {
        data = { raw: await response.text() };
    }
    const payload = { status: response.status, data };
    out.value = JSON.stringify(payload, null, 2);
}

/**
 * Muestra mensajes de error en el textarea correspondiente.
 *
 * @param {string} outId - ID del textarea donde se mostrará el error
 * @param {string} message - mensaje de error
 */
function handleError(outId, message) {
    const out = document.getElementById(outId);
    out.value = JSON.stringify({ error: message }, null, 2);
}

// ======================== SEMILLAS ========================

async function crearSemilla() {
    const body = {
        codigo: document.getElementById('sc_codigo').value.trim(),
        nombre: document.getElementById('sc_nombre').value.trim(),
        precio: parseFloat(document.getElementById('sc_precio').value || '0'),
        stock: parseInt(document.getElementById('sc_stock').value || '0'),
        tipoSemilla: document.getElementById('sc_tipo').value.trim(),
        porcentajeGerminacion: parseFloat(document.getElementById('sc_germinacion').value || '0'),
        proveedorNit: document.getElementById('sc_proveedor').value.trim(),
        fechaIngreso: getDateTimeValue('sc_fecha')
    };
    if (!body.codigo) {
        return handleError('sc_out', 'El código es obligatorio');
    }
    try {
        const res = await fetch(BASE_SEMILLAS, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        await handleResponse(res, 'sc_out');
    } catch (err) {
        handleError('sc_out', err.message);
    }
}

async function buscarSemilla() {
    const codigo = document.getElementById('ss_codigo').value.trim();
    if (!codigo) {
        return handleError('ss_out', 'Debe ingresar el código');
    }
    try {
        const res = await fetch(`${BASE_SEMILLAS}/${encodeURIComponent(codigo)}`);
        await handleResponse(res, 'ss_out');
    } catch (err) {
        handleError('ss_out', err.message);
    }
}

async function actualizarSemilla() {
    const codigo = document.getElementById('su_codigo').value.trim();
    if (!codigo) {
        return handleError('su_out', 'Debe ingresar el código de la ruta');
    }
    const body = {
        codigo: codigo,
        nombre: document.getElementById('su_nombre').value.trim(),
        precio: parseFloat(document.getElementById('su_precio').value || '0'),
        stock: parseInt(document.getElementById('su_stock').value || '0'),
        tipoSemilla: document.getElementById('su_tipo').value.trim(),
        porcentajeGerminacion: parseFloat(document.getElementById('su_germinacion').value || '0'),
        proveedorNit: document.getElementById('su_proveedor').value.trim(),
        fechaIngreso: getDateTimeValue('su_fecha')
    };
    try {
        const res = await fetch(`${BASE_SEMILLAS}/${encodeURIComponent(codigo)}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        await handleResponse(res, 'su_out');
    } catch (err) {
        handleError('su_out', err.message);
    }
}

async function eliminarSemilla() {
    const codigo = document.getElementById('sd_codigo').value.trim();
    if (!codigo) {
        return handleError('sd_out', 'Debe ingresar el código');
    }
    try {
        const res = await fetch(`${BASE_SEMILLAS}/${encodeURIComponent(codigo)}`, {
            method: 'DELETE'
        });
        // La eliminación puede no devolver JSON
        const out = document.getElementById('sd_out');
        if (res.headers.get('content-type')?.includes('application/json')) {
            await handleResponse(res, 'sd_out');
        } else {
            const text = await res.text();
            out.value = JSON.stringify({ status: res.status, data: text || '(sin cuerpo)' }, null, 2);
        }
    } catch (err) {
        handleError('sd_out', err.message);
    }
}

async function listarSemillas() {
    const tipo = document.getElementById('sl_tipo').value.trim();
    const germ = document.getElementById('sl_germinacion').value.trim();
    const desde = getDateTimeValue('sl_desde');
    const hasta = getDateTimeValue('sl_hasta');
    const params = new URLSearchParams();
    if (tipo) params.append('tipo', tipo);
    if (germ) params.append('germinacionMin', germ);
    if (desde) params.append('desde', desde);
    if (hasta) params.append('hasta', hasta);
    const url = params.toString() ? `${BASE_SEMILLAS}?${params.toString()}` : BASE_SEMILLAS;
    try {
        const res = await fetch(url);
        await handleResponse(res, 'sl_out');
    } catch (err) {
        handleError('sl_out', err.message);
    }
}

// ======================== PROVEEDORES ========================

async function crearProveedor() {
    const body = {
        nit: document.getElementById('pc_nit').value.trim(),
        nombre: document.getElementById('pc_nombre').value.trim(),
        ciudad: document.getElementById('pc_ciudad').value.trim(),
        telefono: document.getElementById('pc_telefono').value.trim(),
        fechaRegistro: getDateTimeValue('pc_fecha'),
        activo: document.getElementById('pc_activo').checked
    };
    if (!body.nit) {
        return handleError('pc_out', 'El NIT es obligatorio');
    }
    try {
        const res = await fetch(BASE_PROVEEDORES, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        await handleResponse(res, 'pc_out');
    } catch (err) {
        handleError('pc_out', err.message);
    }
}

async function buscarProveedor() {
    const nit = document.getElementById('ps_nit').value.trim();
    if (!nit) {
        return handleError('ps_out', 'Debe ingresar el NIT');
    }
    try {
        const res = await fetch(`${BASE_PROVEEDORES}/${encodeURIComponent(nit)}`);
        await handleResponse(res, 'ps_out');
    } catch (err) {
        handleError('ps_out', err.message);
    }
}

async function actualizarProveedor() {
    const nit = document.getElementById('pu_nit').value.trim();
    if (!nit) {
        return handleError('pu_out', 'Debe ingresar el NIT de la ruta');
    }
    const body = {
        nit: nit,
        nombre: document.getElementById('pu_nombre').value.trim(),
        ciudad: document.getElementById('pu_ciudad').value.trim(),
        telefono: document.getElementById('pu_telefono').value.trim(),
        fechaRegistro: getDateTimeValue('pu_fecha'),
        activo: document.getElementById('pu_activo').checked
    };
    try {
        const res = await fetch(`${BASE_PROVEEDORES}/${encodeURIComponent(nit)}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        await handleResponse(res, 'pu_out');
    } catch (err) {
        handleError('pu_out', err.message);
    }
}

async function eliminarProveedor() {
    const nit = document.getElementById('pd_nit').value.trim();
    if (!nit) {
        return handleError('pd_out', 'Debe ingresar el NIT');
    }
    try {
        const res = await fetch(`${BASE_PROVEEDORES}/${encodeURIComponent(nit)}`, {
            method: 'DELETE'
        });
        const out = document.getElementById('pd_out');
        if (res.headers.get('content-type')?.includes('application/json')) {
            await handleResponse(res, 'pd_out');
        } else {
            const text = await res.text();
            out.value = JSON.stringify({ status: res.status, data: text || '(sin cuerpo)' }, null, 2);
        }
    } catch (err) {
        handleError('pd_out', err.message);
    }
}

async function listarProveedores() {
    const nombre = document.getElementById('pl_nombre').value.trim();
    const ciudad = document.getElementById('pl_ciudad').value.trim();
    const activo = document.getElementById('pl_activo').checked;
    const params = new URLSearchParams();
    if (nombre) params.append('nombre', nombre);
    if (!nombre && ciudad) params.append('ciudad', ciudad);
    if (!nombre && activo) params.append('activo', 'true');
    const url = params.toString() ? `${BASE_PROVEEDORES}?${params.toString()}` : BASE_PROVEEDORES;
    try {
        const res = await fetch(url);
        await handleResponse(res, 'pl_out');
    } catch (err) {
        handleError('pl_out', err.message);
    }
}