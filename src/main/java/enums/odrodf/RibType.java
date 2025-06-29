package enums.odrodf;

import enums.OdrType;

public enum RibType {
	S(OdrType.S);

	public final OdrType output;

    private RibType(OdrType output) {
        this.output = output;
    }

	public OdrType getOutput() {
		return output;
	}
}
