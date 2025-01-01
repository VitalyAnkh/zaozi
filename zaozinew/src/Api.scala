// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi

import org.llvm.mlir.scalalib.{Block, Context}

import java.lang.foreign.Arena

// This is all User APIs
trait ClockConstructor:
  def Clock(): Clock

trait ResetConstructor:
  def Reset():      Reset
  def AsyncReset(): Reset

trait UIntConstructor:
  def UInt(width: Width): UInt

trait BitsConstructor:
  def Bits(width: Width): Bits

trait SIntConstructor:
  def SInt(width: Width): SInt

trait BoolConstructor:
  def Bool(): Bool
  extension (bool: Boolean)
    /** Create a constant Bool from BigInt. */
    def B(
      using Arena,
      Block
    ): Const[Bool]

trait ConstructorApi:
  def Wire[T <: Data](
    refType: T
  )(
    using Arena,
    Context,
    Block,
    sourcecode.File,
    sourcecode.Line,
    sourcecode.Name
  ): Wire[T]
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
  ): Reg[T]
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
  ): Reg[T]
  extension (bigInt: BigInt)
    def U(
      using Arena,
      Block
    ): Const[UInt]
    def B(
      using Arena,
      Block
    ): Const[Bits]
    def S(
      using Arena,
      Block
    ): Const[SInt]
  extension (bool:   Boolean)
    def B(
      using Arena,
      Block
    ): Const[Bool]

trait AsBits[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def asBits(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bits]
trait AsBool[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def asBool(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Bool]
trait AsSInt[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def asSInt(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[SInt]
trait AsUInt[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def asUInt(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[UInt]
trait MonoConnect[D <: Data, SRC <: Referable[D], SINK <: Referable[D]]:
  extension (ref: SINK)
    def :=(
      that: SRC
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line
    ): Unit

trait Cvt[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def zext(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Neg[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def unary_!(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Not[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def unary_~(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait AndR[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def andR(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait OrR[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def orR(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait XorR[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def xorR(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Add[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def +(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Sub[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def -(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Mul[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def *(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Div[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def /(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Rem[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def %(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Lt[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def <(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Leq[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def <=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Gt[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def >(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Geq[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def >=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Eq[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def ===(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Neq[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def =/=(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait And[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def &(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Or[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def |(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Xor[D <: Data, RET <: Data, R <: Referable[D]]:
  extension (ref: R)
    def ^(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[RET]
trait Cat[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def ##(
      that: R
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[D]

trait Shl[D <: Data, THAT, OUT <: Data, R <: Referable[D]]:
  extension (ref: R)
    def <<(
      that: THAT
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[OUT]
trait Shr[D <: Data, THAT, OUT <: Data, R <: Referable[D]]:
  extension (ref: R)
    def >>(
      that: THAT
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[D]
trait Head[D <: Data, THAT, OUT <: Data, R <: Referable[D]]:
  extension (ref: R)
    def head(
      that: THAT
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[OUT]
trait Tail[D <: Data, THAT, OUT <: Data, R <: Referable[D]]:
  extension (ref: R)
    def tail(
      that: THAT
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[OUT]
trait Pad[D <: Data, THAT, OUT <: Data, R <: Referable[D]]:
  extension (ref: R)
    def pad(
      that: THAT
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[OUT]
trait ExtractRange[D <: Data, IDX, E <: Data, R <: Referable[D]]:
  extension (ref: R)
    def extractRange(
      hi: IDX,
      lo: IDX
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[E]
trait ExtractElement[D <: Data, E <: Data, R <: Referable[D], IDX]:
  extension (ref: R)
    def extractElement(
      idx: IDX
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[E]
trait Mux[Cond <: Data, CondR <: Referable[Cond]]:
  extension (ref: CondR)
    def ?[Ret <: Data](
      con: Referable[Ret],
      alt: Referable[Ret]
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Node[Ret]
trait RefElement[D <: Data, E <: Data, R <: Referable[D], IDX]:
  extension (ref: R)
    def ref(
      idx: IDX
    )(
      using Block,
      sourcecode.File,
      sourcecode.Line,
      sourcecode.Name
    ): Ref[E]
trait BitsApi[R <: Referable[Bits]]
    extends AsSInt[Bits, R]
    with AsUInt[Bits, R]
    with Not[Bits, Bits, R]
    with AndR[Bits, Bool, R]
    with OrR[Bits, Bool, R]
    with XorR[Bits, Bool, R]
    with Eq[Bits, Bool, R]
    with Neq[Bits, Bool, R]
    with And[Bits, Bits, R]
    with Or[Bits, Bits, R]
    with Xor[Bits, Bits, R]
    with Cat[Bits, R]
    with Shl[Bits, Int | Referable[UInt], Bits, R]
    with Shr[Bits, Int | Referable[UInt], Bits, R]
    with Head[Bits, Int, Bits, R]
    with Tail[Bits, Int, Bits, R]
    with Pad[Bits, Int, Bits, R]
    with ExtractRange[Bits, Int, Bits, R]
    with ExtractElement[Bits, Bool, R, Int | Ref[UInt]]
    with BitsConstructor

trait BoolApi[R <: Referable[Bool]]
    extends AsBits[Bool, R]
    with Neg[Bool, Bool, R]
    with Eq[Bool, Bool, R]
    with Neq[Bool, Bool, R]
    with And[Bool, Bool, R]
    with Or[Bool, Bool, R]
    with Xor[Bool, Bool, R]
    with Mux[Bool, R]
    with BoolConstructor

trait UIntApi[R <: Referable[UInt]]
    extends AsBits[UInt, R]
    with Add[UInt, UInt, R]
    with Sub[UInt, UInt, R]
    with Mul[UInt, UInt, R]
    with Div[UInt, UInt, R]
    with Rem[UInt, UInt, R]
    with Lt[UInt, Bool, R]
    with Leq[UInt, Bool, R]
    with Gt[UInt, Bool, R]
    with Geq[UInt, Bool, R]
    with Eq[UInt, Bool, R]
    with Neq[UInt, Bool, R]
    with Shl[UInt, Int | Referable[UInt], UInt, R]
    with Shr[UInt, Int | Referable[UInt], UInt, R]
    with UIntConstructor
trait SIntApi[R <: Referable[SInt]]
    extends AsBits[SInt, R]
    with Add[SInt, SInt, R]
    with Sub[SInt, SInt, R]
    with Mul[SInt, SInt, R]
    with Div[SInt, SInt, R]
    with Rem[SInt, SInt, R]
    with Lt[SInt, Bool, R]
    with Leq[SInt, Bool, R]
    with Gt[SInt, Bool, R]
    with Geq[SInt, Bool, R]
    with Neq[SInt, Bool, R]
    with Shl[SInt, Int | Referable[UInt], SInt, R]
    with Shr[SInt, Int | Referable[UInt], SInt, R]
    with SIntConstructor
trait ClockApi[R <: Referable[Clock]] extends ClockConstructor
trait ResetApi[R <: Referable[Reset]] extends ResetConstructor
