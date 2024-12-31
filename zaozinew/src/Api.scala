// SPDX-License-Identifier: Apache-2.0
package me.jiuyang.zaozi

import org.llvm.circt.scalalib.firrtl.capi.FirrtlNameKind
import org.llvm.circt.scalalib.firrtl.operation.{RegApi, RegResetApi, WireApi, given}
import org.llvm.mlir.scalalib.{Block, Context, Type, given}

import java.lang.foreign.Arena

trait ZaoziTypeApi:
  type Width = Int
  given Conversion[Int, Width] with
    def apply(x:  Int):                      Width = x
    extension (x: Int) override def convert: Width = x
  end given
  extension (ref: Int) def W: Width = ref
  end extension

  def Clock(): Clock = new Object with Clock:
    override val const: Boolean = false
    override val ref:   Boolean = false
    override val refRw: Boolean = false

  end Clock

  def AsyncReset(): AsyncReset = new Object with AsyncReset:
    override val const: Boolean = false
    override val ref:   Boolean = false
    override val refRw: Boolean = false

  end AsyncReset

  def Wire[T <: Data](
    refType: T
  )(
    using Arena,
    Context,
    Block,
    sourcecode.File,
    sourcecode.Line,
    sourcecode.Name
  ) =
    val op = summon[WireApi].op(
      name = valName,
      location = locate,
      nameKind = FirrtlNameKind.Interesting,
      tpe = refType.toMlirType
    )
    op.operation.appendToBlock()
    new Object with Wire[T]:
      def operation = op.operation
      def tpe       = refType
      def refer     = op.operation.getResult(0)
  def Reg[T <: Data](
    refType: T
  )(
    using Arena,
    Context,
    Block,
    Ref[Clock],
    sourcecode.File,
    sourcecode.Line,
    sourcecode.Name
  ) =
    val op = summon[RegApi].op(
      name = valName,
      location = locate,
      nameKind = FirrtlNameKind.Interesting,
      tpe = refType.toMlirType,
      clock = summon[Ref[Clock]].refer
    )
    op.operation.appendToBlock()
    new Object with Wire[T]:
      def operation = op.operation
      def tpe       = refType
      def refer     = op.operation.getResult(0)
  def RegInit[T <: Data](
    refType: T
  )(
    using Arena,
    Context,
    Block,
    Ref[Clock],
    Ref[Reset],
    sourcecode.File,
    sourcecode.Line,
    sourcecode.Name
  ) =
    val op = summon[RegResetApi].op(
      name = valName,
      location = locate,
      nameKind = FirrtlNameKind.Interesting,
      tpe = refType.toMlirType,
      clock = summon[Ref[Clock]].refer,
      reset = summon[Ref[Reset]].refer
    )
    op.operation.appendToBlock()
    new Object with Wire[T]:
      def operation = op.operation

      def tpe = refType

      def refer = op.operation.getResult(0)
