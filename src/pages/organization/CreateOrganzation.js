import { useState } from 'react';
import { createOrg } from '../../api/orgApi';
import '../../styles/form.css';

export default function CreateOrganization() {
  const [name, setName] = useState('');
  const [code, setCode] = useState('');

  const submit = async e => {
    e.preventDefault();
    await createOrg({ name, code });
    alert('Organization created');
  };

  return (
    <form onSubmit={submit} className="form">
      <h2>Create Organization</h2>
      <input placeholder="Org Name" onChange={e => setName(e.target.value)} />
      <input placeholder="Org Code" onChange={e => setCode(e.target.value)} />
      <button>Create</button>
    </form>
  );
}
