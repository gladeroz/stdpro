package model;

import java.util.ArrayList;

import enums.Options;
import enums.TypeInput;

public class ConfigItem {

	private Integer id;
	private String label;
	private TypeInput type;
	private String value;
	private String configName;
	private Boolean mandatory;
	
	private ArrayList<Options> options;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public TypeInput getType() {
		return type;
	}
	public void setType(TypeInput type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public ArrayList<Options> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<Options> options) {
		this.options = options;
	}
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
}
