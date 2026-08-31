import "../style/LogoutButton.css";

function LogoutButton({ onLogout }) {
  return (
    <button
      className="mt-4 inline-flex items-center justify-center rounded-lg bg-ink-700 px-4 py-2.5 font-semibold text-white shadow-xs transition hover:bg-ink-950 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-500"
      type="button"
      onClick={onLogout}>
      Log out
    </button>
  );
}

export default LogoutButton;
