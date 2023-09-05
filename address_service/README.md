# Address Service

This is a small cloudflare workers service written in rust that will deliver
contact information if the user is authenticated via a valid JWT token

## Setup

To setup this project simply run cargo build to download dependencies and build the library.

``` sh
$ cargo build
```

## Run unit tests

To run unit 

``` sh
$ cargo test
```

## Run integration tests

``` sh
$ make test_integration
```
