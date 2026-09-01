import "../style/LogoutButton.css";

function LogoutButton({ onLogout }) {
  return (
    <button
      className="mt-4 inline-flex items-center justify-center rounded-lg bg-primary px-4 py-2.5 font-semibold text-primary-contrast shadow-xs transition hover:bg-primary-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-accent-400"
      type="button"
      onClick={onLogout}>
      Log out
    </button>
  );
}

export default LogoutButton;
