import "../style/LogoutButton.css";

function LogoutButton({ onLogout }) {
  return (
    <button className="logout-button" type="button" onClick={onLogout}>
      Log out
    </button>
  );
}

export default LogoutButton;
