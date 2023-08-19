{ pkgs ? import (fetchTarball https://github.com/nixos/nixpkgs/archive/9d8a066fd49dbd026e9b6c02c71a7d221e47e84e.tar.gz) {} }:


pkgs.mkShell {
  buildInputs = [
  ];
}
