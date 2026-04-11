package app.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigStore {
	private List<ConfigOdrJson> store;
}
