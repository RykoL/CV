export const getContactDetails = async (url, token) => {
  const resp = await fetch(`${url}/contact-details`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return await resp.text()
}

export const getTokenFromURL = () => {
  const params = new URLSearchParams(window.location.search)
  return params.get("token");
}
