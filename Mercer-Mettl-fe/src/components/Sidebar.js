import '../styles/sidebar.css';

export default function Sidebar({ menu = [], onSelect }) {
  return (
    <aside className="sidebar">
      <h3>Mercer | Mettl</h3>

      <ul>
        {menu.map(item => (
          <li
            key={item}
            onClick={() => onSelect(item)}
          >
            {item}
          </li>
        ))}
      </ul>
    </aside>
  );
}
