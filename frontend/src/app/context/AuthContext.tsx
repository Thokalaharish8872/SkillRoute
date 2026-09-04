import { createContext, useContext, useState, useEffect, useCallback, ReactNode } from "react";
import { post } from "../utils/api";

interface AuthContextType {
  token: string | null;
  email: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(null);
  const [email, setEmail] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Load token when application starts
  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedEmail = localStorage.getItem("email");

    console.log("Stored token:", storedToken);
    console.log("Stored email:", storedEmail);

    if (storedToken) {
      // Check if token is expired
      try {
        const payload = JSON.parse(atob(storedToken.split('.')[1]));
        const expirationTime = payload.exp * 1000;
        
        if (Date.now() >= expirationTime) {
          console.log("Token expired, clearing localStorage");
          localStorage.removeItem("token");
          localStorage.removeItem("email");
          setToken(null);
          setEmail(null);
        } else {
          setToken(storedToken);
          if (storedEmail) {
            setEmail(storedEmail);
          }
        }
      } catch (error) {
        console.error("Error parsing token:", error);
        localStorage.removeItem("token");
        localStorage.removeItem("email");
        setToken(null);
        setEmail(null);
      }
    }

    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    try {
      const response = await post("/auth/login", {
        email,
        password,
      });

      console.log("Login response status:", response.status);

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Login failed");
      }

      const data = await response.json();

      console.log("Login response:", data);

      // Store in React state
      setToken(data.token);
      setEmail(data.email);

      // Store in browser localStorage
      localStorage.setItem("token", data.token);
      localStorage.setItem("email", data.email);

      console.log("Token stored:", localStorage.getItem("token"));
      console.log("Email stored:", localStorage.getItem("email"));
    } catch (error) {
      console.error("Login error:", error);
      throw error;
    }
  };

  const register = async (
    name: string,
    email: string,
    password: string
  ) => {
    try {
      const response = await post("/auth/register", {
        username: name,
        email,
        password,
      });

      console.log("Register response status:", response.status);

      if (response.status !== 201) {
        const error = await response.json();
        throw new Error(error.message || "Registration failed");
      }

      console.log("Registration successful");

      // Automatically login after registration
      await login(email, password);
    } catch (error) {
      console.error("Registration error:", error);
      throw error;
    }
  };

  const logout = useCallback(() => {
    setToken(null);
    setEmail(null);

    localStorage.removeItem("token");
    localStorage.removeItem("email");

    console.log("Token and email removed from localStorage");
  }, []);

  // Check token expiration periodically
  useEffect(() => {
    const interval = setInterval(() => {
      const storedToken = localStorage.getItem("token");
      if (storedToken) {
        try {
          const payload = JSON.parse(atob(storedToken.split('.')[1]));
          const expirationTime = payload.exp * 1000;
          
          if (Date.now() >= expirationTime) {
            console.log("Token expired, redirecting to login");
            logout();
            window.location.href = "/auth";
          }
        } catch (error) {
          console.error("Error checking token expiration:", error);
          logout();
          window.location.href = "/auth";
        }
      }
    }, 60000); // Check every minute

    return () => clearInterval(interval);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <AuthContext.Provider
      value={{
        token,
        email,
        login,
        register,
        logout,
        isAuthenticated: !!token,
        isLoading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);

  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return context;
};