package edu.cu.cs.Sombra.VisualTree;

import java.io.File;

import org.fit.vips.Vips;

import edu.cu.cs.Sombra.Tree.BaseTreeNode;

public class VisualTree extends BaseTreeNode {
	private int pageRectHeight;
	private int pageRectLeft;
	private int pageRectTop;
	private int pageRectWidth;
	private String pageTitle;
	private String url;
	private int windowHeight;
	private int windowWidth;
	private int order;

	public VisualTree() {
		super(null, 0);
		this.init();
	}

	private void init() {
		this.pageRectHeight = 0;
		this.pageRectLeft = 0;
		this.pageRectTop = 0;
		this.pageRectWidth = 0;
		this.windowHeight = 0;
		this.windowWidth = 0;
		this.order = 0;
		this.pageTitle = "";
		this.url = "";
	}

	public static VisualTree getVisualTree(String filename) {
		String outputFilename = "VT_" + filename;
		File file = new File(outputFilename + ".xml");
		if (!(file.exists() && !file.isDirectory())) {
			try {
				Vips vips = new Vips();
				vips.setOutputFileName(outputFilename);
				// disable graphics output
				vips.enableGraphicsOutput(false);
				// disable output to separate folder (no necessary, it's default
				// value is false)
				vips.enableOutputToFolder(false);
				// set permitted degree of coherence
				vips.setPredefinedDoC(8);
				// start segmentation on page
				vips.startSegmentation(filename);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		VisualTreeParser parser = new VisualTreeParser();
		return parser.parse(outputFilename + ".xml");

	}

	public void setPageRectHeight(int value) {
		this.pageRectHeight = value;
	}

	public int getPageRectHeight() {
		return this.pageRectHeight;
	}

	public void setPageRectLeft(int value) {
		this.pageRectLeft = value;
	}

	public int getPageRectLeft() {
		return this.pageRectLeft;
	}

	public void setPageRectTop(int value) {
		this.pageRectTop = value;
	}

	public int getPageRectTop() {
		return this.pageRectTop;
	}

	public void setPageRectWidth(int value) {
		this.pageRectWidth = value;
	}

	public int getPageRectWidth() {
		return this.pageRectWidth;
	}

	public void setWindowHeight(int value) {
		this.windowHeight = value;
	}

	public int getWindowHeight() {
		return this.windowHeight;
	}

	public void setWindowWidth(int value) {
		this.windowWidth = value;
	}

	public int getWindowWidth() {
		return this.windowWidth;
	}

	public void setOrder(int value) {
		this.order = value;
	}

	public int getOrder() {
		return this.order;
	}

	public void setPageTitle(String value) {
		this.pageTitle = value;
	}

	public String getPageTitle() {
		return this.pageTitle;
	}

	public void setUrl(String value) {
		this.url = value;
	}

	public String getUrl() {
		return this.url;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tree: ");
		sb.append(this.url);
		sb.append("\n");
		for (BaseTreeNode node : this.getChildren()) {
			sb.append("\t");
			sb.append(node);
			sb.append("\n");
		}
		return sb.toString();
	}
}
