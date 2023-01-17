import axios from "axios";

export default axios.create({
  baseURL: "http://34.134.102.72/api",
  headers: {
    "Content-type": "application/json"
  }
});
