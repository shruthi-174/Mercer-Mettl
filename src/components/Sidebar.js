import '../styles/sidebar.css';

export default function Sidebar({ menu = [] }) {
  return (
    <aside className="sidebar">
      <h3>Mercer | Mettl</h3>

      <ul>
        {menu.map(item => (
          <li key={item}>{item}</li>
        ))}
      </ul>
    </aside>
  );
}
