//// SPDX-License-Identifier: Apache-2.0
//// This file provides the API to translate between zaozi and circt
//package me.jiuyang.zaozi.firrtl.internal
//
//import me.jiuyang.zaozi.firrtl.{Analog, AsyncReset, Bundle, BundleField, Clock, Reset, SInt, UInt, Vec, ZaoziType}
//import org.llvm.circt.scalalib.firrtl.capi.{FirrtlBundleField as CirctFirrtlBundleField, given}
//import org.llvm.mlir.scalalib.{Context as MlirContext, Type as MlirType}
//
//import java.lang.foreign.Arena
//
//inline def toCirct[T <: ZaoziType](
//  tpe: T
//)(
//  using Arena,
//  MlirContext
//): MlirType =
//  val typeApi = summon[org.llvm.circt.scalalib.firrtl.capi.TypeApi]
//  tpe match
//    case tpe: Clock      =>
//      val circtType = typeApi.getClock
//      if(tpe.const)
//        circtType.getConstType
//      circtType
//    case tpe: Reset      =>
//      typeApi.getReset
//    case tpe: AsyncReset =>
//      typeApi.getAsyncReset
//    case tpe: UInt       =>
//      tpe.width.getUInt
//    case tpe: SInt       =>
//      tpe.width.getSInt
//    case tpe: Analog     =>
//      tpe.width.getAnolog
//    case tpe: Bundle     =>
//      tpe.fields.map(toCirct).getBundle
//    case tpe: Vec[_]     =>
//      toCirct(tpe.element).getVector(tpe.count)
//
//inline def toCirct(
//  bundleField: BundleField[?]
//)(
//  using Arena,
//  MlirContext
//): CirctFirrtlBundleField =
//  val bundleFieldApi = summon[org.llvm.circt.scalalib.firrtl.capi.FirrtlBundleFieldApi].createFirrtlBundleField(bundleField.name, bundleField.isFlip, bundleField.tpe.toCirct)
//
//extension (tpe:         MlirType)
//  inline def toZaoziType(
//    using Arena
//  ): ZaoziType =
//    if (tpe.isClock) tpe.asZaoziClock
//    else if (tpe.isReset) tpe.asZaoziReset
//    else if (tpe.isAsyncReset) tpe.asZaoziAsyncReset
//    else if (tpe.isUInt) tpe.asUInt
//    else if (tpe.isSInt) tpe.asSInt
//    else if (tpe.isAnalog) tpe.asAnalog
//    else if (tpe.isBundle) tpe.asBundle
//    else if (tpe.isVector) tpe.asVec[ZaoziType]
//    else throw new Exception("Unmatched ZaoziType")
//
//  inline def asZaoziClock:      Clock      =
//    require(tpe.isClock)
//    Clock(tpe.isConst, tpe.isRef, false)
//  inline def asZaoziReset:      Reset      =
//    require(tpe.isReset)
//    Reset(tpe.isConst, tpe.isRef, false)
//  inline def asZaoziAsyncReset: AsyncReset =
//    require(tpe.isAsyncReset)
//    AsyncReset(tpe.isConst, tpe.isRef, false)
//  inline def asUInt:            UInt       =
//    require(tpe.isUInt)
//    UInt(tpe.width(true).toInt, tpe.isConst, tpe.isRef, false)
//  inline def asSInt:            SInt       =
//    require(tpe.isSInt)
//    SInt(tpe.width(true).toInt, tpe.isConst, tpe.isRef, false)
//  inline def asAnalog:          Analog     =
//    require(tpe.isAnalog)
//    Analog(tpe.width(true).toInt, tpe.isConst, tpe.isRef, false)
//  inline def asBundle(
//    using Arena
//  ): Bundle =
//    require(tpe.isBundle)
//    Bundle(Seq.tabulate(tpe.getBundleNumFields.toInt)(i => tpe.getBundleFieldByIndex(i).toZaozi), tpe.isConst)
//  inline def asVec[T <: ZaoziType](
//    using Arena
//  ): Vec[T] =
//    require(tpe.isVector)
//    new Vec[T](tpe.getElementType.toZaoziType, tpe.getBundleNumFields.toInt, tpe.isConst)
//
//// BundleField Conversion
//extension (bundleField: CirctFirrtlBundleField)
//  inline def toZaozi(
//    using Arena
//  ): BundleField[ZaoziType] =
//    BundleField(bundleField.getName(), bundleField.getIsFlip(), bundleField.getType().toZaoziType)
