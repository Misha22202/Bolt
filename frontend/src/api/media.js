import { apiFetch } from "@/api/http.js"

export function uploadImage (file) {
    const formData = new FormData()

    formData.append("file", file)

    return apiFetch('/api/admin/media', {
        method: 'POST',
        body: formData
    })
}