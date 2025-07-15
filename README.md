# 📚 Library Management System

Sistema completo de gerenciamento de biblioteca desenvolvido com **Java Spring Boot** no backend e **React** no frontend. Inclui autenticação JWT, CRUD completo de usuários e livros, dashboard com estatísticas em tempo real e uma interface moderna e responsiva.

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-brightgreen)](https://spring.io)
[![React](https://img.shields.io/badge/React-18-blue)](https://reactjs.org/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-orange)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> Gerencie sua biblioteca com autenticação segura, controle de usuários e livros, gráficos e KPIs interativos.

---

## 🚀 Funcionalidades

### 🔐 Autenticação
- Login e registro de usuários
- Autenticação JWT com refresh token
- Controle de acesso baseado em roles (Estudante/Bibliotecário)
- Middleware de autenticação automática

### 👥 Gerenciamento de Usuários
- Listagem de usuários com filtros
- Busca por nome e email
- Estatísticas de usuários ativos/inativos
- Diferentes tipos de usuário (Estudante, Bibliotecário)

### 📖 Gerenciamento de Livros
- CRUD completo de livros
- Busca por título, autor ou ISBN
- Filtros por disponibilidade e categoria
- Categorização automática
- Controle de disponibilidade

### 📊 Dashboard & Relatórios
- Estatísticas em tempo real
- Gráficos interativos (barras, pizza, linha)
- KPIs de performance
- Relatórios visuais por período
- Atividade recente do sistema

### 🎨 Interface Moderna
- Design responsivo e intuitivo
- Componentes reutilizáveis
- Navegação fluida
- Feedback visual para ações
- Tabelas com ordenação e paginação

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.2.0**
- **Spring Security (JWT)**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Maven**

### Frontend
- **React 18+**
- **React Router**
- **Axios**
- **React Hooks**
- **CSS3**

### Ferramentas
- **Git**
- **JWT**
- **RESTful API**
- **Responsive Design**

---

## 📁 Estrutura do Projeto

library-project/
├── library-management-system/ # Backend Spring Boot
│ ├── src/main/java/com/library/
│ │ ├── config/ # Configurações (Security, JWT)
│ │ ├── controller/ # Controllers REST
│ │ ├── entity/ # Entidades JPA
│ │ ├── repository/ # Repositories
│ │ ├── service/ # Lógica de negócio
│ │ └── dto/ # DTOs
│ ├── src/main/resources/
│ │ ├── application.yml # Configurações
│ │ └── data.sql # Dados iniciais
│ └── pom.xml # Dependências Maven
│
├── library-dashboard/ # Frontend React
│ ├── src/
│ │ ├── components/ # Componentes reutilizáveis
│ │ ├── pages/ # Páginas principais
│ │ ├── services/ # Serviços de API
│ │ ├── contexts/ # Contextos React
│ │ └── navigation/ # Rotas
│ ├── public/
│ └── package.json # Dependências NPM
│
└── README.md # Este arquivo

pgsql
Copy
Edit

---

## ⚙️ Instalação e Configuração

### Pré-requisitos
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.8+

### 🗄️ Configuração do Banco de Dados

```sql
CREATE DATABASE library_db;

CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'library123';
GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'localhost';
FLUSH PRIVILEGES;
🔧 Backend (Spring Boot)
bash
Copy
Edit
git clone https://github.com/PedroAbreu017/library-project.git
cd library-project/library-management-system

# Edite o application.yml com suas credenciais MySQL

mvn clean install
mvn spring-boot:run
Acesse: http://localhost:8080

🎨 Frontend (React)
bash
Copy
Edit
cd ../library-dashboard

npm install
npm start
Acesse: http://localhost:3000

🚀 Como Usar
Primeiro Acesso

Registre-se e faça login

Dashboard

Acompanhe estatísticas e relatórios

Gerenciar Usuários

Busque, edite e visualize usuários

Gerenciar Livros

Adicione, edite e filtre livros

Relatórios

Visualize gráficos interativos

🔒 Endpoints da API
Autenticação
bash
Copy
Edit
POST /api/auth/register
POST /api/auth/login
POST /api/auth/validate
Usuários
bash
Copy
Edit
GET    /api/users
GET    /api/users/{id}
GET    /api/users/count
GET    /api/users/search
Livros
bash
Copy
Edit
GET    /api/books
POST   /api/books
PUT    /api/books/{id}
DELETE /api/books/{id}
GET    /api/books/search
GET    /api/books/stats
Dashboard
bash
Copy
Edit
GET    /api/dashboard/stats
GET    /api/dashboard/recent-activity
🧪 Testes
Backend
bash
Copy
Edit
mvn test
mvn verify
Frontend
bash
Copy
Edit
npm test
npm run test:coverage
📦 Build para Produção
Backend
bash
Copy
Edit
mvn clean package
java -jar target/library-management-system-1.0.0.jar
Frontend
bash
Copy
Edit
npm run build
npm install -g serve
serve -s build
🔧 Configurações Importantes
application.yml
yaml
Copy
Edit
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db
    username: library_user
    password: library123

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: mySecretKey123456789012345678901234567890
  expiration: 86400000 # 24 horas
🚧 Próximas Funcionalidades
 Sistema de empréstimos

 Notificações em tempo real

 Exportação de relatórios (PDF/Excel)

 Dark mode

 App mobile (React Native)

 Sistema de reservas

 Integração com APIs externas

 Deploy automatizado

🤝 Contribuindo
Faça fork do projeto

Crie uma branch (git checkout -b feature/NomeFuncionalidade)

Commit suas mudanças (git commit -m 'Add nova funcionalidade')

Push para a branch (git push origin feature/NomeFuncionalidade)

Abra um Pull Request

📄 Licença
Este projeto está licenciado sob a MIT License – veja o arquivo LICENSE para mais detalhes.

👨‍💻 Autor
Pedro Marsch

💼 LinkedIn

💻 GitHub

📧 Email: pedroabreu6497@gmail.com

📞 Suporte
📧 pedroabreu6497@gmail.com

🐛 Relatar problemas
