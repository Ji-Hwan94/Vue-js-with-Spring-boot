export const commonService = {
  fileDownLoad: (file) => {
    // 파일 다운로드 구현
    const link = document.createElement("a");
    link.href = `/api/files/download/${file.id}`;
    link.download = file.originalName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  },
};
