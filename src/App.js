import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/auth/Login';
import DashboardRouter from './route/DashboardRouter';
import ForgotPassword from './pages/auth/ForgotPassword';
import ResetPassword from './pages/auth/ResetPassword';
import ProtectedRoute from './route/ProtectedRoute';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* DEFAULT ROUTE */}
        <Route path="/" element={<Navigate to="/login" replace />} />

        <Route path="/login" element={<Login />} />
        <Route path="/forgot" element={<ForgotPassword />} />
        <Route path="/reset" element={<ResetPassword />} />

        <Route
          path="/dashboard/*"
          element={
            <ProtectedRoute>
              <DashboardRouter />
            </ProtectedRoute>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}
