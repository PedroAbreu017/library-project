import React, { useState, useEffect } from 'react';

const Reports = () => {
  const [period, setPeriod] = useState('30d');
  const [loading, setLoading] = useState(true);
  const [reportData, setReportData] = useState({
    userGrowth: [],
    bookCategories: [],
    loanStats: [],
    monthlyActivity: []
  });

  // Mock data for charts
  const mockData = {
    userGrowth: [
      { month: 'Jan', users: 45 },
      { month: 'Fev', users: 52 },
      { month: 'Mar', users: 61 },
      { month: 'Abr', users: 58 },
      { month: 'Mai', users: 67 },
      { month: 'Jun', users: 73 },
      { month: 'Jul', users: 81 }
    ],
    bookCategories: [
      { category: 'Programming', count: 25, percentage: 35 },
      { category: 'Fiction', count: 18, percentage: 25 },
      { category: 'Science', count: 12, percentage: 17 },
      { category: 'Business', count: 10, percentage: 14 },
      { category: 'History', count: 6, percentage: 9 }
    ],
    loanStats: [
      { day: 'Seg', loans: 12, returns: 8 },
      { day: 'Ter', loans: 15, returns: 10 },
      { day: 'Qua', loans: 18, returns: 14 },
      { day: 'Qui', loans: 20, returns: 16 },
      { day: 'Sex', loans: 25, returns: 18 },
      { day: 'Sáb', loans: 8, returns: 12 },
      { day: 'Dom', loans: 5, returns: 7 }
    ],
    performance: {
      totalBooks: 150,
      totalUsers: 89,
      activeLoans: 45,
      overdueLoans: 8,
      popularBook: 'Clean Code',
      topUser: 'Maria Silva'
    }
  };

  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      setReportData(mockData);
      setLoading(false);
    }, 1000);
  }, [period]);

  const SimpleBarChart = ({ data, title, xKey, yKey, color = '#007bff' }) => {
    const maxValue = Math.max(...data.map(item => item[yKey]));
    
    return (
      <div className="chart-container">
        <h3 className="chart-title">{title}</h3>
        <div className="simple-chart">
          {data.map((item, index) => (
            <div key={index} className="chart-bar-group">
              <div 
                className="chart-bar"
                style={{
                  height: `${(item[yKey] / maxValue) * 200}px`,
                  backgroundColor: color,
                  width: '30px',
                  borderRadius: '4px 4px 0 0'
                }}
              />
              <span className="chart-label">{item[xKey]}</span>
              <span className="chart-value">{item[yKey]}</span>
            </div>
          ))}
        </div>
      </div>
    );
  };

  const SimpleLineChart = ({ data, title, xKey, yKey1, yKey2, color1 = '#007bff', color2 = '#28a745' }) => {
    const maxValue = Math.max(...data.map(item => Math.max(item[yKey1], item[yKey2])));
    
    return (
      <div className="chart-container">
        <h3 className="chart-title">{title}</h3>
        <div className="chart-legend">
          <span style={{color: color1}}>● {yKey1}</span>
          <span style={{color: color2}}>● {yKey2}</span>
        </div>
        <div className="simple-chart">
          {data.map((item, index) => (
            <div key={index} className="chart-bar-group">
              <div className="chart-bars">
                <div 
                  className="chart-bar"
                  style={{
                    height: `${(item[yKey1] / maxValue) * 150}px`,
                    backgroundColor: color1,
                    width: '15px',
                    borderRadius: '2px'
                  }}
                />
                <div 
                  className="chart-bar"
                  style={{
                    height: `${(item[yKey2] / maxValue) * 150}px`,
                    backgroundColor: color2,
                    width: '15px',
                    borderRadius: '2px'
                  }}
                />
              </div>
              <span className="chart-label">{item[xKey]}</span>
            </div>
          ))}
        </div>
      </div>
    );
  };

  const PieChart = ({ data, title }) => {
    const total = data.reduce((sum, item) => sum + item.count, 0);
    let currentAngle = 0;
    
    return (
      <div className="chart-container">
        <h3 className="chart-title">{title}</h3>
        <div className="pie-chart-container">
          <svg width="200" height="200" className="pie-chart">
            {data.map((item, index) => {
              const percentage = (item.count / total) * 100;
              const angle = (percentage / 100) * 360;
              const startAngle = currentAngle;
              const endAngle = currentAngle + angle;
              
              const x1 = 100 + 80 * Math.cos((startAngle - 90) * Math.PI / 180);
              const y1 = 100 + 80 * Math.sin((startAngle - 90) * Math.PI / 180);
              const x2 = 100 + 80 * Math.cos((endAngle - 90) * Math.PI / 180);
              const y2 = 100 + 80 * Math.sin((endAngle - 90) * Math.PI / 180);
              
              const largeArcFlag = angle > 180 ? 1 : 0;
              const pathData = `M 100 100 L ${x1} ${y1} A 80 80 0 ${largeArcFlag} 1 ${x2} ${y2} Z`;
              
              const colors = ['#007bff', '#28a745', '#ffc107', '#dc3545', '#6f42c1'];
              currentAngle += angle;
              
              return (
                <path
                  key={index}
                  d={pathData}
                  fill={colors[index % colors.length]}
                  stroke="#fff"
                  strokeWidth="2"
                />
              );
            })}
          </svg>
          <div className="pie-legend">
            {data.map((item, index) => {
              const colors = ['#007bff', '#28a745', '#ffc107', '#dc3545', '#6f42c1'];
              return (
                <div key={index} className="legend-item">
                  <span 
                    className="legend-color" 
                    style={{backgroundColor: colors[index % colors.length]}}
                  ></span>
                  <span>{item.category}: {item.count}</span>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    );
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Carregando relatórios...</p>
      </div>
    );
  }

  return (
    <div className="reports-page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Relatórios</h1>
          <p className="page-subtitle">Análises e estatísticas do sistema</p>
        </div>
        <select
          value={period}
          onChange={(e) => setPeriod(e.target.value)}
          className="period-selector"
        >
          <option value="7d">Últimos 7 dias</option>
          <option value="30d">Últimos 30 dias</option>
          <option value="90d">Últimos 90 dias</option>
          <option value="1y">Último ano</option>
        </select>
      </div>

      {/* KPIs */}
      <div className="kpi-grid">
        <div className="kpi-card">
          <div className="kpi-icon blue">📚</div>
          <div className="kpi-content">
            <div className="kpi-value">{reportData.performance?.totalBooks}</div>
            <div className="kpi-label">Total de Livros</div>
            <div className="kpi-change positive">+12% este mês</div>
          </div>
        </div>

        <div className="kpi-card">
          <div className="kpi-icon green">👥</div>
          <div className="kpi-content">
            <div className="kpi-value">{reportData.performance?.totalUsers}</div>
            <div className="kpi-label">Usuários Ativos</div>
            <div className="kpi-change positive">+8% este mês</div>
          </div>
        </div>

        <div className="kpi-card">
          <div className="kpi-icon orange">📖</div>
          <div className="kpi-content">
            <div className="kpi-value">{reportData.performance?.activeLoans}</div>
            <div className="kpi-label">Empréstimos Ativos</div>
            <div className="kpi-change negative">-3% esta semana</div>
          </div>
        </div>

        <div className="kpi-card">
          <div className="kpi-icon red">⚠️</div>
          <div className="kpi-content">
            <div className="kpi-value">{reportData.performance?.overdueLoans}</div>
            <div className="kpi-label">Em Atraso</div>
            <div className="kpi-change negative">+2 desde ontem</div>
          </div>
        </div>
      </div>

      {/* Charts Grid */}
      <div className="charts-grid">
        <div className="chart-card">
          <SimpleBarChart 
            data={reportData.userGrowth}
            title="Crescimento de Usuários"
            xKey="month"
            yKey="users"
            color="#007bff"
          />
        </div>

        <div className="chart-card">
          <PieChart 
            data={reportData.bookCategories}
            title="Livros por Categoria"
          />
        </div>

        <div className="chart-card full-width">
          <SimpleLineChart 
            data={reportData.loanStats}
            title="Empréstimos vs Devoluções (Semana)"
            xKey="day"
            yKey1="loans"
            yKey2="returns"
            color1="#007bff"
            color2="#28a745"
          />
        </div>
      </div>

      {/* Summary Cards */}
      <div className="summary-grid">
        <div className="summary-card">
          <h3>🏆 Livro Mais Popular</h3>
          <p className="summary-value">{reportData.performance?.popularBook}</p>
          <p className="summary-detail">15 empréstimos este mês</p>
        </div>

        <div className="summary-card">
          <h3>⭐ Usuário Mais Ativo</h3>
          <p className="summary-value">{reportData.performance?.topUser}</p>
          <p className="summary-detail">8 livros emprestados</p>
        </div>

        <div className="summary-card">
          <h3>📈 Taxa de Ocupação</h3>
          <p className="summary-value">76%</p>
          <p className="summary-detail">dos livros estão emprestados</p>
        </div>

        <div className="summary-card">
          <h3>⏱️ Tempo Médio</h3>
          <p className="summary-value">12 dias</p>
          <p className="summary-detail">duração dos empréstimos</p>
        </div>
      </div>
    </div>
  );
};

export default Reports;