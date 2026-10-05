function getCookie(name) {
    const prefix = `${name}=`

    return document.cookie
        .split('; ')
        .find((row) => row.startsWith(prefix))
        ?.slice(prefix.length)
}

async function ensureCsrfToken() {
    let csrfToken = getCookie('XSRF-TOKEN')

    if (csrfToken) {
        return decodeURIComponent(csrfToken)
    }

    const response = await fetch('/api/admin/auth/csrf', {
        credentials: 'include'
    })

    if (!response.ok) {
        throw new Error('Не удалось получить защитный токен.')
    }

    csrfToken = getCookie('XSRF-TOKEN')

    if (!csrfToken) {
        throw new Error('Сервер не вернул CSRF-токен.')
    }

    return decodeURIComponent(csrfToken)
}

export async function adminFetch(url, options = {}) {
    const method = (options.method || 'GET').toUpperCase()
    const headers = new Headers(options.headers || {})

    const needsCsrf = ['POST', 'PUT', 'PATCH', 'DELETE'].includes(method)

    if (needsCsrf) {
        const csrfToken = await ensureCsrfToken()

        headers.set('X-XSRF-TOKEN', csrfToken)
    }

    return fetch(url, {
        ...options,
        method,
        headers,
        credentials: 'include'
    })
}

export async function getCurrentAdmin() {
    const response = await adminFetch('/api/admin/auth/me')

    if (!response.ok) {
        return null
    }

    return response.json()
}