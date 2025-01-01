// SPDX-License-Identifier: Apache-2.0
package me.jiuyang.zaozi

import org.llvm.circt.scalalib.firrtl.capi.{given_FirrtlBundleFieldApi, given_TypeApi}
import org.llvm.mlir.scalalib.{Context, Location, LocationApi, Operation, Type, Value, given_LocationApi}

import java.lang.foreign.Arena
import scala.language.dynamics

// Type System, matching MlirType
opaque type Width = Int
trait Data:
  def toMlirType(
    using Arena,
    Context
  ): Type

trait Clock extends Data:
  val const: Boolean
  val ref:   Boolean
  val refRw: Boolean
  def toMlirType(
    using Arena,
    Context
  ): Type =
    val mlirType = summon[org.llvm.circt.scalalib.firrtl.capi.TypeApi].getClock
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

trait Reset extends Data:
  val isAsync: Boolean
  val const:   Boolean
  val ref:     Boolean
  val refRw:   Boolean
  def toMlirType(
    using Arena,
    Context
  ): Type =
    val mlirType =
      if (isAsync) summon[org.llvm.circt.scalalib.firrtl.capi.TypeApi].getAsyncReset
      else 1.getUInt
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

trait UInt extends Data:
  val width: Int
  val const: Boolean
  val ref:   Boolean
  val refRw: Boolean
  def toMlirType(
    using Arena,
    Context
  ): Type =
    val mlirType = width.getUInt
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

trait SInt extends Data:
  val width: Int
  val const: Boolean
  val ref: Boolean
  val refRw: Boolean

  def toMlirType(
                  using Arena,
                  Context
                ): Type =
    val mlirType = width.getSInt
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

trait Bool extends Data:
  val const: Boolean
  val ref: Boolean
  val refRw: Boolean
  def toMlirType(
                  using Arena,
                  Context
                ): Type =
    val mlirType = 1.getUInt
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

trait Bits extends Data:
  val width: Int
  val const: Boolean
  val ref: Boolean
  val refRw: Boolean

  def toMlirType(
                  using Arena,
                  Context
                ): Type =
    val mlirType = width.getUInt
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

trait Analog extends Data:
  val width: Int
  val const: Boolean
  val ref:   Boolean
  val refRw: Boolean
  def toMlirType(
    using Arena,
    Context
  ): Type =
    val mlirType = width.getAnolog
    if (const) mlirType.getConstType
    if (ref) mlirType.toRef(refRw)
    mlirType

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
  val elements: Seq[BundleField[?]]
  val const:    Boolean
  def toMlirType(
    using Arena,
    Context
  ): Type =
    val mlirType = elements
      .map(f =>
        summon[org.llvm.circt.scalalib.firrtl.capi.FirrtlBundleFieldApi]
          .createFirrtlBundleField(f.name, f.isFlip, f.tpe.toMlirType)
      )
      .getBundle
    if (const) mlirType.getConstType
    mlirType

trait BundleField[T <: Data]:
  val name:   String
  val isFlip: Boolean
  val tpe:    T

trait Vec[E <: Data] extends Data:
  val elementType: E
  val count:       Int
  val const:       Boolean
  def toMlirType(
    using Arena,
    Context
  ): Type =
    val mlirType = elementType.toMlirType.getVector(count)
    if (const) mlirType.getConstType
    mlirType

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
