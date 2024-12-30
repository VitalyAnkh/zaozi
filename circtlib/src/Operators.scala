// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.circt.scalalib.{*, given}
import org.llvm.mlir.scalalib
import org.llvm.mlir.scalalib.{Module as MlirModule, *, given}

import java.lang.foreign.Arena

// Structure
class Circuit(val operation: Operation)
trait CircuitApi:
  inline def circuit(
    circuitName: String
  )(
    using arena: Arena,
    context:     Context,
    module:      MlirModule
  ): Circuit
  extension (c: Circuit)
    inline def block(
      using Arena
    ): Block
    inline def appendToModule(
    )(
      using arena: Arena,
      module:      MlirModule
    ): Unit
end CircuitApi
given CircuitApi with
  inline def circuit(
    circuitName: String
  )(
    using arena: Arena,
    context:     Context,
    module:      MlirModule
  ): Circuit = Circuit(
    summon[OperationApi].operationCreate(
      name = "firrtl.circuit",
      location = summon[LocationApi].locationUnknownGet,
      regionBlockTypeLocations = Seq(
        Seq(
          (Seq.empty, Seq.empty)
        )
      ),
      namedAttributes = Seq(
        summon[NamedAttributeApi].namedAttributeGet("name".identifierGet, circuitName.stringAttrGet)
      )
    )
  )
  extension (c: Circuit)
    inline def block(
      using Arena
    ): Block = c.operation.getFirstRegion().getFirstBlock()
    inline def appendToModule(
    )(
      using arena: Arena,
      module:      MlirModule
    ): Unit =
      module.getBody.appendOwnedOperation(c.operation)
end given

class Class(val operation: Operation)
class ExtClass(val operation: Operation)
class ExtModule(val operation: Operation)
class IntModule(val operation: Operation)
class MemModule(val operation: Operation)
class Module(val operation: Operation)
trait ModuleApi:
  inline def module(
    name:        String,
    location:    Location,
    interface:   Seq[(FirrtlBundleField, Location)]
  )(
    using arena: Arena,
    context:     Context,
    circuit:     Circuit
  ): Module
  extension (m: Module)
    inline def block(
      using Arena
    ): Block
    inline def appendToCircuit(
    )(
      using arena: Arena,
      circuit:     Circuit
    ): Unit
    inline def addOperation(
      operation: Operation
    )(
      using Arena
    ): Unit
    inline def getIO(
      idx: Long
    )(
      using Arena
    ): Value
end ModuleApi
given ModuleApi with
  inline def module(
    name:        String,
    location:    Location,
    interface:   Seq[(FirrtlBundleField, Location)]
  )(
    using arena: Arena,
    context:     Context,
    circuit:     Circuit
  ): Module = new Module(
    summon[OperationApi].operationCreate(
      name = "firrtl.module",
      location = location,
      regionBlockTypeLocations = Seq(
        Seq(
          (interface.map(_._1.getType()), interface.map(_._2))
        )
      ),
      namedAttributes =
        val namedAttributeApi = summon[NamedAttributeApi]
        Seq(
          namedAttributeApi.namedAttributeGet(
            "sym_name".identifierGet,
            name.stringAttrGet
          ),
          namedAttributeApi.namedAttributeGet(
            "portDirections".identifierGet,
            interface
              .map:
                case (bf, _) =>
                  if (bf.getIsFlip()) FirrtlDirection.In else FirrtlDirection.Out
              .attrGetPortDirs
          ),
          namedAttributeApi.namedAttributeGet(
            "portLocations".identifierGet,
            interface.map(_._2.getAttribute).arrayAttrGet
          ),
          namedAttributeApi.namedAttributeGet(
            "portAnnotations".identifierGet,
            Seq().arrayAttrGet
          ),
          namedAttributeApi.namedAttributeGet(
            "portSymbols".identifierGet,
            Seq().arrayAttrGet
          ),
          namedAttributeApi.namedAttributeGet(
            "portNames".identifierGet,
            interface.map(_._1.getName().stringAttrGet).arrayAttrGet
          ),
          namedAttributeApi.namedAttributeGet(
            "portTypes".identifierGet,
            interface.map(_._1.getType().typeAttrGet).arrayAttrGet
          )
        )
    )
  )
  extension (m: Module)
    inline def block(
      using Arena
    ): Block = m.operation.getFirstRegion().getFirstBlock()

    inline def appendToCircuit(
    )(
      using arena: Arena,
      circuit:     Circuit
    ): Unit = circuit.block.appendOwnedOperation(m.operation)

    inline def addOperation(
      operation: Operation
    )(
      using Arena
    ): Unit = m.block.appendOwnedOperation(operation)

    inline def getIO(
      idx: Long
    )(
      using Arena
    ): Value = m.block.getArgument(idx)
