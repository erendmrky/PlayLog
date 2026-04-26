import { apiRequest } from "./httpClient";

export function getReviews({ page = 0, size = 10, token }) {
  return apiRequest(`/reviews?page=${page}&size=${size}`, { token });
}

export function getMyReviews({ page = 0, size = 10, token }) {
  return apiRequest(`/reviews/me?page=${page}&size=${size}`, { token });
}

export function getGameReviews({ gameId, page = 0, size = 10, token }) {
  return apiRequest(`/reviews?page=${page}&size=${size}&gameId=${gameId}`, { token });
}

export function createReview({ rating, comment, gameId, token }) {
  return apiRequest("/reviews", {
    method: "POST",
    body: { rating: Number(rating), comment, gameId: Number(gameId) },
    token,
  });
}

export function updateReview({ id, rating, comment, token }) {
  return apiRequest(`/reviews/${id}`, {
    method: "PUT",
    body: { rating: Number(rating), comment },
    token,
  });
}

export function deleteReview(id, token) {
  return apiRequest(`/reviews/${id}`, {
    method: "DELETE",
    token,
  });
}


