# PlayLog Frontend (React + Vite + MUI)

This frontend integrates with the PlayLog Spring Boot backend (`/api/v1`).

## Features implemented

- Auth: register, login, logout (JWT token in localStorage)
- Games: list popular games and view game detail
- Games: search bar on popular games page
- Game detail supports HTML descriptions from RAWG and renders game image content
- Reviews: "My Reviews" page lists only your reviews (edit/delete)
- Review can also be submitted directly from the game detail page
- Game detail also lists reviews written for that game
- Profile: fetch current user from JWT `userId`, soft-delete account
- API error handling for backend `ErrorResponseDTO`
- Material UI based design system (theme, cards, responsive layout, polished forms)