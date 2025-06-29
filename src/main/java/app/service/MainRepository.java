package app.service;

import org.springframework.context.ConfigurableApplicationContext;

import app.repository.gims.SuiviGimsRepository;
import app.repository.gims.TraitementGimsRepository;
import app.repository.odr.CodeEligibleRepository;
import app.repository.odr.CsvRepository;
import app.repository.odr.TraitementOdrRepository;

public class MainRepository {
	static private ConfigurableApplicationContext springContext;

	static private TraitementOdrRepository traitementOdrRepository;
	static private TraitementGimsRepository traitementGimsRepository;
	static private CsvRepository csvRepository;
	static private CodeEligibleRepository codeEligibleRepository;
	static private SuiviGimsRepository suiviGimsRepository;

	public ConfigurableApplicationContext getSpringContext() {
		return springContext;
	}
	public static void setSpringContext(ConfigurableApplicationContext springContext) {
		MainRepository.springContext = springContext;
	}
	public static TraitementOdrRepository getTraitementOdrRepository() {
		if(traitementOdrRepository == null) {
			traitementOdrRepository = springContext.getBean(TraitementOdrRepository.class);
		}
		return traitementOdrRepository;
	}
	public static TraitementGimsRepository getTraitementGimsRepository() {
		if(traitementGimsRepository == null) {
			traitementGimsRepository = springContext.getBean(TraitementGimsRepository.class);
		}
		return traitementGimsRepository;
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
	public static SuiviGimsRepository getSuiviGimsRepository() {
		if(suiviGimsRepository == null) {
			suiviGimsRepository = springContext.getBean(SuiviGimsRepository.class);
		}
		return suiviGimsRepository;
	}
}

