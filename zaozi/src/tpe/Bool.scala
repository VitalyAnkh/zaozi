// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi

import me.jiuyang.zaozi.circtlib.MlirValue
import me.jiuyang.zaozi.internal.NameKind.Droppable
import me.jiuyang.zaozi.internal.{Context, firrtl}

object Bool:
  def apply(): Bool = new Bool
class Bool extends Data:
  def firrtlType = new firrtl.UInt(1.W, false)

trait AsBool[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def asBool(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool]

trait ToConstBool[T]:
  extension (ref: T)
    def B(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Const[Bool]

given ToConstBool[Boolean] with
  extension (ref: Boolean)
    def B(
           using ctx: Context,
           file: sourcecode.File,
           line: sourcecode.Line,
           valName: sourcecode.Name
         ): Const[Bool] =
      val tpe = Bool()
      val mlirTpe = tpe.firrtlType.toMLIR(ctx.handler)
      val const = ctx.handler
        .OpBuilder("firrtl.constant", ctx.currentBlock, SourceLocator(file, line).toMLIR)
        .withNamedAttr(
          "value",
          ctx.handler.firrtlAttrGetIntegerFromString(
            mlirTpe,
            2, // why 2 here
            if(ref) "1" else "0",
            10
          )
        )
        // TODO: circt should support type infer for firrtl.constant
        .withResult(mlirTpe)
        .build()
        .results
        .head
      new Const(const, Bool())

given [R <: Referable[Bool]]: AsBits[Bool, R] with
  extension (ref: R)
    override def asBits(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bits] =
      val mlirValue: MlirValue = ctx.handler
        .OpBuilder(s"firrtl.asUInt", ctx.currentBlock, SourceLocator(file, line).toMLIR)
        .withOperands(Seq(ref.refer))
        .withResultInference(1)
        .build()
        .results
        .head
      new Node[Bits](
        s"${valName.value}_asBits",
        Droppable,
        // todo: from MLIR.
        Bits(1.W),
        mlirValue
      )

given [R <: Referable[Bool]]: Neg[Bool, Bool, R] with
  extension (ref: R)
    override def unary_!(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool] =
      val mlirValue: MlirValue = ctx.handler
        .OpBuilder(s"firrtl.not", ctx.currentBlock, SourceLocator(file, line).toMLIR)
        .withOperands(Seq(ref.refer))
        .withResultInference(1)
        .build()
        .results
        .head
      new Node[Bool](
        s"${valName.value}_not",
        Droppable,
        Bool(),
        mlirValue
      )

given [R <: Referable[Bool]]: Eq[Bool, Bool, R] with
  extension (ref: R)
    def ===(
      that:      R
    )(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool] =
      new Node[Bool](
        s"${valName.value}_eq",
        Droppable,
        // todo: from MLIR.
        Bool(),
        ctx.handler
          .OpBuilder(s"firrtl.eq", ctx.currentBlock, SourceLocator(file, line).toMLIR)
          .withOperands(Seq(ref.refer, that.refer))
          .withResultInference(1)
          .build()
          .results
          .head
      )

given [R <: Referable[Bool]]: Neq[Bool, Bool, R] with
  extension (ref: R)
    def =/=(
      that:      R
    )(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool] =
      new Node[Bool](
        s"${valName.value}_neq",
        Droppable,
        // todo: from MLIR.
        Bool(),
        ctx.handler
          .OpBuilder(s"firrtl.neq", ctx.currentBlock, SourceLocator(file, line).toMLIR)
          .withOperands(Seq(ref.refer, that.refer))
          .withResultInference(1)
          .build()
          .results
          .head
      )

given [R <: Referable[Bool]]: And[Bool, Bool, R] with
  extension (ref: R)
    def &(
      that:      R
    )(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool] =
      new Node[Bool](
        s"${valName.value}_and",
        Droppable,
        // todo: from MLIR.
        ref.tpe,
        ctx.handler
          .OpBuilder(s"firrtl.and", ctx.currentBlock, SourceLocator(file, line).toMLIR)
          .withOperands(Seq(ref.refer, that.refer))
          .withResultInference(1)
          .build()
          .results
          .head
      )

given [R <: Referable[Bool]]: Or[Bool, Bool, R] with
  extension (ref: R)
    def |(
      that:      R
    )(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool] =
      new Node[Bool](
        s"${valName.value}_or",
        Droppable,
        // todo: from MLIR.
        ref.tpe,
        ctx.handler
          .OpBuilder(s"firrtl.or", ctx.currentBlock, SourceLocator(file, line).toMLIR)
          .withOperands(Seq(ref.refer, that.refer))
          .withResultInference(1)
          .build()
          .results
          .head
      )

given [R <: Referable[Bool]]: Xor[Bool, Bool, R] with
  extension (ref: R)
    def ^(
      that:      R
    )(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Bool] =
      new Node[Bool](
        s"${valName.value}_xor",
        Droppable,
        // todo: from MLIR.
        ref.tpe,
        ctx.handler
          .OpBuilder(s"firrtl.xor", ctx.currentBlock, SourceLocator(file, line).toMLIR)
          .withOperands(Seq(ref.refer, that.refer))
          .withResultInference(1)
          .build()
          .results
          .head
      )

given [CondR <: Referable[Bool]]: Mux[Bool, CondR] with
  extension (ref: CondR)
    def ?[Ret <: Data](
      con:       Referable[Ret],
      alt:       Referable[Ret]
    )(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[Ret] =
      // TODO: checking con and alt type equivalence
      val retType = alt.tpe
      new Node[Ret](
        s"${valName.value}_mux",
        Droppable,
        // todo: from MLIR.
        retType,
        ctx.handler
          .OpBuilder(s"firrtl.mux", ctx.currentBlock, SourceLocator(file, line).toMLIR)
          .withOperands(Seq(ref.refer, con.refer, alt.refer))
          .withResultInference(1)
          .build()
          .results
          .head
      )
