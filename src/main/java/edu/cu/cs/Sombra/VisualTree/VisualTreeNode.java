package edu.cu.cs.Sombra.VisualTree;

import edu.cu.cs.Sombra.Tree.BaseTreeNode;

public class VisualTreeNode extends BaseTreeNode {
	private String lineNumbers;
	private int coherenceDegree;	//Degree of coherence
	private int containImgs;
	private boolean isImg;
	private boolean containTable;
	private int containP;
	private int textLength;
	private int linkTextLength;
	private int domCldNum;	//child number in DOM tree
	private int fontSize;
	private String fontWeight;
	private String bgColor;
	
	private int rectLeft;
	private int rectTop;
	private int rectWidth;
	private int rectHeight;
	
	private String ID;
	private int order;
	
	private String src;
	
	public VisualTreeNode(BaseTreeNode parent, int depth) {
		super(parent, depth);
		this.init();
	}
	
	private void init() {
		this.lineNumbers = "";
		this.coherenceDegree = 0;
		this.containImgs = 0;
		this.containTable = false;
		this.isImg = false;
		this.containP = 0;
		this.textLength = 0;
		this.linkTextLength = 0;
		this.domCldNum = 0;
		this.fontSize = 0;
		this.fontWeight = "";
		this.bgColor = "";
		
		this.rectHeight = 0;
		this.rectLeft = 0;
		this.rectTop = 0;
		this.rectWidth = 0;
		
		this.ID = "";
		this.order = 0;
		this.src = null;
	}
	
	public void setLineNumber(String value) {
		this.lineNumbers = value;
	}
	
	public String getLineNumber() {
		return this.lineNumbers;
	}
	
	public void setCoherenceDegree(int value) {
		this.coherenceDegree = value;
	}
	
	public int getCoherenceDegree() {
		return this.coherenceDegree;
	}
	
	public void setContainImgs(int value) {
		this.containImgs = value;
	}
	
	public int getContaiImgs() {
		return this.containImgs;
	}
	
	public void setContainP(int value) {
		this.containP = value;
	}
	
	public int getContaiP() {
		return this.containP;
	}
	
	public void setContainTable(boolean value) {
		this.containTable = value;
	}
	
	public boolean getContainTable() {
		return this.containTable;
	}
	
	public void setTextLength(int value) {
		this.textLength = value;
	}
	
	public int getTextLength() {
		return this.textLength;
	}
	
	public void setLinkTextLength(int value) {
		this.linkTextLength = value;
	}
	
	public int getLinkTextLength (){
		return this.linkTextLength;
	}
	
	public void setDomCldNum(int value) {
		this.domCldNum = value;
	}
	
	public int getDomCldNum() {
		return this.domCldNum;
	}
	
	public void setFontSize(int value) {
		this.fontSize = value;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}
	
	public void setFontWight(String value) {
		this.fontWeight = value;
	}
	
	public String getFontWeight() {
		return this.fontWeight;
	}
	
	public void setBgColor(String value) {
		this.bgColor = value;
	}
	
	public String getBgColor() {
		return this.bgColor;
	}
	
	public void setRectLeft(int value) {
		this.rectLeft = value;
	}
	
	public int getRectLeft() {
		return this.rectLeft;
	}
	
	public void setRectTop(int value) {
		this.rectTop = value;
	}
	
	public int getRectTio() {
		return this.rectTop;
	}
	
	public void setRectWidth(int value) {
		this.rectWidth = value;
	}
	
	public int getRectWidth() {
		return this.rectWidth;
	}
	
	public void setRectHeight(int value) {
		this.rectHeight = value;
	}
	
	public int getRectHeight() {
		return this.rectHeight;
	}
	
	public void setID(String value) {
		this.ID = value;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public void setOrder(int value) {
		this.order = value;
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setIsImg(boolean value) {
		this.isImg = value;
	}
	
	public boolean getIsImg() {
		return this.isImg;
	}
	
	public void setSRC(String value) {
		this.src = value.replaceAll("\u00a0", "");
	}
	
	public String getSRC() {
		return this.src;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Node: ");
		sb.append(this.ID);
		sb.append(", order ");
		sb.append(this.order);
		
		if (!this.src.isEmpty()) {
			sb.append(" ");
			sb.append(this.src);
		}
		sb.append("\n");
		for (BaseTreeNode node : this.getChildren()) {
			for (int i = 0; i < this.getDepth(); i++) {
				sb.append("\t");
			}
			sb.append(node);
		}
		return sb.toString();
	}

}
