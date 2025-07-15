import React, { useState, useEffect } from 'react';
import { userService } from '../services/api';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchUsers();
  }, []);

  useEffect(() => {
    // Filtrar usuários baseado no termo de busca
    if (searchTerm.trim() === '') {
      setFilteredUsers(users);
    } else {
      const filtered = users.filter(user => 
        user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredUsers(filtered);
    }
  }, [users, searchTerm]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await userService.getUsers();
      setUsers(data);
    } catch (error) {
      console.error('Erro ao buscar usuários:', error);
      setError('Erro ao carregar usuários. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    });
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

  const formatPhone = (phone) => {
    if (!phone) return '-';
    // Formatar telefone brasileiro (11) 99999-9999
    const cleaned = phone.replace(/\D/g, '');
    if (cleaned.length === 11) {
      return `(${cleaned.slice(0, 2)}) ${cleaned.slice(2, 7)}-${cleaned.slice(7)}`;
    }
    return phone;
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Carregando usuários...</p>
      </div>
    );
  }

  return (
    <div className="users-page">
      <div className="page-header">
        <h1 className="page-title">Usuários</h1>
        <p className="page-subtitle">Gerencie os usuários do sistema</p>
      </div>

      {error && (
        <div className="alert alert-error" style={{ marginBottom: '24px' }}>
          {error}
          <button 
            onClick={fetchUsers}
            style={{ marginLeft: '12px', padding: '4px 8px', fontSize: '12px' }}
          >
            Tentar novamente
          </button>
        </div>
      )}

      {/* Estatísticas rápidas */}
      <div className="stats-grid" style={{ marginBottom: '32px' }}>
        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon blue">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <span className="stat-title">Total de Usuários</span>
          </div>
          <div className="stat-value">{users.length}</div>
        </div>

        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon green">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <span className="stat-title">Usuários Ativos</span>
          </div>
          <div className="stat-value">
            {users.filter(user => user.isActive).length}
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon orange">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M12 14l9-5-9-5-9 5 9 5z" />
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M12 14l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14z" />
              </svg>
            </div>
            <span className="stat-title">Estudantes</span>
          </div>
          <div className="stat-value">
            {users.filter(user => user.role === 'STUDENT').length}
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon purple">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </div>
            <span className="stat-title">Bibliotecários</span>
          </div>
          <div className="stat-value">
            {users.filter(user => user.role === 'LIBRARIAN').length}
          </div>
        </div>
      </div>

      {/* Tabela de usuários */}
      <div className="table-container">
        <div className="table-header">
          <h2 className="table-title">Lista de Usuários</h2>
          <input
            type="text"
            placeholder="Buscar por nome ou email..."
            value={searchTerm}
            onChange={handleSearch}
            className="search-box"
          />
        </div>

        {filteredUsers.length > 0 ? (
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Email</th>
                <th>Telefone</th>
                <th>Tipo</th>
                <th>Status</th>
                <th>Data de Cadastro</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map((user) => (
                <tr key={user.id}>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <div style={{
                        width: '32px',
                        height: '32px',
                        borderRadius: '50%',
                        backgroundColor: '#007bff',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        fontSize: '12px',
                        fontWeight: '600'
                      }}>
                        {user.name?.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()}
                      </div>
                      <span style={{ fontWeight: '500' }}>{user.name}</span>
                    </div>
                  </td>
                  <td>{user.email}</td>
                  <td>{formatPhone(user.phone)}</td>
                  <td>
                    <span className={`badge ${user.role === 'STUDENT' ? 'student' : 'librarian'}`}>
                      {formatRole(user.role)}
                    </span>
                  </td>
                  <td>
                    <span className={`badge ${user.isActive ? 'active' : 'inactive'}`}>
                      {user.isActive ? 'Ativo' : 'Inativo'}
                    </span>
                  </td>
                  <td>{formatDate(user.createdAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <div style={{ padding: '40px', textAlign: 'center', color: '#666' }}>
            {searchTerm ? (
              <>
                <p>Nenhum usuário encontrado para "{searchTerm}"</p>
                <button 
                  onClick={() => setSearchTerm('')}
                  style={{ 
                    marginTop: '12px', 
                    padding: '8px 16px', 
                    background: '#007bff', 
                    color: 'white', 
                    border: 'none', 
                    borderRadius: '4px',
                    cursor: 'pointer'
                  }}
                >
                  Limpar busca
                </button>
              </>
            ) : (
              <p>Nenhum usuário cadastrado</p>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Users;