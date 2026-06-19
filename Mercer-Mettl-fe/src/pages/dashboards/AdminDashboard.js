import AppLayout from '../../layout/AppLayout';
import RegisterUser from '../users/RegisterUser';
import CreateOrganization from '../organization/CreateOrganzation';
import OrganizationList from '../organization/OrganizationList';
import OrganizationUsers from '../organization/OrganizationUsers';
import { useState } from 'react';
import '../../styles/dashboard.css';


export default function AdminDashboard() {
  const adminMenu = [
    'Dashboard',
    'Create Organization',
    'View All Organizations',
    'Create Org Admin'
  ];

const [selectedOrg, setSelectedOrg] = useState(null);

  return (
    <AppLayout menu={adminMenu}>
      {(activeMenu) => (
        <>
          {activeMenu === 'Dashboard' && (
            <div className="Adminheading">
              <h2>Welcome, Admin 👋</h2>
            </div>
          )}

          {activeMenu === 'Create Organization' && (
            <CreateOrganization />
          )}

          {activeMenu === 'Create Org Admin' && (
            <RegisterUser mode="ORG_ADMIN" />
          )}

          {activeMenu === 'View All Organizations' && (
            <>
              {!selectedOrg ? (
                <OrganizationList onSelectOrg={setSelectedOrg} />
              ) : (
                <OrganizationUsers
                  org={selectedOrg}
                  onBack={() => setSelectedOrg(null)}
                />
              )}
              </>
          )}
        </>
      )}
    </AppLayout>
  );
}
