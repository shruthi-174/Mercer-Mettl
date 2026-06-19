import { useState } from 'react';
import { createOrg } from '../../api/orgApi';
import '../../styles/form.css';

export default function CreateOrganization() {
  const [name, setName] = useState('');
  const [domain, setDomain] = useState('');
  const [contactEmail, setContactEmail] = useState('');
  const [isActive, setIsActive] = useState(true);

  const submit = async (e) => {
    e.preventDefault();

    const payload = {
      name,
      domain,
      contactEmail,
      isActive,
      createdAt: new Date().toISOString()
    };

    try {
      await createOrg(payload);
      alert('Organization created successfully');

      // reset
      setName('');
      setDomain('');
      setContactEmail('');
      setIsActive(true);
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to create organization');
    }
  };

  return (
    <div className="form-wrapper">
      <form className="form-container" onSubmit={submit}>
        <h3>Create Organization</h3>

        <label>Organization Name</label>
        <input
          value={name}
          onChange={e => setName(e.target.value)}
          required
        />

        <label>Domain</label>
        <input
          placeholder="example.com"
          value={domain}
          onChange={e => setDomain(e.target.value)}
          required
        />

        <label>Contact Email</label>
        <input
          type="email"
          value={contactEmail}
          onChange={e => setContactEmail(e.target.value)}
          required
        />

        <label>Status</label>
        <select
          value={isActive}
          onChange={e => setIsActive(e.target.value === 'true')}
        >
          <option value="true">Active</option>
          <option value="false">Inactive</option>
        </select>

        <button type="submit">Create Organization</button>
      </form>
    </div>
  );
}
