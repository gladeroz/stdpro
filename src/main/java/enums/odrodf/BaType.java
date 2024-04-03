package enums.odrodf;

import enums.OdrType;

public enum BaType {
	S(OdrType.S),

	NV(OdrType.NV),

	NS(OdrType.NS),
	NS_RES(OdrType.NS),
	NS_ODR_HD(OdrType.NS),
	NS_ODF_HD(OdrType.NS),
	NS_ODF_AT(OdrType.NS),
	NS_NOT_ELI(OdrType.NS);

	public final OdrType output;

    private BaType(OdrType output) {
        this.output = output;
    }

	public OdrType getOutput() {
		return output;
	}
}
