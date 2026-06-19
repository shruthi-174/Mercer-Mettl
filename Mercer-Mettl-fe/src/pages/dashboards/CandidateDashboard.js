import AppLayout from "../../layout/AppLayout";

import CandidateHome from "../assessment/candidate/CandidateProfile";
import CandidateTests from "../assessment/candidate/CandidateTests";
import CandidateResults from "../assessment/candidate/CandidateResults";

export default function CandidateDashboard() {
  const menu = ["Dashboard", "My Tests", "Results"];

  return (
    <AppLayout menu={menu}>
      {(active) => (
        <>
          {active === "Dashboard" && <CandidateHome />}

          {active === "My Tests" && <CandidateTests />}

          {active === "Results" && <CandidateResults />}
        </>
      )}
    </AppLayout>
  );
}
