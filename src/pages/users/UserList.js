import { useEffect, useState } from 'react';
import { getOrgUsers, deleteUser } from '../../api/userApi';

export default function UserList() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    getOrgUsers().then(res => setUsers(res.data));
  }, []);

  const remove = async id => {
    await deleteUser(id);
    setUsers(users.filter(u => u.id !== id));
  };

  return (
    <table>
      <thead>
        <tr>
          <th>Email</th>
          <th>Role</th>
          <th />
        </tr>
      </thead>
      <tbody>
        {users.map(u => (
          <tr key={u.id}>
            <td>{u.email}</td>
            <td>{u.role}</td>
            <td>
              <button onClick={() => remove(u.id)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
