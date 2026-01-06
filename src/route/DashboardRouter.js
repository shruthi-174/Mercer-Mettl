import { Navigate } from 'react-router-dom';
import { getUserRole } from '../utils/jwt';

import AdminDashboard from '../pages/dashboards/AdminDashboard';
import OrgAdminDashboard from '../pages/dashboards/OrgAdminDashboard';
import RecruiterDashboard from '../pages/dashboards/RecruiterDashboard';
import ProctorDashboard from '../pages/dashboards/ProctorDashboard';
import CandidateDashboard from '../pages/dashboards/CandidateDashboard';

export default function DashboardRouter() {
  const role = getUserRole();

  const dashboards = {
    ADMIN: <AdminDashboard />,
    ORG_ADMIN: <OrgAdminDashboard />,
    RECRUITER: <RecruiterDashboard />,
    PROCTOR: <ProctorDashboard />,
    CANDIDATE: <CandidateDashboard />
  };

  return dashboards[role] || <Navigate to="/login" />;
}
