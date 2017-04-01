package edu.cu.cs.Sombra.Schema;

import java.util.HashSet;
import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class SchemaAnalyzer {
	
	public SchemaAnalyzer(){
		
	}
	
	public boolean analyze(PageStructure page, TemplateStructure temp) {
		Set<DomTreeNode> pageNodes = page.getDomTree().getGoodNodes();
		Set<TemplateFeature> tempValueNodes = temp.templateValueNodes;
		Set<TemplateFeature> tempNameNodes  = temp.templateNameNodes;
		
		Set<DomTreeNode> matched = new HashSet<DomTreeNode>();
		for (TemplateFeature tempNameNode : tempNameNodes) {
			double simMax = -1;
			DomTreeNode peernode = null;
			for (DomTreeNode pageNode: pageNodes) {
				if (matched.contains(pageNode)) {
					continue;
				}
				double sim = tempNameNode.similarity(pageNode);

				if (sim > 999) {
					simMax = sim;
					peernode = pageNode;
					break;
				}
				if (sim > simMax) {
					simMax = sim;
					peernode = pageNode;
				}
			}
			
			if (simMax > TemplateFeature.simThreshold) {
				matched.add(peernode);
				if (tempNameNode.content.equals(peernode.getContent())) {
					page.nameNodes.add(peernode);
				} else {
					page.valueNodes.add(peernode);
				}
			} else { // strong assumption that page should strictly match template
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
		SchemaAnalyzer analyzer = new SchemaAnalyzer();
		PageStructure page = new PageStructure("3.html");
		analyzer.analyze(page, temp);
	}

}
