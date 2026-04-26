import SendIcon from "@mui/icons-material/Send";
import StarIcon from "@mui/icons-material/Star";
import CalendarTodayIcon from "@mui/icons-material/CalendarToday";
import {
  Alert,
  Avatar,
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  Divider,
  List,
  ListItem,
  MenuItem,
  Pagination,
  Rating,
  Skeleton,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import { getGameById } from "../api/gamesApi";
import { createReview, getGameReviews } from "../api/reviewsApi";
import ErrorAlert from "../components/ErrorAlert";
import useAuth from "../hooks/useAuth";
import { addRecentGame } from "../utils/recentGames";
import sanitizeHtml from "../utils/sanitizeHtml";

function ReviewItem({ review }) {
  const initials = (review.user?.username || "?").slice(0, 2).toUpperCase();
  return (
    <Box
      sx={{
        p: 2.5,
        borderRadius: 3,
        background: "rgba(255,255,255,0.03)",
        border: "1px solid rgba(124,58,237,0.1)",
        transition: "border-color 0.2s",
        "&:hover": { borderColor: "rgba(124,58,237,0.25)" },
      }}
    >
      <Stack direction="row" spacing={2} alignItems="flex-start">
        <Avatar
          sx={{
            width: 36,
            height: 36,
            background: "linear-gradient(135deg, #7c3aed, #06d6a0)",
            fontSize: "0.75rem",
            fontWeight: 700,
            flexShrink: 0,
          }}
        >
          {initials}
        </Avatar>
        <Box sx={{ flexGrow: 1, minWidth: 0 }}>
          <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5 }}>
            <Typography variant="subtitle2" sx={{ fontWeight: 700 }}>
              {review.user?.username || "Unknown"}
            </Typography>
            <Rating value={review.rating} readOnly size="small" />
          </Stack>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>
            {review.comment || "No comment"}
          </Typography>
          <Typography variant="caption" color="text.disabled">
            {review.createdDate}
          </Typography>
        </Box>
      </Stack>
    </Box>
  );
}

