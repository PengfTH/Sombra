package edu.cu.cs.Sombra.Tree;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTreeNode {
	
	private BaseTreeNode parent;
	private int depth;
	private List<BaseTreeNode> children;
	
	public BaseTreeNode(BaseTreeNode parent, int depth) {
		this.depth = depth;
		this.parent = parent;	
		this.children = new ArrayList<BaseTreeNode>();
	}
	
	public BaseTreeNode(BaseTreeNode parent) {
		if (parent == null) {
			this.depth = 0;
		} else{
			this.depth = parent.getDepth() + 1;
		
		}
		this.parent = parent;	
		this.children = new ArrayList<BaseTreeNode>();
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public BaseTreeNode getParent() {
		return this.parent;
	}
	
	public List<BaseTreeNode> getChildren(){
		return this.children;
	}
	
	public int childrenNum() {
		return this.children.size();
	}
	
	public List<BaseTreeNode> getSiblings() {
		if (this.parent == null) {
			return null;
		}
		ArrayList<BaseTreeNode> siblings = new ArrayList<BaseTreeNode>(this.parent.getParent().getChildren());
		siblings.remove(this);
		return siblings;
	}
	
	public int getSiblingsNum() {
		return this.getSiblings().size();
	}
	
	public void addChild(BaseTreeNode child) {
		this.children.add(child);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
