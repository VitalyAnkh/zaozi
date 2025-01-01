//// SPDX-License-Identifier: Apache-2.0
package me.jiuyang.zaozi

import org.llvm.circt.scalalib.firrtl.capi.{given_FirrtlBundleFieldApi, given_TypeApi}
import org.llvm.mlir.scalalib.Type

import java.lang.foreign.Arena

trait MlirTypeReflection:
  extension (tpe: Type)
    def toData(
      using Arena
    ):            Data
    def asClock:  Clock
    def asReset:  Reset
    def asUInt:   UInt
    def asSInt:   SInt
    def asAnalog: Analog
    def asBundle(
      using Arena
    ):            Bundle
    def asVec[T <: Data](
      using Arena
    ):            Vec[T]
end MlirTypeReflection

given MlirTypeReflection with
  extension (tpe: Type)
    def toData(
      using Arena
    ): Data =
      if (tpe.isClock) tpe.asClock
      else if (tpe.isReset) tpe.asReset
      else if (tpe.isUInt) tpe.asUInt
      else if (tpe.isSInt) tpe.asSInt
      else if (tpe.isAnalog) tpe.asAnalog
      else if (tpe.isBundle) tpe.asBundle
      else if (tpe.isVector) tpe.asVec[Data]
      else throw new Exception("Unmatched ZaoziType")
    def asClock:  Clock  =
      require(tpe.isClock)
      new Clock:
        val const: Boolean = tpe.isConst
        val ref:   Boolean = tpe.isRef
        // TODO: need C-API
        val refRw: Boolean = false
    def asReset:  Reset  =
      require(tpe.isReset)
      ???
    def asUInt:   UInt   =
      require(tpe.isUInt)
      new UInt:
        val width: Int     = tpe.getBitWidth(true).toInt
        val const: Boolean = tpe.isConst
        val ref:   Boolean = tpe.isRef
        // TODO: need C-API
        val refRw: Boolean = false
    def asSInt:   SInt   =
      require(tpe.isSInt)
      new SInt:
        val width: Int     = tpe.getBitWidth(true).toInt
        val const: Boolean = tpe.isConst
        val ref:   Boolean = tpe.isRef
        // TODO: need C-API
        val refRw: Boolean = false
    def asAnalog: Analog =
      require(tpe.isAnalog)
      new Analog:
        val width: Int     = tpe.getBitWidth(true).toInt
        val const: Boolean = tpe.isConst
        val ref:   Boolean = tpe.isRef
        // TODO: need C-API
        val refRw: Boolean = false
    def asBundle(
      using Arena
    ): Bundle =
      require(tpe.isBundle)
      new Bundle:
        val elements: Seq[BundleField[?]] = Seq.tabulate(tpe.getBundleNumFields.toInt)(i =>
          val bf = tpe.getBundleFieldByIndex(i)
          new Object with BundleField:
            val name   = bf.getName()
            val isFlip = bf.getIsFlip()
            val tpe    = bf.getType().toData
        )
        val const:    Boolean             = tpe.isConst
    def asVec[T <: Data](
      using Arena
    ): Vec[T] =
      require(tpe.isVector)
      new Object with Vec[T]:
        val elementType: T       =
          (
            tpe match
              case tpe if tpe.isClock  => tpe.asClock
              case tpe if tpe.isReset  => tpe.asReset
              case tpe if tpe.isUInt   => tpe.asUInt
              case tpe if tpe.isSInt   => tpe.asSInt
              case tpe if tpe.isAnalog => tpe.asAnalog
              case tpe if tpe.isBundle => tpe.asBundle
              case tpe if tpe.isVector => tpe.asVec[Data]
          ).asInstanceOf[T]
        val count:       Int     = tpe.getElementNum.toInt
        val const:       Boolean = tpe.isConst
end given
