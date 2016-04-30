package gml.reader.model;

import java.io.File;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gml.reader.view.Popout;

import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

public class Logika {

	// TALBICA DO PRZECHOWYWANIA OBIEKTÓW TYPU PARCELA
	public static List<Parcela> parcele = new ArrayList<Parcela>();

	// POLE PRZECHOWUJACE WCZYTANY PLIK GML
	private static File gmlFile;

	//GETTER
	public static File getGmlFile() {
		return gmlFile;
	}
	//SETTER
	public static void setGmlFile(File gml) {
		Logika.gmlFile = gml;
	}

	// OKNO POBIERANIA DANYCH
	public static File loadGML() {
		FileChooser fileChooser = new FileChooser();
		configureFileChooser(fileChooser);
		File gmlFile = fileChooser.showOpenDialog(null);
		return gmlFile;
	}

	// KONFIGURACJA OKNA POBIERANIA DANYCH
	public static void configureFileChooser(FileChooser fileChooser) {
		fileChooser.setTitle("Wska¿ plik GML");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("GML", "*.gml"));
	}

	//PARSOWANIE WCZYTANEGO PLIKU
	public static void parsowanie(){

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(Logika.getGmlFile());
			parcele.clear();

			// ZLICZENIE ZNACZNIKÓW <FEATURE MEMBER>
			NodeList listMember = doc.getElementsByTagName("gml:featureMember");

			// PARSOWANIE PLIKU GML
			outloop:
			for (int i = 0; i < listMember.getLength(); i++) {
				Node fm = listMember.item(i);
				Element fmElement = (Element) fm;

				Element parcelaElem = (Element) fmElement.getElementsByTagName("geo:Parcela").item(0);
				int id = Integer.parseInt(parcelaElem.getElementsByTagName("ID").item(0).getTextContent().trim());
				String nazwa = (parcelaElem.getElementsByTagName("nazwa").item(0).getTextContent().trim());
				int rokEksploatacji = Integer.parseInt(parcelaElem.getElementsByTagName("rok_eksploatacji").item(0).getTextContent().trim());
				double miazszosc = Double.parseDouble(parcelaElem.getElementsByTagName("miazszosc").item(0).getTextContent().trim());
				int sposobKierowaniaStropem = Integer.parseInt(parcelaElem.getElementsByTagName("sposob_kier_stropem").item(0).getTextContent().trim());
				double powierzchnia = Double.parseDouble(parcelaElem.getElementsByTagName("powierzchnia").item(0).getTextContent().trim());

				Element geometryElem = (Element) parcelaElem.getElementsByTagName("Geometry").item(0);
				Element compSurfElem = (Element) geometryElem.getElementsByTagName("gml:CompositeSurface").item(0);
				Element surfMemElem = (Element) compSurfElem.getElementsByTagName("gml:surfaceMember").item(0);
				Element polygonElem = (Element) surfMemElem.getElementsByTagName("gml:Polygon").item(0);
				Element exteriorElem = (Element) polygonElem.getElementsByTagName("gml:exterior").item(0);
				Element ringElem = (Element) exteriorElem.getElementsByTagName("gml:Ring").item(0);
				Element curveMemberElem = (Element) ringElem.getElementsByTagName("gml:curveMember").item(0);
				Element lineStringElem = (Element) curveMemberElem.getElementsByTagName("gml:LineString").item(0);
				NodeList coordList = lineStringElem.getElementsByTagName("gml:coord");

				// ZABEZPIECZENIE PRZED WCZYTANIEM PARCELI BEZ WSPÓ£RZÊDNYCH
				if (coordList.getLength() == 0) {
					Popout.popoutMessage("B£¥D", "Parcelan numer: " + (i+1) + " musi posiadaæ wspó³rzêdne !");
					break outloop;
				}
				// TWORZENIE OBIEKTÓW TYPU PUNKT I ZAPIS DO TABLICY PUNKTY
				List<Punkt> punkty = new ArrayList<Punkt>();
				for (int j = 0; j < coordList.getLength(); j++) {
					Node coordNode = coordList.item(j);
					Element coordElem = (Element) coordNode;
					double X = Double.parseDouble(coordElem.getElementsByTagName("gml:X").item(0).getTextContent().trim());
					double Y = Double.parseDouble(coordElem.getElementsByTagName("gml:Y").item(0).getTextContent().trim());

					Punkt punkt = new Punkt(X, Y);
					punkty.add(punkt);

				} // ZAPIS OBIEKTÓW PARCELA DO TABLICY PARCELE
				Parcela parcela = new Parcela(id, nazwa, rokEksploatacji, miazszosc, sposobKierowaniaStropem,powierzchnia, punkty);
				parcele.add(parcela);

			} // KONIEC GLOWNEJ PETLI
			if (parcele.size() == listMember.getLength()){
				Popout.popoutMessage("SUKCES", "Parsowanie przebieg³o pomyœlnie");
			}

		// OBS£UGA WYJ¥TKÓW
		} catch (ParserConfigurationException e) {
			Popout.popoutMessage("B£¥D", e.getLocalizedMessage().toString());

		} catch (SAXException e) {
			Popout.popoutMessage("B£¥D", e.getLocalizedMessage().toString());

		} catch (IOException e) {
			Popout.popoutMessage("B£¥D", e.getLocalizedMessage().toString());

		} catch (NumberFormatException e){
			Popout.popoutMessage("B£¥D",e.getLocalizedMessage().toString());
		}

	} // KONIEC PARSOWANIE
}
