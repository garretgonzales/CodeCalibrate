import { API_BASE_URL } from "./apiConfig";
import { createApiError } from "./apiError";

export async function login(credentials) {
  const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentials),
  });

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw createApiError(response, body, "Unable to log in.");
  }

  return body;
}

export async function register(registration) {
  const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(registration),
  });

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw createApiError(response, body, "Unable to create the account.");
  }

  return body;
}

export async function getCurrentUser(token) {
  const response = await fetch(`${API_BASE_URL}/api/auth/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw createApiError(response, body, "Unable to verify the session.");
  }

  return body;
}
