import { apiRequest } from "./httpClient";

export function getUserById(id, token) {
  return apiRequest(`/users/${id}`, { token });
}

export function deleteUser(id, token) {
  return apiRequest(`/users/${id}`, {
    method: "DELETE",
    token,
  });
}

