package justinjohnson.java.subtitlebuilder;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Main {

	private static Pattern EXPRESSION_PATTERN, LINE_PATTERN;

	public static final GUI GUI = new GUI();

	public static final String PACKAGE_ROOT = "justinjohnson/java/" + Main.TITLE, TITLE = "subtitlebuilder";
	public static final String[] PARSE_METHOD = { "Expression", "Line" };

	public static Configuration CONFIGURATION = new Configuration();
	public static SubtitleCache SUBTITLE_CACHE;

	static {
		try {
			Main.EXPRESSION_PATTERN = Pattern.compile("[\\s\\S]*?" // Starts with any character
					// Exclude a capital letter followed by 0 to 3 letters to end our expression
					+ "(?<![A-Z]|[A-Z][a-z]|[A-Z][a-z]{2}|[A-Z][a-z]{3})"
					// Ends with one or more exclamation marks, periods, or question marks
					+ "[\\!\\.\\?\u2026]+"
					// Include quotes and double quotes in our search
					+ "['\u2018\u2019\"\u201C\u201D]*");
			Main.LINE_PATTERN = Pattern.compile(".+");
		} catch (Exception e) {
			Main.Error(e, "Pattern matching element(s) failed to compile!");
		}
	}

	public static void Error(Exception e, String message) {
		System.err.println(Main.GUI.log("An error has occured:"));
		System.err.println(Main.GUI.log(message));
		Main.GUI.log(e.getMessage());
		e.printStackTrace();
		return;
	}

	private static void CheckHeadless() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			throw new Exception(Main.TITLE + " cannot run in a headless environment!");
		}
		return;
	}

	private static void FetchAPI() throws Exception {
		String fileName = Main.TITLE + "-api.jsx";
		File api = new File(fileName);
		if (!api.exists()) {
			FileUtils.copyURLToFile(ClassLoader.getSystemResource(Main.PACKAGE_ROOT + "/" + fileName), api);
		}
		return;
	}

	private static void FetchConfiguration() throws Exception {
		String fileName = Main.TITLE + "-configuration.xml";
		File configuration = new File(fileName);
		JAXBContext context = JAXBContext.newInstance(Configuration.class);
		if (!configuration.exists()) {
			Main.GUI.PhotoshopPathSelector();
			Main.GUI.ParseMethodSelector();
			Main.GUI.TemplatePathSelector();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(Main.CONFIGURATION, configuration);
		} else {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Main.CONFIGURATION = (Configuration) unmarshaller.unmarshal(configuration);
		}
		return;
	}

	private static void FetchSubtitles() throws Exception {
		Main.GUI.log("Please wait...");
		File workingDirectory = new File(".");
		ArrayList<File> fileList = new ArrayList<>();
		for (File file : workingDirectory.listFiles()) {
			if (file.getName().endsWith(".docx")) {
				fileList.add(file);
			}
		}
		for (File file : fileList) {
			Main.SUBTITLE_CACHE = new SubtitleCache();
			String fileName = file.getName();
			String fileTitle = FilenameUtils.removeExtension(fileName);
			File saveDirectory = new File(fileTitle);
			if (!(saveDirectory.isDirectory())) {
				if (!saveDirectory.mkdir()) {
					throw new Exception("Could not create directory for subtitles!");
				}
			}
			Main.SUBTITLE_CACHE.setTitle(fileTitle);
			FileInputStream fileInputStream = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(OPCPackage.open(fileInputStream));
			List<XWPFParagraph> paragraphList = document.getParagraphs();
			StringJoiner fileStringJoiner = new StringJoiner("\n");
			for (XWPFParagraph paragraph : paragraphList) {
				fileStringJoiner.add(paragraph.getText());
			}
			Matcher matcher;
			switch (Main.CONFIGURATION.getParseMethod()) {
			case "Line":
				matcher = Main.LINE_PATTERN.matcher(fileStringJoiner.toString());
				break;
			default:
				matcher = Main.EXPRESSION_PATTERN.matcher(fileStringJoiner.toString());
				break;
			}
			ArrayList<String> matchList = new ArrayList<>();
			while (matcher.find()) {
				matchList.add(matcher.group().trim());
			}
			Main.SUBTITLE_CACHE.setSubtitleList(matchList);
			String builderFileName = Main.TITLE + "-builder.jsx";
			File builder = new File(builderFileName);
			if (!builder.exists()) {
				FileUtils.copyURLToFile(ClassLoader.getSystemResource(Main.PACKAGE_ROOT + "/" + builderFileName),
						builder);
			}
			String subtitleCacheFileName = Main.TITLE + "-subtitle-cache.xml";
			File subtitleCache = new File(subtitleCacheFileName);
			JAXBContext context = JAXBContext.newInstance(SubtitleCache.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(Main.SUBTITLE_CACHE, subtitleCache);
			Main.GUI.log("Processing " + fileName + "...");
			ProcessBuilder processBuilder = new ProcessBuilder(Main.CONFIGURATION.getPhotoshopPath(), "-r",
					builder.getAbsolutePath());
			Process process = processBuilder.start();
			Main.GUI.EnableSkipButton(process);
			process.waitFor();
		}
		return;
	}

	public static void main(String[] args) {
		try {
			Main.CheckHeadless();
			Main.FetchConfiguration();
			Main.FetchAPI();
			Main.FetchSubtitles();
		} catch (Exception e) {
			Main.Error(e, "Something prevented the program from running!");
		}
		Main.GUI.log("Done.");
		return;
	}

}
