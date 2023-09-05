mod claims;
mod contact_details;
mod errors;
mod secrets;
mod validator;

use contact_details::{Address, ContactDetails};
use worker::*;

#[event(fetch)]
async fn main(req: Request, env: Env, _ctx: Context) -> Result<Response> {
    let router = Router::new();

    router
        .get("/health", |_req, _ctx| Response::ok(String::new()))
        .get("/contact-details", |req, _ctx| {
            if req.headers().has("Authentication")? != true {
                return Response::error("Not authenticated.", 401);
            }

            let contact_details = ContactDetails {
                name: "John Doe".to_string(),
                telephone: "+61 2 8503 8000".to_string(),
                email: "foo@bar.com".to_string(),
                address: Address {
                    street: "5th streeth".to_string(),
                    zip: 12345,
                    city: "Some city".to_string(),
                },
            };

            Response::from_json(&contact_details)
        })
        .run(req, env)
        .await
}
