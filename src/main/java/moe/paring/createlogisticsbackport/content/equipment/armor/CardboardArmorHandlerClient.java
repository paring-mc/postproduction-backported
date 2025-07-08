package moe.paring.createlogisticsbackport.content.equipment.armor;

import com.google.common.cache.Cache;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageRenderer;
import moe.paring.createlogisticsbackport.foundation.utility.TickBasedCache;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@EventBusSubscriber(value = Dist.CLIENT)
public class CardboardArmorHandlerClient {

	private static final Cache<UUID, Integer> BOXES_PLAYERS_ARE_HIDING_AS = new TickBasedCache<>(20, true);

	@SubscribeEvent
	public static void keepCacheAliveDesignDespiteNotRendering(PlayerTickEvent event) {
		if (event.phase == Phase.START)
			return;
		Player player = event.player;
		if (!CardboardArmorHandler.testForStealth(player))
			return;
		try {
			getCurrentBoxIndex(player);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void playerRendersAsBoxWhenSneaking(RenderPlayerEvent.Pre event) {
		Player player = event.getEntity();
		if (!CardboardArmorHandler.testForStealth(player))
			return;

		event.setCanceled(true);

		if (player == Minecraft.getInstance().player
			&& Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON)
			return;

		PoseStack ms = event.getPoseStack();
		ms.pushPose();
		ms.translate(0, 2 / 16f, 0);

		float movement = (float) player.position()
			.subtract(player.xo, player.yo, player.zo)
			.length();

		if (player.onGround())
			ms.translate(0,
				Math.min(Math.abs(Mth.cos((AnimationTickHolder.getRenderTime() % 256) / 2.0f)) * 2 / 16f, movement * 5),
				0);

		float interpolatedYaw = Mth.lerp(event.getPartialTick(), player.yRotO, player.getYRot());

		try {
			PartialModel model = ExtraPartialModels.PACKAGES_TO_HIDE_AS.get(getCurrentBoxIndex(player));
			PackageRenderer.renderBox(player, interpolatedYaw, ms, event.getMultiBufferSource(),
				event.getPackedLight(), model);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		ms.popPose();
	}

	private static Integer getCurrentBoxIndex(Player player) throws ExecutionException {
		return BOXES_PLAYERS_ARE_HIDING_AS.get(player.getUUID(),
			() -> player.level().random.nextInt(ExtraPartialModels.PACKAGES_TO_HIDE_AS.size()));
	}

}
