import React, { useState, useEffect } from 'react';

const BookModal = ({ book, onSave, onClose }) => {
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    isbn: '',
    category: 'Programming',
    available: true,
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (book) {
      setFormData({
        title: book.title || '',
        author: book.author || '',
        isbn: book.isbn || '',
        category: book.category || 'Programming',
        available: book.available !== undefined ? book.available : true,
      });
    }
  }, [book]);

  const categories = [
    'Programming',
    'Fiction',
    'Non-Fiction',
    'Science',
    'Technology',
    'History',
    'Biography',
    'Self-Help',
    'Business',
    'Education'
  ];

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
    
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: '',
      });
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.title.trim()) {
      newErrors.title = 'Título é obrigatório';
    }

    if (!formData.author.trim()) {
      newErrors.author = 'Autor é obrigatório';
    }

    if (!formData.isbn.trim()) {
      newErrors.isbn = 'ISBN é obrigatório';
    } else if (!/^\d{3}-\d{10}$/.test(formData.isbn) && !/^\d{13}$/.test(formData.isbn)) {
      newErrors.isbn = 'ISBN deve estar no formato 978-0000000000 ou 13 dígitos';
    }

    if (!formData.category) {
      newErrors.category = 'Categoria é obrigatória';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (validateForm()) {
      onSave(formData);
    }
  };

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-content">
        <div className="modal-header">
          <h2 className="modal-title">
            {book ? 'Editar Livro' : 'Adicionar Novo Livro'}
          </h2>
          <button className="modal-close" onClick={onClose}>
            <svg style={{width: 20, height: 20}} fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form onSubmit={handleSubmit} className="modal-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="title">Título *</label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                placeholder="Digite o título do livro"
                className={errors.title ? 'error' : ''}
              />
              {errors.title && <span className="error-message">{errors.title}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="author">Autor *</label>
              <input
                type="text"
                id="author"
                name="author"
                value={formData.author}
                onChange={handleChange}
                placeholder="Digite o nome do autor"
                className={errors.author ? 'error' : ''}
              />
              {errors.author && <span className="error-message">{errors.author}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group" style={{flex: 1}}>
              <label htmlFor="isbn">ISBN *</label>
              <input
                type="text"
                id="isbn"
                name="isbn"
                value={formData.isbn}
                onChange={handleChange}
                placeholder="978-0000000000 ou 13 dígitos"
                className={errors.isbn ? 'error' : ''}
              />
              {errors.isbn && <span className="error-message">{errors.isbn}</span>}
            </div>

            <div className="form-group" style={{flex: 1}}>
              <label htmlFor="category">Categoria *</label>
              <select
                id="category"
                name="category"
                value={formData.category}
                onChange={handleChange}
                className={errors.category ? 'error' : ''}
              >
                {categories.map(category => (
                  <option key={category} value={category}>
                    {category}
                  </option>
                ))}
              </select>
              {errors.category && <span className="error-message">{errors.category}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="checkbox-label">
                <input
                  type="checkbox"
                  name="available"
                  checked={formData.available}
                  onChange={handleChange}
                />
                <span className="checkbox-text">Livro disponível para empréstimo</span>
              </label>
            </div>
          </div>

          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary">
              {book ? 'Atualizar' : 'Adicionar'} Livro
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default BookModal;