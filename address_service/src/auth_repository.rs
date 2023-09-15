use crate::user::User;
use std::collections::HashMap;

pub trait AuthenticationRepository {
    fn get_user_by_access_token(&self, token: &String) -> Option<User>;
}

pub struct LocalAuthenticationRepository {
    users: HashMap<String, User>,
}

impl LocalAuthenticationRepository {
    pub fn new() -> LocalAuthenticationRepository {
        let mut users = HashMap::new();
        users.insert(
            "some-key".to_string(),
            User {
                token: "some-key".to_string(),
                name: String::new(),
                expiry_date: 1000000000,
            },
        );
        LocalAuthenticationRepository { users }
    }
}

impl AuthenticationRepository for LocalAuthenticationRepository {
    fn get_user_by_access_token(&self, token: &String) -> Option<User> {
        self.users.get(token).cloned()
    }
}
