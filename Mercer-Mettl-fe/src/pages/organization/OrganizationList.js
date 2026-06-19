import { useEffect, useState } from 'react';
import { getAllOrgs } from '../../api/orgApi';
import '../../styles/table.css';

export default function OrganizationList({ onSelectOrg }) {
  const [orgs, setOrgs] = useState([]);

  useEffect(() => {
    getAllOrgs().then(res => setOrgs(res.data));
  }, []);

  return (
    <div className="table-container-org">
      <h2 className="table-title">Organizations List</h2>

      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Domain</th>
            <th>Contact Email</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>
          {orgs.map(org => (
            <tr
              key={org.orgId}
              className="clickable-row"
              onClick={() => onSelectOrg(org)}
            >
              <td>{org.name}</td>
              <td>{org.domain}</td>
              <td>{org.contactEmail}</td>
              <td>{org.isActive ? 'Active' : 'Inactive'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
