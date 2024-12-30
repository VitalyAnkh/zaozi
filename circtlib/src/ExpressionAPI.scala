// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.mlir.scalalib.Operation

trait LHSRHSRESULT

class AddPrim(val operation: Operation)
trait AddPrimApi
end AddPrimApi

class AggregateConstant(val operation: Operation)
trait AggregateConstantApi
end AggregateConstantApi

class AndPrim(val operation: Operation)
trait AndPrimApi
end AndPrimApi

class AndRPrim(val operation: Operation)
trait AndRPrimApi
end AndRPrimApi

class AsAsyncResetPrim(val operation: Operation)
trait AsAsyncResetPrimApi
end AsAsyncResetPrimApi

class AsClockPrim(val operation: Operation)
trait AsClockPrimApi
end AsClockPrimApi

class AsSIntPrim(val operation: Operation)
trait AsSIntPrimApi
end AsSIntPrimApi

class AsUIntPrim(val operation: Operation)
trait AsUIntPrimApi
end AsUIntPrimApi

class BitCast(val operation: Operation)
trait BitCastApi
end BitCastApi

class BitsPrim(val operation: Operation)
trait BitsPrimApi
end BitsPrimApi

class BoolConstant(val operation: Operation)
trait BoolConstantApi
end BoolConstantApi

class BundleCreate(val operation: Operation)
trait BundleCreateApi
end BundleCreateApi

class CatPrim(val operation: Operation)
trait CatPrimApi
end CatPrimApi

class ConstCast(val operation: Operation)
trait ConstCastApi
end ConstCastApi

class Constant(val operation: Operation)
trait ConstantApi
end ConstantApi

class CvtPrim(val operation: Operation)
trait CvtPrimApi
end CvtPrimApi

class DShlPrim(val operation: Operation)
trait DShlPrimApi
end DShlPrimApi

class DShlwPrim(val operation: Operation)
trait DShlwPrimApi
end DShlwPrimApi

class DShrPrim(val operation: Operation)
trait DShrPrimApi
end DShrPrimApi

class DivPrim(val operation: Operation)
trait DivPrimApi
end DivPrimApi

class DoubleConstant(val operation: Operation)
trait DoubleConstantApi
end DoubleConstantApi

class EQPrim(val operation: Operation)
trait EQPrimApi
end EQPrimApi

class ElementwiseAndPrim(val operation: Operation)
trait ElementwiseAndPrimApi
end ElementwiseAndPrimApi

class ElementwiseOrPrim(val operation: Operation)
trait ElementwiseOrPrimApi
end ElementwiseOrPrimApi

class ElementwiseXorPrim(val operation: Operation)
trait ElementwiseXorPrimApi
end ElementwiseXorPrimApi

class FEnumCreate(val operation: Operation)
trait FEnumCreateApi
end FEnumCreateApi

class FIntegerConstant(val operation: Operation)
trait FIntegerConstantApi
end FIntegerConstantApi

class GEQPrim(val operation: Operation)
trait GEQPrimApi
end GEQPrimApi

class GTPrim(val operation: Operation)
trait GTPrimApi
end GTPrimApi

class HWStructCast(val operation: Operation)
trait HWStructCastApi
end HWStructCastApi

class HeadPrim(val operation: Operation)
trait HeadPrimApi
end HeadPrimApi

class IntegerAdd(val operation: Operation)
trait IntegerAddApi
end IntegerAddApi

class IntegerMul(val operation: Operation)
trait IntegerMulApi
end IntegerMulApi

class IntegerShl(val operation: Operation)
trait IntegerShlApi
end IntegerShlApi

class IntegerShr(val operation: Operation)
trait IntegerShrApi
end IntegerShrApi

class InvalidValue(val operation: Operation)
trait InvalidValueApi
end InvalidValueApi

class IsTag(val operation: Operation)
trait IsTagApi
end IsTagApi

class LEQPrim(val operation: Operation)
trait LEQPrimApi
end LEQPrimApi

class LTLAndIntrinsic(val operation: Operation)
trait LTLAndIntrinsicApi
end LTLAndIntrinsicApi

class LTLClockIntrinsic(val operation: Operation)
trait LTLClockIntrinsicApi
end LTLClockIntrinsicApi

class LTLConcatIntrinsic(val operation: Operation)
trait LTLConcatIntrinsicApi
end LTLConcatIntrinsicApi

class LTLDelayIntrinsic(val operation: Operation)
trait LTLDelayIntrinsicApi
end LTLDelayIntrinsicApi

class LTLEventuallyIntrinsic(val operation: Operation)
trait LTLEventuallyIntrinsicApi
end LTLEventuallyIntrinsicApi

class LTLGoToRepeatIntrinsic(val operation: Operation)
trait LTLGoToRepeatIntrinsicApi
end LTLGoToRepeatIntrinsicApi

class LTLImplicationIntrinsic(val operation: Operation)
trait LTLImplicationIntrinsicApi
end LTLImplicationIntrinsicApi

class LTLIntersectIntrinsic(val operation: Operation)
trait LTLIntersectIntrinsicApi
end LTLIntersectIntrinsicApi

