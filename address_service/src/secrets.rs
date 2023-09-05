use std::env;

pub fn read_secret_from_env(env_name: &str) -> String {
    env::var(env_name).expect(&format!(
        "The secret '{}' cannot be read from environment variables.",
        env_name
    ))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_read_secret_from_env_should_read_secret() {
        env::set_var("some-secret", "this-is-the-secret");

        let secret = read_secret_from_env("some-secret");

        assert_eq!(secret, "this-is-the-secret".to_string())
    }

    #[test]
    #[should_panic(
        expected = "The secret 'some-other-secret' cannot be read from environment variables."
    )]
    fn test_read_secret_from_env_should_panic_if_secret_is_not_found() {
        read_secret_from_env("some-other-secret");
    }
}