export default function GameDetailPage() {
  const { id } = useParams();
  const { token, clearSession } = useAuth();
  const [game, setGame] = useState(null);
  const [error, setError] = useState("");
  const [reviewError, setReviewError] = useState("");
  const [reviewSuccess, setReviewSuccess] = useState("");
  const [reviewForm, setReviewForm] = useState({ rating: 5, comment: "" });
  const [reviewLoading, setReviewLoading] = useState(false);
  const [gameReviews, setGameReviews] = useState([]);
  const [reviewsPage, setReviewsPage] = useState({ number: 0, totalPages: 0 });

  const safeDescription = useMemo(
    () => sanitizeHtml(game?.description || ""),
    [game?.description]
  );

  useEffect(() => {
    async function loadGame() {
      try {
        setError("");
        const data = await getGameById(id, token);
        setGame(data);
      } catch (e) {
        if (e.status === 401) clearSession();
        setError(e.message);
      }
    }
    loadGame();
  }, [id, token, clearSession]);

  async function loadGameReviews(page = 0) {
    try {
      const data = await getGameReviews({ gameId: id, page, size: 8, token });
      setGameReviews(Array.isArray(data?.content) ? data.content : []);
      setReviewsPage({ number: data?.number ?? page, totalPages: data?.totalPages ?? 0 });
    } catch (e) {
      if (e.status === 401) clearSession();
      setError(e.message);
    }
  }

  useEffect(() => {
    loadGameReviews(0);
  }, [id, token, clearSession]);

  useEffect(() => {
    if (game) {
      addRecentGame({ id: game.id, name: game.name, image: game.backgroundImage });
    }
  }, [game]);

  async function handleCreateReview(event) {
    event.preventDefault();
    setReviewError("");
    setReviewSuccess("");
    setReviewLoading(true);
    try {
      await createReview({ rating: reviewForm.rating, comment: reviewForm.comment, gameId: id, token });
      setReviewSuccess("Your review was added successfully.");
      setReviewForm((prev) => ({ ...prev, comment: "" }));
      await loadGameReviews(0);
    } catch (e) {
      if (e.status === 401) clearSession();
      setReviewError(
        e.message?.toLowerCase().includes("already reviewed")
          ? "You already reviewed this game."
          : e.message
      );
    } finally {
      setReviewLoading(false);
    }
  }

  return (
    <Box>
      <ErrorAlert message={error} />

      {/* Hero image */}
      {game?.backgroundImage ? (
        <Box
          sx={{
            borderRadius: 4,
            overflow: "hidden",
            mb: 3,
            position: "relative",
            boxShadow: "0 20px 60px rgba(0,0,0,0.6)",
          }}
        >
          <CardMedia
            component="img"
            height="380"
            image={game.backgroundImage}
            alt={game.name}
            sx={{ objectFit: "cover" }}
          />
          <Box
            sx={{
              position: "absolute",
              inset: 0,
              background: "linear-gradient(to top, rgba(9,9,15,0.95) 0%, rgba(9,9,15,0.3) 50%, transparent 100%)",
            }}
          />
          {game ? (
            <Box sx={{ position: "absolute", bottom: 0, left: 0, p: 4 }}>
              <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, textShadow: "0 2px 20px rgba(0,0,0,0.8)" }}>
                {game.name}
              </Typography>
              <Stack direction="row" alignItems="center" spacing={1}>
                <CalendarTodayIcon sx={{ fontSize: 14, color: "text.secondary" }} />
                <Typography variant="body2" color="text.secondary">
                  {game.released || "Release date unknown"}
                </Typography>
              </Stack>
            </Box>
          ) : null}
        </Box>
      ) : null}

      {!game ? (
        <Card>
          <CardContent>
            <Skeleton variant="text" height={60} width="60%" sx={{ bgcolor: "rgba(255,255,255,0.05)" }} />
            <Skeleton variant="rounded" height={200} sx={{ mt: 2, bgcolor: "rgba(255,255,255,0.05)" }} />
          </CardContent>
        </Card>
      ) : null}

      {game ? (
        <Stack spacing={3}>
          {/* If no background image, show title here */}
          {!game.backgroundImage ? (
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 800 }}>{game.name}</Typography>
              <Stack direction="row" alignItems="center" spacing={1} sx={{ mt: 0.5 }}>
                <CalendarTodayIcon sx={{ fontSize: 14, color: "text.secondary" }} />
                <Typography variant="body2" color="text.secondary">
                  {game.released || "Release date unknown"}
                </Typography>
              </Stack>
            </Box>
          ) : null}

          {/* Platforms & Genres */}
          <Card>
            <CardContent>
              <Stack spacing={2}>
                <Box>
                  <Typography variant="overline" color="text.secondary" sx={{ letterSpacing: 2, fontSize: "0.7rem" }}>
                    Platforms
                  </Typography>
                  <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap sx={{ mt: 1 }}>
                    {(Array.isArray(game.platforms) ? game.platforms : ["N/A"]).map((p) => (
                      <Chip key={p} label={p} color="primary" variant="outlined" size="small" />
                    ))}
                  </Stack>
                </Box>
                <Box>
                  <Typography variant="overline" color="text.secondary" sx={{ letterSpacing: 2, fontSize: "0.7rem" }}>
                    Genres
                  </Typography>
                  <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap sx={{ mt: 1 }}>
                    {(Array.isArray(game.genres) ? game.genres : ["N/A"]).map((g) => (
                      <Chip key={g} label={g} color="secondary" size="small" />
                    ))}
                  </Stack>
                </Box>
              </Stack>
            </CardContent>
          </Card>

          {/* Description */}
          {safeDescription ? (
            <Card>
              <CardContent>
                <Typography variant="overline" color="text.secondary" sx={{ letterSpacing: 2, fontSize: "0.7rem" }}>
                  About
                </Typography>
                <Box
                  sx={{ mt: 1.5, lineHeight: 1.8, color: "text.secondary", fontSize: "0.95rem" }}
                  dangerouslySetInnerHTML={{ __html: safeDescription }}
                />
              </CardContent>
            </Card>
          ) : null}

          {/* Add Review */}
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 3 }}>
                <StarIcon sx={{ color: "primary.light" }} />
                <Typography variant="h6" sx={{ fontWeight: 700 }}>
                  Leave a Review
                </Typography>
              </Stack>

              <ErrorAlert message={reviewError} />
              {reviewSuccess ? (
                <Alert severity="success" sx={{ mb: 2 }}>
                  {reviewSuccess}
                </Alert>
              ) : null}

              <Box component="form" onSubmit={handleCreateReview}>
                <Stack spacing={2.5}>
                  <Box>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                      Rating
                    </Typography>
                    <Rating
                      value={reviewForm.rating}
                      onChange={(_, newValue) =>
                        setReviewForm((prev) => ({ ...prev, rating: newValue || 1 }))
                      }
                      size="large"
                    />
                  </Box>
                  <TextField
                    label="Your comment"
                    multiline
                    minRows={3}
                    value={reviewForm.comment}
                    onChange={(e) =>
                      setReviewForm((prev) => ({ ...prev, comment: e.target.value }))
                    }
                    fullWidth
                  />
                  <Button
                    type="submit"
                    variant="contained"
                    startIcon={<SendIcon />}
                    disabled={reviewLoading}
                    sx={{ alignSelf: "flex-start", px: 4 }}
                  >
                    {reviewLoading ? "Submitting…" : "Submit Review"}
                  </Button>
                </Stack>
              </Box>
            </CardContent>
          </Card>

          {/* Reviews list */}
          <Card>
            <CardContent>
              <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
                <Typography variant="h6" sx={{ fontWeight: 700 }}>
                  Community Reviews
                </Typography>
                {gameReviews.length > 0 ? (
                  <Chip label={`${gameReviews.length} reviews`} size="small" color="primary" variant="outlined" />
                ) : null}
              </Stack>

              {gameReviews.length === 0 ? (
                <Box sx={{ textAlign: "center", py: 4 }}>
                  <Typography variant="h4" sx={{ opacity: 0.3, mb: 1 }}>⭐</Typography>
                  <Typography color="text.secondary">No reviews yet. Be the first!</Typography>
                </Box>
              ) : (
                <Stack spacing={2}>
                  {gameReviews.map((review) => (
                    <ReviewItem key={review.id} review={review} />
                  ))}
                </Stack>
              )}

              {reviewsPage.totalPages > 1 ? (
                <Stack direction="row" justifyContent="center" sx={{ mt: 3 }}>
                  <Pagination
                    color="primary"
                    page={reviewsPage.number + 1}
                    count={reviewsPage.totalPages}
                    onChange={(_, value) => loadGameReviews(value - 1)}
                  />
                </Stack>
              ) : null}
            </CardContent>
          </Card>
        </Stack>
      ) : null}
    </Box>
  );
}