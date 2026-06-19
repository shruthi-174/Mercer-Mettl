import '../styles/apiAlert.css';

export default function ApiAlert({ type = 'success', message, onClose }) {
  if (!message) return null;

  return (
    <div className={`api-alert ${type}`}>
      <span>{message}</span>
      <button onClick={onClose}>✕</button>
    </div>
  );
}
