package uwu.lopyluna.create_dd.block.BlockProperties.drill.shadow;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.actors.flwdata.ActorData;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import uwu.lopyluna.create_dd.block.BlockPalette.gen.DDPaletteMaterialSpecs;
import uwu.lopyluna.create_dd.block.BlockResources.DDBlockPartialModel;

public class ShadowDrillActorInstance extends ActorInstance {

    ActorData drillHead;
    private final Direction facing;

    public ShadowDrillActorInstance(MaterialManager materialManager, VirtualRenderWorld contraption, MovementContext context) {
        super(materialManager, contraption, context);

        Material<ActorData> material = materialManager.defaultSolid()
                .material(DDPaletteMaterialSpecs.ACTORS);

        BlockState state = context.state;

        facing = state.getValue(ShadowDrillBlock.FACING);

        Direction.Axis axis = facing.getAxis();
        float eulerX = AngleHelper.verticalAngle(facing);

        float eulerY;
        if (axis == Direction.Axis.Y)
            eulerY = 0;
        else
            eulerY = facing.toYRot() + ((axis == Direction.Axis.X) ? 180 : 0);

        drillHead = material.getModel(DDBlockPartialModel.SHADOW_DRILL_HEAD, state).createInstance();

        drillHead.setPosition(context.localPos)
                .setBlockLight(localBlockLight())
                .setRotationOffset(0)
                .setRotationAxis(0, 0, 1)
                 .setLocalRotation(new Quaternionf().rotationXYZ(eulerX * Mth.DEG_TO_RAD, eulerY * Mth.DEG_TO_RAD, 0))
                .setSpeed(getSpeed(facing));
    }

    @Override
    public void beginFrame() {
        drillHead.setSpeed(getSpeed(facing));
    }

    protected float getSpeed(Direction facing) {
        if (context.contraption.stalled || !VecHelper.isVecPointingTowards(context.relativeMotion, facing.getOpposite()))
            return context.getAnimationSpeed();
        return 0;
    }
}
