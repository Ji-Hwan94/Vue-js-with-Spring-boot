import axiosInstance from "@/api/axiosInstance";

export const boardService = {
  getAllBoard: async () => {
    const result = await axiosInstance.get("boards");

    return result;
  },
  createBoard: async (data) => {
    try {
      const result = await axiosInstance.post("boards", data);
      return result;
    } catch (error) {
      console.error("createBoard 에러:", error.response?.data);
      throw error;
    }
  },
  detailBoard: async (id) => {
    try {
      const result = await axiosInstance.get(`boards/${id}`);
      return result;
    } catch (error) {
      throw error;
    }
  },
  updateBoard: async (id, data) => {
    try {
      const result = await axiosInstance.put(`boards/${id}`, data);
      return result;
    } catch (error) {
      console.error("updateBoard 에러:", error.response?.data);
      throw error;
    }
  },
  deleteBoard: async (id) => {
    try {
      await axiosInstance.delete(`boards/${id}`);
    } catch (error) {
      throw error;
    }
  },
};
