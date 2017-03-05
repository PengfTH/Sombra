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
	private Map<DomTreeNode, VisualTreeNode> D2V;
	
	
	public PageStructure(String htmlfile, String vfile) {
		this.DomTree = new DomTree(htmlfile);
		VisualTreeParser parser = new VisualTreeParser();
		this.VTree = parser.parse(vfile);
		this.D2V = new HashMap<DomTreeNode, VisualTreeNode>();
		this.align();
	}
	
	private void align() {
		List<BaseTreeNode> domLeafNodes = this.DomTree.getNodes();
		List<BaseTreeNode> vLeafNodes = this.VTree.getLeafNodes();
		
		for (BaseTreeNode n : domLeafNodes) {
			//System.out.println(((DomTreeNode)n).getSRC().replaceAll("\\s", ""));
		}
		
		System.out.println("#####################");
		
		for (BaseTreeNode n : vLeafNodes) {
			//System.out.println(((VisualTreeNode)n).getSRC().replaceAll("\\s", ""));
		}		
		int count = 0;
		for (BaseTreeNode vNode : vLeafNodes) {
			String[] vSrcs = ((VisualTreeNode)vNode).getSRC().replaceAll("\\s", "").split("####");
			for (String s : vSrcs) {
				System.out.println(s);
			}
			for (BaseTreeNode domNode : domLeafNodes) {
				String dSrc = ((DomTreeNode)domNode).getSRC();
				dSrc = dSrc.replaceAll("\\s", "");
				for (String vSrc : vSrcs) {
					if (dSrc.equals(vSrc)) {
						this.D2V.put((DomTreeNode)domNode, (VisualTreeNode)vNode);
						count++;
					}
				}
			}
		}		
		System.out.println(count);
	}
	
	public Map<DomTreeNode, VisualTreeNode> getD2V() {
		return this.D2V;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageStructure test = new PageStructure("amazon.html", "VIPSResult.xml");
		System.out.println(test.getD2V().size());
		/*for (VisualTreeNode vnode : test.getV2D().keySet()) {
			System.out.println(vnode.getSRC());
		}
		for (BaseTreeNode node : test.DomTree.getLeafNodes()) {
			System.out.println(((DomTreeNode)node).getSRC());
		}*/
		
	}
}
