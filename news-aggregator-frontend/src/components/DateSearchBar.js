import React, { useState } from 'react';
import './DateSearchBar.css';

const DateSearchBar = ({ setArticles }) => {
  const [date, setDate] = useState('');

  const handleSearch = async () => {
    if (!date) {
      console.log('Proszę podać datę'); // Możesz dodać alert dla lepszej widoczności
      return; // Upewnij się, że data nie jest pusta
    }

    try {
      const response = await fetch(`http://localhost:8080/api/articles/date?date=${date}`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const articles = await response.json();
      setArticles(articles); // Upewnij się, że przekazujesz stan wyżej
      console.log(`Szukam artykułów z datą: ${date}`);
    } catch (error) {
      console.error('Error fetching articles by date:', error);
    }
  };

  return (
    <div className="date-search-bar">
      <label htmlFor="date-input" className="search-label">
        Search articles by publish date:
      </label>
      <input
        type="date"
        id="date-input"
        value={date}
        onChange={(e) => setDate(e.target.value)}
        className="search-input"
      />
      <button onClick={handleSearch} className="search-button">
        🔍 Search
      </button>
    </div>
  );
};

export default DateSearchBar;
