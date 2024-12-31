// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.circt.scalalib.{FirrtlBundleField, FirrtlNameKind}
import org.llvm.mlir.scalalib.{Block, Context, Location, Operation, Type, Value}

import java.lang.foreign.Arena

trait HasOperation[T]:
  extension (ref: T)
    def operation: Operation
end HasOperation

class InstanceChoice(val _operation: Operation)
class Instance(val _operation: Operation)
trait InstanceApi extends HasOperation[Instance]:
  inline def op(
    moduleName:   String,
    instanceName: String,
    nameKind:     FirrtlNameKind,
    location:     Location,
    interface:    Seq[FirrtlBundleField]
  )(
    using arena:  Arena,
    context:      Context
  ): Instance
end InstanceApi
class Mem(val _operation: Operation)
class Node(val _operation: Operation)
trait NodeApi extends HasOperation[Node]:
end NodeApi
class Object(val _operation: Operation)
class Reg(val _operation: Operation)
trait RegApi extends HasOperation[Reg]:
end RegApi
class RegReset(val _operation: Operation)
trait RegResetApi extends HasOperation[RegReset]:
end RegResetApi
class Wire(val _operation: Operation)
trait WireApi extends HasOperation[Wire]:
  extension (ref: Wire)
    def result(
      using Arena
    ): Value
end WireApi
