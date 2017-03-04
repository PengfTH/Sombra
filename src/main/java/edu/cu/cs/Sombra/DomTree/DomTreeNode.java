package edu.cu.cs.Sombra.DomTree;

import java.util.ArrayList;
import java.util.List;

import edu.cu.cs.Sombra.Tree.BaseTreeNode;

public class DomTreeNode extends BaseTreeNode {

	private String tag;
	private String id;
	private String classname;
	private List<String> tagPath;
	private String content;
	private String src;
	
	public DomTreeNode(DomTreeNode parent, String tag, String id, String classname, String content, String src) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.tag = tag;
		this.id = id;
		this.classname = classname;
		this.tagPath = new ArrayList<String>();
		if (parent != null) {
			this.tagPath.addAll(((DomTreeNode)this.getParent()).getTagPath());
		}
		this.tagPath.add(tag);
		this.content = content;
		this.src = src;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public List<String> getTagPath() {
		return this.tagPath;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void print() {
		System.out.println("tag: " + this.tag);
		System.out.println("id: " + this.id);
		System.out.println("class: " + this.classname);
		System.out.println("tag path: " + this.tagPath);
		System.out.println();		
	}
	
	public void printSubTree() {
		this.print();
		if (this.childrenNum() == 0) {
			return;
		}
		for (BaseTreeNode c : this.getChildren()) {
			((DomTreeNode)c).printSubTree();
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
