package app.service;

import org.springframework.context.ConfigurableApplicationContext;

import app.repository.CodeEligibleRepository;
import app.repository.CsvRepository;
import app.repository.TraitementRepository;

public class MainRepository {
	static private ConfigurableApplicationContext springContext;
	static private TraitementRepository traitementRepository;
	static private CsvRepository csvRepository;
	static private CodeEligibleRepository codeEligibleRepository;

	public ConfigurableApplicationContext getSpringContext() {
		return springContext;
	}
	public static void setSpringContext(ConfigurableApplicationContext springContext) {
		MainRepository.springContext = springContext;
	}
	public static TraitementRepository getTraitementRepository() {
		if(traitementRepository == null) {
			traitementRepository = springContext.getBean(TraitementRepository.class);
		}
		return traitementRepository;
	}
	public static CsvRepository getCsvRepository() {
		if(csvRepository == null) {
			csvRepository = springContext.getBean(CsvRepository.class);
		}
		return csvRepository;
	}
	public static CodeEligibleRepository getCodeEligibleRepository() {
		if(codeEligibleRepository == null) {
			codeEligibleRepository = springContext.getBean(CodeEligibleRepository.class);
		}
		return codeEligibleRepository;
	}
}
