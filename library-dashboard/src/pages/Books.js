import React, { useState, useEffect } from 'react';
import { bookService } from '../services/api';
import BookModal from '../components/BookModal';

const Books = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [filterStatus, setFilterStatus] = useState('all'); // all, available, unavailable

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      setLoading(true);
      const data = await bookService.getBooks();
      setBooks(data);
    } catch (error) {
      console.error('Erro ao buscar livros:', error);
      alert('Erro ao carregar livros: ' + (error.error || 'Tente novamente'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // Filter books based on search term and status
    let filtered = books;

    if (searchTerm.trim() !== '') {
      filtered = filtered.filter(book => 
        book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.author.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.isbn.includes(searchTerm)
      );
    }

    if (filterStatus !== 'all') {
      filtered = filtered.filter(book => 
        filterStatus === 'available' ? book.available : !book.available
      );
    }

    setFilteredBooks(filtered);
  }, [books, searchTerm, filterStatus]);

  const handleAddBook = () => {
    setEditingBook(null);
    setShowModal(true);
  };

  const handleEditBook = (book) => {
    setEditingBook(book);
    setShowModal(true);
  };

  const handleDeleteBook = async (bookId) => {
    if (window.confirm('Tem certeza que deseja excluir este livro?')) {
      try {
        await bookService.deleteBook(bookId);
        setBooks(books.filter(book => book.id !== bookId));
        alert('Livro excluído com sucesso!');
      } catch (error) {
        console.error('Erro ao excluir livro:', error);
        alert('Erro ao excluir livro: ' + (error.error || 'Tente novamente'));
      }
    }
  };

  const handleSaveBook = async (bookData) => {
    try {
      if (editingBook) {
        // Update existing book
        const updatedBook = await bookService.updateBook(editingBook.id, bookData);
        setBooks(books.map(book => 
          book.id === editingBook.id ? updatedBook : book
        ));
        alert('Livro atualizado com sucesso!');
      } else {
        // Add new book
        const newBook = await bookService.createBook(bookData);
        setBooks([newBook, ...books]);
        alert('Livro criado com sucesso!');
      }
      setShowModal(false);
    } catch (error) {
      console.error('Erro ao salvar livro:', error);
      alert('Erro ao salvar livro: ' + (error.error || 'Tente novamente'));
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR');
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Carregando livros...</p>
      </div>
    );
  }

  return (
    <div className="books-page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Livros</h1>
          <p className="page-subtitle">Gerencie o acervo da biblioteca</p>
        </div>
        <button className="btn btn-primary" onClick={handleAddBook}>
          <svg style={{width: 16, height: 16}} fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Adicionar Livro
        </button>
      </div>

      {/* Estatísticas */}
      <div className="stats-grid" style={{ marginBottom: '32px' }}>
        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon blue">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
            </div>
            <span className="stat-title">Total de Livros</span>
          </div>
          <div className="stat-value">{books.length}</div>
        </div>

        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon green">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <span className="stat-title">Disponíveis</span>
          </div>
          <div className="stat-value">
            {books.filter(book => book.available).length}
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon orange">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <span className="stat-title">Emprestados</span>
          </div>
          <div className="stat-value">
            {books.filter(book => !book.available).length}
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-card-header">
            <div className="stat-icon purple">
              <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style={{width: 20, height: 20}}>
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} 
                      d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
            </div>
            <span className="stat-title">Categorias</span>
          </div>
          <div className="stat-value">
            {new Set(books.map(book => book.category)).size}
          </div>
        </div>
      </div>

      {/* Filtros e busca */}
      <div className="table-container">
        <div className="table-header">
          <h2 className="table-title">Acervo da Biblioteca</h2>
          <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              style={{
                padding: '8px 12px',
                border: '1px solid #ddd',
                borderRadius: '4px',
                fontSize: '14px'
              }}
            >
              <option value="all">Todos os livros</option>
              <option value="available">Disponíveis</option>
              <option value="unavailable">Emprestados</option>
            </select>
            <input
              type="text"
              placeholder="Buscar por título, autor ou ISBN..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-box"
              style={{ minWidth: '250px' }}
            />
          </div>
        </div>

        {filteredBooks.length > 0 ? (
          <table className="table">
            <thead>
              <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Categoria</th>
                <th>Status</th>
                <th>Data de Cadastro</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {filteredBooks.map((book) => (
                <tr key={book.id}>
                  <td>
                    <div style={{ fontWeight: '500' }}>{book.title}</div>
                  </td>
                  <td>{book.author}</td>
                  <td style={{ fontFamily: 'monospace', fontSize: '13px' }}>
                    {book.isbn}
                  </td>
                  <td>
                    <span style={{
                      padding: '4px 8px',
                      backgroundColor: '#f0f8ff',
                      color: '#0066cc',
                      borderRadius: '12px',
                      fontSize: '12px',
                      fontWeight: '500'
                    }}>
                      {book.category}
                    </span>
                  </td>
                  <td>
                    <span className={`badge ${book.available ? 'active' : 'inactive'}`}>
                      {book.available ? 'Disponível' : 'Emprestado'}
                    </span>
                  </td>
                  <td>{formatDate(book.createdAt)}</td>
                  <td>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button
                        onClick={() => handleEditBook(book)}
                        style={{
                          padding: '4px 8px',
                          background: '#007bff',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '12px'
                        }}
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => handleDeleteBook(book.id)}
                        style={{
                          padding: '4px 8px',
                          background: '#dc3545',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '12px'
                        }}
                      >
                        Excluir
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <div style={{ padding: '40px', textAlign: 'center', color: '#666' }}>
            {searchTerm || filterStatus !== 'all' ? (
              <>
                <p>Nenhum livro encontrado com os filtros aplicados</p>
                <button 
                  onClick={() => {
                    setSearchTerm('');
                    setFilterStatus('all');
                  }}
                  className="btn btn-secondary"
                  style={{ marginTop: '12px' }}
                >
                  Limpar filtros
                </button>
              </>
            ) : (
              <>
                <p>Nenhum livro cadastrado</p>
                <button 
                  onClick={handleAddBook}
                  className="btn btn-primary"
                  style={{ marginTop: '12px' }}
                >
                  Adicionar primeiro livro
                </button>
              </>
            )}
          </div>
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <BookModal
          book={editingBook}
          onSave={handleSaveBook}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
};

export default Books;