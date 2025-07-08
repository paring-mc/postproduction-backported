package moe.paring.createlogisticsbackport.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.equipment.clipboard.ClipboardScreen;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.ref.WeakReference;
import java.util.List;

@Mixin(ClipboardScreen.class)
public class ClipboardScreenMixin {
    @Shadow
    List<ClipboardEntry> currentEntries;
    @Unique
    private WeakReference<ClipboardEntry> create_logistics_backport$_clipboardEntry;

    @Inject(remap = false, method = "renderWindow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void drawString(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci, int x, int y, int i) {
        if (create_logistics_backport$_clipboardEntry != null && create_logistics_backport$_clipboardEntry.get() != null) {
            create_logistics_backport$_clipboardEntry.clear();
        }
        create_logistics_backport$_clipboardEntry = new WeakReference<>(currentEntries.get(i));
    }

    @Unique
    private boolean create_logistics_backport$isAddress(ClipboardEntry entry) {
        if (entry == null) return false;
        var string = entry.text.getString();

        return string.startsWith("#") && !string.substring(1).isBlank();
    }

    @Redirect(method = "renderWindow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private int drawStringRedirect(GuiGraphics instance, Font font, String text, int x, int y, int color, boolean dropShadow) {
        var entry = create_logistics_backport$_clipboardEntry.get();

        if (create_logistics_backport$isAddress(entry)) {
            var checked = text.equals("✔");

            RenderSystem.enableBlend();
            (checked ? ExtraGuiTextures.CLIPBOARD_ADDRESS_INACTIVE : ExtraGuiTextures.CLIPBOARD_ADDRESS)
                    .render(instance, x, y - 1);

            return 0;
        }

        return instance.drawString(font, text, x, y, color, dropShadow);
    }

    @Redirect(method = "renderWindow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I"))
    private int drawStringRedirectTextPart(GuiGraphics instance, Font font, FormattedCharSequence sequence, int x, int y, int color, boolean dropShadow) {
        var entry = create_logistics_backport$_clipboardEntry.get();
        if (create_logistics_backport$isAddress(entry)) {
            return instance.drawString(font, sequence, x, y, color == 0x31B25D ? 0x668D7F6B : 0x311A00, dropShadow);
        }

        return instance.drawString(font, sequence, x, y, color, dropShadow);
    }

    @Redirect(method = "renderWindow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
    private List<FormattedCharSequence> split(Font instance, FormattedText text, int what) {
        var entry = create_logistics_backport$_clipboardEntry.get();
        if (!create_logistics_backport$isAddress(entry)) {
            return instance.split(text, what);
        }

        var string = text.getString();
        text = Component.literal(string.substring(1).stripLeading());
        return instance.split(text, what);
    }
}
