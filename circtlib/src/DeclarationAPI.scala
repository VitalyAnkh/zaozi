// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.circt.scalalib.{FirrtlBundleField, FirrtlNameKind}
import org.llvm.mlir.scalalib.{Block, Context, Location, Operation, Type, Value}

import java.lang.foreign.Arena

class InstanceChoice(val operation: Operation)
class Instance(val operation: Operation)
trait InstanceApi:
  inline def instance(
    moduleName:   String,
    instanceName: String,
    nameKind:     FirrtlNameKind,
    location:     Location,
    interface:    Seq[FirrtlBundleField]
  )(
    using arena:  Arena,
    context:      Context,
    module:       Module
  ): Instance
end InstanceApi
class Mem(val operation: Operation)
class Node(val operation: Operation)
class Object(val operation: Operation)
class Reg(val operation: Operation)
trait RegApi:
  inline def reg(
    name:        String,
    location:    Location,
    nameKind:    FirrtlNameKind,
    tpe:         Type,
    clock:       Value
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Reg
end RegApi
class RegReset(val operation: Operation)
trait RegResetApi:
  inline def regReset(
    name:        String,
    location:    Location,
    nameKind:    FirrtlNameKind,
    tpe:         Type,
    clock:       Value,
    reset:       Value
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): RegReset
end RegResetApi
class Wire(val operation: Operation)
trait WireApi:
  def wire(
    name:        String,
    location:    Location,
    nameKind:    FirrtlNameKind,
    tpe:         Type
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Wire
  extension (ref: Wire)
    def result(
      using Arena
    ): Value
end WireApi
