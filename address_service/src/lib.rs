mod auth_repository;
mod auth_service;
mod contact_details;
mod errors;
mod user;
mod validator;

use auth_repository::LocalAuthenticationRepository;
use auth_service::AuthenticationService;
use contact_details::{Address, ContactDetails};
use worker::*;

fn extract_auth_token(req: Request) -> Option<String> {
    req.headers().get("authorization").ok().flatten()
}

fn get_contact_details() -> ContactDetails {
    ContactDetails {
        name: "John Doe".to_string(),
        telephone: "+61 2 8503 8000".to_string(),
        email: "foo@bar.com".to_string(),
        address: Address {
            street: "5th streeth".to_string(),
            zip: 12345,
            city: "Some city".to_string(),
        },
    }
}

#[event(fetch)]
async fn main(req: Request, env: Env, _ctx: Context) -> Result<Response> {
    let router = Router::new();

    router
        .get("/health", |_req, _ctx| Response::ok(String::new()))
        .get("/contact-details", |req, _ctx| {
            let token = extract_auth_token(req);
            let repo = LocalAuthenticationRepository::new();
            let service = AuthenticationService::new(&repo);

            if token.is_none() {
                return Response::error("Not authenticated.", 401);
            }

            let token_val = token.unwrap();
            match service.authenticate(&token_val) {
                Ok(_) => Response::from_json(&get_contact_details()),
                Err(msg) => Response::error(msg, 401),
            }
        })
        .run(req, env)
        .await
}
