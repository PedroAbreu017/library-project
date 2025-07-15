import React from 'react';

const Navbar = ({ user, onLogout, onMenuClick }) => {
  const getUserInitials = (name) => {
    return name
      ?.split(' ')
      .map(word => word[0])
      .slice(0, 2)
      .join('')
      .toUpperCase() || 'U';
  };

  const formatRole = (role) => {
    switch (role) {
      case 'STUDENT':
        return 'Estudante';
      case 'LIBRARIAN':
        return 'Bibliotecário';
      default:
        return role;
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <button className="menu-button" onClick={onMenuClick}>
          <svg className="sidebar-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
        <h1 className="navbar-title">📚 Library Dashboard</h1>
      </div>

      <div className="navbar-right">
        {user && (
          <div className="user-info">
            <div className="user-avatar">
              {getUserInitials(user.name)}
            </div>
            <div className="user-details">
              <span className="user-name">{user.name}</span>
              <span className="user-role">{formatRole(user.role)}</span>
            </div>
          </div>
        )}
        
        <button className="logout-button" onClick={onLogout}>
          Sair
        </button>
      </div>
    </nav>
  );
};

export default Navbar;