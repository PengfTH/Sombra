package edu.cu.cs.Sombra.VisualTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fit.vips.Vips;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import edu.cu.cs.Sombra.Config;
import edu.cu.cs.Sombra.Tree.BaseTreeNode;
import edu.cu.cs.Sombra.util.PhantomUtil;

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
	// public Map<Integer, WebElement> idx2we;

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
		String outputFilename = Config.TEMP_PATH + "VT_" + filename;
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
				vips.startSegmentation(Config.TEMP_PATH + filename);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// parse visual xml file
		VisualTreeParser parser = new VisualTreeParser();
		VisualTree tree = parser.parse(outputFilename + ".xml");

		return tree;
	}

	public void processPhantom(String filename, Set<Integer> goodIndex) {
		// invoke phantom render and fill map
		String currentDir = System.getProperty("user.dir");
		// System.out.println(currentDir);

		Map<Integer, WebElement> idx2we = new HashMap<Integer, WebElement>();
		String fileurl = "file:" + File.separator + File.separator + currentDir + File.separator + Config.TEMP_PATH + File.separator + filename;
		System.out.println(fileurl);
		List<WebElement> elements = PhantomUtil.render(fileurl);
		System.out.println(elements.size());

		File ptm = new File(Config.TEMP_PATH + "phantom_" + filename + ".json");
		if (ptm.exists()) {
		} else {
			try {
				ptm.createNewFile();
				FileWriter fout = new FileWriter(ptm);
				for (WebElement element : elements) {
					if (element.getSize().width == 0 || element.getSize().height == 0
							|| element.getAttribute("sombraid") == null
							|| !goodIndex.contains(Integer.parseInt(element.getAttribute("sombraid")))) {
						continue;
					}
					JSONObject obj = new JSONObject();
					obj.put("sombraid", element.getAttribute("sombraid"));
					obj.put("width", element.getSize().width);
					obj.put("height", element.getSize().height);
					obj.put("x", element.getLocation().x);
					obj.put("y", element.getLocation().y);
					//System.out.println(obj.toString());
					fout.write(obj.toString());
					fout.write("\n");

					/*
					 * System.out.println(element.getText());
					 * System.out.println(element.getLocation().x + ", " +
					 * element.getLocation().y);
					 * System.out.println(element.getSize().width);
					 * System.out.println(element.getSize().height);
					 * System.out.println(element.getSize().getWidth());
					 * System.out.println(element.getSize().getHeight());
					 * System.out.println(element.getAttribute("sombraid"));
					 * System.out.println("*******");
					 */
				}
				fout.close();

				/*
				 * for (WebElement we : elements) { String idx =
				 * we.getAttribute("sombraid"); if (idx != null)
				 * idx2we.put(Integer.parseInt(idx), we); } tree.idx2we =
				 * idx2we;
				 */

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VisualTree.getVisualTree("modified_2.html");

	}
}
