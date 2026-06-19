import api from './axios';

export const generateQuestions = (skill, experience, count) =>
  api.post(`/questions/generate?skill=${skill}&experience=${experience}&count=${count}`);

export const createQuestion = (data) =>
  api.post('/questions', data);

export const getQuestionsByTag = (tag) =>
  api.get(`/questions?tag=${tag}`);

export const getAllQuestions = () =>
  api.get('/questions/all');

export const getRandomQuestions = (count) =>
  api.get(`/questions/random?count=${count}`);

export const getTags = () =>
  api.get('/questions/tags');