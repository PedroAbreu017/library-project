import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token automaticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para lidar com respostas
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/';
    }
    return Promise.reject(error);
  }
);

// Serviços de autenticação
export const authService = {
  login: async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro no login' };
    }
  },

  register: async (userData) => {
    try {
      const response = await api.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro no registro' };
    }
  },

  validateToken: async () => {
    try {
      const response = await api.post('/auth/validate');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Token inválido' };
    }
  },
};

// Serviços de usuários
export const userService = {
  getUsers: async () => {
    try {
      const response = await api.get('/users');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar usuários' };
    }
  },

  getUserById: async (id) => {
    try {
      const response = await api.get(`/users/${id}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar usuário' };
    }
  },

  getUserCount: async () => {
    try {
      const response = await api.get('/users/count');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar estatísticas' };
    }
  },

  searchUsers: async (name) => {
    try {
      const response = await api.get(`/users/search?name=${encodeURIComponent(name)}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro na busca' };
    }
  },

  createUser: async (userData) => {
    try {
      const response = await api.post('/users', userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao criar usuário' };
    }
  },

  updateUser: async (id, userData) => {
    try {
      const response = await api.put(`/users/${id}`, userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao atualizar usuário' };
    }
  },

  deleteUser: async (id) => {
    try {
      await api.delete(`/users/${id}`);
      return { success: true };
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao deletar usuário' };
    }
  },
};

// Serviços de livros (para futuro)
export const bookService = {
  getBooks: async () => {
    try {
      const response = await api.get('/books');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar livros' };
    }
  },

  getBookCount: async () => {
    try {
      const response = await api.get('/books/count');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar estatísticas de livros' };
    }
  },
};

export const dashboardService = {
  getStats: async () => {
    try {
      const response = await api.get('/dashboard/stats');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar estatísticas' };
    }
  },

  getRecentActivity: async () => {
    try {
      const response = await api.get('/dashboard/recent-activity');
      return response.data;
    } catch (error) {
      throw error.response?.data || { error: 'Erro ao buscar atividade recente' };
    }
  },
};

export default api;