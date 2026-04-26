import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    mode: "dark",
    primary: {
      main: "#7c3aed",
      light: "#a78bfa",
      dark: "#5b21b6",
      contrastText: "#ffffff",
    },
    secondary: {
      main: "#06d6a0",
      light: "#34d399",
      dark: "#059669",
      contrastText: "#000000",
    },
    background: {
      default: "#09090f",
      paper: "#12121e",
    },
    text: {
      primary: "#f1f0ff",
      secondary: "#8b8aa8",
    },
    divider: "rgba(124, 58, 237, 0.2)",
    error: {
      main: "#f43f5e",
    },
    success: {
      main: "#06d6a0",
    },
  },
  shape: {
    borderRadius: 16,
  },
  typography: {
    fontFamily: '"Syne", "DM Sans", sans-serif',
    h1: { fontWeight: 800, letterSpacing: "-0.03em" },
    h2: { fontWeight: 800, letterSpacing: "-0.02em" },
    h3: { fontWeight: 700, letterSpacing: "-0.02em" },
    h4: { fontWeight: 700, letterSpacing: "-0.01em" },
    h5: { fontWeight: 600 },
    h6: { fontWeight: 600 },
    button: { fontWeight: 600, textTransform: "none", letterSpacing: "0.01em" },
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: `
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;500;600;700;800&family=DM+Sans:wght@300;400;500;600&display=swap');

        body {
          background: #09090f;
          background-image:
            radial-gradient(ellipse at 20% 0%, rgba(124, 58, 237, 0.15) 0%, transparent 50%),
            radial-gradient(ellipse at 80% 100%, rgba(6, 214, 160, 0.08) 0%, transparent 50%);
          min-height: 100vh;
        }

        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: #09090f; }
        ::-webkit-scrollbar-thumb { background: #7c3aed; border-radius: 3px; }
      `,
    },
    MuiCard: {
      styleOverrides: {
        root: {
          background: "rgba(18, 18, 30, 0.85)",
          backdropFilter: "blur(20px)",
          border: "1px solid rgba(124, 58, 237, 0.15)",
          boxShadow: "0 4px 40px rgba(0, 0, 0, 0.4), inset 0 1px 0 rgba(255,255,255,0.05)",
          transition: "border-color 0.3s ease, box-shadow 0.3s ease",
          "&:hover": {
            borderColor: "rgba(124, 58, 237, 0.35)",
            boxShadow: "0 8px 60px rgba(0, 0, 0, 0.5), 0 0 0 1px rgba(124, 58, 237, 0.2)",
          },
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 10,
          padding: "10px 22px",
          fontFamily: '"Syne", sans-serif',
          fontWeight: 600,
        },
        contained: {
          background: "linear-gradient(135deg, #7c3aed 0%, #5b21b6 100%)",
          boxShadow: "0 4px 20px rgba(124, 58, 237, 0.4)",
          "&:hover": {
            background: "linear-gradient(135deg, #8b5cf6 0%, #6d28d9 100%)",
            boxShadow: "0 6px 28px rgba(124, 58, 237, 0.6)",
          },
        },
        outlined: {
          borderColor: "rgba(124, 58, 237, 0.5)",
          color: "#a78bfa",
          "&:hover": {
            borderColor: "#7c3aed",
            background: "rgba(124, 58, 237, 0.1)",
          },
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          "& .MuiOutlinedInput-root": {
            borderRadius: 12,
            background: "rgba(255,255,255,0.03)",
            "& fieldset": {
              borderColor: "rgba(124, 58, 237, 0.25)",
            },
            "&:hover fieldset": {
              borderColor: "rgba(124, 58, 237, 0.5)",
            },
            "&.Mui-focused fieldset": {
              borderColor: "#7c3aed",
              boxShadow: "0 0 0 3px rgba(124, 58, 237, 0.1)",
            },
          },
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          fontFamily: '"Syne", sans-serif',
          fontWeight: 600,
        },
        colorPrimary: {
          background: "rgba(124, 58, 237, 0.2)",
          color: "#a78bfa",
          border: "1px solid rgba(124, 58, 237, 0.4)",
        },
        colorSecondary: {
          background: "rgba(6, 214, 160, 0.15)",
          color: "#34d399",
          border: "1px solid rgba(6, 214, 160, 0.3)",
        },
      },
    },
    MuiDivider: {
      styleOverrides: {
        root: {
          borderColor: "rgba(124, 58, 237, 0.15)",
        },
      },
    },
    MuiPagination: {
      styleOverrides: {
        root: {
          "& .MuiPaginationItem-root": {
            borderRadius: 8,
            "&.Mui-selected": {
              background: "linear-gradient(135deg, #7c3aed 0%, #5b21b6 100%)",
            },
          },
        },
      },
    },
    MuiAlert: {
      styleOverrides: {
        root: {
          borderRadius: 12,
        },
      },
    },
  },
});

export default theme;