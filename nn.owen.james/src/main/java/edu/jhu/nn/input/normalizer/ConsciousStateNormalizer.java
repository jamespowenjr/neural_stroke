package edu.jhu.nn.input.normalizer;

public class ConsciousStateNormalizer implements Normalizer {

	@Override
	public String normalize(String data) {
		if("F".equals(data)){
			return "1";
		}
		else if("D".equals(data)){
			return "0.5";
		}
		else{
			return "0";
		}
	}

}
