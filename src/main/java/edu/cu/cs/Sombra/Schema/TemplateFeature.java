package edu.cu.cs.Sombra.Schema;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import info.debatty.java.stringsimilarity.Levenshtein;

public class TemplateFeature {
	public String tagPath;
	public String vPath;
	public double vWeight;
	public String id;
	public String fieldName;
	
	public static final double tagPathPara = 1;
	public static final double vPathPara = 1;
	public static final double vWeightPara = 1;
	public static final double contentPara = 1;
	public static final double simThreshold = (double) 2;
	
	protected Levenshtein leven = new Levenshtein();

	
	public TemplateFeature(String tagPath, String vPath, double vWeight, String id, String fieldName) {
		this.tagPath = tagPath;
		this.vPath = vPath;
		this.vWeight = vWeight;
		this.id = id;
		this.fieldName = fieldName;
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
		
		return res;
	}
}
