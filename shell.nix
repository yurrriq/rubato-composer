{ pkgs ? import ./nix }:

let
  rubato-composer = import ./. {};
in

pkgs.mkShell {
  buildInputs = with pkgs; [
    ant
    jdk
    rubato-composer.nativeBuildInputs
  ];
}
