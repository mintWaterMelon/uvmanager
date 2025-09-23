const API_BASE_URL = "http://172.30.1.47:8080";

export async function apiGet<T>(path: string): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${path}`);

    if (!response.ok) {
        throw new Error(`API GET failed: ${response.status}`);
    }

    return response.json();
}

export async function apiPost<TResponse, TRequest>(
    path: string,
    body: TRequest
): Promise<TResponse> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
    });

    if (!response.ok) {
        throw new Error(`API POST failed: ${response.status}`);
    }

    return response.json();
}

export async function apiPut<TResponse, TRequest>(
    path: string,
    body: TRequest
): Promise<TResponse> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
    });

    if (!response.ok) {
        throw new Error(`API PUT failed: ${response.status}`);
    }

    return response.json();
}

export async function apiDelete(path: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error(`API DELETE failed: ${response.status}`);
    }
}