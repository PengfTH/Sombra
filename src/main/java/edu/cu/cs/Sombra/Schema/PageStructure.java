package edu.cu.cs.Sombra.Schema;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.VisualTree.VisualTree;
import edu.cu.cs.Sombra.VisualTree.VisualTreeNode;
import edu.cu.cs.Sombra.VisualTree.VisualTreeParser;

public abstract class PageStructure {
	
	private VisualTree VTree;
	private DomTree DomTree;
	private Map<VisualTreeNode, DomTreeNode> V2D;
	
	
	public PageStructure(String htmlfile, String vfile) {
		File htmlin = new File(htmlfile);
		this.DomTree = new DomTree(htmlfile);
		VisualTreeParser parser = new VisualTreeParser();
		this.VTree = parser.parse(vfile);
		this.V2D = new HashMap<VisualTreeNode, DomTreeNode>();
		this.align();
	}
	
	private void align() {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
