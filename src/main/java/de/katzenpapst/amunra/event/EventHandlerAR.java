package de.katzenpapst.amunra.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.katzenpapst.amunra.AmunRa;
import de.katzenpapst.amunra.item.ItemThermalSuit;
import de.katzenpapst.amunra.mob.DamageSourceAR;
import de.katzenpapst.amunra.mob.entity.IEntityNonOxygenBreather;
import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;

public class EventHandlerAR {

    @SubscribeEvent
    public void entityLivingEvent(final LivingUpdateEvent event) {
        final EntityLivingBase entityLiving = event.entityLiving;
        if (!(entityLiving instanceof IEntityNonOxygenBreather nonOxygenBreather)) {
            return;
        }

        if (entityLiving.ticksExisted % 100 == 0) {
            CelestialBody body;
            final boolean isInSealedArea = OxygenUtil.isAABBInBreathableAirBlock(entityLiving);

            if (entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider gcProvider) {
                body = gcProvider.getCelestialBody();
            } else {
                body = GalacticraftCore.planetOverworld;
            }
            if (!nonOxygenBreather.canBreatheIn(body.atmosphere, isInSealedArea)) {
                // should I add these events about suffocation that GC does?
                entityLiving.attackEntityFrom(DamageSourceAR.dsSuffocate, 1);
            }
        }
    }

    @SubscribeEvent
    public void onThermalArmorEvent(final ThermalArmorEvent event) {
        // I sure hope this works with other mods...

        if (event.armorStack != null && event.armorStack.getItem() instanceof ItemThermalSuit) {
            event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
        }

    }

    @SideOnly(Side.CLIENT)
    private void processGravityEvent(final ZeroGravityEvent event) {
        if (!(event.entity instanceof EntityPlayer player)) {
            return;
        }
        if (AmunRa.proxy.doCancelGravityEvent(player)) {
            event.setCanceled(true);
        }

    }

    // gravity events. these should be client-only
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGravityEvent(final ZeroGravityEvent.InFreefall event) {
        this.processGravityEvent(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGravityEvent(final ZeroGravityEvent.Motion event) {
        this.processGravityEvent(event);
    }
}
