export const base64ToArrayBuffer = base64 => {
  let binary_string = window.atob(base64);
  let len = binary_string.length;
  let bytes = new Uint8Array(len);
  for (let i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes.buffer;
};

export const openNewTab = (URL, titleName) => {
  try {
    let newPage = window.open(URL);
    newPage.onload = function() {
      newPage.document.title = titleName;
    };
  } catch (e) {
    this.$message.warning('Please allow to open new tab in your browser');
  }
};
