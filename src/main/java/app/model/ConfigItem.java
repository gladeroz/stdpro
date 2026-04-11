package app.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import enums.Options;
import enums.TypeInput;

@Getter
@Setter
public class ConfigItem {

	private Integer id;
	private String label;
	private TypeInput type;
	private String value;
	private String configName;
	private Boolean mandatory;
	private ArrayList<Options> options;
}
