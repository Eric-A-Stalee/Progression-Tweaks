package com.theprogrammingturkey.progressiontweaks.client.renderer;

import com.theprogrammingturkey.progressiontweaks.entity.EntityTomahawk;
import com.theprogrammingturkey.progressiontweaks.items.ProgressionItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderTomahawk extends Render<EntityTomahawk>
{
	public RenderTomahawk(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityTomahawk entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate((float) x, (float) y, (float) z);
		if(!entity.isInBlock())
			entity.rotationPitch += 10;
		GlStateManager.rotate(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(0, 0.0F, 1.0F, 1.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90f, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if(this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		Minecraft.getMinecraft().getRenderItem().renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

		if(this.renderOutlines)
		{
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public ItemStack getStackToRender(EntityTomahawk entityIn)
	{
		return new ItemStack(ProgressionItems.TOMAHAWK);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityTomahawk entity)
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}