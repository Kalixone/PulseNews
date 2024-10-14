// components/SearchBar.js
import React, { useState } from 'react';
import './SearchBar.css'; // Upewnij się, że masz odpowiedni styl

const SearchBar = ({ onLocationChange }) => {
  const [location, setLocation] = useState('');

  const handleSearch = () => {
    if (location) {
      onLocationChange(location); // Przekazuje nową lokalizację do ArticleList
      setLocation(''); // Czyści pole po wyszukaniu
    }
  };

  return (
    <div className="search-bar">
      <label htmlFor="location-input" className="search-label">
        Search articles by US cities:
      </label>
      <input
        type="text"
        id="location-input"
        placeholder="Enter a US city..."
        value={location}
        onChange={(e) => setLocation(e.target.value)}
        className="search-input"
      />
      <button onClick={handleSearch} className="search-button">
        🔍 Search
      </button>
    </div>
  );
};

export default SearchBar;
