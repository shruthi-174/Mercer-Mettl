import { useState } from 'react';
import Sidebar from '../components/Sidebar';
import Navbar from '../components/Navbar';
import '../styles/layout.css';

export default function AppLayout({ children, menu = [] }) {
  const [activeMenu, setActiveMenu] = useState('Dashboard');

  return (
    <div className="layout">
      <Sidebar
        menu={menu}
        onSelect={setActiveMenu}
      />

      <div className="content">
        <Navbar />
        <div className="page">
          {children(activeMenu)}
        </div>
      </div>
    </div>
  );
}
