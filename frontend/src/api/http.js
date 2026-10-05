export class ApiError extends Error {
    constructor(message, status, data = null) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.data = data;
    }
}

async function readResponseBody(response) {
    const contentType = response.headers.get('content-type') || ''

    if (contentType.includes('application/json')) {
        return response.json()
    }

    const text = await response.text()

    return text || null
}

function getErrorMessage(data, fallbackMessage) {
    if (!data) {
        return fallbackMessage
    }

    if (typeof data === 'string') {
        return data
    }

    if (typeof data.message === 'string') {
        return data.message
    }

    if (typeof data.error === 'string') {
        return data.error
    }

    return fallbackMessage
}

export async function apiFetch(url, options = {}) {
    const isFormData = options.body instanceof FormData

    const response = await fetch(url, {
        ...options,
        headers: {
            ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
            ...(options.headers || {})
        }
    })

    const data = await readResponseBody(response)

    if (!response.ok) {
        throw new ApiError(
            getErrorMessage(data, `Ошибка запроса: ${response.status}`),
            response.status,
            data
        )
    }

    return data
}