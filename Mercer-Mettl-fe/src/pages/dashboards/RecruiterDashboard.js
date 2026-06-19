import AppLayout from "../../layout/AppLayout";
import { useState, useEffect } from "react";

import CreateTest from "../assessment/CreateTest";
import TestsList from "../assessment/TestsList";
import Reports from "../assessment/Reports";
import TeamManagement from "../assessment/team/TeamManagement";
import { getMyProfile } from "../../api/candidateApi";

import "../../styles/dashboard.css";

export default function RecruiterDashboard() {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    const res = await getMyProfile();

    setProfile(res.data);
  };

  const recruiterMenu = [
    "Dashboard",
    "Create Test",
    "Tests",
    "Teams",
    "Reports",
  ];

  const [selectedTestId, setSelectedTestId] = useState(null);

  return (
    <AppLayout menu={recruiterMenu}>
      {(activeMenu) => (
        <>
          {activeMenu === "Dashboard" && (
            <div className="Adminheading">
              <h2>Welcome, Recruiter 👋</h2>
              <p>Create and manage assessments for candidates</p>
            </div>
          )}

          {activeMenu === "Create Test" && <CreateTest />}

          {activeMenu === "Tests" && <TestsList />}
          {activeMenu === "Reports" && <Reports />}
          {activeMenu === "Teams" && (
    profile ?
    <TeamManagement orgId={profile.orgId}/>
    :
    <p>Loading profile...</p>
)}
        </>
      )}
    </AppLayout>
  );
}
