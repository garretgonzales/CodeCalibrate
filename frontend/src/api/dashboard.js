import { API_BASE_URL } from "./apiConfig";
import { createApiError } from "./apiError";

export async function getDashboard(token) {
  const response = await fetch(`${API_BASE_URL}/api/dashboard`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw createApiError(response, body, "Unable to load your dashboard.");
  }

  return body;
}
