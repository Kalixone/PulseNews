import React, { useState } from 'react';
import './KeywordSearchBar.css';

const KeywordSearchBar = ({ setArticles }) => {
  const [keyword, setKeyword] = useState('');

  const handleSearch = async () => {
    if (!keyword) {
      console.log('Proszę podać słowo kluczowe'); // Możesz dodać alert dla lepszej widoczności
      return; // Upewnij się, że słowo kluczowe nie jest puste
    }

    try {
      const response = await fetch(`http://localhost:8080/api/articles/search?word=${keyword}`);
      if (!response.ok) {
        throw new Error('Failed to fetch articles');
      }
      const articles = await response.json();
      setArticles(articles); // Upewnij się, że setArticles jest poprawnie przekazywana
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