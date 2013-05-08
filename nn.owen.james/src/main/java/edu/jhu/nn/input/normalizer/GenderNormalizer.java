package edu.jhu.nn.input.normalizer;

public class GenderNormalizer implements Normalizer {

	@Override
	public String normalize(String data) {
		if("M".equals(data)){
			return "0";
		}
		else{
			return "1";
		}
	}

}
