package moe.paring.createlogisticsbackport.content.equipment.armor;

import moe.paring.createlogisticsbackport.registry.ExtraItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.UUID;

@EventBusSubscriber
public class CardboardArmorHandler {

	@SubscribeEvent
	public static void playerHitboxChangesWhenHidingAsBox(EntityEvent.Size event) {
		Entity entity = event.getEntity();
		if (!entity.isAddedToWorld())
			return;
		if (!testForStealth(entity))
			return;
		
		event.setNewSize(EntityDimensions.fixed(0.6F, 0.8F));
		event.setNewEyeHeight(0.6F);
	}

	@SubscribeEvent
	public static void playersStealthWhenWearingCardboard(LivingVisibilityEvent event) {
		LivingEntity entity = event.getEntity();
		if (!testForStealth(entity))
			return;
		event.modifyVisibility(0);
	}

	@SubscribeEvent
	public static void mobsMayLoseTargetWhenItIsWearingCardboard(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.tickCount % 16 != 0)
			return;
		if (!(entity instanceof Mob mob))
			return;

		if (testForStealth(mob.getTarget())) {
			mob.setTarget(null);
			if (mob.targetSelector != null)
				mob.targetSelector.getRunningGoals()
					.forEach(wrappedGoal -> {
						if (wrappedGoal.getGoal() instanceof TargetGoal tg)
							tg.stop();
					});
		}

		if (entity instanceof NeutralMob nMob && entity.level() instanceof ServerLevel sl) {
			UUID uuid = nMob.getPersistentAngerTarget();
			if (uuid != null && testForStealth(sl.getEntity(uuid)))
				nMob.stopBeingAngry();
		}

		if (testForStealth(mob.getLastHurtByMob())) {
			mob.setLastHurtByMob(null);
			mob.setLastHurtByPlayer(null);
		}
	}

	public static boolean testForStealth(Entity entityIn) {
		if (!(entityIn instanceof LivingEntity entity))
			return false;
		if (entity.getPose() != Pose.CROUCHING)
			return false;
		if (entity instanceof Player player && player.getAbilities().flying)
			return false;
		if (!ExtraItems.CARDBOARD_HELMET.isIn(entity.getItemBySlot(EquipmentSlot.HEAD)))
			return false;
		if (!ExtraItems.CARDBOARD_CHESTPLATE.isIn(entity.getItemBySlot(EquipmentSlot.CHEST)))
			return false;
		if (!ExtraItems.CARDBOARD_LEGGINGS.isIn(entity.getItemBySlot(EquipmentSlot.LEGS)))
			return false;
		if (!ExtraItems.CARDBOARD_BOOTS.isIn(entity.getItemBySlot(EquipmentSlot.FEET)))
			return false;
		return true;
	}

}
