import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import SearchIcon from "@mui/icons-material/Search";
import WhatshotIcon from "@mui/icons-material/Whatshot";
import HistoryIcon from "@mui/icons-material/History";
import {
  Autocomplete,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Chip,
  CircularProgress,
  InputAdornment,
  Skeleton,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { getPopularGames, searchGames } from "../api/gamesApi";
import ErrorAlert from "../components/ErrorAlert";
import useAuth from "../hooks/useAuth";
import { getRecentGames } from "../utils/recentGames";

function GameCard({ game }) {
  return (
    <Card
      sx={{
        height: "100%",
        display: "flex",
        flexDirection: "column",
        overflow: "hidden",
        transition: "transform 0.25s ease",
        "&:hover": {
          transform: "translateY(-4px)",
          "& .game-overlay": { opacity: 1 },
        },
      }}
    >
      {game.image ? (
        <Box sx={{ position: "relative", overflow: "hidden" }}>
          <CardMedia
            component="img"
            height="180"
            image={game.image}
            alt={game.name}
            sx={{ transition: "transform 0.4s ease", "&:hover": { transform: "scale(1.04)" } }}
          />
          <Box
            className="game-overlay"
            sx={{
              position: "absolute",
              inset: 0,
              background: "linear-gradient(to top, rgba(9,9,15,0.9) 0%, transparent 60%)",
              opacity: 0.6,
              transition: "opacity 0.3s ease",
            }}
          />
        </Box>
      ) : (
        <Box
          sx={{
            height: 180,
            background: "linear-gradient(135deg, rgba(124,58,237,0.3), rgba(6,214,160,0.15))",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <Typography variant="h2" sx={{ opacity: 0.3 }}>🎮</Typography>
        </Box>
      )}
      <CardContent sx={{ flexGrow: 1, pb: 1 }}>
        <Typography variant="subtitle1" sx={{ fontWeight: 700, lineHeight: 1.3, fontSize: "0.95rem" }}>
          {game.name}
        </Typography>
      </CardContent>
      <CardActions sx={{ px: 2, pb: 2 }}>
        <Button
          component={RouterLink}
          to={`/games/${game.id}`}
          variant="contained"
          endIcon={<ArrowForwardIcon />}
          fullWidth
          size="small"
        >
          View Details
        </Button>
      </CardActions>
    </Card>
  );
}

function SkeletonCard() {
  return (
    <Box>
      <Skeleton variant="rectangular" height={180} sx={{ borderRadius: 2, bgcolor: "rgba(255,255,255,0.05)" }} />
      <Skeleton sx={{ mt: 1, bgcolor: "rgba(255,255,255,0.05)" }} />
      <Skeleton width="60%" sx={{ bgcolor: "rgba(255,255,255,0.05)" }} />
    </Box>
  );
}

const gridSx = {
  display: "grid",
  gap: 2.5,
  gridTemplateColumns: {
    xs: "1fr",
    sm: "repeat(2, 1fr)",
    md: "repeat(3, 1fr)",
    lg: "repeat(5, 1fr)",
  },
};

export default function PopularGamesPage() {
  const navigate = useNavigate();
  const { token, clearSession } = useAuth();

  const [popularGames, setPopularGames] = useState([]);
  const [recentGames, setRecentGames] = useState([]);
  const [popularLoading, setPopularLoading] = useState(true);
  const [error, setError] = useState("");

  const [inputValue, setInputValue] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [searchLoading, setSearchLoading] = useState(false);

  // Load popular games once on mount
  useEffect(() => {
    async function loadPopular() {
      try {
        setError("");
        setPopularLoading(true);
        const data = await getPopularGames(token);
        setPopularGames(Array.isArray(data) ? data : []);
        setRecentGames(getRecentGames());
      } catch (e) {
        if (e.status === 401) clearSession();
        setError(e.message);
      } finally {
        setPopularLoading(false);
      }
    }
    loadPopular();
  }, [token, clearSession]);

  async function handleSearch() {
    const trimmed = inputValue.trim();
    if (!trimmed) {
      setSearchResults([]);
      return;
    }
    try {
      setSearchLoading(true);
      const data = await searchGames(trimmed, token);
      setSearchResults(Array.isArray(data) ? data : []);
    } catch (e) {
      if (e.status === 401) clearSession();
      setError(e.message);
    } finally {
      setSearchLoading(false);
    }
  }

  const isSearching = searchResults.length > 0;

  return (
    <section>
      {/* Header */}
      <Stack direction="row" justifyContent="space-between" alignItems="flex-start" sx={{ mb: 4 }}>
        <Box>
          <Stack direction="row" alignItems="center" spacing={1.5} sx={{ mb: 0.5 }}>
            <WhatshotIcon sx={{ color: "primary.light", fontSize: 28 }} />
            <Typography variant="h4" sx={{ fontWeight: 800 }}>
              Popular Games
            </Typography>
          </Stack>
          <Typography variant="body2" color="text.secondary">
            Trending titles right now
          </Typography>
        </Box>
        <Chip label={`${popularGames.length} games`} color="secondary" sx={{ mt: 0.5 }} />
      </Stack>

      {/* Live Search — results appear as dropdown while typing */}
      <Box sx={{ mb: 4 }}>
        <Autocomplete
          freeSolo
          open={isSearching || searchLoading}
          options={searchResults}
          getOptionLabel={(option) =>
            typeof option === "string" ? option : option?.name || ""
          }
          isOptionEqualToValue={(option, value) => option.id === value?.id}
          inputValue={inputValue}
          onInputChange={(_, value, reason) => {
            setInputValue(value);
            if (reason === "clear" || value === "") setSearchResults([]);
          }}
          onChange={(_, value) => {
            if (value && typeof value !== "string" && value?.id) {
              navigate(`/games/${value.id}`);
            }
          }}
          filterOptions={(x) => x}
          loading={searchLoading}
          noOptionsText="No games found"
          renderOption={(props, option) => {
            const { key, ...rest } = props;
            return (
              <Box
                component="li"
                key={key}
                {...rest}
                sx={{
                  display: "flex !important",
                  alignItems: "center !important",
                  gap: "12px !important",
                  py: "8px !important",
                  px: "12px !important",
                  borderRadius: "10px !important",
                  mx: "4px !important",
                  cursor: "pointer",
                }}
              >
                {option.image ? (
                  <Box
                    component="img"
                    src={option.image}
                    alt={option.name}
                    sx={{ width: 56, height: 38, borderRadius: 1.5, objectFit: "cover", flexShrink: 0 }}
                  />
                ) : (
                  <Box
                    sx={{
                      width: 56,
                      height: 38,
                      borderRadius: 1.5,
                      background: "linear-gradient(135deg, rgba(124,58,237,0.3), rgba(6,214,160,0.15))",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      fontSize: "1.1rem",
                      flexShrink: 0,
                    }}
                  >
                    🎮
                  </Box>
                )}
                <Typography variant="body2" sx={{ fontWeight: 600 }}>
                  {option.name}
                </Typography>
              </Box>
            );
          }}
          renderInput={(params) => (
            <TextField
              {...params}
              label="Search games"
              placeholder="Press Enter to search… e.g. The Witcher, Red Dead"
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  handleSearch();
                }
              }}
              InputProps={{
                ...params.InputProps,
                startAdornment: (
                  <InputAdornment position="start">
                    {searchLoading
                      ? <CircularProgress size={18} sx={{ color: "primary.light" }} />
                      : <SearchIcon sx={{ color: "text.secondary" }} />
                    }
                  </InputAdornment>
                ),
              }}
            />
          )}
          slotProps={{
            paper: {
              sx: {
                mt: 0.5,
                background: "#12121e",
                border: "1px solid rgba(124,58,237,0.3)",
                borderRadius: 3,
                boxShadow: "0 20px 60px rgba(0,0,0,0.7)",
                "& .MuiAutocomplete-listbox": { p: 1 },
                "& .MuiAutocomplete-option:hover, & .MuiAutocomplete-option.Mui-focused": {
                  background: "rgba(124,58,237,0.15) !important",
                },
                "& .MuiAutocomplete-noOptions": {
                  color: "text.secondary",
                  fontSize: "0.875rem",
                },
              },
            },
          }}
        />
      </Box>

      <ErrorAlert message={error} />

      {/* Recently Opened — hidden while typing */}
      {!isSearching && recentGames.length > 0 ? (
        <Box sx={{ mb: 5 }}>
          <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 2 }}>
            <HistoryIcon sx={{ color: "secondary.main", fontSize: 20 }} />
            <Typography variant="h6" sx={{ fontWeight: 700 }}>
              Recently Opened
            </Typography>
          </Stack>
          <Box sx={gridSx}>
            {recentGames.map((game) => (
              <GameCard key={game.id} game={game} />
            ))}
          </Box>
        </Box>
      ) : null}

      {/* Popular games grid — hidden while typing */}
      {!isSearching && popularLoading ? (
        <Box sx={gridSx}>
          {[1, 2, 3, 4, 5].map((n) => <SkeletonCard key={n} />)}
        </Box>
      ) : null}

      {!isSearching && !popularLoading ? (
        <Box sx={gridSx}>
          {popularGames.map((game) => (
            <GameCard key={game.id} game={game} />
          ))}
        </Box>
      ) : null}
    </section>
  );
}