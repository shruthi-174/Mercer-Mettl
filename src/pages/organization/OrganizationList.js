import { useEffect, useState } from 'react';
import { getAllOrgs } from '../../api/orgApi';
import '../../styles/table.css';

export default function OrganizationList() {
  const [orgs, setOrgs] = useState([]);

  useEffect(() => {
    getAllOrgs().then(res => setOrgs(res.data));
  }, []);

  return (
    <div className="page">
      <h2>Organizations</h2>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Code</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {orgs.map(o => (
            <tr key={o.id}>
              <td>{o.name}</td>
              <td>{o.code}</td>
              <td>{o.active ? 'Active' : 'Inactive'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
