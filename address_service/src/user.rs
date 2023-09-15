#[derive(Clone)]
pub struct User {
    pub name: String,
    pub token: String,
    pub expiry_date: u32,
}
