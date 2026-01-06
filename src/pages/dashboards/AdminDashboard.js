import AppLayout from '../../layout/AppLayout';

export default function AdminDashboard() {
  const adminMenu = [
    'Dashboard',
    'Create Organization',
    'View All Organizations',
    'Create Org Admin',
    'Change User Roles'
  ];

  return (
    <AppLayout menu={adminMenu}>
      <h2>Admin Dashboard</h2>
      <p>Welcome, Admin 👋</p>
    </AppLayout>
  );
}
