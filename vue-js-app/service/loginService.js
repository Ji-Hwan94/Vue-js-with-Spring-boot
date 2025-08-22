import axiosInstance from "../api/axiosInstance";

export const loginService = {
  getLogin: async (params) => {
    const result = await axiosInstance.get("/api/users", { params });
  },
};

