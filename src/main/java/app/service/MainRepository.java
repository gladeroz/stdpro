package app.service;

import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;

import app.repository.gims.SuiviGimsRepository;
import app.repository.gims.TraitementGimsRepository;
import app.repository.odr.CodeEligibleRepository;
import app.repository.odr.CsvRepository;
import app.repository.odr.TraitementOdrRepository;

public class MainRepository {
	@Getter
    private static volatile ConfigurableApplicationContext springContext;

	private static volatile TraitementOdrRepository traitementOdrRepository;
	private static volatile TraitementGimsRepository traitementGimsRepository;
	private static volatile CsvRepository csvRepository;
	private static volatile CodeEligibleRepository codeEligibleRepository;
	private static volatile SuiviGimsRepository suiviGimsRepository;

    public static void setSpringContext(ConfigurableApplicationContext springContext) {
		MainRepository.springContext = springContext;
	}

	public static synchronized TraitementOdrRepository getTraitementOdrRepository() {
		if(traitementOdrRepository == null) {
			traitementOdrRepository = springContext.getBean(TraitementOdrRepository.class);
		}
		return traitementOdrRepository;
	}

	public static synchronized TraitementGimsRepository getTraitementGimsRepository() {
		if(traitementGimsRepository == null) {
			traitementGimsRepository = springContext.getBean(TraitementGimsRepository.class);
		}
		return traitementGimsRepository;
	}

	public static synchronized CsvRepository getCsvRepository() {
		if(csvRepository == null) {
			csvRepository = springContext.getBean(CsvRepository.class);
		}
		return csvRepository;
	}

	public static synchronized CodeEligibleRepository getCodeEligibleRepository() {
		if(codeEligibleRepository == null) {
			codeEligibleRepository = springContext.getBean(CodeEligibleRepository.class);
		}
		return codeEligibleRepository;
	}

	public static synchronized SuiviGimsRepository getSuiviGimsRepository() {
		if(suiviGimsRepository == null) {
			suiviGimsRepository = springContext.getBean(SuiviGimsRepository.class);
		}
		return suiviGimsRepository;
	}
}

