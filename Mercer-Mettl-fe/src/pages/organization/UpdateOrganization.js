import { useState, useEffect } from 'react';
import { getMyProfile } from '../../api/userApi';
import axios from '../../api/axios';
import ApiAlert from '../../components/ApiAlert';
import '../../styles/form.css';

export default function UpdateOrganization() {
  const [orgId, setOrgId] = useState(null);
  const [form, setForm] = useState({
    name: '',
    contactEmail: '',
    isActive: true
  });

  const [alert, setAlert] = useState({
    type: '',
    message: ''
  });

  // Fetch orgId dynamically
  useEffect(() => {
    getMyProfile()
      .then(res => {
        setOrgId(res.data.orgId);
      })
      .catch(() => {
        setAlert({
          type: 'error',
          message: 'Failed to load organization'
        });
      });
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: name === 'isActive' ? value === 'true' : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!orgId) return;

    setAlert({ type: '', message: '' });

    try {
      const res = await axios.put(`/org/update/${orgId}`, {
        ...form,
        updatedAt: new Date().toISOString()
      });

      setAlert({
        type: 'success',
        message: res.data.message
      });
    } catch (err) {
      setAlert({
        type: 'error',
        message: err.response?.data?.message || 'Error updating organization'
      });
    }
  };

  return (
    <div className="form-wrapper">
      <form className="form-container" onSubmit={handleSubmit}>
        <h3>Update Organization</h3>

        <ApiAlert
          type={alert.type}
          message={alert.message}
          onClose={() => setAlert({ type: '', message: '' })}
        />

        <label>Organization Name</label>
        <input
          name="name"
          value={form.name}
          onChange={handleChange}
          required
        />

        <label>Contact Email</label>
        <input
          type="email"
          name="contactEmail"
          value={form.contactEmail}
          onChange={handleChange}
          required
        />

        <label>Status</label>
        <select
          name="isActive"
          value={form.isActive.toString()}
          onChange={handleChange}
        >
          <option value="true">Active</option>
          <option value="false">Inactive</option>
        </select>

        <button type="submit" disabled={!orgId}>
          Update Organization
        </button>
      </form>
    </div>
  );
}
