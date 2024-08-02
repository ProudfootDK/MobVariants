package com.github.nyuppo.mixin;

import com.github.nyuppo.MoreMobVariants;
import com.github.nyuppo.config.Variants;
import com.github.nyuppo.variant.MobVariant;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SheepWoolFeatureRenderer.class)
public class SheepWoolFeatureMixin {
    @ModifyArgs(
            method = "Lnet/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer;render(Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFFI)V")
    )
    private void mixinSheepFurTexture(Args args) {
        SheepEntity sheepEntity = args.get(6);
        NbtCompound nbt = new NbtCompound();
        sheepEntity.writeCustomDataToNbt(nbt);

        if (nbt.contains(MoreMobVariants.NBT_KEY)) {
            String variant = nbt.getString(MoreMobVariants.NBT_KEY);
            if (variant.isEmpty()) {
                return;
            }

            String[] split = Variants.splitVariant(variant);

            if (Variants.getVariant(EntityType.SHEEP, Identifier.of(split[0], split[1])).hasCustomWool()) {
                args.set(2, Identifier.of(split[0], "textures/entity/sheep/wool/" + split[1] + ".png"));

                if (sheepEntity.hasCustomName()) {
                    MobVariant nametagVariant = Variants.getVariantFromNametag(EntityType.SHEEP, sheepEntity.getName().getString());
                    if (nametagVariant != null) {
                        Identifier identifier = nametagVariant.getIdentifier();
                        args.set(2, Identifier.of(identifier.getNamespace(), "textures/entity/sheep/wool/" + identifier.getPath() + ".png"));
                    }
                }
            }
        }
    }
}
