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
		Set<NameNode> tempNameNodes  = temp.templateNameNodes;
		
		Set<DomTreeNode> matched = new HashSet<DomTreeNode>();
		for (NameNode tempNameNode : tempNameNodes) {
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
			
			
		}
		
		return false;
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
