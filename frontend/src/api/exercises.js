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

export async function getExerciseById(exerciseId) {
  const response = await fetch(`${API_BASE_URL}/api/exercises/${exerciseId}`);

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(body.message ?? "Unable to load the exercise.");
  }

  return body;
}

export async function submitExercise(exerciseId, sourceCode, token) {
  const response = await fetch(
    `${API_BASE_URL}/api/exercises/${exerciseId}/submissions`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ sourceCode }),
    },
  );

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(body.message ?? "Unable to submit the exercise.");
  }

  return body;
}
