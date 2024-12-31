// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>

import mill._
import mill.scalalib.TestModule.Utest
import mill.scalalib._
import mill.scalalib.scalafmt._
import mlirlib.circtInstallPath
import os.Path

object v {
  val scala = "3.5.2"
  val mainargs = ivy"com.lihaoyi::mainargs:0.7.6"
  val oslib = ivy"com.lihaoyi::os-lib:0.11.3"
  val upickle = ivy"com.lihaoyi::upickle:4.0.2"
  val utest = ivy"com.lihaoyi::utest:0.8.4"
  val sourcecode = ivy"com.lihaoyi::sourcecode:0.4.2"
  val pprint = ivy"com.lihaoyi::pprint:0.9.0"
}

object zaozi extends ScalaModule with ScalafmtModule {
  def scalaVersion = T(v.scala)
  def ivyDeps = T(Seq(v.mainargs, v.oslib, v.upickle, v.sourcecode))
  def moduleDeps = Seq(circtlib)
  object tests extends ScalaTests with Utest {
    def ivyDeps = Agg(v.utest)

    override def forkArgs: T[Seq[String]] = T(
      super.forkArgs() ++ circtlib.forkArgs()
    )
  }
}

object zaozinew extends ScalaModule with ScalafmtModule {
  def scalaVersion = T(v.scala)
  def ivyDeps = T(Seq(v.mainargs, v.oslib, v.upickle, v.sourcecode))
  def moduleDeps = Seq(circtlib)
  object tests extends ScalaTests with Utest {
    def ivyDeps = Agg(v.utest)

    override def forkArgs: T[Seq[String]] = T(
      super.forkArgs() ++ circtlib.forkArgs()
    )
  }
}


// The Scala API
object circtlib extends ScalaModule with ScalafmtModule with PanamaModule { outer =>
  def scalaVersion = T(v.scala)

  def includeConstantsFile = millSourcePath / "capi" / "includeConstants.txt"

  def includeFunctionsFile = millSourcePath / "capi" / "includeFunctions.txt"

  def includeStructsFile = millSourcePath / "capi" / "includeStructs.txt"

  def includeTypedefsFile = millSourcePath / "capi" / "includeTypedefs.txt"

  def includeUnionsFile = millSourcePath / "capi" / "includeUnions.txt"

  def includeVarsFile = millSourcePath / "capi" / "includeVars.txt"

  def linkLibrariesFile = millSourcePath / "capi" / "linkLibraries.txt"

  def target = T("org.llvm.circt")

  def headerClassName = T("CAPI")

  def header = T(PathRef(millSourcePath / "capi" / "jextract-headers.h"))

  def circtInstallPath = T(os.Path(T.ctx().env.getOrElse("CIRCT_INSTALL_PATH", "CIRCT_INSTALL_PATH not found, you are not in the nix env?")))

  def jextractBinary = T(os.Path(T.ctx().env.getOrElse("JEXTRACT_INSTALL_PATH", "JEXTRACT_INSTALL_PATH not found, you are not in the nix env?")) / "bin" / "jextract")

  def includePaths = T(Seq(PathRef(circtInstallPath() / "include")))

  def libraryPaths = T(Seq(PathRef(circtInstallPath() / "lib")))

  override def moduleDeps = Seq(mlirlib)

  def ivyDeps = Agg(v.pprint)

  object tests extends ScalaModule with Utest {
    override def forkArgs: T[Seq[String]] = T(outer.forkArgs)

    override def moduleDeps = Seq(circtlib, mlirlib)

    def ivyDeps = Agg(v.utest)

    override def scalaVersion = outer.scalaVersion
  }
}

object mlirlib extends ScalaModule with ScalafmtModule with PanamaModule {
  def scalaVersion = T(v.scala)

  def includeConstantsFile = millSourcePath / "capi" / "includeConstants.txt"

  def includeFunctionsFile = millSourcePath / "capi" / "includeFunctions.txt"

  def includeStructsFile = millSourcePath / "capi" / "includeStructs.txt"

  def includeTypedefsFile = millSourcePath / "capi" / "includeTypedefs.txt"

  def includeUnionsFile = millSourcePath / "capi" / "includeUnions.txt"

  def includeVarsFile = millSourcePath / "capi" / "includeVars.txt"

  def linkLibrariesFile = millSourcePath / "capi" / "linkLibraries.txt"

  def target = T("org.llvm.mlir")

  def headerClassName = T("CAPI")

  def header = T(PathRef(millSourcePath / "capi" / "jextract-headers.h"))

  def circtInstallPath = T(os.Path(T.ctx().env.getOrElse("CIRCT_INSTALL_PATH", "CIRCT_INSTALL_PATH not found, you are not in the nix env?")))

  def jextractBinary = T(os.Path(T.ctx().env.getOrElse("JEXTRACT_INSTALL_PATH", "JEXTRACT_INSTALL_PATH not found, you are not in the nix env?")) / "bin" / "jextract")

  def includePaths = T(Seq(PathRef(circtInstallPath() / "include")))