class LTLNonConsecutiveRepeatIntrinsic(val operation: Operation)
trait LTLNonConsecutiveRepeatIntrinsicApi
end LTLNonConsecutiveRepeatIntrinsicApi

class LTLNotIntrinsic(val operation: Operation)
trait LTLNotIntrinsicApi
end LTLNotIntrinsicApi

class LTLOrIntrinsic(val operation: Operation)
trait LTLOrIntrinsicApi
end LTLOrIntrinsicApi

class LTLRepeatIntrinsic(val operation: Operation)
trait LTLRepeatIntrinsicApi
end LTLRepeatIntrinsicApi

class LTLUntilIntrinsic(val operation: Operation)
trait LTLUntilIntrinsicApi
end LTLUntilIntrinsicApi

class LTPrim(val operation: Operation)
trait LTPrimApi
end LTPrimApi

class ListConcat(val operation: Operation)
trait ListConcatApi
end ListConcatApi

class ListCreate(val operation: Operation)
trait ListCreateApi
end ListCreateApi

class MulPrim(val operation: Operation)
trait MulPrimApi
end MulPrimApi

class MultibitMux(val operation: Operation)
trait MultibitMuxApi
end MultibitMuxApi

class Mux2CellIntrinsic(val operation: Operation)
trait Mux2CellIntrinsicApi
end Mux2CellIntrinsicApi

class Mux4CellIntrinsic(val operation: Operation)
trait Mux4CellIntrinsicApi
end Mux4CellIntrinsicApi

class MuxPrim(val operation: Operation)
trait MuxPrimApi
end MuxPrimApi

class NEQPrim(val operation: Operation)
trait NEQPrimApi
end NEQPrimApi

class NegPrim(val operation: Operation)
trait NegPrimApi
end NegPrimApi

class NotPrim(val operation: Operation)
trait NotPrimApi
end NotPrimApi

class ObjectAnyRefCast(val operation: Operation)
trait ObjectAnyRefCastApi
end ObjectAnyRefCastApi

class ObjectSubfield(val operation: Operation)
trait ObjectSubfieldApi
end ObjectSubfieldApi

class OpenSubfield(val operation: Operation)
trait OpenSubfieldApi
end OpenSubfieldApi

class OpenSubindex(val operation: Operation)
trait OpenSubindexApi
end OpenSubindexApi

class OrPrim(val operation: Operation)
trait OrPrimApi
end OrPrimApi

class OrRPrim(val operation: Operation)
trait OrRPrimApi
end OrRPrimApi

class PadPrim(val operation: Operation)
trait PadPrimApi
end PadPrimApi

class Path(val operation: Operation)
trait PathApi
end PathApi

class RWProbe(val operation: Operation)
trait RWProbeApi
end RWProbeApi

class RefCast(val operation: Operation)
trait RefCastApi
end RefCastApi

class RefResolve(val operation: Operation)
trait RefResolveApi
end RefResolveApi

class RefSend(val operation: Operation)
trait RefSendApi
end RefSendApi

class RefSub(val operation: Operation)
trait RefSubApi
end RefSubApi

class RemPrim(val operation: Operation)
trait RemPrimApi
end RemPrimApi

class ShlPrim(val operation: Operation)
trait ShlPrimApi
end ShlPrimApi

class ShrPrim(val operation: Operation)
trait ShrPrimApi
end ShrPrimApi

class SizeOfIntrinsic(val operation: Operation)
trait SizeOfIntrinsicApi
end SizeOfIntrinsicApi

class SpecialConstant(val operation: Operation)
trait SpecialConstantApi
end SpecialConstantApi

class StringConstant(val operation: Operation)
trait StringConstantApi
end StringConstantApi

class SubPrim(val operation: Operation)
trait SubPrimApi
end SubPrimApi

class Subaccess(val operation: Operation)
trait SubaccessApi
end SubaccessApi

class Subfield(val operation: Operation)
trait SubfieldApi
end SubfieldApi

class Subindex(val operation: Operation)
trait SubindexApi
end SubindexApi

class Subtag(val operation: Operation)
trait SubtagApi
end SubtagApi

class TagExtract(val operation: Operation)
trait TagExtractApi
end TagExtractApi

class TailPrim(val operation: Operation)
trait TailPrimApi
end TailPrimApi

class UninferredResetCast(val operation: Operation)
trait UninferredResetCastApi
end UninferredResetCastApi

class UnresolvedPath(val operation: Operation)
trait UnresolvedPathApi
end UnresolvedPathApi

class VectorCreate(val operation: Operation)
trait VectorCreateApi
end VectorCreateApi

class VerbatimExpr(val operation: Operation)
trait VerbatimExprApi
end VerbatimExprApi

class VerbatimWire(val operation: Operation)
trait VerbatimWireApi
end VerbatimWireApi

class XMRDeref(val operation: Operation)
trait XMRDerefApi
end XMRDerefApi

class XMRRef(val operation: Operation)
trait XMRRefApi
end XMRRefApi

class XorPrim(val operation: Operation)
trait XorPrimApi
end XorPrimApi

class XorRPrim(val operation: Operation)
trait XorRPrimApi
end XorRPrimApi
