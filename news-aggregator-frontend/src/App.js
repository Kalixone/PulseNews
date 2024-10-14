import React, { useState, useEffect } from 'react';
import './App.css';
import ArticleList from './components/ArticleList';
import { getTotalNumberOfArticles, getRandomArticle } from './services/articleService';
import 'font-awesome/css/font-awesome.min.css';

function App() {
  const [currentTime, setCurrentTime] = useState(new Date());
  const [countdown, setCountdown] = useState(0);
  const [totalArticles, setTotalArticles] = useState(0);
  const [randomArticle, setRandomArticle] = useState(null);
  const [displayedArticles, setDisplayedArticles] = useState([]); // Dodaj stan dla wyświetlanych artykułów

  const loadTotalArticles = async () => {
    try {
      const response = await getTotalNumberOfArticles();
      setTotalArticles(response.data);
    } catch (error) {
      console.error('Error fetching total articles:', error);
    }
  };

  const loadRandomArticle = async () => {
    try {
      const response = await getRandomArticle();
      setRandomArticle(response.data);
    } catch (error) {
      console.error('Error fetching random article:', error);
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
      <header className="App-header" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <button className="article-count-button" onClick={loadTotalArticles}>
            Load Total Articles
          </button>
          <div className="article-count" style={{ color: '#ffffff', marginLeft: '20px', marginTop: '-5px' }}>
            Total Articles: {totalArticles}
          </div>
        </div>
        <div className="header-container">
          <h1 style={{ color: '#ffffff' }}>News Aggregation</h1>
        </div>
        <h2 className="header-subtitle">Stay updated with the latest news articles</h2>
        <div className="current-time" style={{ display: 'flex', alignItems: 'center', color: '#ffffff', justifyContent: 'center', marginBottom: '10px' }}>
          <span>{currentTime.toLocaleTimeString()}</span>
          <i
            className="fa fa-envelope"
            style={{ color: '#ffffff', fontSize: '30px', marginLeft: '20px', cursor: 'pointer' }}
            onClick={handleMailClick}
            title="Send Email"
          ></i>
        </div>
      </header>
      <ArticleList setDisplayedArticles={setDisplayedArticles} /> {/* Przekazujemy wyświetlane artykuły */}
      {randomArticle && ( // Enhanced display for random article
        <div className="random-article">
          <h3>{randomArticle.title}</h3>
          <p>{randomArticle.content}</p>
          {randomArticle.image && (
            <img src={randomArticle.image} alt={randomArticle.title} style={{ width: '800px', height: 'auto', borderRadius: '8px' }} />
          )}
          <a href={randomArticle.link} className="read-more-link" target="_blank" rel="noopener noreferrer">
            Read more about this article!
          </a>
        </div>
      )}
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
