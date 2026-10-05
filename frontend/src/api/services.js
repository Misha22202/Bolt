import { apiFetch } from "@/api/http.js";

export function getPublicServices() {
    return apiFetch('/api/public/services')
}

export function getAdminServices() {
    return apiFetch('/api/admin/services')
}

export function createService(payload) {
    return apiFetch('/api/admin/services', {
        method: 'POST',
        body: JSON.stringify(payload),
    })
}

export function updateService(id, payload) {
    return apiFetch(`/api/admin/services/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
    })
}

export function deleteService(id) {
    return apiFetch(`/api/admin/services/${id}`, {
        method: 'DELETE',
    })
}