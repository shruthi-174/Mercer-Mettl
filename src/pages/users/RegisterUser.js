import { useState } from 'react';
import { registerUser } from '../../api/userApi';

export default function RegisterUser() {
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('RECRUITER');

  const submit = async e => {
    e.preventDefault();
    await registerUser({ email, role });
    alert('User Registered');
  };

  return (
    <form onSubmit={submit} className="form">
      <h2>Register User</h2>
      <input placeholder="Email" onChange={e => setEmail(e.target.value)} />
      <select onChange={e => setRole(e.target.value)}>
        <option>RECRUITER</option>
        <option>PROCTOR</option>
        <option>CANDIDATE</option>
      </select>
      <button>Create</button>
    </form>
  );
}
