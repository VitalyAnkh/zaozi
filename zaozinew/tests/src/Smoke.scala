// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi.tests

import me.jiuyang.zaozi.default.{*, given}
import me.jiuyang.zaozi.{ConstructorApi, Interface, Parameter, UInt, UIntApi, Wire}
import org.llvm.circt.scalalib.firrtl.capi.{given_DialectHandleApi, given_ModuleApi}
import org.llvm.circt.scalalib.firrtl.operation.{Circuit, CircuitApi, given_CircuitApi}
import org.llvm.mlir.scalalib.{Block, Context, ContextApi, LocationApi, given_ContextApi, given_LocationApi, given_ModuleApi, given_NamedAttributeApi, given_RegionApi, given_TypeApi, given_ValueApi, Module as MlirModule, ModuleApi as MlirModuleApi}

import java.lang.foreign.Arena

def firrtlTest[P <: Parameter, I <: Interface[P]](
  parameter:  P,
  interface:  I,
  moduleName: Option[String] = None
)(checkLines: String*
)(body:       (Arena, Block) ?=> (P, Wire[I]) => Unit
): Unit =
  val arena = Arena.ofConfined()
  given Arena = arena
  // For each Module it has its own context here.
  val context   = summon[ContextApi].contextCreate
  context.loadFirrtlDialect()
  given Context = context
  // We construct the module firstly.
  val ctx       = summon[ConstructorApi].Module(moduleName.getOrElse("NoName"), parameter, interface)(body)
  // Then based on the module to construct the circuit.
  val circuit: Circuit = summon[CircuitApi].op("Passthrough")
  given MlirModule = summon[MlirModuleApi].moduleCreateEmpty(summon[LocationApi].locationUnknownGet)
  circuit.appendToModule()
  val out          = new StringBuilder
  summon[MlirModule].exportFIRRTL(out ++= _)
  if (checkLines.isEmpty)
    println(s"please add lines to check for:\n$out")
    assert(false)
  else checkLines.foreach(l => assert(out.toString.contains(l)))

import utest.*

case class SimpleParameter(width: Int) extends Parameter

class PassthroughInterface(parameter: SimpleParameter) extends Interface[SimpleParameter](parameter):
  val i = Flipped(UInt(parameter.width.W))
  val o = Aligned(UInt(parameter.width.W))

object Smoke extends TestSuite:
  val tests = Tests:
    val parameter = SimpleParameter(32)
    val out       = new StringBuilder
    test("Passthrough")
      firrtlTest(parameter, PassthroughInterface(parameter))("wire io : { i : UInt<32>, flip o : UInt<32> }"):
        (p, io) =>