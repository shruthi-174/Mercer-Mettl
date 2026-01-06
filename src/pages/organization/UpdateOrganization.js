import { useState } from 'react';
import { updateOrg } from '../../api/orgApi';

export default function UpdateOrganization({ orgId }) {
  const [name, setName] = useState('');

  const submit = async e => {
    e.preventDefault();
    await updateOrg(orgId, { name });
    alert('Updated');
  };

  return (
    <form onSubmit={submit}>
      <input placeholder="New Name" onChange={e => setName(e.target.value)} />
      <button>Update</button>
    </form>
  );
}
