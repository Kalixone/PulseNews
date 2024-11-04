import React, { useState, useEffect } from 'react';
import './App.css';
import ArticleList from './components/ArticleList';
import { getTotalNumberOfArticles } from './services/articleService';
import 'font-awesome/css/font-awesome.min.css';

function App() {
  const [currentTime, setCurrentTime] = useState(new Date());
  const [countdown, setCountdown] = useState(0);
  const [totalArticles, setTotalArticles] = useState(0);
  const [displayedArticles, setDisplayedArticles] = useState([]);

  const loadTotalArticles = async () => {
    try {
      const response = await getTotalNumberOfArticles();
      setTotalArticles(response.data);
    } catch (error) {
      console.error('Error fetching total articles:', error);
    }
  };

  useEffect(() => {
    loadTotalArticles();

    const intervalId = setInterval(() => {
      setCurrentTime(new Date());
      const now = new Date();
      const nextFetchTime = new Date(now.getTime());
      nextFetchTime.setHours(now.getHours() + 1, 0, 0, 0);
      const timeDiff = nextFetchTime - now;
      setCountdown(Math.floor(timeDiff / 1000));
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);

  const handleMailClick = () => {
    window.location.href = "mailto:piotr.kamil.kaliszuk@gmail.com?subject=Contact&body=Hello, I'd like to reach out to you.";
  };

  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
  };

  return (
    <div className="App">
      <header className="App-header">
        <div className="header-container">
          <h1>Pulse News</h1>
        </div>
        <h2 className="header-subtitle">Stay updated with the latest news articles</h2>
        <div className="current-time">
          <span>{currentTime.toLocaleTimeString()}</span>
          <i
            className="fa fa-envelope"
            onClick={handleMailClick}
            title="Send Email"
          ></i>
        </div>
      </header>
      <ArticleList setDisplayedArticles={setDisplayedArticles} />

      <footer className="App-footer">
        <p>New articles are updated every hour. Stay tuned!</p>
        <div className="countdown" style={{ color: '#ffffff' }}>
          Next articles fetch in: {formatTime(countdown)}
        </div>
      </footer>
    </div>
  );
}

export default App;