  def libraryPaths = T(Seq(PathRef(circtInstallPath() / "lib")))

  object tests extends ScalaTests with Utest {
    def ivyDeps = Agg(v.utest)

    override def forkArgs: T[Seq[String]] = T(
      super.forkArgs() ++ Seq("--enable-native-access=ALL-UNNAMED", "--enable-preview")
        ++ libraryPaths().map(p => s"-Djava.library.path=${p.path}")
    )
  }
}

// Panama API
trait PanamaModule extends JavaModule {

  // API to regenerate all C-API from headers
  def dumpIncludes(): Command[Unit] = T.command {
    val f = os.temp()
    os.proc(
      Seq(jextractBinary().toString, header().path.toString)
        ++ includePaths().flatMap(p => Seq("-I", p.path.toString))
        ++ Seq("--dump-includes", f.toString)
    ).call()
    os.remove(includeConstantsFile())
    os.remove(includeFunctionsFile())
    os.remove(includeStructsFile())
    os.remove(includeTypedefsFile())
    os.remove(includeUnionsFile())
    os.remove(includeVarsFile())
    os.write(includeConstantsFile(), "")
    os.write(includeFunctionsFile(), "")
    os.write(includeStructsFile(), "")
    os.write(includeTypedefsFile(), "")
    os.write(includeUnionsFile(), "")
    os.write(includeVarsFile(), "")

    os.read.lines(f).filter(s => s.nonEmpty && !s.startsWith("#")).filter(str => !str.contains("jextract")).foreach { str =>
      val writeStr = str.replaceFirst("--include.+? ", "") + "\n"
      if (str.startsWith("--include-constant")) os.write.append(includeConstantsFile(), writeStr)
      if (str.startsWith("--include-function")) os.write.append(includeFunctionsFile(), writeStr)
      if (str.startsWith("--include-struct")) os.write.append(includeStructsFile(), writeStr)
      if (str.startsWith("--include-typedef")) os.write.append(includeTypedefsFile(), writeStr)
      if (str.startsWith("--include-union")) os.write.append(includeUnionsFile(), writeStr)
      if (str.startsWith("--include-var")) os.write.append(includeVarsFile(), writeStr)
    }
  }

  override def generatedSources: Target[Seq[PathRef]] = T {
    super.generatedSources() ++ {
      // @formatter:off
      os.proc(
        Seq(jextractBinary().toString, header().path.toString)
          ++ includePaths().flatMap(p => Seq("-I", p.path.toString))
          ++ Seq(
          "-t", target(),
          "--header-class-name", headerClassName(),
          "--source",
          "--output", T.dest.toString
        ) ++ includeFunctions().flatMap(f => Seq("--include-function", f)) ++
          includeConstants().flatMap(f => Seq("--include-constant", f)) ++
          includeStructs().flatMap(f => Seq("--include-struct", f)) ++
          includeTypedefs().flatMap(f => Seq("--include-typedef", f)) ++
          includeUnions().flatMap(f => Seq("--include-union", f)) ++
          includeVars().flatMap(f => Seq("--include-var", f)) ++
          linkLibraries().flatMap(l => Seq("-l", l))
      ).call(T.dest)
      // @formatter:on
      Seq(PathRef(T.dest))
    }
  }

  override def javacOptions: Target[Seq[String]] = T(super.javacOptions() ++ Seq("--enable-preview", "--release", "21"))

  override def forkArgs: T[Seq[String]] = T(
    super.forkArgs() ++ Seq("--enable-native-access=ALL-UNNAMED", "--enable-preview")
      ++ libraryPaths().map(p => s"-Djava.library.path=${p.path}")
  )

  def includeConstantsFile: Target[Path]
  def includeConstants: Target[IndexedSeq[String]] = T.input(os.read.lines(includeConstantsFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def includeFunctionsFile: Target[Path]
  def includeFunctions: Target[IndexedSeq[String]] = T.input(os.read.lines(includeFunctionsFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def includeStructsFile: Target[Path]
  def includeStructs: Target[IndexedSeq[String]] = T.input(os.read.lines(includeStructsFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def includeTypedefsFile: Target[Path]
  def includeTypedefs: Target[IndexedSeq[String]] = T.input(os.read.lines(includeTypedefsFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def includeUnionsFile: Target[Path]
  def includeUnions: Target[IndexedSeq[String]] = T.input(os.read.lines(includeUnionsFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def includeVarsFile: Target[Path]
  def includeVars: Target[IndexedSeq[String]] = T.input(os.read.lines(includeVarsFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def linkLibrariesFile: Target[Path]
  def linkLibraries: Target[IndexedSeq[String]] = T.input(os.read.lines(linkLibrariesFile()).filter(s => s.nonEmpty && !s.startsWith("#")).map(_.replaceFirst(" .*", "")))

  def target: Target[String]

  def headerClassName: Target[String]

  def header: Target[PathRef]

  def includePaths: Target[Seq[PathRef]]

  def libraryPaths: Target[Seq[PathRef]]

  def jextractBinary: Target[Path]
}