import { changeRole } from '../../api/userApi';

export default function ChangeRole({ userId }) {
  const update = async role => {
    await changeRole(userId, { role });
    alert('Role updated');
  };

  return (
    <>
      <button onClick={() => update('RECRUITER')}>Recruiter</button>
      <button onClick={() => update('PROCTOR')}>Proctor</button>
    </>
  );
}
