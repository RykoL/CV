use std::collections::HashSet;

use jsonwebtoken::{decode, DecodingKey, Validation};

use crate::claims;

pub struct TokenValidator<'a> {
    secret: &'a [u8],
    iss: String,
}

impl TokenValidator<'_> {
    pub fn new(key: &[u8], issuer: String) -> TokenValidator {
        TokenValidator {
            secret: key,
            iss: issuer,
        }
    }

    pub fn validate(&self, token: String) -> bool {
        let mut validation = Validation::new(jsonwebtoken::Algorithm::HS256);
        validation.iss = Some(HashSet::from([self.iss.clone()]));
        match decode::<claims::Claims>(&token, &DecodingKey::from_secret(self.secret), &validation)
        {
            Ok(_) => true,
            Err(_) => false,
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use jsonwebtoken::{encode, EncodingKey, Header};

    #[test]
    fn test_validate_returns_true_on_valid_token() {
        let key = b"some_key";

        let claims = claims::Claims {
            sub: String::from("foo@bar.com"),
            iss: "issuer@bar.com".to_string(),
            exp: 1000000000000,
        };

        let token = match encode(&Header::default(), &claims, &EncodingKey::from_secret(key)) {
            Ok(t) => t,
            Err(_) => String::new(),
        };

        let token_validator = TokenValidator::new(key, "issuer@bar.com".to_string());
        assert_eq!(token_validator.validate(token), true);
    }

    #[test]
    fn test_validate_returns_false_on_malformed_token() {
        let key = b"some_key";
        let token_validator = TokenValidator::new(key, "issuer@bar.com".to_string());
        assert_eq!(
            token_validator.validate("some_invalid_token".to_string()),
            false
        );
    }

    #[test]
    fn test_validate_returns_false_on_wrong_issuer() {
        let key = b"some_key";

        let claims = claims::Claims {
            sub: String::from("foo@bar.com"),
            iss: "not-the-issuer@bar.com".to_string(),
            exp: 1000000000000,
        };

        let token = match encode(&Header::default(), &claims, &EncodingKey::from_secret(key)) {
            Ok(t) => t,
            Err(_) => String::new(),
        };

        let token_validator = TokenValidator::new(key, "issuer@bar.com".to_string());
        assert_eq!(token_validator.validate(token), false);
    }

    #[test]
    fn test_validate_returns_false_on_wrong_key() {
        let key = b"some_key";
        let different_key = b"some other key";

        let claims = claims::Claims {
            sub: String::from("foo@bar.com"),
            iss: "not-the-issuer@bar.com".to_string(),
            exp: 1000000000000,
        };

        let token = match encode(
            &Header::default(),
            &claims,
            &EncodingKey::from_secret(different_key),
        ) {
            Ok(t) => t,
            Err(_) => String::new(),
        };

        let token_validator = TokenValidator::new(key, "issuer@bar.com".to_string());
        assert_eq!(token_validator.validate(token), false);
    }

    #[test]
    fn test_validate_returns_false_on_expiry() {
        let key = b"some_key";

        let claims = claims::Claims {
            sub: String::from("foo@bar.com"),
            iss: "issuer@bar.com".to_string(),
            exp: 100,
        };

        let token = match encode(&Header::default(), &claims, &EncodingKey::from_secret(key)) {
            Ok(t) => t,
            Err(_) => String::new(),
        };

        let token_validator = TokenValidator::new(key, "issuer@bar.com".to_string());
        assert_eq!(token_validator.validate(token), false);
    }
}
