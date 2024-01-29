package com.github.nyuppo.client.render.entity.feature;

import com.github.nyuppo.MoreMobVariants;
import com.github.nyuppo.config.Variants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ShearedWoolColorFeatureRenderer<T extends SheepEntity, M extends SheepEntityModel<T>> extends FeatureRenderer<T, M> {
    public ShearedWoolColorFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T sheepEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!sheepEntity.isSheared()) {
             return;
        }

        NbtCompound nbt = new NbtCompound();
        sheepEntity.writeCustomDataToNbt(nbt);

        if (nbt.contains(MoreMobVariants.NBT_KEY)) {
            Identifier variant = new Identifier(nbt.getString(MoreMobVariants.NBT_KEY));
            if (Variants.getVariant(EntityType.SHEEP, variant).hasColorWhenSheared()) {
                float[] hs = SheepEntity.getRgbColor(sheepEntity.getColor());

                RenderLayer FUR_OVERLAY = RenderLayer.getEntityCutoutNoCull(MoreMobVariants.id("textures/entity/sheep/sheared_color_overlay/" + variant.getPath() + ".png"));
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(FUR_OVERLAY);
                ((Model)this.getContextModel()).render(matrices, vertexConsumer, 0xF00000, OverlayTexture.DEFAULT_UV, hs[0], hs[1], hs[2], 1.0f);
            }
        }


    };

}
