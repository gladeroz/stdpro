package app.model;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import enums.Job;

@Getter
@Setter
public class ConfigCollection {

	private Collection<ConfigItem> configComptagePdf;
	private Collection<ConfigItem> configSuffixPrefix;
	private Collection<ConfigItem> configOcr;
	private Collection<ConfigItem> configCodeBarre;
	private Collection<ConfigItem> configSendMail;
	private Collection<ConfigItem> configExtractZone;
	private Collection<ConfigItem> configOdr;
	private Collection<ConfigItem> configGims;

	public Collection<ConfigItem> getSpecificConfig(Job job){
        return switch (job) {
            case COMPTAGE_PDF -> getConfigComptagePdf();
            case SUFFIX_PREFIX -> getConfigSuffixPrefix();
            case OCR -> getConfigOcr();
            case SEND_MAIL -> getConfigSendMail();
            case EXTRACT_ZONE -> getConfigExtractZone();
            case CODE_BARRE -> getConfigCodeBarre();
            case ODR -> getConfigOdr();
            case GIMS -> getConfigGims();
            default -> null;
        };
	}
}
