import { useContext, useState } from 'react';
import { AuthContext } from '../../context/AuthContext';
import '../../styles/login.css';
import loginLogo from '../../img/homepage.avif';

export default function Login() {
  const { loginUser } = useContext(AuthContext);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    await loginUser(email, password);
    window.location.href = '/dashboard';
  };

  return (
    <div className="login-page">
      <div className="login-card">

        {/* LEFT SIDE – BRAND / IMAGE */}
        <div className="login-left">
          <img
            src={loginLogo}
            alt="Mercer Mettl Login"
            className="login-image"
          />
        </div>

        {/* RIGHT SIDE – FORM */}
        <div className="login-right">
          <h2>Welcome!</h2>
          <p className="subtitle">Sign in to your Account</p>

          <form onSubmit={submit}>
            <input
              type="email"
              placeholder="Email Address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <div className="forgot">Forgot Password?</div>

            <div className="btn-group">
              <button type="submit" className="btn-primary">
                SIGN IN
              </button>
              <button type="button" className="btn-outline">
                SIGN UP
              </button>
            </div>
          </form>
        </div>

      </div>
    </div>
  );
}
