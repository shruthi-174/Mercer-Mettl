import AppLayout from "../../layout/AppLayout";
import RegisterUser from "../users/RegisterUser";
import UpdateOrganization from "../organization/UpdateOrganization";
import OrganizationDetails from "../organization/OrganizationDetails";
import TeamSelector from "../assessment/team/TeamSelector";
import { getMyProfile } from "../../api/candidateApi";
import "../../styles/dashboard.css";

import {useEffect,useState} from "react";

export default function OrgAdminDashboard() {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    const res = await getMyProfile();

    setProfile(res.data);
  };

  const menu = [
    "Dashboard",
    "Update Organization",
    "Add User",
    "View Organization Details",
    "Teams",
  ];

  return (
    <AppLayout menu={menu}>
      {(activeMenu) => (
        <>
          {activeMenu === "Dashboard" && (
            <div className="Adminheading">
              <h2>Welcome, Org Admin 👋</h2>
              <p>Manage your organization and users from here.</p>
            </div>
          )}

          {activeMenu === "Update Organization" && <UpdateOrganization />}

          {activeMenu === "Add User" && <RegisterUser />}

          {activeMenu === "View Organization Details" && (
            <OrganizationDetails />
          )}

          {activeMenu === "Teams" && profile && (
    <TeamSelector orgId={profile.orgId} />
)}
        </>
      )}
    </AppLayout>
  );
}
