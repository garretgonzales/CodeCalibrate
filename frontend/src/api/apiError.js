export function createApiError(response, body, fallbackMessage) {
  const error = new Error(body.message ?? fallbackMessage);
  error.status = response.status;
  error.fieldErrors = body.errors ?? {};
  return error;
}
