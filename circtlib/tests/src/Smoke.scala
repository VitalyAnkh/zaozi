// SPDX-License-Identifier: Apache-2.0
package me.jiuyang.zaozi.circtlib.tests

import org.llvm.circt.scalalib.operator.{*, given}
import org.llvm.circt.scalalib.{FirrtlBundleFieldApi, FirrtlNameKind, TypeApi, given_DialectHandleApi, given_FirrtlBundleFieldApi, given_FirrtlDirectionApi, given_ModuleApi, given_TypeApi}
import org.llvm.mlir.scalalib.{Context, ContextApi, LocationApi, Module as MlirModule, ModuleApi as MlirModuleApi, given}
import utest.*

import java.lang.foreign.Arena

object Smoke extends TestSuite:
  val tests = Tests:
    test("C-API"):
      test("Load Panama Context"):
        val arena   = Arena.ofConfined()
        given Arena = arena
        test("Load Dialect"):
          val context         = summon[ContextApi].contextCreate
          context.loadFirrtlDialect()
          given Context       = context
          val unknownLocation = summon[LocationApi].locationUnknownGet
          test("Create MlirModule"):
            val rootModule: MlirModule = summon[MlirModuleApi].moduleCreateEmpty(unknownLocation)
            given MlirModule = rootModule
            test("Create Circuit"):
              val circuit: Circuit = summon[CircuitApi].circuit("Passthrough")
              circuit.appendToModule()
              given Circuit = circuit
              test("Create CirctModule"):
                val api     = summon[FirrtlBundleFieldApi]
                val typeApi = summon[TypeApi]
                val module: Module = summon[ModuleApi].module(
                  "Passthrough",
                  unknownLocation,
                  Seq(
                    (api.createFirrtlBundleField("i", true, 32.getUInt), unknownLocation),
                    (api.createFirrtlBundleField("o", false, 32.getUInt), unknownLocation)
                  )
                )
                module.appendToCircuit()
                given Module = module
                val out      = new StringBuilder
                test("Connect"):
                  summon[ConnectApi]
                    .connect(module.getIO(0), module.getIO(1), unknownLocation)
                    .operation
                    .print(out ++= _)
                  assert(
                    out.toString == """"firrtl.connect"(%arg1, %arg0) : (!firrtl.uint<32>, !firrtl.uint<32>) -> ()""".stripMargin
                  )
                test("Structure"):
                  test("Instance"):
                    summon[InstanceApi]
                      .instance(
                        "SomeModule",
                        "inst0",
                        FirrtlNameKind.Interesting,
                        unknownLocation,
                        Seq(
                          api.createFirrtlBundleField("i", true, 32.getUInt),
                          api.createFirrtlBundleField("o", true, 32.getUInt)
                        )
                      )
                      .operation
                      .print(out ++= _)
                    assert(
                      out.toString == """%0:2 = "firrtl.instance"() <{moduleName = @SomeModule, name = "inst0", nameKind = #firrtl<name_kind interesting_name>, portDirections = array<i1: false, false>, portNames = ["i", "o"]}> : () -> (!firrtl.uint<32>, !firrtl.uint<32>)"""
                    )
                  test("Wire"):
                    given org.llvm.mlir.scalalib.Block = module.block
                    summon[WireApi]
                      .wire(
                        name = "someWire",
                        location = unknownLocation,
                        nameKind = FirrtlNameKind.Droppable,
                        tpe = 32.getSInt
                      )
                      .operation
                      .print(out ++= _)
                    assert(
                      out.toString == """%0 = "firrtl.wire"() <{name = "someWire", nameKind = #firrtl<name_kind droppable_name>}> : () -> !firrtl.sint<32>"""
                    )
                  test("Reg"):
                    given org.llvm.mlir.scalalib.Block = module.block
                    val clock                          = summon[WireApi].wire(
                      name = "clock",
                      location = unknownLocation,
                      nameKind = FirrtlNameKind.Droppable,
                      tpe = summon[TypeApi].getClock
                    )
                    clock.operation.print(out ++= _)
                    out ++= "\n"
                    summon[RegApi]
                      .reg(
                        name = "someReg",
                        location = unknownLocation,
                        nameKind = FirrtlNameKind.Droppable,
                        tpe = 32.getUInt,
                        clock = clock.result
                      )
                      .operation
                      .print(out ++= _)
                    assert(
                      out.toString ==
                        """%0 = "firrtl.wire"() <{name = "clock", nameKind = #firrtl<name_kind droppable_name>}> : () -> !firrtl.clock
                          |%1 = "firrtl.reg"(%0) <{name = "someReg", nameKind = #firrtl<name_kind droppable_name>}> : (!firrtl.clock) -> !firrtl.uint<32>""".stripMargin
                    )
                  test("RegReset"):
                    given org.llvm.mlir.scalalib.Block = module.block
                    val clock                          = summon[WireApi].wire(
                      name = "clock",
                      location = unknownLocation,
                      nameKind = FirrtlNameKind.Droppable,
                      tpe = summon[TypeApi].getClock
                    )
                    val reset                          = summon[WireApi].wire(
                      name = "reset",
                      location = unknownLocation,
                      nameKind = FirrtlNameKind.Droppable,
                      tpe = 1.getUInt
                    )
                    clock.operation.print(out ++= _)
                    out ++= "\n"
                    reset.operation.print(out ++= _)
                    out ++= "\n"
                    summon[RegResetApi]
                      .regReset(
                        name = "someReg",
                        location = unknownLocation,
                        nameKind = FirrtlNameKind.Droppable,
                        tpe = 32.getUInt,
                        clock = clock.result,
                        reset = reset.result
                      )
                      .operation
                      .print(out ++= _)
                    assert(
                      out.toString ==
                        """%0 = "firrtl.wire"() <{name = "clock", nameKind = #firrtl<name_kind droppable_name>}> : () -> !firrtl.clock
                          |%1 = "firrtl.wire"() <{name = "reset", nameKind = #firrtl<name_kind droppable_name>}> : () -> !firrtl.uint<1>
                          |%2 = "firrtl.regreset"(%0, %1) <{name = "someReg", nameKind = #firrtl<name_kind droppable_name>}> : (!firrtl.clock, !firrtl.uint<1>) -> !firrtl.uint<32>""".stripMargin
                    )
