package edu.mermet.tp8.fenetres;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FenetreConfigurationAide {
	public  FenetreConfigurationAide(JMenu menuApplication){
		JFrame f = new JFrame("Configuration des menus");
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		generateConfig(f, menuApplication);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void generateConfig(JFrame f, JMenu menuApplication){
		// ------ récupération des entêtes des items du menu application ------
		String[] menuItems = new String[menuApplication.getItemCount()];
		for(int i = 0; i < menuApplication.getItemCount(); i++) {
			menuItems[i] = menuApplication.getItem(i).getText();
			System.out.println(menuItems[i]);
		}
		// ------ parse XML ------
		File configXML = new File("/home/julien/.ihm/" + System.getProperty("user.name") + ".xml");
		if(!configXML.exists() || configXML.isDirectory()) {
			// ------ création ------
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("properties");
				doc.appendChild(rootElement);

				// staff elements
				Element staff = doc.createElement("utilisateur");
				rootElement.appendChild(staff);

				// set attribute to staff element
				Attr attr = doc.createAttribute("nom");
				attr.setValue(System.getProperty("user.name"));
				staff.setAttributeNode(attr);

				// firstname elements
				Element firstname = doc.createElement("firstname");
				firstname.appendChild(doc.createTextNode("yong"));
				staff.appendChild(firstname);

				// lastname elements
				Element lastname = doc.createElement("lastname");
				lastname.appendChild(doc.createTextNode("mook kim"));
				staff.appendChild(lastname);

				// nickname elements
				Element nickname = doc.createElement("nickname");
				nickname.appendChild(doc.createTextNode("mkyong"));
				staff.appendChild(nickname);

				// salary elements
				Element salary = doc.createElement("salary");
				salary.appendChild(doc.createTextNode("100000"));
				staff.appendChild(salary);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				File dossier = new File("/home/julien/.ihm/");
				if (!dossier.exists()) {
					dossier.mkdir();
					System.out.println("Directory is created!");
				}

				StreamResult result = new StreamResult(new File("/home/julien/.ihm/" + System.getProperty("user.name") + ".xml"));

				transformer.transform(source, result);

			} catch (ParserConfigurationException | TransformerException e) {
				e.printStackTrace();
			}
		}
			// ------ lecture  ------
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(configXML);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("user");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("user id : " + eElement.getAttribute("id"));
					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
					System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

				}
			}
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}

	}
}
