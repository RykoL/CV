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
const suspendOneSecond = () => {
  return new Promise((res, rej) => {
    setTimeout(() => {
      res()
    }, 1000)
  })
}

export const activateContactDetailsLoader = () => {
  const loaderTemplate = document.querySelector('#contact-loader-template');
  const content = loaderTemplate.content.cloneNode(true)
  const targetContainer = document.querySelector('#contact-data');
  targetContainer.appendChild(content);
  return suspendOneSecond()
}
