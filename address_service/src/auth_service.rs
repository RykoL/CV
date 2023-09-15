use crate::{auth_repository::AuthenticationRepository, user::User};

pub struct AuthenticationService<'r> {
    repository: &'r dyn AuthenticationRepository,
}

impl<'r> AuthenticationService<'r> {
    pub fn new(repository: &'r dyn AuthenticationRepository) -> AuthenticationService {
        AuthenticationService {
            repository: repository,
        }
    }

    pub fn authenticate(&self, token: &String) -> Result<User, String> {
        self.repository
            .get_user_by_access_token(token)
            .ok_or("No token like this exists.".to_string())
    }
}
