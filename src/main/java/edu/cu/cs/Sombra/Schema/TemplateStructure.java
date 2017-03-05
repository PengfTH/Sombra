package edu.cu.cs.Sombra.Schema;

import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import info.debatty.java.stringsimilarity.Levenshtein;

public class TemplateStructure {
	
	public final float tagPathPara = 1;
	public final float vPathPara = 1;
	public final float vWeightPara = 1;
	public final float contentPara = 1;
	
	private Levenshtein leven = new Levenshtein();

	
	public void pageAlign(String url1,String v1, String url2, String v2){
		PageStructure page1 = new PageStructure(url1, v1);
		PageStructure page2 = new PageStructure(url2, v2);
		DomTree domT1 = page1.getDomTree();
		DomTree domT2 = page2.getDomTree();
		Set<DomTreeNode> goodNodes1 = domT1.getGoodNodes();
		Set<DomTreeNode> goodNodes2 = domT2.getGoodNodes();
		for (DomTreeNode node1 : goodNodes1) {
			float simMax = -1;
			DomTreeNode peernode;
			for (DomTreeNode node2 : goodNodes2) {
				float sim = this.similarity(node1, node2);
				if (sim > simMax) {
					simMax = sim;
					peernode = node2;
				}
			}
		}
	}
	
	public float similarity(DomTreeNode node1, DomTreeNode node2) {
		float res = 0;
		if (node1.getTagPathString().equals(node2.getTagPathString())) {
			res += this.tagPathPara;
		}
		if (node1.getVPath().equals(node2.getVPath())) {
			res += this.vPathPara;
		}
		res += this.vWeightPara * Math.exp(-Math.abs(node1.getVWeight() - node2.getVWeight()));
		float editD = (float)this.leven.distance(node1.getSRC(), node2.getSRC());
		res += this.contentPara * Math.exp(-editD);
		return res;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
