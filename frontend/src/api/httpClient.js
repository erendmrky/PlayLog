const API_ROOT = import.meta.env.VITE_API_ROOT || "/api/v1";

let forbiddenHandler = null;

export function setForbiddenHandler(handler) {
  forbiddenHandler = handler;
}

export class ApiError extends Error {
  constructor(message, status, details) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.details = details;
  }
}

export async function apiRequest(path, { method = "GET", body, token } = {}) {
  const response = await fetch(`${API_ROOT}${path}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  const text = await response.text();
  let data = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = { message: text };
    }
  }

  if (!response.ok) {
    const message =
      data?.error?.message || data?.message || `Request failed with ${response.status}`;
    const error = new ApiError(message, response.status, data);

    if (response.status === 403 && typeof forbiddenHandler === "function") {
      forbiddenHandler(error);
    }

    throw error;
  }

  return data;
}


