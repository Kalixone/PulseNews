import React, { useState, useEffect } from 'react';
import {
  getAllArticles,
  getArticlesByLocation,
  getArticlesNotConnectedToUS,
  getRandomArticle,
  findArticlesByDate,
} from '../services/articleService';
import SearchBar from './SearchBar';
import DateSearchBar from './DateSearchBar';
import KeywordSearchBar from './KeywordSearchBar';
import './ArticleList.css';

const ArticleList = ({ setDisplayedArticles }) => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [fetchingNonUSArticles, setFetchingNonUSArticles] = useState(false);
  const [location, setLocation] = useState('');
  const [date, setDate] = useState('');
  const [error, setError] = useState(null);
  const [randomArticle, setRandomArticle] = useState(null);
  const [showingRandomArticle, setShowingRandomArticle] = useState(false);

  // Fetch articles based on current filter settings
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        let response;

        if (fetchingNonUSArticles) {
          response = await getArticlesNotConnectedToUS(page, size);
        } else if (location) {
          response = await getArticlesByLocation(location, page, size);
        } else if (date) {
          response = await findArticlesByDate(date, page, size);
        } else {
          response = await getAllArticles(page, size);
        }

        setArticles(response.data.content || []);
        setTotalPages(response.data.totalPages);
        setDisplayedArticles(response.data.content || []);
      } catch (error) {
        console.error('Error fetching articles:', error);
        setError('Error fetching articles. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [page, size, fetchingNonUSArticles, location, date, setDisplayedArticles]);

  const fetchNotConnectedToUSArticles = () => {
    setFetchingNonUSArticles(true);
    setPage(0);
    setShowingRandomArticle(false);
    setLocation(''); // Clear location
    setDate(''); // Clear date
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(page + 1);
      window.scrollTo(0, 0);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(page - 1);
      window.scrollTo(0, 0);
    }
  };

  const handleRandomArticle = async () => {
    try {
      const response = await getRandomArticle();
      setRandomArticle(response.data);
      setShowingRandomArticle(true);
      setArticles([]); // Clear previous articles when showing random article
      setLocation(''); // Clear location
      setDate(''); // Clear date
      setFetchingNonUSArticles(false); // Reset non-US fetching flag
      setPage(0); // Reset page
    } catch (error) {
      console.error('Error fetching random article:', error);
      setError('Error fetching random article. Please try again later.');
    }
  };

  const handleLocationChange = (newLocation) => {
    setLocation(newLocation);
    setPage(0);
    setShowingRandomArticle(false);
    setDate(''); // Clear date
    setFetchingNonUSArticles(false); // Reset non-US fetching flag
  };

  const handleDateChange = (newDate) => {
    setDate(newDate);
    setPage(0);
    setShowingRandomArticle(false);
    setLocation(''); // Clear location
    setFetchingNonUSArticles(false); // Reset non-US fetching flag
  };

  const handleKeywordChange = (keyword) => {
    // Reset states related to fetching articles
    setLocation(''); // Clear location
    setDate(''); // Clear date
    setFetchingNonUSArticles(false); // Reset non-US fetching flag
    setPage(0); // Reset to first page
  };

  if (loading) {
    return <p>Loading articles...</p>;
  }

  return (
    <div>
      <SearchBar onLocationChange={handleLocationChange} />
      <DateSearchBar setArticles={setArticles} />
      <KeywordSearchBar onKeywordChange={handleKeywordChange} setArticles={setArticles} />
      <button onClick={fetchNotConnectedToUSArticles} className="fetch-non-us-articles-button">
        Show Articles Not Connected to US
      </button>
      <button onClick={handleRandomArticle} className="fetch-random-article-button">
        Show Random Article
      </button>
      {error && <p className="error-message">{error}</p>}

      {showingRandomArticle && randomArticle && (
        <div className="random-article">
          <h2>{randomArticle.title}</h2>
          <p className="publish-date">{new Date(randomArticle.publishDate).toLocaleDateString()}</p>
          {randomArticle.location && <p>Location: {randomArticle.location}</p>}
          {randomArticle.image && <img src={randomArticle.image} alt={randomArticle.title} />}
          <a href={randomArticle.link} className="read-more-link" target="_blank" rel="noopener noreferrer">
            Read more about this article!
          </a>
        </div>
      )}

      {!showingRandomArticle && (
        <ul className="article-list">
          {Array.isArray(articles) && articles.length > 0 ? (
            articles.map((article) => (
              <li key={article.id} className="article-item">
                <h2>{article.title}</h2>
                <p className="publish-date">{new Date(article.publishDate).toLocaleDateString()}</p>
                {article.location && <p>Location: {article.location}</p>}
                {article.image && <img src={article.image} alt={article.title} />}
                <a href={article.link} className="read-more-link" target="_blank" rel="noopener noreferrer">
                  Read more about this article!
                </a>
              </li>
            ))
          ) : (
            <p>No articles found.</p>
          )}
        </ul>
      )}

      {!showingRandomArticle && (
        <div style={{ display: 'flex', justifyContent: 'center', margin: '20px 0' }}>
          <button onClick={handlePreviousPage} disabled={page === 0} style={{ marginRight: '10px' }}>
            Previous
          </button>
          <button onClick={handleNextPage} disabled={page >= totalPages - 1}>
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default ArticleList;
