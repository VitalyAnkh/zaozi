// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi.default

import me.jiuyang.zaozi.*
import org.llvm.circt.scalalib.firrtl.capi.{given_FirrtlBundleFieldApi, given_TypeApi}
import org.llvm.mlir.scalalib.{Block, Context, Type, given_TypeApi, Module as CirctModule}

import java.lang.foreign.Arena

// When Import the default, all method in ConstructorApi should be exported
val constructorApi = summon[ConstructorApi]
export constructorApi.*

given TypeImpl with
  extension (ref: Reset)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType =
        if (ref._isAsync)
          summon[org.llvm.circt.scalalib.firrtl.capi.TypeApi].getAsyncReset
        else
          1.getUInt
      mlirType
  extension (ref: Clock)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = summon[org.llvm.circt.scalalib.firrtl.capi.TypeApi].getClock
      mlirType
  extension (ref: UInt)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = ref._width.getUInt
      mlirType
  extension (ref: SInt)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = ref._width.getSInt
      mlirType
  extension (ref: Bits)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = ref._width.getUInt
      mlirType
  extension (ref: Analog)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = ref._width.getAnolog
      mlirType
  extension (ref: Bool)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = 1.getUInt
      mlirType
  extension (ref: Bundle)
    def elements: Seq[BundleField[?]] =
      require(!ref.instantiating)
      ref._elements.values.toSeq
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      ref.instantiating = false
      val mlirType = elements
        .map(f =>
          summon[org.llvm.circt.scalalib.firrtl.capi.FirrtlBundleFieldApi]
            .createFirrtlBundleField(f._name, f._isFlip, f._tpe.toMlirType)
        )
        .getBundle
      mlirType
    def FlippedImpl[T <: Data](
      name: Option[String],
      tpe:  T
    )(
      using sourcecode.Name
    ): BundleField[T] =
      require(ref.instantiating)
      val bf = new BundleField[T]:
        val _name:   String  = name.getOrElse(valName)
        val _isFlip: Boolean = true
        val _tpe:    T       = tpe
      ref._elements += (valName -> bf)
      bf

    def AlignedImpl[T <: Data](
      name: Option[String],
      tpe:  T
    )(
      using sourcecode.Name
    ): BundleField[T] =
      require(ref.instantiating)
      val bf = new BundleField[T]:
        val _name:   String  = name.getOrElse(valName)
        val _isFlip: Boolean = false
        val _tpe:    T       = tpe
      ref._elements += (valName -> bf)
      bf

  extension (ref: Vec[?])
    def elementType: Data = ref._elementType
    def count:       Int  = ref._count
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type =
      val mlirType = elementType.toMlirType.getVector(count)
      mlirType
end given

given ConstructorApi with
  def Clock():            Clock = ???
  def Reset():            Reset = ???
  def AsyncReset():       Reset = ???
  def UInt(width: Width): UInt  = new UInt:
    private[zaozi] val _width: Int = width._width

  def Bits(width: Width): Bits = new Bits:
    private[zaozi] val _width: Int = width._width

  def SInt(width: Width): SInt = new SInt:
    private[zaozi] val _width: Int = width._width

  def Bool(): Bool = new Object with Bool

  def Module[P <: Parameter, I <: Interface[P]](
    name:      String,
    parameter: P,
    interface: I
  )(body:      (Arena, Block) ?=> (P, Wire[I]) => Unit
  )(
    using Arena,
    Context
  ): CirctModule = ???

  def Wire[T <: Data](
    refType: T
  )(
    using Arena,
    Context,
    Block,
    sourcecode.File,
    sourcecode.Line,
    sourcecode.Name
  ): Wire[T] = ???

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
  ): Reg[T] = ???

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
  ): Reg[T] = ???
  extension (bigInt: BigInt)
    def U(
      using Arena,
      Block
    ): Const[UInt] = ???
    def B(
      using Arena,
      Block
    ): Const[Bits] = ???
    def S(
      using Arena,
      Block
    ): Const[SInt] = ???
  extension (bool:   Boolean)
    def B(
      using Arena,
      Block
    ): Const[Bool] = ???
end given

given [R <: Referable[Bits]]: BitsApi[R] with
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
  def Clock(): Clock = new Clock:
    def toMlirType(
      using Arena,
      Context
    ): Type =
      val mlirType = summon[org.llvm.circt.scalalib.firrtl.capi.TypeApi].getClock
      mlirType
end given

given [R <: Referable[Reset]]: ResetApi[R] with
  def Reset(): Reset = new Reset:
    val _isAsync: Boolean = false

  def AsyncReset(): Reset = new Reset:
    val _isAsync: Boolean = true
end given

given WidthApi with
  extension (int: Int)
    def W: Width = new Width:
      val _width: Int = int
end given
