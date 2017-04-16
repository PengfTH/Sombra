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

			if (simMax > TemplateFeature.simThresholdName && tempNameNode.content.equals(peernode.getContent())) {
				page.nameNodes.add(peernode);
				matched.add(peernode);
				if (debug) {
					System.out.println("**************");
					tempNameNode.print();
					System.out.println("name peernode score: " + simMax);
					peernode.print();
				}
			} else {
				// page.nameNodes.clear();
				// return false;
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

			if (simMax > TemplateFeature.simThresholdValue) {
				page.valueNodes.add(peernode);
				matched.add(peernode);
				if (debug) {
					System.out.println("**************");
					tempValueNode.print();
					System.out.println("value peernode score: " + simMax);
					peernode.print();
				}
			} else {
				// page.nameNodes.clear();
				// page.valueNodes.clear();
				// return false;
			}

		}

		// potential name and value nodes	
		/*
		for (DomTreeNode node : pageNodes) {
			// compare with Template Name Nodes
			if (matched.contains(node)) {
				continue;
			}
			for (TemplateFeature tempName : temp.templateNameNodes) {
				if (tempName.tagPath.equals(node.getTagPathString())
						&& tempName.vPath.substring(0, tempName.vPath.lastIndexOf('-'))
								.equals(node.getVPath().substring(0, node.getVPath().lastIndexOf('-')))) {
					matched.add(node);
					page.nameNodes.add(node);
					break;
				}
			}

			// compare with Template Value Nodes
			if (matched.contains(node)) {
				continue;
			}
			for (TemplateFeature tempValue : temp.templateValueNodes) {
				if (tempValue.tagPath.equals(node.getTagPathString())
						&& tempValue.vPath.substring(0, tempValue.vPath.lastIndexOf('-'))
								.equals(node.getVPath().substring(0, node.getVPath().lastIndexOf('-')))) {
					matched.add(node);
					page.valueNodes.add(node);
					break;
				}
			}
			
		}
		*/
		

		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateStructure temp = new TemplateStructure();
		temp.pageAlign("1.html", "2.html");
		System.out.println("Template generated");
		
		System.out.println("*****************Template Name Nodes*****************");
		System.out.println(temp.templateNameNodes.size());
		for (TemplateFeature name : temp.templateNameNodes) {
			//name.print();
		}
		System.out.println("*****************Template Value Nodes*****************");
		System.out.println(temp.templateValueNodes.size());
		for (TemplateFeature value : temp.templateValueNodes) {
			//value.print();
		}
		
		SchemaAnalyzer analyzer = new SchemaAnalyzer();
		PageStructure page = new PageStructure("3.html");
		analyzer.debug = false;
		if (analyzer.analyze(page, temp)) {
			System.out.println("*****************Name Nodes*****************");
			System.out.println(page.nameNodes.size());
			for (DomTreeNode node : page.nameNodes) {
				//node.print();
			}
			System.out.println("*****************Value Nodes*****************");
			System.out.println(page.valueNodes.size());
			for (DomTreeNode node : page.valueNodes) {
				//node.print();
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
