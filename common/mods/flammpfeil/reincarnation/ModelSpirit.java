package mods.flammpfeil.reincarnation;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelSpirit extends ModelBase {

	ModelRenderer Shape1;
	public ModelSpirit(){

		textureWidth = 16;
		textureHeight = 16;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);
		Shape1.setRotationPoint(0F, 0F, 0F);
		Shape1.setTextureSize(textureWidth, textureHeight);
		Shape1.mirror = true;
	 }

	float rot;

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 1.5f, 0);
		GL11.glRotatef(rot, 0, 1, 0);

		GL11.glRotatef(-54.73561032f, 1, 0, 1);

		//GL11.glScalef(4.0f, 4.0f, 4.0f);
		Shape1.render(f5);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLivingBase,
			float par2, float par3, float par4) {
		super.setLivingAnimations(par1EntityLivingBase, par2, par3, par4);

		//(prev * (1.0 - par4) + now * par4)

		rot = (float)(360.0 * ((par1EntityLivingBase.ticksExisted % 200) / 200.0));

	}
}
