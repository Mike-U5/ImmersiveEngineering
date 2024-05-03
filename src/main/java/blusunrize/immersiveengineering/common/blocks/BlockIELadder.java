package blusunrize.immersiveengineering.common.blocks;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLadder;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockIELadder extends BlockLadder {
	public BlockIELadder(String name) {
		super();
		this.setBlockName(ImmersiveEngineering.MODID + "." + name);
		this.setResistance(10f);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		GameRegistry.registerBlock(this, name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("immersiveengineering:treated_ladder");
	}
}
