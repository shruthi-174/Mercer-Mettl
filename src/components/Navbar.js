import { useContext, useState } from 'react';
import { AuthContext } from '../context/AuthContext';
import '../styles/navbar.css';

export default function Navbar() {
  const { user, logout } = useContext(AuthContext);
  const [open, setOpen] = useState(false);

  return (
    <header className="navbar">

      {/* RIGHT ICON */}
      <div className="navbar-right">
        <div className="user-icon" onClick={() => setOpen(!open)}>
          👤
        </div>

        {open && (
          <div className="dropdown">
            <button onClick={logout}>Logout</button>
          </div>
        )}
      </div>
    </header>
  );
}
