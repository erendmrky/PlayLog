import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
import PersonIcon from "@mui/icons-material/Person";
import EmailIcon from "@mui/icons-material/Email";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import {
  Avatar,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  Stack,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { deleteUser, getUserById } from "../api/usersApi";
import ErrorAlert from "../components/ErrorAlert";
import useAuth from "../hooks/useAuth";

function InfoRow({ icon, label, value }) {
  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        gap: 2,
        p: 2,
        borderRadius: 3,
        background: "rgba(255,255,255,0.03)",
        border: "1px solid rgba(124,58,237,0.1)",
      }}
    >
      <Box
        sx={{
          width: 38,
          height: 38,
          borderRadius: 2,
          background: "rgba(124,58,237,0.15)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          flexShrink: 0,
        }}
      >
        {icon}
      </Box>
      <Box>
        <Typography variant="caption" color="text.secondary" sx={{ display: "block", letterSpacing: 1, textTransform: "uppercase", fontSize: "0.65rem" }}>
          {label}
        </Typography>
        <Typography variant="body1" sx={{ fontWeight: 600 }}>
          {value}
        </Typography>
      </Box>
    </Box>
  );
}

export default function ProfilePage() {
  const { token, userId, clearSession } = useAuth();
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [error, setError] = useState("");
  const [confirmOpen, setConfirmOpen] = useState(false);

  useEffect(() => {
    async function load() {
      if (!userId) {
        setError("Token does not include user id.");
        return;
      }
      try {
        setError("");
        const data = await getUserById(userId, token);
        setUser(data);
      } catch (e) {
        if (e.status === 401) clearSession();
        setError(e.message);
      }
    }
    load();
  }, [token, userId, clearSession]);

  async function handleDeleteUser() {
    if (!userId) return;
    try {
      await deleteUser(userId, token);
      clearSession();
      navigate("/register");
    } catch (e) {
      setError(e.message);
    } finally {
      setConfirmOpen(false);
    }
  }

  return (
    <Box sx={{ maxWidth: 600, mx: "auto" }}>
      <Card
        sx={{
          overflow: "visible",
          position: "relative"
        }}
      >
        <CardContent sx={{ p: { xs: 3, sm: 5 } }}>
          {/* Header */}
          <Stack alignItems="center" sx={{ mb: 4 }}>
            <Avatar
              sx={{
                width: 88,
                height: 88,
                mb: 2,
                background: "linear-gradient(135deg, #7c3aed, #06d6a0)",
                fontSize: "2rem",
                fontWeight: 800,
                boxShadow: "0 8px 32px rgba(124,58,237,0.4)",
              }}
            >
              {user?.username?.slice(0, 1).toUpperCase() || <PersonIcon sx={{ fontSize: 40 }} />}
            </Avatar>
            <Typography variant="h4" sx={{ fontWeight: 800, mb: 0.5 }}>
              {user?.username || "—"}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              PlayLog Member
            </Typography>
          </Stack>

          <ErrorAlert message={error} />
          {!user && !error ? (
            <Stack alignItems="center" sx={{ py: 4 }}>
              <CircularProgress />
            </Stack>
          ) : null}

          {user ? (
            <Stack spacing={2}>
              <InfoRow
                icon={<AccountCircleIcon sx={{ color: "primary.light", fontSize: 20 }} />}
                label="Username"
                value={user.username}
              />
              <InfoRow
                icon={<EmailIcon sx={{ color: "secondary.main", fontSize: 20 }} />}
                label="Email"
                value={user.email}
              />

              <Divider sx={{ my: 1 }} />

              <Box
                sx={{
                  p: 2.5,
                  borderRadius: 3,
                  background: "rgba(244,63,94,0.05)",
                  border: "1px solid rgba(244,63,94,0.15)",
                }}
              >
                <Typography variant="subtitle2" sx={{ fontWeight: 700, color: "error.main", mb: 0.5 }}>
                  Danger Zone
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Permanently delete your account and all associated data.
                </Typography>
                <Button
                  color="error"
                  variant="contained"
                  startIcon={<DeleteForeverIcon />}
                  onClick={() => setConfirmOpen(true)}
                  sx={{
                    background: "linear-gradient(135deg, #f43f5e, #be123c)",
                    "&:hover": { background: "linear-gradient(135deg, #fb7185, #f43f5e)" },
                  }}
                >
                  Delete Account
                </Button>
              </Box>
            </Stack>
          ) : null}
        </CardContent>
      </Card>

      {/* Confirm dialog */}
      <Dialog
        open={confirmOpen}
        onClose={() => setConfirmOpen(false)}
        PaperProps={{
          sx: {
            background: "#12121e",
            border: "1px solid rgba(244,63,94,0.3)",
            borderRadius: 4,
          },
        }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Delete Account?</DialogTitle>
        <DialogContent>
          <DialogContentText color="text.secondary">
            This action is permanent and cannot be undone. All your reviews and data will be lost.
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button onClick={() => setConfirmOpen(false)} variant="outlined">
            Cancel
          </Button>
          <Button
            onClick={handleDeleteUser}
            color="error"
            variant="contained"
            sx={{ background: "linear-gradient(135deg, #f43f5e, #be123c)" }}
          >
            Yes, Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}