import {
  AppBar,
  Box,
  Button,
  Container,
  Stack,
  Toolbar,
  Typography,
} from "@mui/material";
import { Link as RouterLink, useLocation, useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function Navbar() {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { to: "/games/popular", label: "Popular Games" },
    { to: "/reviews", label: "Reviews" },
    { to: "/profile", label: "Profile" },
  ];

  async function handleLogout() {
    await logout();
    navigate("/login");
  }

  return (
    <AppBar position="sticky" elevation={0}>
      <Container maxWidth="lg">
        <Toolbar disableGutters sx={{ justifyContent: "space-between", gap: 2 }}>
          <Stack direction="row" spacing={1} alignItems="center">
            <Box
              component="img"
              src="/playlog.png"
              alt="PlayLog logo"
              sx={{ width: 32, height: 32, objectFit: "contain", borderRadius: 1 }}
            />
            <Typography
              variant="h6"
              component={RouterLink}
              to="/games/popular"
              sx={{ color: "inherit", textDecoration: "none" }}
            >
              PlayLog
            </Typography>
          </Stack>

          {isAuthenticated ? (
            <Stack direction="row" spacing={1} alignItems="center" flexWrap="wrap">
              {menuItems.map((item) => (
                <Button
                  key={item.to}
                  component={RouterLink}
                  to={item.to}
                  color={location.pathname.startsWith(item.to) ? "secondary" : "inherit"}
                  variant={location.pathname.startsWith(item.to) ? "contained" : "text"}
                >
                  {item.label}
                </Button>
              ))}
              <Button color="inherit" onClick={handleLogout} variant="outlined">
                Logout
              </Button>
            </Stack>
          ) : (
            <Box>
              <Button component={RouterLink} to="/login" color="inherit" sx={{ mr: 1 }}>
                Login
              </Button>
              <Button component={RouterLink} to="/register" color="secondary" variant="contained">
                Register
              </Button>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
}


