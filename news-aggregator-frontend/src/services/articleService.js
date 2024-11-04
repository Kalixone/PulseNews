import axios from 'axios';

const API_BASE_URL = 'https://pulsenews-5.onrender.com/api/articles';

export const getAllArticles = async () => {
  return await axios.get(`${API_BASE_URL}`);
};

export const getArticlesByLocation = async (location, page = 0, size = 10) => {
  const finalURL = `${API_BASE_URL}/location?location=${location}&page=${page}&size=${size}`;
  return await axios.get(finalURL);
};

export const getArticlesNotConnectedToUS = async (page = 0, size = 10) => {
  const finalURL = `${API_BASE_URL}/not-connected?page=${page}&size=${size}`;
  return await axios.get(finalURL);
};

export const getTotalNumberOfArticles = async () => {
  return await axios.get(`${API_BASE_URL}/counter`);
};

export const getRandomArticle = async () => {
  return await axios.get(`${API_BASE_URL}/random`);
};

export const searchArticlesByWord = async (word) => {
  const finalURL = `${API_BASE_URL}/search?word=${word}`;
  return await axios.get(finalURL);
};

export const findArticlesByDate = async (date, page = 0, size = 10) => {
  const finalURL = `${API_BASE_URL}/date?date=${date}&page=${page}&size=${size}`;
  return await axios.get(finalURL);
};
