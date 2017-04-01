package edu.cu.cs.Sombra.Schema;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class NameNode extends TemplateFeature{
	public String fieldName;
	
	public NameNode(String tagPath, String vPath, double vWeight, String id, String fieldName) {
		super(tagPath, vPath, vWeight, id);
		this.fieldName = fieldName;
	}
	
	public double similarity(DomTreeNode node) {
		double res = 0;
		res += super.similarity(node);
		
		//content
		double editD = this.leven.distance(node.getContent(), this.fieldName);
		res += this.contentPara * Math.exp(-editD / 2.0);
		return res;
	}
}
