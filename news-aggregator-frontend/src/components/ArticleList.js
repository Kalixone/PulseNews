import React, { useState, useEffect } from 'react';
import {
  getAllArticles,
  getArticlesByLocation,
  getArticlesNotConnectedToUS,
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
  const [error, setError] = useState(null);
  const [filterState, setFilterState] = useState({
    fetchingNonUSArticles: false,
    location: '',
    date: '',
    keyword: '',
  });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        let response;

        if (filterState.fetchingNonUSArticles) {
          response = await getArticlesNotConnectedToUS(page, size);
        } else if (filterState.location) {
          response = await getArticlesByLocation(filterState.location, page, size);
        } else if (filterState.date) {
          response = await findArticlesByDate(filterState.date, page, size);
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
  }, [page, size, filterState, setDisplayedArticles]);

  const fetchNotConnectedToUSArticles = () => {
    setFilterState({
      fetchingNonUSArticles: true,
      location: '',
      date: '',
      keyword: '',
    });
    setPage(0);
  };

  const handleLocationChange = (newLocation) => {
    setFilterState((prev) => ({
      ...prev,
      location: newLocation,
      fetchingNonUSArticles: false,
      date: '',
      keyword: '',
    }));
    setPage(0);
  };

  const handleDateChange = (newDate) => {
    setFilterState((prev) => ({
      ...prev,
      date: newDate,
      fetchingNonUSArticles: false,
      location: '',
      keyword: '',
    }));
    setPage(0);
  };

  const handleKeywordChange = (keyword) => {
    setFilterState((prev) => ({
      ...prev,
      keyword: keyword,
      fetchingNonUSArticles: false,
      location: '',
      date: '',
    }));
    setPage(0);
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
      {error && <p className="error-message">{error}</p>}

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

      <div style={{ display: 'flex', justifyContent: 'center', margin: '20px 0' }}>
        <button onClick={() => setPage((prev) => Math.max(prev - 1, 0))} disabled={page === 0} style={{ marginRight: '10px' }}>
          Previous
        </button>
        <button onClick={() => setPage((prev) => Math.min(prev + 1, totalPages - 1))} disabled={page >= totalPages - 1}>
          Next
        </button>
      </div>
    </div>
  );
};

export default ArticleList;
