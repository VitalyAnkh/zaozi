// SPDX-License-Identifier: Apache-2.0
package me.jiuyang.zaozi

import org.llvm.mlir.scalalib.{Context, Location, LocationApi, Operation, Type, Value, given_LocationApi}

import java.lang.foreign.Arena
import scala.language.dynamics

// Type System, matching MlirType

// TODO: it should be opaque?
trait Width:
  val _width: Int

trait Data:
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type

trait Clock extends Data:
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait Reset extends Data:
  private[zaozi] val _isAsync: Boolean
  final def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait UInt extends Data:
  private[zaozi] val _width: Int
  final def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait SInt extends Data:
  private[zaozi] val _width: Int
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait Bool extends Data:
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait Bits extends Data:
  private[zaozi] val _width: Int
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait Analog extends Data:
  private[zaozi] val _width: Int
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

/** Due to Scala not allowing deferred macro call(calling user defined macro from outer macro). Any implementation to
  * [[DynamicSubfield]] should make sure the dynamic access is to a val that has a return type of [[BundleField]]. For
  * now jiuyang cannot come up with better solution to let user define their own macro, however they can still implement
  * their own [[Bundle]].
  */
trait DynamicSubfield:
  def getRefViaFieldValName[E <: Data](
    refer:        Value,
    fieldValName: String,
    context:      Context,
    file:         sourcecode.File,
    line:         sourcecode.Line,
    valName:      sourcecode.Name
  ): Ref[E]

trait Bundle extends Data:
  private[zaozi] var instantiating = true
  // valName -> BundleField
  private[zaozi] val _elements: collection.mutable.Map[String, BundleField[?]] =
    collection.mutable.Map[String, BundleField[?]]()
  def Flipped[T <: Data](
    tpe: T
  )(
    using TypeImpl,
    sourcecode.Name
  ): BundleField[T] = this.FlippedImpl(None, tpe)
  def Aligned[T <: Data](
    tpe: T
  )(
    using TypeImpl,
    sourcecode.Name
  ): BundleField[T] = this.AlignedImpl(None, tpe)
  def Flipped[T <: Data](
    name: String,
    tpe:  T
  )(
    using TypeImpl,
    sourcecode.Name
  ): BundleField[T] = this.FlippedImpl(Some(name), tpe)
  def Aligned[T <: Data](
    name: String,
    tpe:  T
  )(
    using TypeImpl,
    sourcecode.Name
  ): BundleField[T] = this.AlignedImpl(Some(name), tpe)

  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl

trait BundleField[T <: Data]:
  private[zaozi] val _name:   String
  private[zaozi] val _isFlip: Boolean
  private[zaozi] val _tpe:    T

trait Vec[E <: Data] extends Data:
  private[zaozi] val _elementType: E
  private[zaozi] val _count:       Int
  def toMlirType(
    using Arena,
    Context,
    TypeImpl
  ): Type = this.toMlirTypeImpl
// Type System: Referable to contain an operation which has a single value.
trait HasOperation:
  def operation: Operation

trait Referable[T <: Data] extends Dynamic:
  def tpe:   T
  def refer: Value

trait Instance[T <: Data] extends Referable[T] with HasOperation

trait Ref[T <: Data] extends Referable[T] with HasOperation

trait Node[T <: Data] extends Referable[T] with HasOperation

trait Const[T <: Data] extends Referable[T] with HasOperation

trait Reg[T <: Data] extends Referable[T] with HasOperation

trait Wire[T <: Data] extends Referable[T] with HasOperation

inline def locate(
  using Arena,
  Context,
  sourcecode.File,
  sourcecode.Line
): Location =
  summon[LocationApi].locationFileLineColGet(
    summon[sourcecode.File].value,
    summon[sourcecode.Line].value,
    0
  )

inline def valName(
  using sourcecode.Name
): String = summon[sourcecode.Name].value

trait TypeImpl:
  extension (ref: Reset)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: Clock)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: UInt)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: SInt)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: Bits)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: Analog)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: Bool)
    def toMlirTypeImpl(
      using Arena,
      Context
    ): Type
  extension (ref: Bundle)
    def elements: Seq[BundleField[?]]
    def toMlirTypeImpl(
      using Arena,
      Context
    ):            Type
    def FlippedImpl[T <: Data](
      name: Option[String],
      tpe:  T
    )(
      using sourcecode.Name
    ):            BundleField[T]
    def AlignedImpl[T <: Data](
      name: Option[String],
      tpe:  T
    )(
      using sourcecode.Name
    ):            BundleField[T]
  extension (ref: Vec[?])
    def elementType: Data
    def count:       Int
    def toMlirTypeImpl(
      using Arena,
      Context
    ):               Type
