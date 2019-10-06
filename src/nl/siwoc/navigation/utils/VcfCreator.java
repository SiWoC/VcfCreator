package nl.siwoc.navigation.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import net.sourceforge.cardme.io.VCardWriter;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.errors.ErrorSeverity;
import net.sourceforge.cardme.vcard.errors.VCardError;
import net.sourceforge.cardme.vcard.types.FormattedNameType;
import net.sourceforge.cardme.vcard.types.GeographicPositionType;
import net.sourceforge.cardme.vcard.types.NameType;
import nl.siwoc.navigation.utils.vcfcreator.ui.Localizer;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class VcfCreator {

    private String _prefix = "a";
    private static String NUMBERFORMAT = "%03d";
    private String _routeName;
    private ArrayList<VCardImpl> _vCards = new ArrayList<VCardImpl>();
    private XPathFactory factory;
    private XPath xPath;

    public String getPrefix() {
        return _prefix;
    }

    public void setPrefix(String prefix) {
        _prefix = prefix;
    }

    public String getRouteName() {
        return _routeName;
    }

    public void setRouteName(String routeName) {
        _routeName = routeName;
    }

    public ArrayList<VCardImpl> getVCards() {
        return _vCards;
    }

    public void setVCards(ArrayList<VCardImpl> vCards) {
        _vCards = vCards;
    }

    public VcfCreator() {
        factory = XPathFactory.newInstance();
        xPath = factory.newXPath();
    }
    
    public List<VCardError> export(File outputFolder) {
        return export(_vCards, outputFolder);
    }

    public List<VCardError> export(List<VCardImpl> vCards, File outputFolder) {

        List<VCardError> errors = new ArrayList<VCardError>();

        for (int i = 0; i < vCards.size(); i++) {
            VCardImpl vCard = vCards.get(i);
            String filename = buildFilename(vCard);
            errors.addAll(export(vCard, outputFolder, filename));
        }
        return errors;
    }

    public List<VCardError> export(VCardImpl vCard, File outputFolder, String filename) {

        VCardWriter writer = new VCardWriter();
        writer.setVCard(vCard);
        String vCardString = writer.buildVCardString();
        if (writer.hasErrors()) {
            return vCard.getErrors();
        }
        try {
            File outputFile = new File(outputFolder, filename);
            FileUtils.writeStringToFile(outputFile, vCardString);
        } catch (IOException e) {
            VCardError error = new VCardError(e, ErrorSeverity.FATAL);
            List<VCardError> errors = new ArrayList<VCardError>();
            errors.add(error);
            return errors;
        }
        return new ArrayList<VCardError>(); // so addAll will always work (but add nothing)
    }

    private String buildFilename(VCardImpl vCard) {
        String filename = vCard.getFormattedName().getFormattedName();
        return filename + ".vcf";
    }

    public ArrayList<VCardImpl> tryParseRte(File inputFile) throws ParseException {

        try (InputStream fis = new FileInputStream(inputFile);
        		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-16"));) {

        	_vCards.clear();
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    if (!line.matches("Ozi.*")) {
                        return null;
                    } else {
                        firstLine = false;
                    }
                }
                if (line.matches("R,.*")) {
                    if (_routeName != null && !"".equals(_routeName)) {
                        throw new ParseException(MessageFormat.format(Localizer.getLocalizedText("ERROR_MULTIPLE_ROUTES"), "rte"));
                    }
                    String[] entries = line.split(",");
                    _routeName = entries[2];
                } else if (line.matches("W,.*")) {
                    String[] entries = line.split(",");
                    _vCards.add(createVCard(Integer.parseInt(entries[3]), entries[4], entries[5], entries[6]));
                }
            }
            return _vCards;
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<VCardImpl> tryParseItn(File inputFile) throws ParseException {

        try (InputStream fis = new FileInputStream(inputFile);
        		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-16"));) {

        	_vCards.clear();
            String line;

            int number = 1;
            while ((line = br.readLine()) != null) {
                while (!firstCharIsNumber(line)) {
                    line = line.substring(1);
                }
                String[] entries = line.split("\\|");
                if (entries.length >= 4) {
                    Double latitude = Double.parseDouble(entries[1]) / 100000;
                    Double longitude = Double.parseDouble(entries[0]) / 100000;
                    _vCards.add(createVCard(number++, entries[2], latitude, longitude));
                }
            }
            return _vCards;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<VCardImpl> tryParseRte2(File inputFile) throws ParseException {

    	try (InputStream fis = new FileInputStream(inputFile);
        		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-16"));) {

    		_vCards.clear();
            String line;

            int number = 1;
            while ((line = br.readLine()) != null) {
                String[] entries = line.split("\\|");
                if (entries.length >= 4) {
                    Double latitude = Double.parseDouble(entries[5]);
                    Double longitude = Double.parseDouble(entries[4]);
                    _vCards.add(createVCard(number++, entries[0].substring(1), latitude, longitude));
                }
            }
            return _vCards;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<VCardImpl> tryParseTrp(File inputFile) throws ParseException {
        try (InputStream fis = new FileInputStream(inputFile);
        		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-16"));) {
            
        	_vCards.clear();
        	String line;
            String[] entries;
            String name = null;
            Double longitude = null;
            Double latitude = null;
            
            int number;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Start Stop=")) {
                    entries = line.split(" ");
                    number = Integer.parseInt(entries[entries.length - 1]) + 1;
                    String stopLine = br.readLine();
                    // read till the end of this stop
                    while (stopLine != null && !stopLine.startsWith("End Sto")) {
                        if (stopLine.startsWith("Longitude=")) {
                            entries = stopLine.split("=");
                            longitude = Double.parseDouble(entries[entries.length - 1]) / 1000000;
                        }
                        if (stopLine.startsWith("Latitude=")) {
                            entries = stopLine.split("=");
                            latitude = Double.parseDouble(entries[entries.length - 1]) / 1000000;
                        }
                        if (stopLine.startsWith("Address=")) {
                            entries = stopLine.split("=");
                            name = entries[entries.length - 1];
                        }
                        stopLine = br.readLine();
                    }
                    //System.out.println("3-" + number + "-" + name + "-" + latitude + "-" + longitude);
                    _vCards.add(createVCard(number, name, latitude, longitude));
                }
            }
            return _vCards;
        } catch (Exception e) {
            return null;
        }
    }

    private VCardImpl createVCard(int number, String name, Double latitude, Double longitude) {
        VCardImpl vCard = new VCardImpl();
        setVCardName(vCard, _prefix + String.format(NUMBERFORMAT, number) + " " + name);
        vCard.setGeographicPosition(new GeographicPositionType(longitude, latitude));
        return vCard;
    }

    private VCardImpl createVCard(int number, String name, String latitude, String longitude) {
        VCardImpl vCard = new VCardImpl();
        setVCardName(vCard, _prefix + String.format(NUMBERFORMAT, number) + " " + name);
        vCard.setGeographicPosition(new GeographicPositionType(longitude, latitude));
        return vCard;
    }

    public ArrayList<VCardImpl> tryParseGpx(File inputFile) throws ParseException {
        _vCards.clear();
        try {
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            docBuilder.setValidating(false);
            //docBuilder.setNamespaceAware(true);
            DocumentBuilder domParser = docBuilder.newDocumentBuilder();
            Document document = domParser.parse(inputFile);
            NodeList routeNodeList = document.getElementsByTagName("rte");
            if (routeNodeList.getLength() > 1) {
                throw new ParseException(MessageFormat.format(Localizer.getLocalizedText("ERROR_MULTIPLE_ROUTES"), "gpx"));
            }
            NodeList childs = routeNodeList.item(0).getChildNodes();
            int number = 1;
            for (int i = 0; i < childs.getLength(); i++) {
                Node child = childs.item(i);
                String nodeName = child.getNodeName();
                if (nodeName.equals("name")) {
                    _routeName = child.getTextContent().trim();
                } else if (nodeName.equals("rtept")) {
                    _vCards.add(createVCardGpx(number++, child));
                }
            }
            return _vCards;
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    private VCardImpl createVCardGpx(int number, Node routePointNode) {
        VCardImpl vCard = new VCardImpl();
        String name = "";
        NodeList childs = routePointNode.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            Node child = childs.item(i);
            String nodeName = child.getNodeName();
            if (nodeName.equals("name")) {
                name = child.getTextContent().trim();
            }
        }
        setVCardName(vCard, _prefix + String.format(NUMBERFORMAT, number) + " " + name);
        String latitude = routePointNode.getAttributes().getNamedItem("lat").getNodeValue();
        String longitude = routePointNode.getAttributes().getNamedItem("lon").getNodeValue();
        vCard.setGeographicPosition(new GeographicPositionType(longitude, latitude));
        return vCard;
    }

    public ArrayList<VCardImpl> tryParseKml(File inputFile) throws ParseException {
        _vCards.clear();
        try {
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            docBuilder.setValidating(false);
            //docBuilder.setNamespaceAware(true);
            DocumentBuilder domParser = docBuilder.newDocumentBuilder();
            Document document = domParser.parse(inputFile);
            XPathExpression expr = xPath.compile("/kml/Document/Folder/Placemark");
            Object result = expr.evaluate(document, XPathConstants.NODESET);
            NodeList childs = (NodeList) result;
            //NodeSet folderNodes = (NodeSet) xPath.evaluate(folder, document, XPathConstants.NODESET);
            //System.out.println(childs.getLength());
            int number = 1;
            for (int i = 0; i < childs.getLength(); i++) {
                Node child = childs.item(i);
                 _vCards.add(createVCardKml(number++, child));
            }
            return _vCards;
        } catch (XPathExpressionException e) {
            //e.printStackTrace();
            return null;
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    private VCardImpl createVCardKml(int number, Node routePointNode) throws XPathExpressionException {
        String name;
        String coordinates;
        XPathExpression expr = xPath.compile("name/text()");
        name = (String) expr.evaluate(routePointNode, XPathConstants.STRING);
        expr = xPath.compile("Point/coordinates/text()");
        coordinates = (String) expr.evaluate(routePointNode, XPathConstants.STRING);
        String[] entries = coordinates.split(",");
        //System.out.println(name + "--" + entries[1] + "-" + entries[0]);
        return createVCard(number, name, entries[1], entries[0]);
    }

    private void setVCardName(VCardImpl vCard, String name) {
        name = normalize(name.replaceAll("/", "_"));
        vCard.setFormattedName(new FormattedNameType(name));
        vCard.setName(new NameType(name));
    }

    private boolean firstCharIsNumber(String line) {
        char firstChar = line.charAt(0);
        if ((firstChar >= '0' && firstChar <= '9') || firstChar == '+' || firstChar == '-') {
            return true;
        }
        return false;
    }

	public static String normalize (String input) {
		input = input.replaceAll("ß", "ss");
		input = Normalizer.normalize(input, Normalizer.Form.NFD);
		return input.replaceAll("[^\\x00-\\x7F]", "");		
	}
    
}
