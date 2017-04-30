package edu.cu.cs.Sombra.Schema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.*;
import org.openqa.selenium.WebElement;

import edu.cu.cs.Sombra.Config;
import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import edu.cu.cs.Sombra.DomTree.PhantomFeature;
import edu.cu.cs.Sombra.Tree.BaseTreeNode;
import edu.cu.cs.Sombra.VisualTree.VisualTree;
import edu.cu.cs.Sombra.VisualTree.VisualTreeNode;
import edu.cu.cs.Sombra.util.PhantomUtil;

public class PageStructure {

	private VisualTree VTree;
	private String url;
	private DomTree DomTree;
	public Set<DomTreeNode> nameNodes;
	public Set<DomTreeNode> valueNodes;
	public Map<DomTreeNode, String> V2N;
	
	public PageStructure(String htmlfile) {
		this.url = htmlfile;
		this.DomTree = new DomTree(htmlfile);
		this.VTree = VisualTree.getVisualTree("modified_" + htmlfile);
		this.nameNodes = new HashSet<DomTreeNode>();
		this.valueNodes = new HashSet<DomTreeNode>();
		this.V2N = new HashMap<DomTreeNode, String>();
		this.treeAlign("modified_" + htmlfile);
	}

	/*
	 * Align DOM Tree and Visual Tree
	 */
	private void treeAlign(String htmlfile) {
		List<BaseTreeNode> domLeafNodes = this.DomTree.getNodes();
		List<BaseTreeNode> vLeafNodes = this.VTree.getLeafNodes();
		for (BaseTreeNode domNode : domLeafNodes) {
			int sombraid = ((DomTreeNode) domNode).getSombraid();

			for (BaseTreeNode vNode : vLeafNodes) {
				List<Integer> list = ((VisualTreeNode) vNode).getSombraIds();
				if (list.contains(sombraid)) {
					((DomTreeNode) domNode).setVPath(((VisualTreeNode) vNode).getID());
					((DomTreeNode) domNode).setVWeight(
							((VisualTreeNode) vNode).getRectHeight() * ((VisualTreeNode) vNode).getRectWidth());
					this.DomTree.addGoodNodes((DomTreeNode) domNode);
					break;
				}
			}

		}
		
		Set<Integer> goodIndex = new HashSet<Integer>();
		for (DomTreeNode good : this.DomTree.getGoodNodes()) {
			goodIndex.add(good.getSombraid());
		}

		this.VTree.processPhantom(htmlfile, goodIndex);

		File vfile = new File(Config.TEMP_PATH + "phantom_" + htmlfile + ".json");
		if (vfile.exists()) {
			int index = 0;
			try {
				String line;
				BufferedReader br = new BufferedReader(new FileReader(vfile));
				while ((line = br.readLine()) != null) {
					JSONObject obj = new JSONObject(line);
					while (Integer.parseInt(obj.getString("sombraid")) != this.DomTree.getGoodNodes().get(index)
							.getSombraid()) {
						index++;
					}
					PhantomFeature _pf = new PhantomFeature((Integer) obj.get("x"), (Integer) obj.get("y"),
							(Integer) obj.get("width"), (Integer) obj.get("height"));
					this.DomTree.getGoodNodes().get(index).pf = _pf;
					this.DomTree.getGoodNodes().get(index).setVWeight(_pf.height * _pf.width);
					// System.out.println(obj.getString("sombraid"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<DomTreeNode> newgood = new ArrayList<DomTreeNode>();
			for (DomTreeNode good : this.DomTree.getGoodNodes()) {
				if (good.pf != null) {
					newgood.add(good);
				}
			}
			this.DomTree.setGoodNodes(newgood);
		}

	}

	public DomTree getDomTree() {
		return this.DomTree;
	}

	public Set<DomTreeNode> value2name() {
		// one-to-one
		List<DomTreeNode> valuelist = new ArrayList<DomTreeNode>();
		valuelist.addAll(valueNodes);
		Set<DomTreeNode> matched = this.value2name(valuelist, new HashSet<DomTreeNode>());

		// 2nd round
		valuelist.clear();
		for (DomTreeNode node : nameNodes) {
			if (!matched.contains(node)) {
				valuelist.add(node);
			}
		}

		matched.addAll(this.value2name(valuelist, matched));
		// nameNodes.addAll(matched);
		// potential name and value nodes
		for (DomTreeNode node : this.DomTree.getGoodNodes()) {
			if (matched.contains(node)) {
				continue;
			}
			for (DomTreeNode mnode : matched) {
				if (mnode.getTagPathString().equals(node.getTagPathString())
						&& mnode.getVPath().substring(0, mnode.getVPath().lastIndexOf('-'))
								.equals(node.getVPath().substring(0, node.getVPath().lastIndexOf('-')))) {
					if (nameNodes.contains(mnode)) {
						nameNodes.add(node);
					} else {
						valueNodes.add(node);
					}
				}
			}

		}

		valuelist.clear();
		valuelist.addAll(valueNodes);
		matched.addAll(this.value2name(valuelist, matched));
		
		for (DomTreeNode valuenode : valueNodes) {
			if (matched.contains(valuenode))
				continue;
			if (valuenode.getId() != null && !valuenode.getId().isEmpty())
				V2N.put(valuenode, valuenode.getId());
			else if (valuenode.getClass() != null && !valuenode.getClassname().isEmpty())
				V2N.put(valuenode, valuenode.getClassname());
			else if (valuenode.getParent() != null && !((DomTreeNode) valuenode.getParent()).getId().isEmpty())
				V2N.put(valuenode, ((DomTreeNode) valuenode.getParent()).getId());
		}
		
		cleanV2N();
		return matched;
	}

	private void cleanV2N() {
		//only clean name node :
		for (DomTreeNode node : this.V2N.keySet()) {
			if (this.V2N.get(node).contains(":")) {
				String s = this.V2N.get(node).replaceAll(":", "");
				this.V2N.put(node, s);
			}
		}
		
	}

	private double nodeDistance(DomTreeNode node1, DomTreeNode node2) {
		double dist = 0.0;

		DomTreeNode parent1 = node1;
		DomTreeNode parent2 = node2;
		while (parent1 != null && parent2 != null) {
			int sombraid1 = parent1.getSombraid();
			int sombraid2 = parent2.getSombraid();
			if (sombraid1 == sombraid2)
				break;
			else if (sombraid1 > sombraid2) {
				parent1 = (DomTreeNode) parent1.getParent();
			} else {
				parent2 = (DomTreeNode) parent2.getParent();
			}
			dist++;
		}
		if (dist >= 3)
			return -1.0;
		dist += Math.abs(node1.getVWeight() - node2.getVWeight());
		dist += node1.pf.distance(node2.pf);
		return dist;
	}

	private Set<DomTreeNode> value2name(List<DomTreeNode> valuelist, Set<DomTreeNode> matched) {
		// one-to-one
		Collections.sort(valuelist, new Comparator<DomTreeNode>() {
			public int compare(DomTreeNode node1, DomTreeNode node2) {
				if (node1.getSombraid() == node2.getSombraid())
					return 0;
				if (node1.getSombraid() > node2.getSombraid())
					return 1;
				return -1;
			}
		});

		for (DomTreeNode valuenode : valuelist) {
			if (matched.contains(valuenode))
				continue;
			double minDist = -1;
			DomTreeNode candidate = null;
			// sombraid-based
			for (DomTreeNode namenode : nameNodes) {
				if (matched.contains(namenode) || valuenode.equals(namenode)
						|| namenode.getVPath() != valuenode.getVPath())
					continue;
				double dist = nodeDistance(valuenode, namenode);
				if ((minDist == -1 || minDist > dist) && dist > 0) {
					minDist = dist;
					candidate = namenode;
				}
			}
			if (candidate != null) {
				if (valuenode.getContent().contains(":")) {
					V2N.put(candidate, valuenode.getContent());
				}
				else {
					V2N.put(valuenode, candidate.getContent());
				}
				matched.add(candidate);
				matched.add(valuenode);
			}

		}

		return matched;
	}
	
	private Map<String, String> node2value() {
		Map<String, String> N2V = new HashMap<String, String>();
		for (DomTreeNode valuenode : V2N.keySet()) {
			N2V.put(V2N.get(valuenode), valuenode.getContent());
		}
		return N2V;
	}
	
	public String generateRowString(List<String> attributes) {
		Map<String, String> N2V = node2value();
		StringBuilder sb = new StringBuilder();
		for (String attribute : attributes) {
			if (N2V.containsKey(attribute)) { 
				sb.append(N2V.get(attribute));
			} else {
				sb.append("null");
			}
			sb.append(",");
		}
		sb.append(url);
		sb.append("\n");
		return sb.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageStructure test = new PageStructure("0001049356.html");
		
		// test.getDomTree().traverse();
		/*
		 * for (VisualTreeNode vnode : test.getV2D().keySet()) {
		 * System.out.println(vnode.getSRC()); } for (BaseTreeNode node :
		 * test.DomTree.getLeafNodes()) {
		 * System.out.println(((DomTreeNode)node).getSRC()); }
		 */

	}
}
