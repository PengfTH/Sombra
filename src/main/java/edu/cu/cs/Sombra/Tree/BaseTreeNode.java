package edu.cu.cs.Sombra.Tree;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTreeNode {
	
	private BaseTreeNode parent;
	private int depth;
	private List<BaseTreeNode> children;
	private List<BaseTreeNode> siblings;
	
	public BaseTreeNode(BaseTreeNode parent, int depth) {
		this.depth = depth;
		this.parent = parent;	
		this.children = new ArrayList<BaseTreeNode>();
		this.siblings = new ArrayList<BaseTreeNode>();
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public BaseTreeNode getParent() {
		return this.parent;
	}
	
	public List<BaseTreeNode> children(){
		return this.children;
	}
	
	public int childrenNum() {
		return this.children.size();
	}
	
	public List<BaseTreeNode> getSiblings() {
		return this.siblings;
	}
	
	public int getSiblingsNum() {
		return this.siblings.size();
	}
	
	public void addChild(BaseTreeNode child) {
		this.children.add(child);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
