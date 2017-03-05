package edu.cu.cs.Sombra.VisualTree;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class VisualTreeParser {
	
	public VisualTreeParser() {
		
	}
	
	public static void main(String[] args) {
		VisualTreeParser parser = new VisualTreeParser();
		VisualTree vtree = parser.parse("VIPSResult.xml");
		System.out.println(vtree.toString());
	}
	
	private VisualTree parse(Document doc) {
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName(VisualTreeConstant.ELEM_VISUALTREE);
		if (nodes.getLength() == 0) {
			System.out.println("Tag: " + VisualTreeConstant.ELEM_VISUALTREE + " not found");
			return null;
		}
		return parseVisualTree(nodes.item(0));
	}
	
	public VisualTree parse(String path) {
		try {
			File file = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			
			return this.parse(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private VisualTree parseVisualTree(Node node) {
		Element element = (Element) node;
		VisualTree vtree = new VisualTree();
		vtree.setOrder(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_ORDER)));
		vtree.setPageRectHeight(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_PAGERECTHEIGHT)));
		vtree.setPageRectLeft(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_PAGERECTLEFT)));
		vtree.setPageRectTop(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_PAGERECTTOP)));
		vtree.setPageRectWidth(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_PAGERECTWIDTH)));
		vtree.setPageTitle(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_PAGETITLE));
		vtree.setUrl(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_Url));
		vtree.setWindowHeight(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_WIDNOWWIDTH)));
		vtree.setWindowWidth(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREE_WINDOWHEIGHT)));
		
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals(VisualTreeConstant.ELEM_VISUALTREENODE)) {
				VisualTreeNode vnode = parseVisualTreeNode(child, null, 1);
				vtree.addChild(vnode);
			}
		}
		return vtree;
	}
	
	private VisualTreeNode parseVisualTreeNode(Node node, VisualTreeNode parent, int depth) {
		Element element = (Element) node;
		VisualTreeNode vnode = new VisualTreeNode(parent, depth);
		vnode.setBgColor(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_BGCOLOR));
		vnode.setCoherenceDegree(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_DOC)));
		vnode.setContainImgs(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_CONTAINIMG)));
		vnode.setContainP(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_CONTAINP)));
		vnode.setContainTable(Boolean.parseBoolean((element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_CONTAINTABLE))));
		vnode.setDomCldNum(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_DOMCLDNUM)));
		vnode.setFontSize(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_FONTSIZE)));
		vnode.setFontWight(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_FONTWEIGHT));
		vnode.setID(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_ID));
		vnode.setIsImg(Boolean.parseBoolean(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_ISIMG)));
		vnode.setLineNumber(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_SOURCEINDEX));
		vnode.setLinkTextLength(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_LINKTEXTLEN)));
		vnode.setOrder(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_ORDER)));
		vnode.setRectHeight(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_RECTHEIGHT)));
		vnode.setRectLeft(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_RECTLEFT)));
		vnode.setRectTop(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_RECTTOP)));
		vnode.setRectWidth(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_RECTWIDTH)));
		vnode.setTextLength(Integer.parseInt(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_TEXTLEN)));
		vnode.setSRC(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_SRC));
		vnode.setSombraIds(element.getAttribute(VisualTreeConstant.ATTR_VISUALTREENODE_SOMBRAID));
		
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals(VisualTreeConstant.ELEM_VISUALTREENODE)) {
				VisualTreeNode cvnode = parseVisualTreeNode(child, vnode, depth+1);
				vnode.addChild(cvnode);
			}
		}
		
		return vnode;
	}
}