end given
class Formal(val operation: Operation)
class Layer(val operation: Operation)
class OptionCase(val operation: Operation)
class Option(val operation: Operation)

// Declarations
class InstanceChoice(val operation: Operation)
class Instance(val operation: Operation)
class Mem(val operation: Operation)
class Node(val operation: Operation)
class Object(val operation: Operation)
class Reg(val operation: Operation)
class RegReset(val operation: Operation)
class Wire(val operation: Operation)

// Statements
class Assert(val operation: Operation)
class Assume(val operation: Operation)
class Attach(val operation: Operation)
class Connect(val operation: Operation)
trait ConnectApi:
  inline def connect(
    src:         Value,
    dst:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    module:      Module
  ): Connect
end ConnectApi
given ConnectApi with
  inline def connect(
    src:         Value,
    dst:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    module:      Module
  ): Connect =
    val connect = Connect(
      summon[OperationApi].operationCreate(
        name = "firrtl.connect",
        location = location,
        operands = Seq(dst, src)
      )
    )
    module.addOperation(connect.operation)
    connect
end given

class Cover(val operation: Operation)
class Force(val operation: Operation)
class LayerBlock(val operation: Operation)
class Match(val operation: Operation)
class MatchingConnect(val operation: Operation)
class Printf(val operation: Operation)
class Propassign(val operation: Operation)
class RefDefine(val operation: Operation)
class RefForceInitial(val operation: Operation)
class RefForce(val operation: Operation)
class RefReleaseInitial(val operation: Operation)
class RefRelease(val operation: Operation)
class Skip(val operation: Operation)
class Stop(val operation: Operation)
class IntVerifAssert(val operation: Operation)
class IntVerifAssume(val operation: Operation)
class IntVerifCover(val operation: Operation)
class When(val operation: Operation)

