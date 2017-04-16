package edu.cu.cs.Sombra.DomTree;

import java.util.ArrayList;
import java.util.List;

import edu.cu.cs.Sombra.Tree.BaseTreeNode;

public class DomTreeNode extends BaseTreeNode {

	private String tag;
	private String id;
	private int sombraid;
	private String classname;
	private List<String> tagPath;
	private String src;
	private String content;
	
	private String vPath;
	private int vWeight;
	
	public PhantomFeature pf;
	
	public DomTreeNode(DomTreeNode parent, String tag, String id, String classname, String src, String content) {
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
		this.src = src;
		this.content = content;
	}
	
	public void setSombraid(int i) {
		this.sombraid = i;
	}
	
	public int getSombraid() {
		return this.sombraid;
	}
	
	public void setVPath(String p) {
		this.vPath = p;
	}
	
	public String getVPath() {
		return this.vPath;
	}
	
	public void setVWeight(int w) {
		this.vWeight = w;
	}
	
	public int getVWeight() {
		return this.vWeight;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public List<String> getTagPath() {
		return this.tagPath;
	}
	
	public String getTagPathString() {
		String path = "";
		if (this.tagPath.size() != 0) {
			path += this.tagPath.get(0);
			for (int i=1; i<this.tagPath.size(); i++) {
				path += "/" + this.tagPath.get(i);
			}
		}	
		return path;
	}
	
	public String getSRC() {
		return this.src;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void print() {
		//if (this.vWeight == 0) return;
		System.out.println("tagpath: " + this.getTagPathString());
		System.out.println("id: " + this.id);
		System.out.println("vPath: " + this.getVPath());
		System.out.println("vWeight: " + this.getVWeight());
		System.out.println("Content: " + this.getContent());
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
