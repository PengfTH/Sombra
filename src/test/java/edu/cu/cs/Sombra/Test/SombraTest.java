package edu.cu.cs.Sombra.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cu.cs.Sombra.Config;
import edu.cu.cs.Sombra.Schema.PageStructure;
import edu.cu.cs.Sombra.Schema.SchemaAnalyzer;
import edu.cu.cs.Sombra.Schema.TemplateStructure;

public class SombraTest {
	
	public static void main(String[] args) {
		String path = Config.FILE_PATH;
		File folder = new File(path);
		if (!folder.isDirectory() || folder.listFiles().length < 3) {
			return;
		}
		
		List<File> webpages = new ArrayList<File>();
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".html")) {
				webpages.add(file);
			}
		}
		
		TemplateStructure temp = new TemplateStructure();
		temp.pageAlign(webpages.get(0).getName(), webpages.get(1).getName());
		
		SchemaAnalyzer analyzer = new SchemaAnalyzer();
		List<PageStructure> pages = new ArrayList<PageStructure>();
		Set<String> attrSet = new HashSet<String>();
		for (File file : webpages) {
			PageStructure page = new PageStructure(file.getName());
			analyzer.analyze(page, temp);
			page.value2name();
			pages.add(page);
			attrSet.addAll(page.V2N.values());
		}
		
		List<String> attrList = new ArrayList<String>();
		attrList.addAll(attrSet);
		List<String> rows = new ArrayList<String>();
		for (PageStructure page : pages) {
			rows.add(page.generateRowString(attrList));
		}
		
		boolean iswrite = false;
		if (iswrite) {
			FileWriter writer;
			try {
				writer = new FileWriter(path + "table.csv");
				for (String row : rows) {
					writer.write(row);
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
