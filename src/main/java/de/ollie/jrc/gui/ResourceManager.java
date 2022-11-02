package de.ollie.jrc.gui;

import static de.ollie.jrc.util.Checks.ensure;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import de.ollie.jrc.logger.Logger;
import lombok.Getter;

public class ResourceManager {

	private static final Logger LOGGER = Logger.getLogger(ResourceManager.class.getSimpleName());

	@Getter
	public enum Localization {

		DE("de"),
		EN("en");

		private String token;

		private Localization(String token) {
			this.token = token;
		}
	}

	public static final ResourceManager INSTANCE = new ResourceManager();

	private static final String TOKEN_PLACEHOLDER = "${language}";

	private Map<Localization, Properties> resources = new HashMap<>();

	public ResourceManager() {
		this(
				System.getProperty("resource.localization.files.dir", "src/main/resources/localization/"),
				System
				.getProperty(
						"resource.file.name.pattern",
								"jasperreportscleaner_" + TOKEN_PLACEHOLDER + "_resources.properties"));
	}

	public ResourceManager(String localizationFilesDir, String fileNamePattern) {
		for (Localization localization : Localization.values()) {
			loadResourceFile(localizationFilesDir, fileNamePattern.replace(TOKEN_PLACEHOLDER, localization.getToken()))
					.ifPresent(properties -> resources.put(localization, properties));
		}
	}

	private Optional<Properties> loadResourceFile(String localizationFilesDir, String fileName) {
		String path = completePath(changeToSlashes(localizationFilesDir)) + changeToSlashes(fileName);
		try (FileReader fileReader = new FileReader(path)) {
			Properties properties = new Properties();
			properties.load(fileReader);
			return Optional.of(properties);
		} catch (Exception e) {
			System.out.println(path);
			LOGGER.error("Something went wrong while reading resource file: " + path, e);
		}
		return Optional.empty();
	}

	private String completePath(String path) {
		return path + (!path.endsWith("/") ? "/" : "");
	}

	private String changeToSlashes(String path) {
		return path.replace("\\", "/");
	}

	public String getString(Localization localization, String resourceId) {
		ensure(localization != null, "localization cannot be null.");
		ensure(resourceId != null, "resource id cannot be null.");
		Properties properties = resources.get(localization);
		if (properties == null) {
			return resourceId;
		}
		return properties.getProperty(resourceId, resourceId);
	}

}
