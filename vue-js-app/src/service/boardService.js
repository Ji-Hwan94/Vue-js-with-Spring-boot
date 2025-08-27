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
  createFileBoard: async (data) => {
    try {
      const formData = new FormData();
      formData.append("title", data.title);
      formData.append("description", data.description);

      // 파일이 있으면 추가
      if (data.files && data.files.length > 0) {
        data.files.forEach((file) => {
          if (file) {
            formData.append("files", file);
          }
        });
      }

      const result = await axiosInstance.post(`boards/with-files`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      return result;
    } catch (error) {}
  },
  updateFileBoard: async (id, data) => {
    try {
      console.log(data, data.deleteFileIds.length);
      const formData = new FormData();
      formData.append("title", data.title);
      formData.append("description", data.description);

      // 파일이 있으면 추가
      if (data.files && data.files.length > 0) {
        data.files.forEach((file) => {
          if (file) {
            formData.append("files", file);
          }
        });
      }

      // 삭제할 파일 ID가 있으면 추가
      if (data.deleteFileIds && data.deleteFileIds.length > 0) {
        data.deleteFileIds.forEach((fileId) => {
          formData.append("deleteFileIds", fileId);
        });
      }

      const result = await axiosInstance.put(
        `boards/${id}/with-files`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      return result;
    } catch (error) {
      console.error("updateFileBoard 에러:", error.response?.data);
      throw error;
    }
  },
};
