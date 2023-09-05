const oneSecond = 1000;
const baseURL = "http://localhost:8787";

const sleep = (interval) => new Promise(resolve => setTimeout(resolve, interval));

const isUp = async () => {
  try {
    return (await fetch(`${baseURL}/health`)).status === 200
  } catch (error) {
    return false;
  }
}


module.exports = {
  oneSecond,
  sleep,
  isUp,
  baseURL
}
