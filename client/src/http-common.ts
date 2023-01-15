import axios from "axios";

export default axios.create({
  baseURL: "http://34.173.224.5/api",
  headers: {
    "Content-type": "application/json"
  }
});
