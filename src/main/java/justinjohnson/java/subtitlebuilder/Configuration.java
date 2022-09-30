package justinjohnson.java.subtitlebuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Configuration {

	@XmlAttribute
	private String parseMethod, photoshopPath, templatePath;

	public Configuration() {
		this.parseMethod = "";
		this.photoshopPath = "";
		this.templatePath = "";
		return;
	}

	public String getParseMethod() {
		return this.parseMethod;
	}

	public String getPhotoshopPath() {
		return this.photoshopPath;
	}

	public String getTemplatePath() {
		return this.templatePath;
	}

	public void setParseMethod(String parseMethod) {
		this.parseMethod = parseMethod;
		return;
	}

	public void setPhotoshopPath(String photoshopPath) {
		this.photoshopPath = photoshopPath;
		return;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
		return;
	}
}
