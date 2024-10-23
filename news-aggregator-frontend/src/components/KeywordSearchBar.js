import React, { useState } from 'react';
import './KeywordSearchBar.css';

const KeywordSearchBar = ({ setArticles }) => {
  const [keyword, setKeyword] = useState('');

  const handleSearch = async () => {
    if (!keyword) {
      console.log('Please enter a keyword');
      return;
    }

    try {
        const response = await fetch(`https://pulsenewsapp-env.eba-trzjtpw7.us-east-1.elasticbeanstalk.com/api/articles/search?word=${keyword}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
      const articles = await response.json();
      setArticles(articles);
      console.log(`Szukam artykułów z hasłem: ${keyword}`);
    } catch (error) {
      console.error('Error fetching articles by keyword:', error);
    }
  };

  return (
    <div className="keyword-search-bar">
      <label htmlFor="keyword-input" className="search-label">
        Search articles by keywords:
      </label>
      <input
        type="text"
        id="keyword-input"
        placeholder="Enter keywords..."
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        className="search-input"
      />
      <button onClick={handleSearch} className="search-button">
        🔍 Search
      </button>
    </div>
  );
};

export default KeywordSearchBar;
