package edu.cu.cs.Sombra.Schema;

import java.util.Objects;

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
	public static final double simThresholdName = (double) 1.9;
	public static final double simThresholdValue = (double) 0.9;
	
	protected Levenshtein leven = new Levenshtein();

	
	public TemplateFeature(String tagPath, String vPath, double vWeight, String id, String content) {
		this.tagPath = tagPath;
		this.vPath = vPath;
		this.vWeight = vWeight;
		this.id = id;
		this.content = content;
	}
	
	public void print() {
		System.out.println("tagpath: " + this.tagPath);
		System.out.println("id: " + this.id);
		System.out.println("vPath: " + this.vPath);
		System.out.println("vWeight: " + this.vWeight);
		System.out.println("Content: " + this.content);
		System.out.println();		
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
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TemplateFeature))
			return false;
		TemplateFeature of = (TemplateFeature) other;
		if (tagPath.equals(of.tagPath)) {
			if (vWeight == of.vWeight) {
				if (id.equals(of.id)) {
					if (content.equals(of.content)) {
						return true;
					}
				}
			}
		}
		if (tagPath == of.tagPath && vWeight == of.vWeight && id.equals(of.id)
				&& content.equals(of.content))
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(vPath, vWeight, tagPath, id, content);
	}
	
	
}
