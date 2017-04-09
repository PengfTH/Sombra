package edu.cu.cs.Sombra.Schema;

import java.util.HashSet;
import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class SchemaAnalyzer {

	public SchemaAnalyzer() {

	}

	public boolean analyze(PageStructure page, TemplateStructure temp) {
		Set<DomTreeNode> pageNodes = page.getDomTree().getGoodNodes();
		Set<TemplateFeature> tempValueNodes = temp.templateValueNodes;
		Set<TemplateFeature> tempNameNodes = temp.templateNameNodes;

		Set<DomTreeNode> matched = new HashSet<DomTreeNode>();

		// match name nodes
		for (TemplateFeature tempNameNode : tempNameNodes) {
			//System.out.println("**************");
			//tempNameNode.print();

			double simMax = -1;
			DomTreeNode peernode = null;
			for (DomTreeNode pageNode : pageNodes) {
				if (matched.contains(pageNode)) {
					continue;
				}
				double sim = tempNameNode.similarity(pageNode);
				if (sim > simMax) {
					simMax = sim;
					peernode = pageNode;
					if (sim > 999) {
						break;
					}
				}
			}
			
			//System.out.println("name peernode score: " + simMax);
			//peernode.print();
			
			
			

			if (simMax > TemplateFeature.simThreshold && tempNameNode.content.equals(peernode.getContent())) {
				page.nameNodes.add(peernode);
				matched.add(peernode);
			} else {
				page.nameNodes.clear();
				return false;
			}
		}

		// match value nodes
		for (TemplateFeature tempValueNode : tempValueNodes) {
			//System.out.println("**************");
			//tempValueNode.print();
			
			double simMax = -1;
			DomTreeNode peernode = null;
			for (DomTreeNode pageNode : pageNodes) {
				if (matched.contains(pageNode)) {
					continue;
				}
				double sim = tempValueNode.similarity(pageNode);
				if (sim > simMax) {
					simMax = sim;
					peernode = pageNode;
					if (sim > 999) {
						break;
					}
				}
			}
			
			//System.out.println("value peernode score: " + simMax);
			//peernode.print();
			
			

			if (simMax > TemplateFeature.simThreshold) {
				page.valueNodes.add(peernode);
				matched.add(peernode);
			} else {
				page.nameNodes.clear();
				page.valueNodes.clear();
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateStructure temp = new TemplateStructure();
		temp.pageAlign("1.html", "2.html");
		System.out.println("Template generated");
		SchemaAnalyzer analyzer = new SchemaAnalyzer();
		PageStructure page = new PageStructure("3.html");
		if (analyzer.analyze(page, temp)) {
			System.out.println("Name Nodes");
			for (DomTreeNode node : page.nameNodes) {
				node.print();
			}
			System.out.println("Value Nodes");
			for (DomTreeNode node : page.valueNodes) {
				node.print();
			}
		} else {
			System.out.println("No match");
		}
	}

}
