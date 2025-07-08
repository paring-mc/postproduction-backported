package moe.paring.createlogisticsbackport.content.kinetics.chainConveyor;

import java.util.List;

import moe.paring.createlogisticsbackport.registry.ExtraPackets;
import org.apache.commons.lang3.mutable.MutableBoolean;

import moe.paring.createlogisticsbackport.registry.ExtraPackets;
import com.simibubi.create.foundation.utility.RaycastHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class ChainPackageInteractionHandler {

	public static boolean onUse() {
		Minecraft mc = Minecraft.getInstance();
		MutableBoolean success = new MutableBoolean(false);

		ChainConveyorPackage.physicsDataCache.get(mc.level)
			.asMap()
			.forEach((i, data) -> {
				if (success.booleanValue())
					return;
				if (data == null || data.targetPos == null || data.beReference == null)
					return;
				AABB bounds = new AABB(data.targetPos, data.targetPos).move(0, -.25, 0)
					.expandTowards(0, 0.5, 0)
					.inflate(0.45);

				double range = mc.player.getAttribute(ForgeMod.BLOCK_REACH.get())
					.getValue() + 1;
				Vec3 from = RaycastHelper.getTraceOrigin(mc.player);
				Vec3 to = RaycastHelper.getTraceTarget(mc.player, range, from);

				if (bounds.clip(from, to)
					.isEmpty())
					return;

				ChainConveyorBlockEntity ccbe = data.beReference.get();
				if (ccbe == null || ccbe.isRemoved())
					return;

				for (ChainConveyorPackage pckg : ccbe.getLoopingPackages()) {
					if (pckg.netId == i) {
						ExtraPackets.getChannel()
							.sendToServer(
								new ChainPackageInteractionPacket(ccbe.getBlockPos(), null, pckg.chainPosition, null));
						success.setTrue();
						return;
					}
				}

				for (BlockPos connection : ccbe.connections) {
					List<ChainConveyorPackage> list = ccbe.travellingPackages.get(connection);
					if (list == null)
						continue;
					for (ChainConveyorPackage pckg : list) {
						if (pckg.netId == i) {
							ExtraPackets.getChannel()
								.sendToServer(new ChainPackageInteractionPacket(ccbe.getBlockPos(), connection,
									pckg.chainPosition, null));
							success.setTrue();
							return;
						}
					}
				}

			});

		return success.booleanValue();
	}

}
