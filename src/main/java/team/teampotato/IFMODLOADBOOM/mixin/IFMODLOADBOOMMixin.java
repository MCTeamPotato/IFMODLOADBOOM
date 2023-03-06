package team.teampotato.IFMODLOADBOOM.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.teampotato.IFMODLOADBOOM.IFMODLOADBOOM;

@Mixin(TitleScreen.class)
public class IFMODLOADBOOMMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		IFMODLOADBOOM.LOGGER.info("hElLo!bOoMeWoRlD!mIxInLoAdEr!");
	}
}
