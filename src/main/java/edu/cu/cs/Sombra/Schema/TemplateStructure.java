package edu.cu.cs.Sombra.Schema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		List<DomTreeNode> goodNodes1 = domT1.getGoodNodes();
		List<DomTreeNode> goodNodes2 = domT2.getGoodNodes();

		Set<DomTreeNode> matched1 = new HashSet<DomTreeNode>();
		Set<DomTreeNode> matched2 = new HashSet<DomTreeNode>();

		Map<DomTreeNode, DomTreeNode> peermap12 = new HashMap<DomTreeNode, DomTreeNode>();
		Map<DomTreeNode, DomTreeNode> peermap21 = new HashMap<DomTreeNode, DomTreeNode>();

		// One-to-one match

		for (DomTreeNode node1 : goodNodes1) {
			double simMax = -1;
			DomTreeNode peernode = null;
			for (DomTreeNode node2 : goodNodes2) {
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
			
			peermap12.put(node1, peernode);
		}
		
		for (DomTreeNode node2 : goodNodes2) {
			double simMax = -1;
			DomTreeNode peernode = null;
			for (DomTreeNode node1 : goodNodes1) {
				double sim = this.similarity(node1, node2);

				if (sim > 999) {
					simMax = sim;
					peernode = node1;
					break;
				}

				if (sim > simMax) {
					simMax = sim;
					peernode = node1;
				}
			}
			peermap21.put(node2, peernode);
		}

		for (DomTreeNode node1 : goodNodes1) {
			DomTreeNode peernode = peermap12.get(node1);
			if (peermap21.get(peernode).equals(node1)) {
				double simMax = this.similarity(node1, peernode);
				if (simMax > TemplateFeature.simThresholdName) {
					matched1.add(node1);
					matched2.add(peernode);
					// Name Node
					if (node1.getContent().equals(peernode.getContent())) {
						TemplateFeature nameNode = new TemplateFeature(node1.getTagPathString(), node1.getVPath(),
								0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getId(), node1.getContent());
						this.templateNameNodes.add(nameNode);
					}
					// Value Node
					else {
						TemplateFeature valueNode = new TemplateFeature(node1.getTagPathString(), node1.getVPath(),
								0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getId(), node1.getContent());
						this.templateValueNodes.add(valueNode);
					}

				}
			}
		}

		for (DomTreeNode node1 : goodNodes1) {
			if (matched1.contains(node1)) {
				continue;
			}

			double simMax = -1;
			DomTreeNode peernode = null;

			if (!matched2.contains(peermap12.get(node1))) {
				peernode = peermap12.get(node1);
				simMax = this.similarity(node1, peernode);
			} 
			else {
				for (DomTreeNode node2 : goodNodes2) {
					// System.out.println(node1.getContent() + " ### " +
					// node2.getContent());
					// one-to-one peer nodes
					if (matched2.contains(node2)) {
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
			}

			if (simMax > TemplateFeature.simThresholdValue) {
				matched1.add(node1);
				matched2.add(peernode);
				// Name Node
				if (node1.getContent().equals(peernode.getContent())) {
					TemplateFeature nameNode = new TemplateFeature(node1.getTagPathString(), node1.getVPath(),
							0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getId(), node1.getContent());
					this.templateNameNodes.add(nameNode);
				}
				// Value Node
				else {
					TemplateFeature valueNode = new TemplateFeature(node1.getTagPathString(), node1.getVPath(),
							0.5 * (node1.getVWeight() + peernode.getVWeight()), node1.getId(), node1.getContent());
					this.templateValueNodes.add(valueNode);
				}

			}

		}
		// this.refine(page1, page2);
	}

	private void refine(PageStructure page1, PageStructure page2) {
		SchemaAnalyzer analyzer = new SchemaAnalyzer();
		analyzer.analyze(page1, this);
		analyzer.analyze(page2, this);
		Set<DomTreeNode> temp = new HashSet<DomTreeNode>();

		Set<DomTreeNode> matched1 = page1.value2name();
		Set<DomTreeNode> matched2 = page2.value2name();
		temp.addAll(matched1);
		temp.addAll(matched2);
		this.templateNameNodes.clear();
		for (DomTreeNode node : temp) {
			TemplateFeature nameNode = new TemplateFeature(node);
			this.templateNameNodes.add(nameNode);
		}

		Set<DomTreeNode> value1 = page1.V2N.keySet();
		Set<DomTreeNode> value2 = page2.V2N.keySet();
		temp.clear();
		temp.addAll(value1);
		temp.addAll(value2);
		this.templateValueNodes.clear();
		for (DomTreeNode node : temp) {
			TemplateFeature valueNode = new TemplateFeature(node);
			this.templateValueNodes.add(valueNode);
		}
	}

	public double similarity(DomTreeNode node1, DomTreeNode node2) {
		TemplateFeature nameNode1 = new TemplateFeature(node1);
		return nameNode1.similarity(node2);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateStructure test = new TemplateStructure();
		test.pageAlign("1.html", "2.html");
	}

}
