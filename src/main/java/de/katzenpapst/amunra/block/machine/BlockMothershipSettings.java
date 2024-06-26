package de.katzenpapst.amunra.block.machine;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.katzenpapst.amunra.AmunRa;
import de.katzenpapst.amunra.GuiIds;
import de.katzenpapst.amunra.block.BlockMachineMeta;
import de.katzenpapst.amunra.helper.CoordHelper;
import de.katzenpapst.amunra.tile.TileEntityMothershipSettings;

public class BlockMothershipSettings extends AbstractBlockMothershipRestricted {

    protected final String frontTexture;
    private IIcon iconFront = null;

    public BlockMothershipSettings(final String name, final String frontTexture, final String sideTexture) {
        super(name, sideTexture);
        this.frontTexture = frontTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        this.iconFront = reg.registerIcon(this.frontTexture);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        final int realMeta = ((BlockMachineMeta) this.parent).getRotationMeta(meta);
        // we have the front thingy at front.. but what is front?
        // east is the output
        // I think front is south
        final ForgeDirection front = CoordHelper.rotateForgeDirection(ForgeDirection.SOUTH, realMeta);
        // ForgeDirection output = CoordHelper.rotateForgeDirection(ForgeDirection.EAST, realMeta);

        if (side == front.ordinal()) {
            return this.iconFront;
        }
        return this.blockIcon;
    }

    @Override
    protected void openGui(final World world, final int x, final int y, final int z, final EntityPlayer entityPlayer) {
        entityPlayer.openGui(AmunRa.instance, GuiIds.GUI_MS_SETTINGS, world, x, y, z);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityMothershipSettings();
    }

    @Override
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
}
