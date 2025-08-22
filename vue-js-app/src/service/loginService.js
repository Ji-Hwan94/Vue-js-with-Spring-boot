import axiosInstance from "@/api/axiosInstance";

export const loginService = {
  getLogin: async (params) => {
    const result = await axiosInstance.post("users/login", params);

    return result;
  },
};
