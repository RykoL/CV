use serde::{Deserialize, Serialize};

#[derive(Deserialize, Serialize, Debug)]
pub struct ContactDetails {
    pub name: String,
    pub telephone: String,
    pub email: String,
    pub address: Address,
}

#[derive(Deserialize, Serialize, Debug)]
pub struct Address {
    pub street: String,
    pub zip: i32,
    pub city: String,
}
