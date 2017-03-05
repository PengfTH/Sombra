package edu.cu.cs.Sombra.Schema;

import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import edu.cu.cs.Sombra.Tree.BaseTreeNode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.VisualTree.VisualTree;
import edu.cu.cs.Sombra.VisualTree.VisualTreeNode;
import edu.cu.cs.Sombra.VisualTree.VisualTreeParser;

public class PageStructure {
	
	private VisualTree VTree;
	private DomTree DomTree;
	private Map<VisualTreeNode, DomTreeNode> V2D;
	
	
	public PageStructure(String htmlfile, String vfile) {
		this.DomTree = new DomTree(htmlfile);
		VisualTreeParser parser = new VisualTreeParser();
		this.VTree = parser.parse(vfile);
		this.V2D = new HashMap<VisualTreeNode, DomTreeNode>();
		this.align();
	}
	
	private void align() {
		List<BaseTreeNode> domLeafNodes = this.DomTree.getLeafNodes();
		List<BaseTreeNode> vLeafNodes = this.VTree.getLeafNodes();
		
		for (BaseTreeNode n : domLeafNodes) {
			//System.out.println(((DomTreeNode)n).getSRC().replaceAll("\\s", ""));
		}
		
		System.out.println("#####################");
		
		for (BaseTreeNode n : vLeafNodes) {
			//System.out.println(((VisualTreeNode)n).getSRC().replaceAll("\\s", ""));
		}		
		
		for (BaseTreeNode vNode : vLeafNodes) {
			String vSrc = ((VisualTreeNode)vNode).getSRC();
			for (BaseTreeNode domNode : domLeafNodes) {
				String dSrc = ((DomTreeNode)domNode).getSRC();
				vSrc = vSrc.replaceAll("\\s", "");
				dSrc = dSrc.replaceAll("\\s", "");
				if (vSrc.equals(dSrc)) {
					this.V2D.put((VisualTreeNode)vNode, (DomTreeNode)domNode);
				}
			}
		}		
	}
	
	public Map<VisualTreeNode, DomTreeNode> getV2D() {
		return this.V2D;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageStructure test = new PageStructure("amazon.html", "VIPSResult.xml");
		System.out.println(test.getV2D().size());
		/*for (VisualTreeNode vnode : test.getV2D().keySet()) {
			System.out.println(vnode.getSRC());
		}
		for (BaseTreeNode node : test.DomTree.getLeafNodes()) {
			System.out.println(((DomTreeNode)node).getSRC());
		}*/
		
	}
}
