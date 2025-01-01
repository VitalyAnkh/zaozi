// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi.default

import me.jiuyang.zaozi.*
import org.llvm.mlir.scalalib.Block

given [R <: Referable[Bits]]: BitsApi[R] with
  def Bits(width: Width): Bits = ???
  extension (ref: R)
    def asSInt(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def asUInt(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def unary_~(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def andR(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def orR(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def xorR(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def ===(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def =/=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def &(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def |(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def ^(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def ##(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def <<(
      that: Int | Referable[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def >>(
      that: Int | Referable[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def head(
      that: Int
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def tail(
      that: Int
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def pad(
      that: Int
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def extractRange(
      hi: Int,
      lo: Int
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def extractElement(
      idx: Int | Ref[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
end given

given [R <: Referable[UInt]]: UIntApi[R] with
  def UInt(width: Width): UInt = ???
  extension (ref: R)
    def asBits(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def +(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def -(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def *(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def /(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def %(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def <(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def <=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def >(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def >=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def ===(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def =/=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def <<(
      that: Int | Referable[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
    def >>(
      that: Int | Referable[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt] = ???
end given

given [R <: Referable[SInt]]: SIntApi[R] with
  def SInt(width: Width): SInt = ???
  extension (ref: R)
    def asBits(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits] = ???
    def +(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def -(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def *(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def /(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def %(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def <(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def <=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def >(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def >=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def ===(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def =/=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool] = ???
    def <<(
      that: Int | Referable[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
    def >>(
      that: Int | Referable[UInt]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt] = ???
end given

given [R <: Referable[Clock]]: ClockApi[R] with
  def Clock(): Clock = ???
end given

given [R <: Referable[Reset]]: ResetApi[R] with
  def Reset(): Reset = ???
  def AsyncReset(): Reset = ???
end given
