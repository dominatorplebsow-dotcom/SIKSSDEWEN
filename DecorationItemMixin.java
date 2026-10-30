package pl.example.cobwebpainting.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DecorationItem.class)
public class DecorationItemMixin {

    @Redirect(
        method = "useOnBlock",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/item/ItemUsageContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;")
    )
    private BlockPos cobwebpainting$redirectPos(ItemUsageContext ctx) {
        BlockPos clicked = ctx.getBlockPos();
        World world = ctx.getWorld();

        if (world.getBlockState(clicked).isOf(Blocks.COBWEB)) {
            Direction side = ctx.getSide();
            if (side.getAxis().isHorizontal()) {
                BlockPos behind = clicked.offset(side.getOpposite());
                if (world.getBlockState(behind).isSolid()) {
                    return behind;
                }
            }
        }
        return clicked;
    }
}
