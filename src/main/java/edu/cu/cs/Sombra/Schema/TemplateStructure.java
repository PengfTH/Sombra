package edu.cu.cs.Sombra.Schema;

import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import info.debatty.java.stringsimilarity.Levenshtein;

public class TemplateStructure {
	
	public final double tagPathPara = 1;
	public final double vPathPara = 3;
	public final double vWeightPara = 5;
	public final double contentPara = 5;
	public final double simThreshold = (double) 5;
	
	private Levenshtein leven = new Levenshtein();

	
	public void pageAlign(String url1,String v1, String url2, String v2){
		PageStructure page1 = new PageStructure(url1, v1);
		PageStructure page2 = new PageStructure(url2, v2);
		DomTree domT1 = page1.getDomTree();
		DomTree domT2 = page2.getDomTree();
		Set<DomTreeNode> goodNodes1 = domT1.getGoodNodes();
		Set<DomTreeNode> goodNodes2 = domT2.getGoodNodes();
		for (DomTreeNode node1 : goodNodes1) {
			double simMax = -1;
			DomTreeNode peernode = null;
			int peers = 0;
			for (DomTreeNode node2 : goodNodes2) {
				double sim = this.similarity(node1, node2);
				if (sim > this.simThreshold){
					peers++;
				}
				if (sim > simMax) {
					simMax = sim;
					peernode = node2;
				}
			}
			//System.out.println(simMax);
			if (simMax > simThreshold) {
				System.out.println(node1.getTagPathString());
				System.out.println(peernode.getTagPathString());
				System.out.println(node1.getSRC());
				System.out.println(peernode.getSRC());
				System.out.println();
				
			}
			
			
		}
	}
	
	public double similarity(DomTreeNode node1, DomTreeNode node2) {
		double res = 0;
		double editD = this.leven.distance(node1.getTagPathString(), node2.getTagPathString());
		res += this.tagPathPara * Math.exp(-editD);
		if (node1.getVPath().equals(node2.getVPath())) {
			res += this.vPathPara;
		}
		res += this.vWeightPara * Math.exp(-Math.abs(node1.getVWeight() - node2.getVWeight()));
		editD = this.leven.distance(node1.getSRC(), node2.getSRC());
		res += this.contentPara * Math.exp(-editD);
		return res;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateStructure test = new TemplateStructure();
		test.pageAlign("amazon.html", "VIPSResult.xml", "amazon2.html", "VIPSResult2.xml");

	}

}
