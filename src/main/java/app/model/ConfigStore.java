package app.model;

import java.util.List;

public class ConfigStore {
	private List<ConfigOdrJson> store;

	public List<ConfigOdrJson> getStore() {
		return store;
	}

	public void setStore(List<ConfigOdrJson> store) {
		this.store = store;
	}
}
