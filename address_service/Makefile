ADDRESS_SERVICE_WASM_TARGET=target/wasm32-unknown-unknown/release/address_service.wasm
RUST_SOURCES=$(shell find src -name "*.rs")

test_integration: $(ADDRESS_SERVICE_WASM_TARGET)
	{ wrangler dev & echo $$! > wrangler.pid ;}
	npm test || true
	cat wrangler.pid | xargs kill
	rm wrangler.pid


$(ADDRESS_SERVICE_WASM_TARGET): $(RUST_SOURCES)
	cargo install -q worker-build && worker-build --release
