import axios from 'axios';

const API_URL = 'http://localhost:8080/api/articles/location';
const API_NOT_CONNECTED_URL = 'http://localhost:8080/api/articles/not-connected';
const API_COUNTER_URL = 'http://localhost:8080/api/articles/counter';
const API_RANDOM_URL = 'http://localhost:8080/api/articles/random';
const API_SEARCH_URL = 'http://localhost:8080/api/articles/search';
const API_DATE_URL = 'http://localhost:8080/api/articles/date';

export const getAllArticles = async () => {
  return await axios.get('http://localhost:8080/api/articles');
};

export const getArticlesByLocation = async (location, page = 0, size = 10) => {
  const finalURL = `${API_URL}?location=${location}&page=${page}&size=${size}`;
  return await axios.get(finalURL);
};

export const getArticlesNotConnectedToUS = async (page = 0, size = 10) => {
  return await axios.get(`${API_NOT_CONNECTED_URL}?page=${page}&size=${size}`);
};

export const getTotalNumberOfArticles = async () => {
  return await axios.get(API_COUNTER_URL);
};

export const getRandomArticle = async () => {
  return await axios.get(API_RANDOM_URL);
};

export const searchArticlesByWord = async (word) => {
  const finalURL = `${API_SEARCH_URL}?word=${word}`;
  return await axios.get(finalURL);
};

export const findArticlesByDate = async (date, page = 0, size = 10) => {
  const finalURL = `${API_DATE_URL}?date=${date}&page=${page}&size=${size}`;
  return await axios.get(finalURL);
};
