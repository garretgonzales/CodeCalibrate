import { API_BASE_URL } from "./apiConfig";

export async function getRecommendedExercise(token) {
  const response = await fetch(`${API_BASE_URL}/api/exercises/recommended`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(body.message ?? "Unable to load the recommended exercise.");
  }

  return body;
}
