package mods.flammpfeil.reincarnation;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSpirit extends RenderLiving {

	public RenderSpirit() {
		super(new ModelSpirit(), 0.0f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation("flammpfeil.reincarnation","textures/entity/spirit.png");
	}

}
