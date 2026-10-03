/// <reference types="vite/client" />
import axios from "axios";

export const API_BASE_URL = import.meta.env.VITE_API_URL || "";
export const VITE_API_URL = API_BASE_URL;

axios.defaults.baseURL = API_BASE_URL;

/**
 * Check if token is expired by decoding JWT
 */
export const isTokenExpired = (token: string): boolean => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = payload.exp * 1000; // Convert to milliseconds
    return Date.now() >= expirationTime;
  } catch (error) {
    console.error('Error checking token expiration:', error);
    return true; // Assume expired if we can't check
  }
};

/**
 * Decode the JWT stored in localStorage and return the userId claim.
 * Returns 0 if the token is missing or cannot be decoded.
 */
export const getUserIdFromToken = (): number => {
  try {
    const token = localStorage.getItem('token');
    if (!token) return 0;
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.userId ?? 0;
  } catch {
    return 0;
  }
};


/**
 * Show toast notification for token expiration
 */
const showTokenExpiredMessage = () => {
  // Remove any existing toast
  const existingToast = document.getElementById('token-expired-toast');
  if (existingToast) {
    existingToast.remove();
  }
  
  // Create a temporary toast element
  const toast = document.createElement('div');
  toast.id = 'token-expired-toast';
  toast.className = 'fixed top-4 right-4 bg-yellow-500 text-white px-6 py-3 rounded-lg shadow-lg z-50 font-medium';
  toast.textContent = 'Your session has expired. Please login again.';
  document.body.appendChild(toast);
  
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transition = 'opacity 0.3s';
    setTimeout(() => toast.remove(), 300);
  }, 3000);
};

// Global Axios Request Interceptor to include JWT token with every request
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      if (isTokenExpired(token)) {
        console.log('Token expired in axios interceptor, logging out user');
        showTokenExpiredMessage();
        localStorage.removeItem("token");
        localStorage.removeItem("email");
        window.location.href = "/auth";
        return Promise.reject(new Error("Token expired"));
      }
      config.headers = config.headers || {};
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Global Axios Response Interceptor to handle 401 Unauthorized
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.log('Received 401 response in axios interceptor, logging out user');
      showTokenExpiredMessage();
      localStorage.removeItem("token");
      localStorage.removeItem("email");
      window.location.href = "/auth";
    }
    return Promise.reject(error);
  }
);

/**
 * Make API call with automatic token expiration handling
 */
export const apiCall = async (
  endpoint: string,
  options: RequestInit = {}
): Promise<Response> => {
  const token = localStorage.getItem("token");
  
  // Check if token exists and is not expired
  if (token && isTokenExpired(token)) {
    console.log('Token expired, logging out user');
    showTokenExpiredMessage();
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    window.location.href = "/auth";
    throw new Error("Token expired");
  }

  const headers: HeadersInit = {
    "Content-Type": "application/json",
    ...options.headers,
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  // Handle 401 Unauthorized (token expired or invalid)
  if (response.status === 401) {
    console.log('Received 401 response, logging out user');
    showTokenExpiredMessage();
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    window.location.href = "/auth";
    throw new Error("Unauthorized - Please login again");
  }

  return response;
};

/**
 * GET request
 */
export const get = async (endpoint: string): Promise<Response> => {
  return apiCall(endpoint, { method: "GET" });
};

/**
 * POST request
 */
export const post = async (endpoint: string, data: any): Promise<Response> => {
  return apiCall(endpoint, {
    method: "POST",
    body: JSON.stringify(data),
  });
};

/**
 * PUT request
 */
export const put = async (endpoint: string, data: any): Promise<Response> => {
  return apiCall(endpoint, {
    method: "PUT",
    body: JSON.stringify(data),
  });
};

/**
 * DELETE request
 */
export const del = async (endpoint: string): Promise<Response> => {
  return apiCall(endpoint, { method: "DELETE" });
};
