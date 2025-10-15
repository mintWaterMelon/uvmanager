import { ApiError } from "./client";

export function getApiErrorMessage(error: unknown) {
    if (error instanceof ApiError) {
        if (error.status === 400) {
            return "요청 값이 올바르지 않습니다.";
        }

        if (error.status === 401 || error.status === 403) {
            return "접근 권한이 없습니다.";
        }

        if (error.status === 404) {
            return "서버에서 데이터를 찾을 수 없습니다.";
        }

        if (error.status >= 500) {
            return "서버 오류가 발생했습니다.";
        }

        return `API 오류가 발생했습니다. 상태 코드: ${error.status}`;
    }

    if (error instanceof TypeError) {
        return "서버에 연결할 수 없습니다. 네트워크 또는 서버 실행 상태를 확인하세요.";
    }

    return "알 수 없는 오류가 발생했습니다.";
}

export function logApiError(error: unknown) {
    if (error instanceof ApiError) {
        console.error("API Error", {
            status: error.status,
            url: error.url,
            body: error.body,
            message: error.message,
        });

        return;
    }

    console.error(error);
}