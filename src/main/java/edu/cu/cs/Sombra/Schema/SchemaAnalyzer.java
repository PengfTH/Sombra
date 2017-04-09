package edu.cu.cs.Sombra.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class SchemaAnalyzer {
	public boolean debug = false;
	public SchemaAnalyzer() {

	}

	public boolean analyze(PageStructure page, TemplateStructure temp) {
		List<DomTreeNode> pageNodes = page.getDomTree().getGoodNodes();
		Set<TemplateFeature> tempValueNodes = temp.templateValueNodes;
		Set<TemplateFeature> tempNameNodes = temp.templateNameNodes;
		
		if (debug) 
			System.out.println("Temp Name Node size: " + tempNameNodes.size());
		

		Set<DomTreeNode> matched = new HashSet<DomTreeNode>();
		
		// match name nodes
		for (TemplateFeature tempNameNode : tempNameNodes) {
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
				
			

			if (simMax > TemplateFeature.simThreshold && tempNameNode.content.equals(peernode.getContent())) {
				page.nameNodes.add(peernode);
				matched.add(peernode);
				if (debug) {
					System.out.println("**************");
					tempNameNode.print();
					System.out.println("name peernode score: " + simMax);
					peernode.print();	
				}
			} else {
				//page.nameNodes.clear();
				//return false;
			}
		}

		// match value nodes
		for (TemplateFeature tempValueNode : tempValueNodes) {
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
			
			
			
			

			if (simMax > TemplateFeature.simThreshold) {
				page.valueNodes.add(peernode);
				matched.add(peernode);
				if (debug) {
					System.out.println("**************");
					tempValueNode.print();
					System.out.println("value peernode score: " + simMax);
					peernode.print();
				}
			} else {
				//page.nameNodes.clear();
				//page.valueNodes.clear();
				//return false;
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
		TemplateFeature f1 = null;
		TemplateFeature f2 = null;
		for (TemplateFeature tempNameNode : temp.templateNameNodes) {
			//tempNameNode.print();
			if (tempNameNode.hashCode() == 2122142350) {
				if (f1 == null)
					f1 = tempNameNode;
				else
					f2 = tempNameNode;
			}
		}
		System.out.println(f1.equals(f2));
		analyzer.debug = true;
		if (analyzer.analyze(page, temp)) {
			System.out.println("Name Nodes: ");
			System.out.println(page.nameNodes.size());
			for (DomTreeNode node : page.nameNodes) {
				node.print();
			}
			System.out.println("Value Nodes");
			System.out.println(page.valueNodes.size());
			for (DomTreeNode node : page.valueNodes) {
				node.print();
			}
		} else {
			System.out.println("No match");
		}
		page.value2name();
		for (DomTreeNode node : page.V2N.keySet()) {
			System.out.println(page.V2N.get(node) + " : " + node.getContent());
		}
	}

}
