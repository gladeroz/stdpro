package enums.odrodf;

import enums.OdrType;

public enum FactType {
	S(OdrType.S),
	
	NV(OdrType.NV),
	
	NS(OdrType.NS);
	
	public final OdrType output;
	 
    private FactType(OdrType output) {
        this.output = output;
    }

	public OdrType getOutput() {
		return output;
	}
}
