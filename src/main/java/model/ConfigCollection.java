package model;

import java.util.Collection;

import enums.Job;

public class ConfigCollection {
	
	private Collection<ConfigItem> configComptagePdf;
	private Collection<ConfigItem> configSuffixPrefix;
	private Collection<ConfigItem> configOcr;
	private Collection<ConfigItem> configSendMail;
	private Collection<ConfigItem>  configExtractZone;

	public Collection<ConfigItem> getSpecificConfig(Job job){
		switch (job) {
		case COMPTAGE_PDF:
			return getConfigComptagePdf();
		case SUFFIX_PREFIX:
			return getConfigSuffixPrefix();
		case OCR:
			return getConfigOcr();
		case SEND_MAIL:
			return getConfigSendMail();
		case EXTRACT_ZONE:
			return getConfigExtractZone();
		default:
			return null;
		}
	}

	public Collection<ConfigItem> getConfigComptagePdf() {
		return configComptagePdf;
	}

	public void setConfigComptagePdf(Collection<ConfigItem> configComptagePdf) {
		this.configComptagePdf = configComptagePdf;
	}

	public Collection<ConfigItem> getConfigSuffixPrefix() {
		return configSuffixPrefix;
	}

	public void setConfigSuffixPrefix(Collection<ConfigItem> configSuffixPrefix) {
		this.configSuffixPrefix = configSuffixPrefix;
	}
	
	public Collection<ConfigItem> getConfigOcr() {
		return configOcr;
	}

	public void setConfigOcr(Collection<ConfigItem> configOcr) {
		this.configOcr = configOcr;
	}

	public Collection<ConfigItem> getConfigSendMail() {
		return configSendMail;
	}

	public void setConfigSendMail(Collection<ConfigItem> configSendMail) {
		this.configSendMail = configSendMail;
	}

	public Collection<ConfigItem> getConfigExtractZone() {
		return configExtractZone;
	}

	public void setConfigExtractZone(Collection<ConfigItem> configExtractZone) {
		this.configExtractZone = configExtractZone;
	}
}
