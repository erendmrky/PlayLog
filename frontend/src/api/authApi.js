import { apiRequest } from "./httpClient";

export function login(username, password) {
  return apiRequest("/auth/login", {
    method: "POST",
    body: { username, password },
  });
}

export function register({ email, username, password }) {
  return apiRequest("/auth/register", {
    method: "POST",
    body: { email, username, password },
  });
}

export function logout(token) {
  return apiRequest("/auth/logout", {
    method: "POST",
    token,
  });
}

