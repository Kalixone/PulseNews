// services/articleService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/articles/location';
const API_NOT_CONNECTED_URL = 'http://localhost:8080/api/articles/not-connected'; // Nowy URL dla artykułów niezwiązanych
const API_COUNTER_URL = 'http://localhost:8080/api/articles/counter'; // URL dla pobierania liczby artykułów
const API_RANDOM_URL = 'http://localhost:8080/api/articles/random';
const API_SEARCH_URL = 'http://localhost:8080/api/articles/search'; // Nowy URL dla wyszukiwania
const API_DATE_URL = 'http://localhost:8080/api/articles/date'; // Nowy URL dla wyszukiwania po dacie

// Funkcja do pobierania wszystkich artykułów
export const getAllArticles = async () => {
  return await axios.get('http://localhost:8080/api/articles');
};

// Funkcja do pobierania artykułów na podstawie lokalizacji
export const getArticlesByLocation = async (location, page = 0, size = 10) => {
  const finalURL = `${API_URL}?location=${location}&page=${page}&size=${size}`; // Dodaj page i size do URL
  console.log('Fetching articles from URL:', finalURL); // Debugging
  return await axios.get(finalURL);
};

// Funkcja do pobierania artykułów niezwiązanych z miastami USA
export const getArticlesNotConnectedToUS = async (page = 0, size = 10) => {
  console.log('Fetching non-US connected articles from URL:', API_NOT_CONNECTED_URL); // Debugging
  return await axios.get(`${API_NOT_CONNECTED_URL}?page=${page}&size=${size}`);
};

// Funkcja do pobierania całkowitej liczby artykułów
export const getTotalNumberOfArticles = async () => {
  console.log('Fetching total number of articles from URL:', API_COUNTER_URL); // Debugging
  return await axios.get(API_COUNTER_URL);
};

// Funkcja do pobierania losowego artykułu
export const getRandomArticle = async () => {
  console.log('Fetching random article from URL:', API_RANDOM_URL); // Debugging
  return await axios.get(API_RANDOM_URL);
};

// Funkcja do wyszukiwania artykułów według słowa kluczowego
export const searchArticlesByWord = async (word) => {
  const finalURL = `${API_SEARCH_URL}?word=${word}`;
  console.log('Searching articles by word from URL:', finalURL); // Debugging
  return await axios.get(finalURL);
};

// Funkcja do wyszukiwania artykułów według daty
export const findArticlesByDate = async (date, page = 0, size = 10) => {
  const finalURL = `${API_DATE_URL}?date=${date}&page=${page}&size=${size}`;
  console.log('Finding articles by date from URL:', finalURL); // Debugging
  return await axios.get(finalURL);
};
