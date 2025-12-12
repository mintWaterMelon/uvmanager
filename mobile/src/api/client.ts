const API_BASE_URL = process.env.EXPO_PUBLIC_API_BASE_URL;

if (!API_BASE_URL) {
    throw new Error("EXPO_PUBLIC_API_BASE_URL is not defined");
}

export type ApiRequestOptions = {
    signal?: AbortSignal;
};

export class ApiError extends Error {
    status: number;
    url: string;
    body: string;

    constructor(message: string, status: number, url: string, body: string) {
        super(message);

        this.name = "ApiError";
        this.status = status;
        this.url = url;
        this.body = body;
    }
}

export async function apiGet<T>(
    path: string,
    options?: ApiRequestOptions
): Promise<T> {
    const url = `${API_BASE_URL}${path}`;

    const response = await fetch(url, {
        signal: options?.signal,
    });

    return handleResponse<T>(response, url, "GET");
}

export async function apiPost<TResponse, TRequest>(
    path: string,
    body: TRequest,
    options?: ApiRequestOptions
): Promise<TResponse> {
    const url = `${API_BASE_URL}${path}`;

    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
        signal: options?.signal,
    });

    return handleResponse<TResponse>(response, url, "POST");
}

export async function apiPut<TResponse, TRequest>(
    path: string,
    body: TRequest,
    options?: ApiRequestOptions
): Promise<TResponse> {
    const url = `${API_BASE_URL}${path}`;

    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
        signal: options?.signal,
    });

    return handleResponse<TResponse>(response, url, "PUT");
}

export async function apiDelete(
    path: string,
    options?: ApiRequestOptions
): Promise<void> {
    const url = `${API_BASE_URL}${path}`;

    const response = await fetch(url, {
        method: "DELETE",
        signal: options?.signal,
    });

    await handleResponse<void>(response, url, "DELETE");
}

async function handleResponse<T>(
    response: Response,
    url: string,
    method: string
): Promise<T> {
    if (!response.ok) {
        const bodyText = await safeReadText(response);

        throw new ApiError(
            `API ${method} failed: ${response.status}`,
            response.status,
            url,
            bodyText
        );
    }

    if (response.status === 204) {
        return undefined as T;
    }

    return response.json();
}

async function safeReadText(response: Response) {
    try {
        return await response.text();
    } catch {
        return "";
    }
}
