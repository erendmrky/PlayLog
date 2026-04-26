import { apiRequest } from "./httpClient";

function mapGame(game) {
  if (!game) {
    return null;
  }

  return {
    ...game,
    backgroundImage: game.backgroundImage || game.background_image || game.image || "",
    description: game.description || "",
    genres: game.genres || game.genre || [],
  };
}

export async function getPopularGames(token) {
  const data = await apiRequest("/games/popular", { token });
  return Array.isArray(data)
    ? data.map((game) => ({ ...game, image: game.image || game.background_image || "" }))
    : [];
}

export async function searchGames(query, token) {
  const data = await apiRequest(`/games/search?query=${encodeURIComponent(query)}`, { token });
  return Array.isArray(data)
    ? data.map((game) => ({ ...game, image: game.image || game.background_image || "" }))
    : [];
}

export async function getGameById(id, token) {
  const data = await apiRequest(`/games/${id}`, { token });
  return mapGame(data);
}



