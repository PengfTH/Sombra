package edu.cu.cs.Sombra.Schema;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class ValueNode extends TemplateFeature {
	public String content;
	
	public ValueNode(String tagPath, String vPath, double vWeight, String id, String content) {
		super(tagPath, vPath, vWeight, id);
		this.content = content;
	}
	
	public double similarity(DomTreeNode node) {
		double res = 0;
		res += super.similarity(node);
		
		//content
		double editD = this.leven.distance(node.getContent(), this.content);
		res += this.contentPara * Math.exp(-editD / 2.0);
		return res;
	}

}
