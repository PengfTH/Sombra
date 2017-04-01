package edu.cu.cs.Sombra.Schema;

import java.util.HashSet;
import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class TemplateStructure {

	public Set<TemplateFeature> templateNameNodes = new HashSet<TemplateFeature>();
	public Set<TemplateFeature> templateValueNodes = new HashSet<TemplateFeature>();

	public void pageAlign(String url1, String url2) {
		PageStructure page1 = new PageStructure(url1);
		PageStructure page2 = new PageStructure(url2);
		DomTree domT1 = page1.getDomTree();
		DomTree domT2 = page2.getDomTree();
		Set<DomTreeNode> goodNodes1 = domT1.getGoodNodes();
		Set<DomTreeNode> goodNodes2 = domT2.getGoodNodes();
		Set<DomTreeNode> matched = new HashSet<DomTreeNode>();
		for (DomTreeNode node1 : goodNodes1) {
			double simMax = -1;
			DomTreeNode peernode = null;
			for (DomTreeNode node2 : goodNodes2) {
				// one-to-one peer nodes
				if (matched.contains(node2)) {
					continue;
				}
				double sim = this.similarity(node1, node2);
				if (sim > 999) {
					simMax = sim;
					peernode = node2;
					break;
				}

				if (sim > simMax) {
					simMax = sim;
					peernode = node2;
				}
			}

			if (simMax > TemplateFeature.simThreshold) {
				matched.add(peernode);
				// Name Node
				if (node1.getContent().equals(peernode.getContent())) {
					TemplateFeature nameNode = new TemplateFeature(node1.getTag(), node1.getVPath(),
							0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getId(), node1.getContent());
					this.templateNameNodes.add(nameNode);
				}
				// Value Node
				else {
					TemplateFeature valueNode = new TemplateFeature(node1.getTag(), node1.getVPath(),
							0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getId(), null);
					this.templateValueNodes.add(valueNode);
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
		TemplateFeature nameNode1 = new TemplateFeature(node1.getTag(), node1.getVPath(),
				node1.getVWeight(), node1.getId(), node1.getContent());
		return nameNode1.similarity(node2);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateStructure test = new TemplateStructure();
		test.pageAlign("1.html", "2.html");
	}

}
