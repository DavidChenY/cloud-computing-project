import axios from "axios";

export default axios.create({
  baseURL: "http://34.69.78.123/api",
  headers: {
    "Content-type": "application/json"
  }
});
