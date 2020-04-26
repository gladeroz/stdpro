package traitement;

import java.util.Collection;

import enums.LogLevel;
import model.ConfigItem;
import traitement.config.CustomConfigSuffixe;
import utils.Logger;

public class SuffixePrefixe {
	public static CustomConfigSuffixe initConfig(Collection<ConfigItem> config) {
		return null;
	}

	public static void traitement(Collection<ConfigItem> config) {
		Logger.print(LogLevel.INFO, "Traitement 'Suffixe/Prefixe' en cours");

		Logger.print(LogLevel.DEBUG, "Configuration en cours de traitement");
		CustomConfigSuffixe conf = initConfig(config);

		Logger.print(LogLevel.DEBUG, "Lancement du Traitement");
		job(conf);
	}
	
	public static void job(CustomConfigSuffixe config) {
		long startTime = System.nanoTime();
		long endTime = System.nanoTime();

		Logger.print(LogLevel.INFO, "Temps de Traiment :" + (endTime - startTime));
	}
}
