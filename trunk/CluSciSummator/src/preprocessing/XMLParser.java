/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

import data.Document;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utility.Global;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class XMLParser {
    //Field Dokumen hasil parsing
    private Document _parsedDocument;

    private ArrayList<ArrayList<String>> contentContainer = new ArrayList<ArrayList<String>>(15);
    /*
     * Konstruktor
     */
    public XMLParser() {
        _parsedDocument = new Document();
        for (String tagRhetoric : Global.rhetoricalStatusList) {
            int indexRhetoric = Global.rhetoricalStatusList.indexOf(tagRhetoric);
            ArrayList<String> temp = new ArrayList<String>();
            contentContainer.add(temp);
        }
    }

    /**
     * Method parseDocument : Melakukan parsing satu dokumen
     * @param URI
     * @param docID 
     */
    public void parseDocument(String URI, int docID) {
        _parsedDocument.setID(docID);
        
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                StringBuffer textBuffer = new StringBuffer();
                ArrayList<String> authors = new ArrayList<String>();
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    textBuffer.setLength(0);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase(Global.listTag.TAG_TITLE.toString())) {
                        String temp = textBuffer.toString().trim();
                        temp = temp.replaceAll("\\r\\n|\\r|\\n", " ");
                        _parsedDocument.setTitle(temp);
                    } else if (qName.equalsIgnoreCase(Global.listTag.TAG_AUTHOR.toString())) {
                        authors.add(textBuffer.toString().trim());
                    } else if (qName.equalsIgnoreCase(Global.listTag.TAG_PAPER.toString())) {
                        _parsedDocument.setAuthors(authors);
                        for (String rhetoricStatus : Global.rhetoricalStatusList) {
                            int indexRhetoric = Global.rhetoricalStatusList.indexOf(rhetoricStatus);
                            _parsedDocument.content.put(rhetoricStatus, contentContainer.get(indexRhetoric));
                        }
                    } else {
                        String temp = textBuffer.toString().trim();
                        temp = temp.replaceAll("\\r\\n|\\r|\\n", " ");
                        
                        int indexRhetoric = Global.rhetoricalStatusList.indexOf(qName.toLowerCase());
                        if (indexRhetoric != -1) {
                            ArrayList<String> container = contentContainer.get(indexRhetoric);
                            container.add(temp);
                            contentContainer.remove(indexRhetoric);
                            contentContainer.add(indexRhetoric, container);
                        }
                    }
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    textBuffer.append(ch, start, length);
                }
            };
            saxParser.parse(URI, handler);
        } catch (Exception e) {
        }
    }

    /*
     * Public access dari dokumen hasil parsing
     */
    public Document getParsedDocument() {
        return _parsedDocument;
    }
}
