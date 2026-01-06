import { useState } from 'react';
import { forgotPassword } from '../../api/authApi';


export default function ForgotPassword() {
const [email, setEmail] = useState('');


const submit = async () => {
await forgotPassword(email);
alert('If email exists, reset token logged in backend');
};


return (
<>
<input placeholder='Email' onChange={e => setEmail(e.target.value)} />
<button onClick={submit}>Send</button>
</>
);
}