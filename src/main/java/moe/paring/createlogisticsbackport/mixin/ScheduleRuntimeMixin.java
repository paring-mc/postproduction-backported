package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.schedule.ScheduleEntry;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import moe.paring.createlogisticsbackport.content.trains.schedule.destination.IHaveScheduleInstructionRunner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScheduleRuntime.class)
public class ScheduleRuntimeMixin {
    @Inject(method = "startCurrentInstruction", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, remap = false)
    private void startCurrentInstruction(CallbackInfoReturnable<DiscoveredPath> cir, ScheduleEntry entry) {
        var instruction = entry.instruction;

        if (instruction instanceof IHaveScheduleInstructionRunner) {
            cir.setReturnValue(((IHaveScheduleInstructionRunner) instruction).start((ScheduleRuntime) (Object) this));
            cir.cancel();
        }
    }
}
