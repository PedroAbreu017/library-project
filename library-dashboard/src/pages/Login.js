import React, { useState } from 'react';
import { authService } from '../services/api';

const Login = ({ onLogin }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.email || !formData.password) {
      setError('Por favor, preencha todos os campos');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await authService.login(formData.email, formData.password);
      onLogin({
        id: response.userId,
        name: response.name,
        email: response.email,
        role: response.role,
      }, response.token);
    } catch (error) {
      setError(error.error || 'Erro no login');
    } finally {
      setLoading(false);
    }
  };

  const fillTestData = () => {
    setFormData({
      email: 'test@test.com',
      password: '123456',
    });
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title">📚 Library Dashboard</h1>
          <p className="login-subtitle">Faça login para acessar o sistema</p>
        </div>

        <form onSubmit={handleSubmit} className="login-form">
          {error && (
            <div className="alert alert-error">
              {error}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Digite seu email"
              disabled={loading}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Senha</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Digite sua senha"
              disabled={loading}
              required
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary btn-full"
            disabled={loading}
          >
            {loading ? (
              <span className="loading-text">
                <span className="loading-spinner-small"></span>
                Entrando...
              </span>
            ) : (
              'Entrar'
            )}
          </button>

          <button 
            type="button" 
            className="btn btn-secondary btn-full"
            onClick={fillTestData}
            disabled={loading}
          >
            🧪 Preencher dados de teste
          </button>
        </form>

        <div className="login-footer">
          <p>Dados de teste: test@test.com / 123456</p>
        </div>
      </div>
    </div>
  );
};

export default Login;