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

import edu.cu.cs.Sombra.DomTree.DomTree;
import edu.cu.cs.Sombra.DomTree.DomTreeNode;
import edu.cu.cs.Sombra.DomTree.PhantomFeature;
import edu.cu.cs.Sombra.Tree.BaseTreeNode;
import edu.cu.cs.Sombra.VisualTree.VisualTree;
import edu.cu.cs.Sombra.VisualTree.VisualTreeNode;
import edu.cu.cs.Sombra.util.PhantomUtil;

public class PageStructure {

	private VisualTree VTree;
	private DomTree DomTree;
	public Set<DomTreeNode> nameNodes;
	public Set<DomTreeNode> valueNodes;
	public Map<DomTreeNode, String> V2N;

	public PageStructure(String htmlfile) {
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
			/*
			 * WebElement element = this.VTree.idx2we.get(sombraid); if (element
			 * != null) { ((DomTreeNode)
			 * domNode).setVWeight(element.getSize().getHeight() *
			 * element.getSize().getWidth()); } else { ((DomTreeNode)
			 * domNode).setVWeight(-1); continue; }
			 */

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

		// System.out.println("goodnodes size = " + goodIndex.size());

		this.VTree.processPhantom(htmlfile, goodIndex);

		File vfile = new File("phantom_" + htmlfile + ".json");
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
			// System.out.println("idx2pf size = " + this.idx2pf.size());
		}

		// System.out.println(this.DomTree.getGoodNodes().size());
		// PhantomUtil.close();
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

		for (DomTreeNode node : valuelist) {
			nameNodes.remove(node);
		}
		valueNodes.addAll(valuelist);
		matched.addAll(this.value2name(valuelist, matched));
		nameNodes.addAll(matched);
		return matched;
	}

	private double nodeDistance(DomTreeNode node1, DomTreeNode node2) {
		double dist = 0.0;
		/*
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
			diff++;
		}
		while (parent1 != null) {
			parent1 = (DomTreeNode) parent1.getParent();
			diff++;
		}
		while (parent2 != null) {
			parent2 = (DomTreeNode) parent2.getParent();
			diff++;
		}
		*/
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
		double minDist = -1;
		DomTreeNode candidate = null;
		for (DomTreeNode valuenode : valuelist) {
			// sombraid-based
			for (DomTreeNode namenode : nameNodes) {
				if (matched.contains(namenode)) 
					continue;
				double dist = nodeDistance(valuenode, namenode);
				if (minDist == -1 || minDist > dist) {
					minDist = dist;
					candidate = namenode;
				}
			}
			V2N.put(valuenode, candidate.getContent());
			matched.add(candidate);
		}
		
		return matched;
	}
	
	private Set<DomTreeNode> value2name1(List<DomTreeNode> valuelist, Set<DomTreeNode> matched) {
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
			// sombraid-based
			List<DomTreeNode> sombraList = DomTree.getGoodNodes();
			int pos = sombraList.indexOf(valuenode);
			int back = pos - 1;
			int ford = pos + 1;
			int thredDis = 30;
			boolean ismatch = false;

			while (back >= 0 && ford < sombraList.size()) {
				DomTreeNode backCandidate = sombraList.get(back);
				DomTreeNode forwCandidate = sombraList.get(ford);
				if (nodeDistance(backCandidate, valuenode) < nodeDistance(forwCandidate, valuenode)) {
					if (backCandidate.getVPath().equals(valuenode.getVPath()) && nameNodes.contains(backCandidate)
							&& !matched.contains(backCandidate) && nodeDistance(backCandidate, valuenode) < thredDis) {
						matched.add(backCandidate);
						V2N.put(valuenode, backCandidate.getContent());
						ismatch = true;
						break;
					}
					back--;
				} else {
					if (forwCandidate.getVPath().equals(valuenode.getVPath()) && nameNodes.contains(forwCandidate)
							&& !matched.contains(forwCandidate) && nodeDistance(forwCandidate, valuenode) < thredDis) {
						matched.add(forwCandidate);
						V2N.put(valuenode, forwCandidate.getContent());
						ismatch = true;
						break;
					}
					ford++;
				}
			}
			while (!ismatch && back >= 0) {
				DomTreeNode backCandidate = sombraList.get(back);
				if (backCandidate.getVPath().equals(valuenode.getVPath()) && nameNodes.contains(backCandidate)
						&& !matched.contains(backCandidate) && nodeDistance(backCandidate, valuenode) < thredDis) {
					matched.add(backCandidate);
					V2N.put(valuenode, backCandidate.getContent());
					ismatch = true;
					break;
				}
				back--;
			}
			while (!ismatch && ford < sombraList.size()) {
				DomTreeNode forwCandidate = sombraList.get(ford);
				if (forwCandidate.getVPath().equals(valuenode.getVPath()) && nameNodes.contains(forwCandidate)
						&& !matched.contains(forwCandidate) && nodeDistance(forwCandidate, valuenode) < thredDis) {
					matched.add(forwCandidate);
					V2N.put(valuenode, forwCandidate.getContent());
					ismatch = true;
					break;
				}
				ford++;
			}
		}
		
		return matched;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageStructure test = new PageStructure("1.html");
		// test.getDomTree().traverse();
		/*
		 * for (VisualTreeNode vnode : test.getV2D().keySet()) {
		 * System.out.println(vnode.getSRC()); } for (BaseTreeNode node :
		 * test.DomTree.getLeafNodes()) {
		 * System.out.println(((DomTreeNode)node).getSRC()); }
		 */

	}
}
