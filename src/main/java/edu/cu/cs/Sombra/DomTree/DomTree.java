package edu.cu.cs.Sombra.DomTree;

import java.io.File;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.cu.cs.Sombra.Tree.BaseTreeNode;

public class DomTree {

	private DomTreeNode root;
	private int nodenum;
	private String title;

	public DomTree(String filename) {
		File input = new File(filename);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			this.title = doc.title();
			Element cur = doc.body();
			this.root = new DomTreeNode(null, cur.tagName(), cur.id(), cur.className(), cur.html());
			this.addChildrenNode(this.root, cur);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addChildrenNode(DomTreeNode curnode, Element cur) {
		if (cur.children() == null || cur.children().size() == 0) { 	
			return;
		}
		for (Element e : cur.children()) {
			DomTreeNode tmpnode = new DomTreeNode(curnode, e.tagName(), e.id(), e.className(), e.html());
			curnode.addChild((DomTreeNode)tmpnode);
			this.addChildrenNode(tmpnode, e);
		}
	}
	
	public void traverse() {
		this.root.printSubTree();
	}
	
	public List<BaseTreeNode> getLeafNodes() {
		return this.root.getLeafNodes();
	}
	
	public List<BaseTreeNode> getNodes() {
		return this.root.getNodes();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DomTree domtree = new DomTree("login.html");
		//domtree.traverse();
		for (BaseTreeNode node : domtree.root.getLeafNodes()) {
			((DomTreeNode)node).print();
		}

	}

}
