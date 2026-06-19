import api from './axios';

export const createTest = (data) =>
  api.post('/tests', data);

export const getAllTests = () =>
  api.get('/tests');

export const getTest = (id) =>
  api.get(`/tests/${id}`);

export const deleteTest = (id) =>
  api.delete(`/tests/${id}`);

export const publishTest = (id) =>
  api.post(`/tests/${id}/publish`);

export const addQuestionsToTest = (testId, questionIds) =>
  api.post(`/tests/${testId}/questions`, { questionIds });

export const removeQuestionFromTest = (testId, questionId) =>
  api.delete(`/tests/${testId}/questions/${questionId}`);

export const getTestQuestions = (testId) =>
  api.get(`/tests/${testId}/questions`);

export const sendInviteToTeam = (testId, teamId)=>
    api.post(
       `/tests/${testId}/invite?teamId=${teamId}`
    );
export const getMyTests=() =>
api.get(
 "/tests/my-tests"
);
export const getMyResults = () =>
api.get(
"/tests/my-results"
);