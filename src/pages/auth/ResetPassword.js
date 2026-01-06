import { useState } from 'react';
import { resetPassword } from '../../api/authApi';


export default function ResetPassword() {
const [token, setToken] = useState('');
const [newPassword, setPassword] = useState('');


const submit = async () => {
await resetPassword({ token, newPassword });
alert('Password reset success');
};


return (
<>
<input placeholder='Reset Token' onChange={e => setToken(e.target.value)} />
<input placeholder='New Password' onChange={e => setPassword(e.target.value)} />
<button onClick={submit}>Reset</button>
</>
);
}