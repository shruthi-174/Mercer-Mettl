import { useEffect, useState } from 'react';
import { getOrgUsers } from '../../api/userApi';
import '../../styles/table.css';

export default function OrganizationUsers({ org, onBack }) {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    getOrgUsers(org.orgId).then(res => setUsers(res.data));
  }, [org]);

  return (
    <div className="table-container">
      <button className="back-btn" onClick={onBack}>
        ← Back to Organizations
      </button>

      <h2 className="table-title">{org.name} — Users</h2>

      <table>
        <thead>
          <tr>
            <th>Full Name</th>
            <th>Email</th>
            <th>Role</th>
          </tr>
        </thead>

        <tbody>
          {users.map(user => (
            <tr key={user.userId}>
              <td>{user.fullName}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
