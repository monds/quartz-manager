import axios from 'axios';

export const getJobs = () => {
  // console.log(shortGap);
  // console.log(`${queryString.stringify({shortGap, steadyGap})}`);
  // return axios.get(`http://localhost:8080/api/candle/${interval}?${queryString.stringify({shortGap, steadyGap, from})}`);
  return axios.get('http://localhost:8080/api/jobs');
  // return jsonData;
}

export const getJobHistory = (job, page = 1) => {
  return axios.get(`http://localhost:8080/api/job/${job}/history?page=${page}`);
}