package justinjohnson.java.subtitlebuilder;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class SubtitleCache {

	@XmlElement
	private ArrayList<String> subtitle;
	@XmlAttribute
	private String title;

	public SubtitleCache() {
		this.subtitle = new ArrayList<>();
		this.title = "";
		return;
	}

	public ArrayList<String> getSubtitleList() {
		return this.subtitle;
	}

	public String getTitle() {
		return this.title;
	}

	public void setSubtitleList(ArrayList<String> subtitleList) {
		this.subtitle = subtitleList;
		return;
	}

	public void setTitle(String title) {
		this.title = title;
		return;
	}

}
