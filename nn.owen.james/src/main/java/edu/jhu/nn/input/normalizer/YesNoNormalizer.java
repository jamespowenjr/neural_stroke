package edu.jhu.nn.input.normalizer;

public class YesNoNormalizer implements Normalizer {

	@Override
	public String normalize(String data) {
		if("Y".equals(data)){
			return "1";
		}
		else if("N".equals(data)){
			return "0.5";
		}
		else{
			return "0";
		}
	}

}
