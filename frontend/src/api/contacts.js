import {apiFetch} from "@/api/http.js";

export function  getPublicServices() {
    return apiFetch('/api/public/contacts')
}

export function getAdminContacts() {
    return apiFetch('/api/admin/contacts')
}

export function updateContacts(payload) {
    return apiFetch('/api/admin/contacts', {
        method: 'PUT',
        body: JSON.stringify(payload)
    })
}