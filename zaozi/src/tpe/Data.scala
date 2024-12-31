// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi.firrtl

import me.jiuyang.zaozi.MonoConnect
import org.llvm.mlir.scalalib.Block

trait Data:
  def firrtlType: FirrtlType

trait SourceInfo

given [D <: Data, SRC <: Referable[D], SINK <: Referable[D]]: MonoConnect[D, SRC, SINK] with
  extension (ref: SINK)
    def :=(
      that:      SRC
    )(
      using block: Block,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Unit =
      
      ctx.handler
        .OpBuilder("firrtl.connect", ctx.currentBlock, SourceLocator(file, line).toMLIR)
        .withOperand( /* dest */ ref.refer)
        .withOperand( /* src */ that.refer)
        .build()
