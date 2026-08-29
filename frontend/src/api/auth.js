import { API_BASE_URL } from "./apiConfig";

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
    throw new Error(body.message ?? "Unable to log in.");
  }

  return body;
}