// Expressions
class Add(val operation: Operation)
class AggregateConstant(val operation: Operation)
class AndPrim(val operation: Operation)
class AndRPrim(val operation: Operation)
class AsAsyncResetPrim(val operation: Operation)
class AsClockPrim(val operation: Operation)
class AsSIntPrim(val operation: Operation)
class AsUIntPrim(val operation: Operation)
class BitCast(val operation: Operation)
class BitsPrim(val operation: Operation)
class BoolConstant(val operation: Operation)
class BundleCreate(val operation: Operation)
class CatPrim(val operation: Operation)
class ConstCast(val operation: Operation)
class Constant(val operation: Operation)
class CvtPrim(val operation: Operation)
class DShlPrim(val operation: Operation)
class DShlwPrim(val operation: Operation)
class DShrPrim(val operation: Operation)
class DivPrim(val operation: Operation)
class DoubleConstant(val operation: Operation)
class EQPrim(val operation: Operation)
class ElementwiseAndPrim(val operation: Operation)
class ElementwiseOrPrim(val operation: Operation)
class ElementwiseXorPrim(val operation: Operation)
class FEnumCreate(val operation: Operation)
class FIntegerConstant(val operation: Operation)
class GEQPrim(val operation: Operation)
class GTPrim(val operation: Operation)
class HWStructCast(val operation: Operation)
class HeadPrim(val operation: Operation)
class IntegerAdd(val operation: Operation)
class IntegerMul(val operation: Operation)
class IntegerShl(val operation: Operation)
class IntegerShr(val operation: Operation)
class InvalidValue(val operation: Operation)
class IsTag(val operation: Operation)
class LEQPrim(val operation: Operation)
class LTLAndIntrinsic(val operation: Operation)
class LTLClockIntrinsic(val operation: Operation)
class LTLConcatIntrinsic(val operation: Operation)
class LTLDelayIntrinsic(val operation: Operation)
class LTLEventuallyIntrinsic(val operation: Operation)
class LTLGoToRepeatIntrinsic(val operation: Operation)
class LTLImplicationIntrinsic(val operation: Operation)
class LTLIntersectIntrinsic(val operation: Operation)
class LTLNonConsecutiveRepeatIntrinsic(val operation: Operation)
class LTLNotIntrinsic(val operation: Operation)
class LTLOrIntrinsic(val operation: Operation)
class LTLRepeatIntrinsic(val operation: Operation)
class LTLUntilIntrinsic(val operation: Operation)
class LTPrim(val operation: Operation)
class ListConcat(val operation: Operation)
class ListCreate(val operation: Operation)
class MulPrim(val operation: Operation)
class MultibitMux(val operation: Operation)
class Mux2CellIntrinsic(val operation: Operation)
class Mux4CellIntrinsic(val operation: Operation)
class MuxPrim(val operation: Operation)
class NEQPrim(val operation: Operation)
class NegPrim(val operation: Operation)
class NotPrim(val operation: Operation)
class ObjectAnyRefCast(val operation: Operation)
class ObjectSubfield(val operation: Operation)
class OpenSubfield(val operation: Operation)
class OpenSubindex(val operation: Operation)
class OrPrim(val operation: Operation)
class OrRPrim(val operation: Operation)
class PadPrim(val operation: Operation)
class Path(val operation: Operation)
class RWProbe(val operation: Operation)
class RefCast(val operation: Operation)
class RefResolve(val operation: Operation)
class RefSend(val operation: Operation)
class RefSub(val operation: Operation)
class RemPrim(val operation: Operation)
class ShlPrim(val operation: Operation)
class ShrPrim(val operation: Operation)
class SizeOfIntrinsic(val operation: Operation)
class SpecialConstant(val operation: Operation)
class StringConstant(val operation: Operation)
class SubPrim(val operation: Operation)
class Subaccess(val operation: Operation)
class Subfield(val operation: Operation)
class Subindex(val operation: Operation)
class Subtag(val operation: Operation)
class TagExtract(val operation: Operation)
class TailPrim(val operation: Operation)
class UninferredResetCast(val operation: Operation)
class UnresolvedPath(val operation: Operation)
class VectorCreate(val operation: Operation)
class VerbatimExpr(val operation: Operation)
class VerbatimWire(val operation: Operation)
class XMRDeref(val operation: Operation)
class XMRRef(val operation: Operation)
class XorPrim(val operation: Operation)
class XorRPrim(val operation: Operation)

// Intrinsics
class ClockDividerIntrinsic(val operation: Operation)
class ClockGateIntrinsic(val operation: Operation)
class ClockInverterIntrinsic(val operation: Operation)
class DPICallIntrinsic(val operation: Operation)
class FPGAProbeIntrinsic(val operation: Operation)
class GenericIntrinsic(val operation: Operation)
class HasBeenResetIntrinsic(val operation: Operation)
class IsXIntrinsic(val operation: Operation)
class PlusArgsTestIntrinsic(val operation: Operation)
class PlusArgsValueIntrinsic(val operation: Operation)
class UnclockedAssumeIntrinsic(val operation: Operation)

// Types
class AnalogType
class AnyRefType
class AsyncResetType
class BaseTypeAliasType
class BoolType
class BundleType
class ClassType
class ClockType
class DoubleType
class FEnumType
class FVectorType
class FIntegerType
class LHSType
class ListType
class OpenBundleType
class OpenVectorType
class PathType
class RefType
class ResetType
class SIntType
class StringType
class UIntType

class AugmentedBundleTypeAttr
class AugmentedGroundTypeAttr
class AugmentedVectorTypeAttr
class InternalPathAttr
class MemoryInitAttr
class ParamDeclAttr
