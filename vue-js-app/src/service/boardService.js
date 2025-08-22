import axiosInstance from "@/api/axiosInstance";

export const boardService = {
  getAllBoard: async () => {
    const result = await axiosInstance.get("boards");

    return result;
  },
};
