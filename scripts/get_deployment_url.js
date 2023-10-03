const fs = require('node:fs/promises')
const CONTACT_SERVICE_NAME = 'contact-service'
const RENDER_API_BASE_URL = 'https://api.render.com/v1/services'
const renderToken = process.env.RENDER_API_TOKEN

const fetchService = async () => {
  const resp = await fetch(`${RENDER_API_BASE_URL}?name=${CONTACT_SERVICE_NAME}`, {
    headers: {
      'Accept': 'application/json',
      'Authorization': `Bearer ${renderToken}`
    }
  })

  return await resp.json()
}

const extractPublicURL = (services) => {
  return services[0].service.serviceDetails.url
}

const writeURLToData = async (publicURL) => {
  const data = `contactServicePublicURL: ${publicURL}`
  await fs.writeFile("_data/url.yaml", data)
}

fetchService()
  .then(extractPublicURL)
  .then(writeURLToData)
