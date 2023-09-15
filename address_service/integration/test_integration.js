const assert = require("assert")
const { oneSecond, isUp, sleep, baseURL } = require("./util")

describe("Address Service", () => {

  /*
   * Wait until worker is up with retry backoff
   */
  before(async function() {
    this.timeout(20 * oneSecond)
    let retry = 0;

    while (!(await isUp()) && retry <= 5) {
      retry++;
      await sleep(oneSecond * retry)
    }
  })

  it("should return contact details", async () => {
    const response = await fetch(`${baseURL}/contact-details`, {
      headers: {
        "authorization": "some-key"
      }
    });
    const body = await response.json();

    const expectedBody = {
      name: "John Doe",
      telephone: "+61 2 8503 8000",
      email: "foo@bar.com",
      address: {
        street: "5th streeth",
        zip: 12345,
        city: "Some city"
      }
    }

    assert.equal(body.name, expectedBody.name);
    assert.equal(body.telephone, expectedBody.telephone);
    assert.equal(body.email, expectedBody.email);
    assert.equal(body.address.street, expectedBody.address.street);
    assert.equal(body.address.zip, expectedBody.address.zip);
    assert.equal(body.address.city, expectedBody.address.city);
  })

  it("should return status 401 if no auth token is sent", async () => {
    const response = await fetch(`${baseURL}/contact-details`);

    assert.equal(response.status, 401)
  })

  it("should return status 401 if auth token is malformed", async () => {
    const response = await fetch(`${baseURL}/contact-details`, {
      headers: {
        "authorization": "Bearer asldhasdkjh"
      }
    });

    assert.equal(response.status, 401)
  })
})
