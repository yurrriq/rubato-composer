{ pkgs ? import ./nix }:

pkgs.callPackage ./release.nix {
  antBuild = pkgs.releaseTools.antBuild;
}
