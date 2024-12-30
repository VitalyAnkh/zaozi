// SPDX-License-Identifier: Apache-2.0
package org.llvm.circt.scalalib.operator

import org.llvm.circt.scalalib.{FirrtlBundleField, FirrtlDirection, FirrtlNameKind, given}
import org.llvm.mlir.scalalib.{Block, Context, Location, LocationApi, NamedAttributeApi, Operation, OperationApi, Type, Value, Module as MlirModule, given}

import java.lang.foreign.Arena

// Structure
given CircuitApi with
  inline def circuit(
    circuitName: String
  )(
    using arena: Arena,
    context:     Context
  ): Circuit = Circuit(
    summon[OperationApi].operationCreate(
      name = "firrtl.circuit",
      location = summon[LocationApi].locationUnknownGet,
      regionBlockTypeLocations = Seq(
        Seq(
          (Seq.empty, Seq.empty)
        )
      ),
      namedAttributes =
        val namedAttributeApi = summon[NamedAttributeApi]
        Seq(
          // ::mlir::StringAttr
          namedAttributeApi.namedAttributeGet("name".identifierGet, circuitName.stringAttrGet)
          // ::mlir::ArrayAttr
          // namedAttributeApi.namedAttributeGet("annotations".identifierGet, ???),
          // ::mlir::ArrayAttr
          // namedAttributeApi.namedAttributeGet("enable_layers".identifierGet, ???),
          // ::mlir::ArrayAttr
          // namedAttributeApi.namedAttributeGet("disable_layers".identifierGet, ???),
          // ::circt::firrtl::LayerSpecializationAttr
          // namedAttributeApi.namedAttributeGet("default_layer_specialization".identifierGet, ???),
          // ::mlir::ArrayAttr
          // namedAttributeApi.namedAttributeGet("select_inst_choice".identifierGet, ???),
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
given ModuleApi with
  inline def module(
    name:        String,
    location:    Location,
    interface:   Seq[(FirrtlBundleField, Location)]
  )(
    using arena: Arena,
    context:     Context
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
          // ::mlir::StringAttr
          namedAttributeApi.namedAttributeGet(
            "sym_name".identifierGet,
            name.stringAttrGet
          ),
          // ::circt::firrtl::ConventionAttr
          // namedAttributeApi.namedAttributeGet("convention".identifierGet, ???),
          // ::mlir::DenseBoolArrayAttr
          namedAttributeApi.namedAttributeGet(
            "portDirections".identifierGet,
            interface
              .map:
                case (bf, _) =>
                  if (bf.getIsFlip()) FirrtlDirection.In else FirrtlDirection.Out
              .attrGetPortDirs
          ),
          // ::mlir::ArrayAttr
          namedAttributeApi.namedAttributeGet(
            "portLocations".identifierGet,
            interface.map(_._2.getAttribute).arrayAttrGet
          ),
          // ::mlir::ArrayAttr
          namedAttributeApi.namedAttributeGet(
            "portAnnotations".identifierGet,
            Seq().arrayAttrGet
          ),
          // ::mlir::ArrayAttr
          namedAttributeApi.namedAttributeGet(
            "portSymbols".identifierGet,
            Seq().arrayAttrGet
          ),
          // ::mlir::ArrayAttr
          namedAttributeApi.namedAttributeGet(
            "portNames".identifierGet,
            interface.map(_._1.getName().stringAttrGet).arrayAttrGet
          ),
          // ::mlir::ArrayAttr
          namedAttributeApi.namedAttributeGet(
            "portTypes".identifierGet,
            interface.map(_._1.getType().typeAttrGet).arrayAttrGet
          )
          // ::mlir::ArrayAttr
          // namedAttributeApi.namedAttributeGet("annotations".identifierGet, ???),
          // ::mlir::ArrayAttr
          // namedAttributeApi.namedAttributeGet("layers".identifierGet, ???)
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

// Declarations
given InstanceApi with
  inline def instance(
    moduleName:   String,
    instanceName: String,
    nameKind:     FirrtlNameKind,
    location:     Location,
    interface:    Seq[FirrtlBundleField]
  )(
    using arena:  Arena,
    context:      Context,
    module:       Module
  ): Instance =
    val instance = new Instance(
      summon[OperationApi].operationCreate(
        name = "firrtl.instance",
        location = location,
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::FlatSymbolRefAttr
            namedAttributeApi.namedAttributeGet("moduleName".identifierGet, moduleName.flatSymbolRefAttrGet),
            // ::mlir::StringAttr
            namedAttributeApi.namedAttributeGet("name".identifierGet, instanceName.stringAttrGet),
            // ::circt::firrtl::NameKindEnumAttr
            namedAttributeApi.namedAttributeGet("nameKind".identifierGet, nameKind.attrGetNameKind),
            // ::mlir::DenseBoolArrayAttr
            namedAttributeApi.namedAttributeGet(
              "portDirections".identifierGet,
              interface
                .map: bf =>
                  if (bf.getIsFlip()) FirrtlDirection.In else FirrtlDirection.Out
                .attrGetPortDirs
            ),
            // ::mlir::ArrayAttr
            namedAttributeApi.namedAttributeGet(
              "portNames".identifierGet,
              interface.map(_.getName().stringAttrGet).arrayAttrGet
            )
            // ::mlir::ArrayAttr
            // namedAttributeApi.namedAttributeGet("annotations".identifierGet, Seq().arrayAttrGet),
            // ::mlir::ArrayAttr
            // namedAttributeApi.namedAttributeGet("portAnnotations".identifierGet, Seq().arrayAttrGet),
            // ::mlir::ArrayAttr
            // namedAttributeApi.namedAttributeGet("layers".identifierGet, Seq().arrayAttrGet)
            // ::mlir::UnitAttr
            // namedAttributeApi.namedAttributeGet("lowerToBind".identifierGet, ???),
            // ::circt::hw::InnerSymAttr
            // namedAttributeApi.namedAttributeGet("inner_sym".identifierGet, ???)
          )
        ,
        resultsTypes = Some(interface.map(_.getType()))
      )
    )
    module.addOperation(instance.operation)
    instance
end given
given RegApi with
  inline def reg(
    name:        String,
    location:    Location,
    nameKind:    FirrtlNameKind,
    tpe:         Type,
    clock:       Value
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Reg =
    val reg = Reg(
      summon[OperationApi].operationCreate(
        name = "firrtl.reg",
        location = location,
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::StringAttr
            namedAttributeApi.namedAttributeGet("name".identifierGet, name.stringAttrGet),
            // ::circt::firrtl::NameKindEnumAttr
            namedAttributeApi.namedAttributeGet("nameKind".identifierGet, nameKind.attrGetNameKind)
            // ::mlir::ArrayAttr
            // namedAttributeApi.namedAttributeGet("annotations".identifierGet, ???),
            // ::circt::hw::InnerSymAttr
            // namedAttributeApi.namedAttributeGet("inner_sym".identifierGet, ???),
            // ::mlir::UnitAttr
            // namedAttributeApi.namedAttributeGet("forceable".identifierGet, ???)
          )
        ,
        operands = Seq(clock),
        resultsTypes = Some(Seq(tpe))
      )
    )
    block.appendOwnedOperation(reg.operation)
    reg
end given
given RegResetApi with
  inline def regReset(
    name:        String,
    location:    Location,
    nameKind:    FirrtlNameKind,
    tpe:         Type,
    clock:       Value,
    reset:       Value
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): RegReset =
    val regReset = RegReset(
      summon[OperationApi].operationCreate(
        name = "firrtl.regreset",
        location = location,
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::StringAttr
            namedAttributeApi.namedAttributeGet("name".identifierGet, name.stringAttrGet),
            // ::circt::firrtl::NameKindEnumAttr
            namedAttributeApi.namedAttributeGet("nameKind".identifierGet, nameKind.attrGetNameKind)
            // ::mlir::ArrayAttr
            // namedAttributeApi.namedAttributeGet("annotations".identifierGet, ???),
            // ::circt::hw::InnerSymAttr
            // namedAttributeApi.namedAttributeGet("inner_sym".identifierGet, ???),
            // ::mlir::UnitAttr
            // namedAttributeApi.namedAttributeGet("forceable".identifierGet, ???)
          )
        ,
        operands = Seq(clock, reset),
        resultsTypes = Some(Seq(tpe))
      )
    )
    block.appendOwnedOperation(regReset.operation)
    regReset
end given
given WireApi with
  def wire(
    name:        String,
    location:    Location,
    nameKind:    FirrtlNameKind,
    tpe:         Type
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Wire =
    val wire = Wire(
      summon[OperationApi].operationCreate(
        name = "firrtl.wire",
        location = location,
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::StringAttr
            namedAttributeApi.namedAttributeGet("name".identifierGet, name.stringAttrGet),
            // ::circt::firrtl::NameKindEnumAttr
            namedAttributeApi.namedAttributeGet("nameKind".identifierGet, nameKind.attrGetNameKind)
            // ::mlir::ArrayAttr
            // namedAttributeApi.namedAttributeGet("annotations".identifierGet, ???),
            // ::circt::hw::InnerSymAttr
            // namedAttributeApi.namedAttributeGet("inner_sym".identifierGet, ???),
            // ::mlir::UnitAttr
            // namedAttributeApi.namedAttributeGet("forceable".identifierGet, ???)
          )
        ,
        resultsTypes = Some(Seq(tpe))
      )
    )
    block.appendOwnedOperation(wire.operation)
    wire
  extension (ref: Wire)
    def result(
      using Arena
    ): Value = ref.operation.getResult(0)

end given

// Statements
given ConnectApi with
  inline def connect(
    src:         Value,
    dst:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Connect =
    val op = Connect(
      summon[OperationApi].operationCreate(
        name = "firrtl.connect",
        location = location,
        operands = Seq(dst, src)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

// Expression
given AddPrimApi with
  def addPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AddPrim =
    val op = AddPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.add",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given AndPrimApi with
  def andPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AndPrim =
    val op = AndPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.and",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given AndRPrimApi with
  def andRPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AndRPrim =
    val op = AndRPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.andr",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given AsAsyncResetPrimApi with
  def asAsyncResetPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AsAsyncResetPrim =
    val op = AsAsyncResetPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.asAsyncReset",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given AsClockPrimApi with
  def AsClockPrimPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AsClockPrim =
    val op = AsClockPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.asClock",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given AsSIntPrimApi with
  def asSIntPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AsSIntPrim =
    val op = AsSIntPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.asSInt",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given AsUIntPrimApi with
  def asUIntPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AsUIntPrim =
    val asUIntPrim = AsUIntPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.asUInt",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(asUIntPrim.operation)
    asUIntPrim
end given

given BitsPrimApi with
  def bitsPrim(
    input:       Value,
    hi:          Int,
    lo:          Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): BitsPrim =
    val op = BitsPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.bits",
        location = location,
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("hi".identifierGet, hi.toLong.toIntegerAttribute(32.toUnsignedInteger)),
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("lo".identifierGet, lo.toLong.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given CatPrimApi with
  def catPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): BitsPrim =
    val op = BitsPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.cat",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given ConstantApi with
  def constant(
    input:       BigInt,
    width:       Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): BitsPrim =
    val op = BitsPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.constant",
        location = location,
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("value".identifierGet, input.toIntegerAttribute(width.toIntegerType))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given CvtPrimApi with
  def cvtPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): CvtPrim =
    val op = CvtPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.cvt",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given DShlPrimApi with
  def dShlPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): DShlPrim =
    val op = DShlPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.dshl",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given DShrPrimApi with
  def dShrPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): DShrPrim =
    val op = DShrPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.dshr",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given DivPrimApi with
  def divPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): DivPrim =
    val op = DivPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.div",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given EQPrimApi with
  def eqPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): EQPrim =
    val op = EQPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.eq",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given GEQPrimApi with
  def geqPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): GEQPrim =
    val op = GEQPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.geq",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given GTPrimApi with
  def gtPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): GTPrim =
    val op = GTPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.gt",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given HeadPrimApi with
  def headPrim(
    input:       Value,
    amount:      Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): HeadPrim =
    val op = HeadPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.head",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("amount".identifierGet, amount.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given LEQPrimApi with
  def leqPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): LEQPrim =
    val op = LEQPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.leq",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given LTPrimApi with
  def ltPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): LTPrim =
    val op = LTPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.lt",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given MulPrimApi with
  def mulPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): MulPrim =
    val op = MulPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.mul",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given MuxPrimApi with
  def muxPrim(
    sel:         Value,
    high:        Value,
    low:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): MuxPrim =
    val op = MuxPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.mux",
        location = location,
        operands = Seq(sel, high, low),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given NeqPrimApi with
  def neqPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): NeqPrim =
    val op = NeqPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.neq",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given NegPrimApi with
  def negPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): NegPrim =
    val op = NegPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.neg",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given NotPrimApi with
  def notPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): NotPrim =
    val op = NotPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.not",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given OrPrimApi with
  def orPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): OrPrim =
    val op = OrPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.or",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given OrRPrimApi with
  def orRPrim(
    input:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): AndRPrim =
    val op = AndRPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.orr",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given PadPrimApi with
  def padPrim(
    input:       Value,
    amount:      Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): PadPrim =
    val op = PadPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.pad",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("amount".identifierGet, amount.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given RemPrimApi with
  def remPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): DivPrim =
    val op = DivPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.rem",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given ShlPrimApi with
  def shlPrim(
    input:       Value,
    amount:      Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): ShlPrim =
    val op = ShlPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.shl",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("amount".identifierGet, amount.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given ShrPrimApi with
  def shrPrim(
    input:       Value,
    amount:      Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): ShrPrim =
    val op = ShrPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.shr",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi.namedAttributeGet("amount".identifierGet, amount.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given SubPrimApi with
  def subPrim(
    lhs:         Value,
    rhs:         Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): SubPrim =
    val op = SubPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.sub",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given SubaccessApi with
  def subaccess(
    input:       Value,
    index:       Value,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Subaccess =
    val op = Subaccess(
      summon[OperationApi].operationCreate(
        name = "firrtl.subaccess",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given SubfieldApi with
  def subfield(
    input:       Value,
    fieldIndex:  Int,
    location:    Location
  )(
    using arena: Arena,
    context:     Context,
    block:       Block
  ): Subfield =
    val op = Subfield(
      summon[OperationApi].operationCreate(
        name = "firrtl.subfield",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi
              .namedAttributeGet("fieldIndex".identifierGet, fieldIndex.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given SubindexApi with
  def subindex(
                input:       Value,
                index:  Int,
                location:    Location
              )(
                using arena: Arena,
                context:     Context,
                block:       Block
              ): Subindex =
    val op = Subindex(
      summon[OperationApi].operationCreate(
        name = "firrtl.subindex",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi
              .namedAttributeGet("index".identifierGet, index.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given TailPrimApi with
  def tailPrim(
                input:       Value,
                amount:  Int,
                location:    Location
              )(
                using arena: Arena,
                context:     Context,
                block:       Block
              ): TailPrim =
    val op = TailPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.amount",
        location = location,
        operands = Seq(input),
        namedAttributes =
          val namedAttributeApi = summon[NamedAttributeApi]
          Seq(
            // ::mlir::IntegerAttr
            namedAttributeApi
              .namedAttributeGet("amount".identifierGet, amount.toIntegerAttribute(32.toUnsignedInteger))
          )
        ,
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given XorPrimApi with
  def xorPrim(
               lhs:         Value,
               rhs:         Value,
               location:    Location
             )(
               using arena: Arena,
               context:     Context,
               block:       Block
             ): XorPrim =
    val op = XorPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.xor",
        location = location,
        operands = Seq(lhs, rhs),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

given XorRPrimApi with
  def xorRPrim(
                input:       Value,
                location:    Location
              )(
                using arena: Arena,
                context:     Context,
                block:       Block
              ): XorRPrim =
    val op = XorRPrim(
      summon[OperationApi].operationCreate(
        name = "firrtl.xorr",
        location = location,
        operands = Seq(input),
        inferredResultsTypes = Some(1)
      )
    )
    block.appendOwnedOperation(op.operation)
    op
end given

