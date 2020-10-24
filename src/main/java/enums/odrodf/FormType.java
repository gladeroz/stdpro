package enums.odrodf;

import enums.OdrType;

public enum FormType {
	S(OdrType.S),
	
	NV(OdrType.NV),
	
	NS_FORM1(OdrType.NS),
	NS_FORM2(OdrType.NS);
	
	public final OdrType output;
	 
    private FormType(OdrType output) {
        this.output = output;
    }
    
	public OdrType getOutput() {
		return output;
	}
}
