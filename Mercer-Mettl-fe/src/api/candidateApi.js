import api from "./axios";

export const getMyProfile = () =>
  api.get("/user/me");

export const startAttempt = (testId, token) =>
  api.get(`/tests/${testId}/attempt`, {
    params: { token }
  });

export const getQuestions = (testId) =>
  api.get(`/tests/${testId}/questions`);

export const submitAttempt = (
  attemptId,
  answers
) =>
  api.post(
    `/tests/attempts/${attemptId}/submit`,
    answers
  );

export const getTeamMembers = (
  teamId
) =>
  api.get(
    `/teams/${teamId}/members`
  );

export const startTestWithToken=(testId,token)=>
api.post(
 `/tests/${testId}/start-token?token=${token}`
);