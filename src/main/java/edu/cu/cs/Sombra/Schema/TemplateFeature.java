package edu.cu.cs.Sombra.Schema;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import info.debatty.java.stringsimilarity.Levenshtein;

public class TemplateFeature {
	public String tagPath;
	public String vPath;
	public double vWeight;
	public String id;
	public String content;
	
	public static final double tagPathPara = 1;
	public static final double vPathPara = 1;
	public static final double vWeightPara = 1;
	public static final double contentPara = 1;
	public static final double simThreshold = (double) 2;
	
	protected Levenshtein leven = new Levenshtein();

	
	public TemplateFeature(String tagPath, String vPath, double vWeight, String id, String content) {
		this.tagPath = tagPath;
		this.vPath = vPath;
		this.vWeight = vWeight;
		this.id = id;
		this.content = content;
	}
	
	public TemplateFeature(DomTreeNode node){
		this.tagPath = node.getTagPathString();
		this.vPath = node.getVPath();
		this.vWeight = node.getVWeight();
		this.id = node.getId();
		this.content = node.getContent();
	}

	
	public double similarity(DomTreeNode node) {
		double res = 0;

		if (!node.getId().isEmpty() && node.getId().equals(this.id)) {
			return 1000;
		}

		// Tag Path
		double editD = this.leven.distance(node.getTagPathString(), this.tagPath);
		res += this.tagPathPara * Math.exp(-editD / 8.0);

		// Visual Path
		if (node.getVPath().equals(this.vPath)) {
			res += this.vPathPara;
		}

		// Visual Weight
		res += this.vWeightPara * Math.exp(-Math.abs(node.getVWeight() - this.vWeight));
		
		//content
		editD = this.leven.distance(node.getContent(), this.content);
		res += this.contentPara * Math.exp(-editD / 2.0);
		
		return res;
	}
}
