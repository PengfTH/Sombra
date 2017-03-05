package edu.cu.cs.Sombra.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import edu.cu.cs.Sombra.Tree.BaseTreeNode;
import edu.cu.cs.Sombra.VisualTree.VisualTree;
import edu.cu.cs.Sombra.VisualTree.VisualTreeNode;
import edu.cu.cs.Sombra.VisualTree.VisualTreeParser;

public class PageStructure {
	
	private VisualTree VTree;
	private DomTree DomTree;
	//private Map<DomTreeNode, VisualTreeNode> D2V;
	
	
	public PageStructure(String htmlfile, String vfile) {
		this.DomTree = new DomTree(htmlfile);
		VisualTreeParser parser = new VisualTreeParser();
		this.VTree = parser.parse(vfile);
		this.treeAlign();
	}
	
	private void treeAlign() {
		List<BaseTreeNode> domLeafNodes = this.DomTree.getNodes();
		List<BaseTreeNode> vLeafNodes = this.VTree.getLeafNodes();
		for (BaseTreeNode domNode : domLeafNodes) {
			int sombraid = ((DomTreeNode) domNode).getSombraid();
			for (BaseTreeNode vNode : vLeafNodes) {
				List<Integer> list = ((VisualTreeNode) vNode).getSombraIds();
				if (list.contains(sombraid)) {
					((DomTreeNode) domNode).setVPath(((VisualTreeNode)vNode).getID());
					((DomTreeNode) domNode).setVWeight(((VisualTreeNode)vNode).getRectHeight() * ((VisualTreeNode)vNode).getRectWidth());
					this.DomTree.addGoodNodes((DomTreeNode) domNode);
					break;
				}
			}
		}		
	}
	
	public DomTree getDomTree() {
		return this.DomTree;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageStructure test = new PageStructure("amazon.html", "VIPSResult.xml");
		//test.getDomTree().traverse();
		/*for (VisualTreeNode vnode : test.getV2D().keySet()) {
			System.out.println(vnode.getSRC());
		}
		for (BaseTreeNode node : test.DomTree.getLeafNodes()) {
			System.out.println(((DomTreeNode)node).getSRC());
		}*/
		
		
	}
}
