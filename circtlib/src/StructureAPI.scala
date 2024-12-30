// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.circt.scalalib.{*, given}
import org.llvm.mlir.scalalib.{Module as MlirModule, *, given}

import java.lang.foreign.Arena

class Circuit(val operation: Operation)
trait CircuitApi:
  inline def circuit(
    circuitName: String
  )(
    using arena: Arena,
    context:     Context
  ): Circuit

  extension (c: Circuit)
    inline def block(
      using Arena
    ): Block
    inline def appendToModule(
    )(
      using arena: Arena,
      module:      MlirModule
    ): Unit
end CircuitApi

class Class(val operation: Operation)
class ExtClass(val operation: Operation)
class ExtModule(val operation: Operation)
class IntModule(val operation: Operation)
class MemModule(val operation: Operation)
class Module(val operation: Operation)
trait ModuleApi:
  inline def module(
    name:        String,
    location:    Location,
    interface:   Seq[(FirrtlBundleField, Location)]
  )(
    using arena: Arena,
    context:     Context
  ): Module

  extension (m: Module)
    inline def block(
      using Arena
    ): Block
    inline def appendToCircuit(
    )(
      using arena: Arena,
      circuit:     Circuit
    ): Unit
    inline def addOperation(
      operation: Operation
    )(
      using Arena
    ): Unit
    inline def getIO(
      idx: Long
    )(
      using Arena
    ): Value
end ModuleApi

class Formal(val operation: Operation)
class Layer(val operation: Operation)
class OptionCase(val operation: Operation)
class Option(val operation: Operation)
