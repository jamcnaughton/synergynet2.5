package apps.basketapp.client;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;

/**
 * The Class StateLoader.
 */
public class StateLoader {

	/**
	 * Gets the tag value.
	 *
	 * @param sTag
	 *            the s tag
	 * @param eElement
	 *            the e element
	 * @return the tag value
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = nlList.item(0);
		
		return nValue.getNodeValue();
	}

	/** The content system. */
	private ContentSystem contentSystem;

	/** The db factory. */
	private DocumentBuilderFactory dbFactory;

	/** The d builder. */
	private DocumentBuilder dBuilder;

	/**
	 * Instantiates a new state loader.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public StateLoader(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Load items.
	 *
	 * @return the list
	 */
	public List<ContentItem> loadItems() {
		List<ContentItem> contentItems = new ArrayList<ContentItem>();
		
		try {
			File fXmlFile = new File(StateRecorder.logPath);
			if (!fXmlFile.exists()) {
				return null;
			}
			InputStream inputStream = new FileInputStream(fXmlFile);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			NodeList items = doc.getDocumentElement().getChildNodes();
			
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				if (item instanceof Text) {
					continue;
				}
				float locX = Float.parseFloat(getTagValue("locx",
						(Element) item));
				float locY = Float.parseFloat(getTagValue("locy",
						(Element) item));
				float angle = Float.parseFloat(getTagValue("angle",
						(Element) item));
				float scale = Float.parseFloat(getTagValue("scale",
						(Element) item));
				
				ContentItem contentItem = null;
				
				if (item.getNodeName().equalsIgnoreCase("video")) {
					URL videoURL = new URL(getTagValue("url", (Element) item));
					contentItem = contentSystem
							.createContentItem(MediaPlayer.class);
					((MediaPlayer) contentItem).setMediaURL(videoURL);
					
				} else if (item.getNodeName().equalsIgnoreCase("image")) {
					URL imageURL = new URL(getTagValue("url", (Element) item));
					contentItem = contentSystem
							.createContentItem(LightImageLabel.class);
					((LightImageLabel) contentItem).setAutoFitSize(false);
					((LightImageLabel) contentItem).drawImage(imageURL);
					
					int width = Integer.parseInt(getTagValue("width",
							(Element) item));
					int height = Integer.parseInt(getTagValue("height",
							(Element) item));
					
					((LightImageLabel) contentItem).setWidth(width);
					((LightImageLabel) contentItem).setHeight(height);
					
				} else if (item.getNodeName().equalsIgnoreCase("text")) {
					
					String txt = getTagValue("txt", (Element) item);
					contentItem = contentSystem
							.createContentItem(MultiLineTextLabel.class);
					((MultiLineTextLabel) contentItem)
							.setCRLFSeparatedString(txt);
					
					String fontFamily = getTagValue("font_family",
							(Element) item);
					int fontSize = Integer.parseInt(getTagValue("font_size",
							(Element) item));
					Color fontColor = new Color(Integer.parseInt(getTagValue(
							"font_color", (Element) item)));
					Color backColor = new Color(Integer.parseInt(getTagValue(
							"back_color", (Element) item)));
					Color borderColor = new Color(Integer.parseInt(getTagValue(
							"border_color", (Element) item)));
					int borderSize = Integer.parseInt(getTagValue(
							"border_size", (Element) item));
					
					((MultiLineTextLabel) contentItem).setTextColour(fontColor);
					((MultiLineTextLabel) contentItem)
							.setBackgroundColour(backColor);
					((MultiLineTextLabel) contentItem).setFont(new Font(
							fontFamily, Font.PLAIN, fontSize));
					((MultiLineTextLabel) contentItem)
							.setBorderSize(borderSize);
					((MultiLineTextLabel) contentItem)
							.setBorderColour(borderColor);
				}
				
				if (contentItem != null) {
					contentItem.setLocalLocation(locX, locY);
					contentItem.setAngle(angle);
					contentItem.setScale(scale);
					contentItems.add(contentItem);
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return contentItems;
	}
}
