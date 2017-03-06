package edu.cu.cs.Sombra.Schema;

public class NameNode extends TemplateFeature{
	public String fieldName;
	
	public NameNode(String tagPath, String vPath, double vWeight, String content) {
		super(tagPath, vPath, vWeight);
		this.fieldName = content;
	}
}
