package model;

import java.util.List;

public class ConfigExtractArea {
	private List<String> line;

	public ConfigExtractArea(List<String> line) {
		super();
		this.setLine(line);
	}

	public List<String> getLine() {
		return line;
	}

	public void setLine(List<String> line) {
		this.line = line;
	}
}
