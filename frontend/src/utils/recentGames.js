const RECENT_GAMES_KEY = "playlog_recent_games";
const MAX_RECENT_GAMES = 10;

export function getRecentGames() {
  try {
    const value = localStorage.getItem(RECENT_GAMES_KEY);
    const parsed = value ? JSON.parse(value) : [];
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

export function addRecentGame(game) {
  if (!game?.id) {
    return;
  }

  const current = getRecentGames();
  const withoutCurrent = current.filter((item) => item.id !== game.id);
  const next = [
    {
      id: game.id,
      name: game.name,
      image: game.image || game.backgroundImage || "",
    },
    ...withoutCurrent,
  ].slice(0, MAX_RECENT_GAMES);

  localStorage.setItem(RECENT_GAMES_KEY, JSON.stringify(next));
}

