import { createContext, useEffect, useMemo, useState } from "react";
import { login as loginApi, logout as logoutApi } from "../api/authApi";
import { getUserIdFromToken } from "../utils/jwt";

export const AuthContext = createContext(null);

const TOKEN_KEY = "playlog_token";

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));

  useEffect(() => {
    if (token) {
      localStorage.setItem(TOKEN_KEY, token);
    } else {
      localStorage.removeItem(TOKEN_KEY);
    }
  }, [token]);

  const value = useMemo(() => {
    const userId = token ? getUserIdFromToken(token) : null;

    return {
      token,
      userId,
      isAuthenticated: Boolean(token),
      async login(username, password) {
        const result = await loginApi(username, password);
        setToken(result.accessToken);
      },
      async logout() {
        if (token) {
          try {
            await logoutApi(token);
          } catch {
            // Local sign-out should still happen even if backend token is expired.
          }
        }
        setToken(null);
      },
      clearSession() {
        setToken(null);
      },
    };
  }, [token]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

