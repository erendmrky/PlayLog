import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import EditNoteIcon from "@mui/icons-material/EditNote";
import SaveIcon from "@mui/icons-material/Save";
import RateReviewIcon from "@mui/icons-material/RateReview";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  CircularProgress,
  MenuItem,
  Pagination,
  Rating,
  Stack,
  TextField,
  Typography,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from "@mui/material";
import { useEffect, useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import { deleteReview, getMyReviews, updateReview } from "../api/reviewsApi";
import ErrorAlert from "../components/ErrorAlert";
import useAuth from "../hooks/useAuth";

const DEFAULT_FORM = { rating: 5, comment: "" };

export default function ReviewsPage() {
  const { token, clearSession } = useAuth();
  const [reviewPage, setReviewPage] = useState({ content: [], number: 0, totalPages: 0 });
  const [form, setForm] = useState(DEFAULT_FORM);
  const [editingId, setEditingId] = useState(null);
  const [deleteId, setDeleteId] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function load(page = 0) {
    try {
      setLoading(true);
      setError("");
      const data = await getMyReviews({ page, size: 10, token });
      const content = Array.isArray(data?.content) ? data.content : [];
      setReviewPage({
        content,
        number: data?.number ?? page,
        totalPages: data?.totalPages ?? 0,
      });
    } catch (e) {
      if (e.status === 401) clearSession();
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(0); }, []);

  function updateField(key, value) {
    setForm((current) => ({ ...current, [key]: value }));
  }

  function startEdit(review) {
    setEditingId(review.id);
    setForm({ rating: review.rating, comment: review.comment || "" });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    try {
      setError("");
      await updateReview({ id: editingId, rating: form.rating, comment: form.comment, token });
      setForm(DEFAULT_FORM);
      setEditingId(null);
      await load(reviewPage.number);
    } catch (e) {
      if (e.status === 401) clearSession();
      setError(e.message);
    }
  }

  async function handleDelete() {
    if (!deleteId) return;
    try {
      setError("");
      await deleteReview(deleteId, token);
      setDeleteId(null);
      await load(reviewPage.number);
    } catch (e) {
      if (e.status === 401) clearSession();
      setError(e.message);
    }
  }

  return (
    <section>
      {/* Header */}
      <Stack direction="row" alignItems="center" spacing={1.5} sx={{ mb: 4 }}>
        <RateReviewIcon sx={{ color: "primary.light", fontSize: 28 }} />
        <Typography variant="h4" sx={{ fontWeight: 800 }}>
          My Reviews
        </Typography>
        {reviewPage.content.length > 0 ? (
          <Chip
            label={`${reviewPage.totalPages > 1 ? "10+" : reviewPage.content.length} reviews`}
            color="secondary"
            size="small"
          />
        ) : null}
      </Stack>

      {/* Edit form */}
      {editingId ? (
        <Card
          sx={{
            mb: 3,
            border: "1px solid rgba(124,58,237,0.4)",
            background: "rgba(124,58,237,0.05)",
          }}
        >
          <CardContent sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ fontWeight: 700, mb: 2.5 }}>
              ✏️ Edit Review
            </Typography>
            <Box component="form" onSubmit={handleSubmit}>
              <Stack spacing={2.5}>
                <Box>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                    Rating
                  </Typography>
                  <Rating
                    value={form.rating}
                    onChange={(_, newValue) => updateField("rating", newValue || 1)}
                    size="large"
                  />
                </Box>
                <TextField
                  label="Comment"
                  multiline
                  minRows={3}
                  value={form.comment}
                  onChange={(e) => updateField("comment", e.target.value)}
                  required
                  fullWidth
                />
                <Stack direction="row" spacing={1.5}>
                  <Button type="submit" variant="contained" startIcon={<SaveIcon />}>
                    Save Changes
                  </Button>
                  <Button
                    type="button"
                    variant="outlined"
                    onClick={() => { setEditingId(null); setForm(DEFAULT_FORM); }}
                  >
                    Cancel
                  </Button>
                </Stack>
              </Stack>
            </Box>
          </CardContent>
        </Card>
      ) : null}

      <ErrorAlert message={error} />

      {loading ? (
        <Stack alignItems="center" sx={{ py: 8 }}>
          <CircularProgress />
        </Stack>
      ) : null}

      {/* Review cards */}
      {!loading ? (
        <Stack spacing={2.5}>
          {reviewPage.content.map((review) => (
            <Card
              key={review.id}
              sx={{
                overflow: "hidden",
                transition: "transform 0.2s ease",
                "&:hover": { transform: "translateY(-2px)" },
              }}
            >
              {review.game?.backgroundImage ? (
                <Box
                  component={RouterLink}
                  to={`/games/${review.game?.rawgId || review.game?.id}`}
                  sx={{ display: "block", position: "relative" }}
                >
                  <CardMedia
                    component="img"
                    height="160"
                    image={review.game.backgroundImage}
                    alt={review.game?.name || "game"}
                    sx={{ transition: "transform 0.4s ease", "&:hover": { transform: "scale(1.03)" } }}
                  />
                  <Box
                    sx={{
                      position: "absolute",
                      inset: 0,
                      background: "linear-gradient(to top, rgba(9,9,15,0.85) 0%, transparent 60%)",
                    }}
                  />
                  <Box sx={{ position: "absolute", bottom: 12, left: 16 }}>
                    <Typography variant="h6" sx={{ fontWeight: 700, textShadow: "0 2px 8px rgba(0,0,0,0.8)" }}>
                      {review.game?.name || `Game #${review.game?.rawgId || "N/A"}`}
                    </Typography>
                  </Box>
                </Box>
              ) : null}

              <CardContent sx={{ p: 3 }}>
                {!review.game?.backgroundImage ? (
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 1.5 }}>
                    {review.game?.name || `Game #${review.game?.rawgId || "N/A"}`}
                  </Typography>
                ) : null}

                <Stack direction="row" justifyContent="space-between" alignItems="flex-start" sx={{ mb: 1.5 }}>
                  <Rating value={review.rating} readOnly size="small" />
                  <Typography variant="caption" color="text.disabled">
                    {review.createdDate}
                  </Typography>
                </Stack>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2, lineHeight: 1.7 }}>
                  {review.comment || "No comment"}
                </Typography>

                <Stack direction="row" justifyContent="space-between" alignItems="center">
                  <Typography variant="caption" color="text.disabled">
                    by{" "}
                    <Box component="span" sx={{ color: "primary.light", fontWeight: 600 }}>
                      {review.user?.username}
                    </Box>
                  </Typography>
                  <Stack direction="row" spacing={1}>
                    <Button
                      size="small"
                      variant="outlined"
                      startIcon={<EditNoteIcon />}
                      onClick={() => startEdit(review)}
                    >
                      Edit
                    </Button>
                    <Button
                      size="small"
                      color="error"
                      variant="outlined"
                      startIcon={<DeleteOutlineIcon />}
                      onClick={() => setDeleteId(review.id)}
                    >
                      Delete
                    </Button>
                  </Stack>
                </Stack>
              </CardContent>
            </Card>
          ))}
        </Stack>
      ) : null}

      {!loading && reviewPage.content.length === 0 ? (
        <Box sx={{ textAlign: "center", py: 10 }}>
          <Typography variant="h2" sx={{ opacity: 0.3, mb: 2 }}>📝</Typography>
          <Typography variant="h6" color="text.secondary">No reviews yet</Typography>
          <Typography variant="body2" color="text.disabled">
            Go explore some games and leave your first review!
          </Typography>
        </Box>
      ) : null}

      {reviewPage.totalPages > 1 ? (
        <Stack direction="row" justifyContent="center" sx={{ mt: 4 }}>
          <Pagination
            color="primary"
            page={reviewPage.number + 1}
            count={Math.max(reviewPage.totalPages, 1)}
            onChange={(_, value) => load(value - 1)}
          />
        </Stack>
      ) : null}

      {/* Delete confirmation dialog */}
      <Dialog
        open={Boolean(deleteId)}
        onClose={() => setDeleteId(null)}
        PaperProps={{
          sx: {
            background: "#12121e",
            border: "1px solid rgba(244,63,94,0.3)",
            borderRadius: 4,
          },
        }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Delete this review?</DialogTitle>
        <DialogContent>
          <DialogContentText color="text.secondary">
            This action cannot be undone. The review will be permanently removed.
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button onClick={() => setDeleteId(null)} variant="outlined">
            Cancel
          </Button>
          <Button
            onClick={handleDelete}
            color="error"
            variant="contained"
            sx={{ background: "linear-gradient(135deg, #f43f5e, #be123c)" }}
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </section>
  );
}