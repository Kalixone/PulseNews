import React, { useState } from 'react';
import './DateSearchBar.css';

const DateSearchBar = ({ setArticles }) => {
  const [date, setDate] = useState('');

  const handleSearch = async () => {
    if (!date) {
      return;
    }

    try {
      const response = await fetch(`http://pulsenewsapp-env.eba-trzjtpw7.us-east-1.elasticbeanstalk.com/api/articles/date?date=${date}`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const articles = await response.json();
      setArticles(articles);
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
