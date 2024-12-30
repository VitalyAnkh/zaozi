// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.mlir.scalalib.{Block, Context, Location, Operation, Value}

import java.lang.foreign.Arena

class Assert(val operation: Operation)
class Assume(val operation: Operation)
class Attach(val operation: Operation)
class Connect(val operation: Operation)
trait ConnectApi:
  inline def connect(
    src:         Value,
    dst:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Connect
end ConnectApi

class Cover(val operation: Operation)
class Force(val operation: Operation)
class LayerBlock(val operation: Operation)
class Match(val operation: Operation)
class MatchingConnect(val operation: Operation)
class Printf(val operation: Operation)
class Propassign(val operation: Operation)
class RefDefine(val operation: Operation)
class RefForceInitial(val operation: Operation)
class RefForce(val operation: Operation)
class RefReleaseInitial(val operation: Operation)
class RefRelease(val operation: Operation)
class Skip(val operation: Operation)
class Stop(val operation: Operation)
class IntVerifAssert(val operation: Operation)
class IntVerifAssume(val operation: Operation)
class IntVerifCover(val operation: Operation)
class When(val operation: Operation)
