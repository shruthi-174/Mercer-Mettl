import Sidebar from '../components/Sidebar';
import Navbar from '../components/Navbar';
import '../styles/layout.css';

export default function AppLayout({ children, menu = [] }) {
  return (
    <div className="layout">
      <Sidebar menu={menu} />
      <div className="content">
        <Navbar />
        <div className="page">{children}</div>
      </div>
    </div>
  );
}
