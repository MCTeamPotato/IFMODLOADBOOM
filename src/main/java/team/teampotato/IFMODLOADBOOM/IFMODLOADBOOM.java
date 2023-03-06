package team.teampotato.IFMODLOADBOOM;

import com.google.gson.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Path;


public class IFMODLOADBOOM implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ifmodloadboom");

	@Override
	public void onInitialize() {
		/*
		部分-1
		JSON生成
		 */
		File configDirectory = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ifmodloadboomConfig");

		if (!configDirectory.exists()) {
			configDirectory.mkdirs();
		}
		File configFile = new File(configDirectory, "IFMODLOADBOOM.json");
		if (!configFile.exists()) {
			try {
				JsonObject defaultConfig = new JsonObject();
				JsonArray modList = new JsonArray();
				modList.add("BlackMod-ID");
				defaultConfig.add("BoomModlist", modList);
				FileWriter writer = new FileWriter(configFile);
				writer.write(defaultConfig.toString());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

/*
部分-2
读取黑名单并且崩溃
 */
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("ifmodloadboomConfig");
		File configFile2 = new File(configPath.toFile(), "IFMODLOADBOOM.json");

		if (configFile2.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
				JsonObject config = new JsonParser().parse(reader).getAsJsonObject();
				JsonArray modList = config.getAsJsonArray("BoomModlist");
				for (JsonElement modElement : modList) {
					String modId = modElement.getAsString();
					if (FabricLoader.getInstance().isModLoaded(modId)) {
						LOGGER.info("Stopping Minecraft because " + modId + " is loaded");
						MinecraftClient.getInstance().stop();
						break;
					}
				}
			} catch (IOException e) {
				LOGGER.error("Failed to read config file", e);
			}
		}

		LOGGER.info("hElLo!iFmOdLoAdBoOmMoD! mOdLoAdEr!");
	}
}
