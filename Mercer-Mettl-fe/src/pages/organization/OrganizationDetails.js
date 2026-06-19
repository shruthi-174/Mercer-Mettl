import { useEffect, useState } from 'react';
import { getMyProfile, changeUserRole, deleteUser } from '../../api/userApi';
import { getOrganizationUsers, getOrganizationDetails } from '../../api/orgApi';
import ApiAlert from '../../components/ApiAlert';
import '../organization/org.css'

export default function OrganizationDetails() {
  const [orgId, setOrgId] = useState(null);
  const [org, setOrg] = useState(null);
  const [users, setUsers] = useState([]);
  const [alert, setAlert] = useState({ type: '', message: '' });

  useEffect(() => {
    getMyProfile().then(res => {
      setOrgId(res.data.orgId);
    });
  }, []);

  const loadData = async (id) => {
    const [orgRes, usersRes] = await Promise.all([
      getOrganizationDetails(id),
      getOrganizationUsers(id)
    ]);
    setOrg(orgRes.data);
    setUsers(usersRes.data);
  };

  useEffect(() => {
    if (orgId) loadData(orgId);
  }, [orgId]);

  const onRoleChange = async (userId, role) => {
    try {
      const res = await changeUserRole(userId, { role });
      setAlert({ type: 'success', message: res.data.message });
      loadData(orgId);
    } catch (err) {
      setAlert({
        type: 'error',
        message: err.response?.data?.message || 'Role change failed'
      });
    }
  };

  const onDelete = async (userId) => {
    try {
      const res = await deleteUser(userId);
      setAlert({ type: 'success', message: res.data.message });
      loadData(orgId);
    } catch (err) {
      setAlert({
        type: 'error',
        message: err.response?.data?.message || 'Delete failed'
      });
    }
  };

  return (
    <div className="table-container-org">

      <ApiAlert
        type={alert.type}
        message={alert.message}
        onClose={() => setAlert({ message: '' })}
      />

      {org && (
        <>
          <h2>{org.name}</h2>
          <p>{org.contactEmail}</p>
        </>
      )}

      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Change Role</th>
            <th>Delete</th>
          </tr>
        </thead>

        <tbody>
          {users.map(u => (
            <tr key={u.userId}>
              <td>{u.fullName}</td>
              <td>{u.email}</td>
              <td>{u.role}</td>

              <td>
                <span className={u.isActive ? 'active' : 'inactive'}>
                  {u.isActive ? 'ACTIVE' : 'INACTIVE'}
                </span>
              </td>

              <td>
                <select
                  value={u.role}
                  disabled={!u.isActive}
                  onChange={(e) => onRoleChange(u.userId, e.target.value)}
                >
                  <option value="RECRUITER">RECRUITER</option>
                  <option value="PROCTOR">PROCTOR</option>
                  <option value="CANDIDATE">CANDIDATE</option>
                </select>
              </td>

              <td>
                <button
                  disabled={!u.isActive}
                  onClick={() => onDelete(u.userId)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
