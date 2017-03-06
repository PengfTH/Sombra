package edu.cu.cs.Sombra.Schema;

public class ValueNode extends TemplateFeature {
	public String content;
	
	public ValueNode(String tagPath, String vPath, double vWeight, String content) {
		super(tagPath, vPath, vWeight);
		this.content = content;
	}
	
	public void getNameNode() {
		
	}

}
