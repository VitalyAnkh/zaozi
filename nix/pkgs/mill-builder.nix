# SPDX-License-Identifier: Apache-2.0
# SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>

{ stdenvNoCC, mill, writeText, makeSetupHook, runCommand, lib }:

{ name, src, millDepsHash, ... }@args:

let
  mill-rt-version = lib.head (lib.splitString "+" mill.jre.version);
  cachePrefix = if stdenvNoCC.isDarwin then "Library/Caches/coursier" else ".cache/coursier";
  self = stdenvNoCC.mkDerivation ({
    name = "${name}-mill-deps";
    inherit src;

    nativeBuildInputs = [
      mill
    ] ++ (args.nativeBuildInputs or [ ]);

    impureEnvVars = [ "JAVA_OPTS" ];

    buildPhase = ''
      runHook preBuild
      export JAVA_OPTS="-Duser.home=$TMPDIR $JAVA_OPTS"

      # Use "https://repo1.maven.org/maven2/" only to keep dependencies integrity
      export COURSIER_REPOSITORIES="central"

      mill -i __.prepareOffline
      mill -i __.scalaCompilerClasspath
      runHook postBuild
    '';

    installPhase = ''
        runHook preInstall
        mkdir -p $out/${cachePrefix}
        mv $TMPDIR/${cachePrefix} $out/${cachePrefix}
        runHook postInstall
      '';

    outputHashAlgo = "sha256";
    outputHashMode = "recursive";
    outputHash = millDepsHash;

    dontShrink = true;
    dontPatchELF = true;

    passthru.setupHook = makeSetupHook
      {
        name = "mill-setup-hook.sh";
        propagatedBuildInputs = [ mill ];
      }
      (writeText "mill-setup-hook" ''
        setupMillCache() {
          local tmpdir=$(mktemp -d)
          export JAVA_OPTS="$JAVA_OPTS -Duser.home=$tmpdir"

          mkdir -p "$tmpdir"/.cache "$tmpdir/.mill/ammonite"

          cp -r "${self}"/${cachePrefix} -T "$tmpdir"/${cachePrefix}
          touch "$tmpdir/.mill/ammonite/rt-${mill-rt-version}.jar"

          echo "JAVA HOME dir set to $tmpdir"
        }

        postUnpackHooks+=(setupMillCache)
      '');
  } // (builtins.removeAttrs args [ "name" "src" "millDepsHash" "nativeBuildInputs" ]));
in
self
