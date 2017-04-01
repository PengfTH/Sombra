package edu.cu.cs.Sombra.Schema;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import info.debatty.java.stringsimilarity.Levenshtein;

public class TemplateStructure {

	public final double tagPathPara = 1;
	public final double vPathPara = 1;
	public final double vWeightPara = 1;
	public final double contentPara = 1;
	public final double simThreshold = (double) 2;

	private Levenshtein leven = new Levenshtein();

	public Set<NameNode> templateNameNodes = new HashSet<NameNode>();
	public Set<TemplateFeature> templateValueNodes = new HashSet<TemplateFeature>();

	public void pageAlign(String url1, String url2) {
		PageStructure page1 = new PageStructure(url1);
		PageStructure page2 = new PageStructure(url2);
		DomTree domT1 = page1.getDomTree();
		DomTree domT2 = page2.getDomTree();
		Set<DomTreeNode> goodNodes1 = domT1.getGoodNodes();
		Set<DomTreeNode> goodNodes2 = domT2.getGoodNodes();
		for (DomTreeNode node1 : goodNodes1) {
			double simMax = -1;
			DomTreeNode peernode = null;
			int peers = 0;
			for (DomTreeNode node2 : goodNodes2) {
				// TODO: one-to-one peer nodes
				double sim = this.similarity(node1, node2);

				if (sim > 999) {
					simMax = sim;
					peernode = node2;
					break;
				}

				if (sim > this.simThreshold) {
					peers++;
				}
				if (sim > simMax) {
					simMax = sim;
					peernode = node2;
				}
			}

			if (simMax > simThreshold) {
				// Name Node
				if (node1.getContent().equals(peernode.getContent())) {
					NameNode nameNode = new NameNode(node1.getTag(), node1.getVPath(),
							0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getContent());
					this.templateNameNodes.add(nameNode);
				}
				// Value Node
				else {
					ValueNode valueNode = new ValueNode(node1.getTag(), node1.getVPath(),
							0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getContent());
					this.templateValueNodes.add((TemplateFeature) valueNode);
				}
				/*
				System.out.println(node1.getTagPathString());
				System.out.println(peernode.getTagPathString());
				System.out.println(node1.getContent());
				System.out.println(peernode.getContent());
				System.out.println(node1.getVPath());
				System.out.println(peernode.getVPath());
				System.out.println(node1.getVWeight());
				System.out.println(peernode.getVWeight());
				System.out.println();
				**/
			}

		}

	}
	

	public double similarity(DomTreeNode node1, DomTreeNode node2) {
		double res = 0;

		if (!node1.getId().isEmpty() && node1.getId().equals(node2.getId())) {
			return 1000;
		}

		// Tag Path
		double editD = this.leven.distance(node1.getTagPathString(), node2.getTagPathString());
		res += this.tagPathPara * Math.exp(-editD / 8.0);

		// System.out.println(node1.getTagPathString());
		// System.out.println(node2.getTagPathString());
		// System.out.println(res);

		// Visual Path
		if (node1.getVPath().equals(node2.getVPath())) {
			res += this.vPathPara;
		}

		// Visual Weight
		res += this.vWeightPara * Math.exp(-Math.abs(node1.getVWeight() - node2.getVWeight()));

		// Content
		editD = this.leven.distance(node1.getContent(), node2.getContent());
		res += this.contentPara * Math.exp(-editD / 2.0);
		return res;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateStructure test = new TemplateStructure();
		test.pageAlign("1.html", "2.html");
	}

}
