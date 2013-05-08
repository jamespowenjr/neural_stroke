package edu.jhu.nn.input.normalizer;

public class NumericalNormalizer implements Normalizer {

	private double basis;
	
	public NumericalNormalizer(double basis){
		this.basis = basis;
	}
	
	@Override
	public String normalize(String data) {
		return Double.parseDouble(data) / basis + "";
	}

}
