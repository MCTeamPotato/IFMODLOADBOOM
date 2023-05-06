package com.teampotato.howdareyouload.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow public abstract void stop();

    @Shadow @Final public File gameDirectory;

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void onRun(CallbackInfo ci) {
        File config = new File(gameDirectory, "config");
        if (!config.exists()) {
            config.mkdirs();
        }
        File configFile = new File(config, "HowDareYouLoad.json");
        if (!configFile.exists()) {
            try {
                JsonObject defaultConfig = new JsonObject();
                JsonArray modList = new JsonArray();
                modList.add("modID1");
                modList.add("modID2");
                defaultConfig.add("mods", modList);
                FileWriter writer = new FileWriter(configFile);
                writer.write(defaultConfig.toString());
                writer.close();
            } catch (IOException e) {
                return;
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            JsonObject configObject = new JsonParser().parse(reader).getAsJsonObject();
            JsonArray modList = configObject.getAsJsonArray("mods");
            for (JsonElement modElement : modList) {
                String modId = modElement.getAsString();
                if (FMLLoader.getLoadingModList().getModFileById(modId) != null) {
                    stop();
                    ci.cancel();
                    break;
                }
            }
        } catch (IOException ignored) {}
    }
}
