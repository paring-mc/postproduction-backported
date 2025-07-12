package moe.paring.createlogisticsbackport.compat.computercraft.peripherals;

import com.simibubi.create.compat.computercraft.implementation.peripherals.SyncedPeripheral;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import moe.paring.createlogisticsbackport.compat.computercraft.ComputerUtil;
import moe.paring.createlogisticsbackport.compat.computercraft.luaObjects.PackageLuaObject;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class PackagerPeripheral extends SyncedPeripheral<PackagerBlockEntity> {

	public PackagerPeripheral(PackagerBlockEntity blockEntity) {
		super(blockEntity);
	}

	@Override
	public void attach(@NotNull IComputerAccess computer) {
		super.attach(computer);
		// Ephemeral nature of address, should not be set on load until a computer
		// explicitly calls setAddress again on the BE.
		blockEntity.hasCustomComputerAddress = false;
	}

	@Override
	public void detach(@NotNull IComputerAccess computer) {
		super.detach(computer);
		// Ephemeral nature of address, should not be set on load until a computer
		// explicitly calls setAddress again on the BE.
		blockEntity.hasCustomComputerAddress = false;
	}

	@LuaFunction(mainThread = true)
	public final boolean makePackage() {
		if (!blockEntity.heldBox.isEmpty())
			return false;
		blockEntity.activate();
		if (blockEntity.heldBox.isEmpty())
			return false;
		return true;
	}

	@LuaFunction(mainThread = true)
	public Map<Integer, Map<String, ?>> list() {
		return ComputerUtil.list(blockEntity.targetInventory.getInventory());
	}

	@LuaFunction(mainThread = true)
	public Map<String, ?> getItemDetail(int slot) throws LuaException {
		return ComputerUtil.getItemDetail(blockEntity.targetInventory.getInventory(), slot);
	}

	@LuaFunction(mainThread = true)
	public final String getAddress() {
		blockEntity.updateSignAddress();
		return blockEntity.signBasedAddress;
	}

	@LuaFunction(mainThread = true)
	public final void setAddress(Optional<String> argument) {
		if (argument.isPresent()) {
			blockEntity.customComputerAddress = argument.get();
			blockEntity.signBasedAddress = argument.get();
			blockEntity.hasCustomComputerAddress = true;
		} else {
			blockEntity.customComputerAddress = "";
			blockEntity.hasCustomComputerAddress = false;
		}
	}

	@LuaFunction(mainThread = true)
	public final PackageLuaObject getPackage() {
		ItemStack box = blockEntity.heldBox;
		if (box.isEmpty())
			return null;
		return new PackageLuaObject(blockEntity, box);
	}

	@NotNull
	@Override
	public String getType() {
		return "Create_Packager";
	}

}
