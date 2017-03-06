package edu.cu.cs.Sombra.Schema;

public abstract class TemplateFeature {
	public String tagPath;
	public String vPath;
	public double vWeight;
	
	public TemplateFeature(String tagPath, String vPath, double vWeight) {
		this.tagPath = tagPath;
		this.vPath = vPath;
		this.vWeight = vWeight;
	}
}
