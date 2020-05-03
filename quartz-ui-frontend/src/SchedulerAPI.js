import axios from 'axios';

export const getJobs = () => {
  return axios.get('http://localhost:8080/api/jobs');
}

export const getJobHistory = (job, page = 0) => {
  return axios.get(`http://localhost:8080/api/job/${job}/history?page=${page}`);
}

export const pauseJob = (jobName) => {
  return axios.get(`http://localhost:8080/api/job/${jobName}/pause`);
}

export const resumeJob = (jobName) => {
  return axios.get(`http://localhost:8080/api/job/${jobName}/resume`);
}