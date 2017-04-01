package edu.cu.cs.Sombra.Schema;

import java.util.Set;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

public class SchemaAnalyzer {
	
	public SchemaAnalyzer(){
		
	}
	
	public boolean analyze(PageStructure page, TemplateStructure temp) {
		Set<DomTreeNode> pageNodes = page.getDomTree().getGoodNodes();
		Set<TemplateFeature> tempValueNodes = temp.templateValueNodes;
		
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
