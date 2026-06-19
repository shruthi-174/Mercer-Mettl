import { useState } from 'react';
import { createOrgAdmin, registerUser } from '../../api/userApi';
import '../../styles/form.css';

export default function RegisterUser({ mode }) {
  const [email, setEmail] = useState('');
  const [fullName, setFullName] = useState('');
  const [password, setPassword] = useState('');
  const [orgId, setOrgId] = useState('');
  const [role, setRole] = useState('RECRUITER');

  const submit = async (e) => {
    e.preventDefault();

    const payload = {
      email,
      password,
      fullName,
      orgId,
      role: mode === 'ORG_ADMIN' ? 'ORG_ADMIN' : role
    };

    if (mode === 'ORG_ADMIN') {
      await createOrgAdmin(payload);
    } else {
      await registerUser(payload);
    }

    alert('User created successfully');
  };

  return (
    <div className="form-wrapper">
      <form className="form-container" onSubmit={submit}>
        <h3>{mode === 'ORG_ADMIN' ? 'Create Org Admin' : 'Create User'}</h3>

        <label>Full Name</label>
        <input onChange={e => setFullName(e.target.value)} required />

        <label>Email</label>
        <input type="email" onChange={e => setEmail(e.target.value)} required />

        <label>Password</label>
        <input type="password" onChange={e => setPassword(e.target.value)} required />

        <label>Organization ID</label>
        <input onChange={e => setOrgId(e.target.value)} required />

        {mode !== 'ORG_ADMIN' && (
          <>
            <label>Role</label>
            <select onChange={e => setRole(e.target.value)}>
              <option>RECRUITER</option>
              <option>PROCTOR</option>
              <option>CANDIDATE</option>
            </select>
          </>
        )}

        <button type="submit">Create</button>
      </form>
    </div>
  );
}
