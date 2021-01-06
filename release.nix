{ stdenv, antBuild, fetchFromGitHub, ghostscript, gzip, imagemagick, texlive }:

let
  version = "4.11";
in antBuild {
  name = "rubato-composer-${version}";
  inherit version;

  src = ./.;
  # src = fetchFromGitHub {
  #   owner = "rubato-composer";
  #   repo = "rubato-composer";
  #   sha256 = "03x9kyw3475fa8gry1gnaan7bqyzcg3y4gg68d1lvfn10g9hb91b";
  #   rev = "9afb8fa41a22b38cd79248adf45ace5733df5e43";
  # };

  patchPhase = ''
    substituteInPlace java/build.xml --replace \
        'source="1.6" target="1.6"' \
        'encoding="UTF-8" source="1.7" target="1.7"'
  '';

  nativeBuildInputs = [
    ghostscript
    gzip
    imagemagick
    (
      texlive.combine {
        inherit (texlive) scheme-small
          cm-super
          listings
          marvosym
          pict2e
          soul
          titlesec
        ;
      }
    )
  ];

  antTargets = [
    "compile"
    "doc"
    # "dist-api"
    # "dist-bin"
    # "dist-src"
  ];

  installPhase = ''
    install -m755 java/dist/rubato.jar -Dt $out/
    install -m644 docs/manual.pdf -Dt $out/
  '';

  meta = with stdenv.lib; {
    homepage = "https://rubato.org/";
    description = "A music software based on the concepts and models of mathematical music theory";
    license = licenses.gpl2;
    mainters = with maintainers; [ yurrriq ];
  };
}
