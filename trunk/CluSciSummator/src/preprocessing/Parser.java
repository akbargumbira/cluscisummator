/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

import data.Document;
import data.Sentence;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author ACER 4741
 */
public class Parser {   
    private Document _parsedDocument;
                
    enum listTag {
        TAG_PAPER("paper"),
        TAG_TITLE("title"),
        TAG_AUTHOR("author"),
        TAG_AIM("aim"),
        TAG_NOV_ADV("nov_adv"),
        TAG_CO_GRO("co_gro"),
        TAG_OTHR("othr"),
        TAG_PREV_OWN("prev_own"),
        TAG_OWN_MTHD("own_mthd"),
        TAG_OWN_FAIL("own_fail"),
        TAG_OWN_RES("own_res"),
        TAG_OWN_CONC("own_conc"),
        TAG_CODI("codi"),
        TAG_GAP_WEAK("gap_weak"),
        TAG_ANTISUPP("antisupp"),
        TAG_SUPPORT("support"),
        TAG_USE("use"),
        TAG_FU("fut");
        
        private String tagName;

        private listTag(String input) {
            this.tagName = input;
        }

        @Override
        public String toString() {
            return tagName;
        }
    }

    public Parser() {
        _parsedDocument = new Document();
    }
   
    public void parseDocument(String URI) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                StringBuffer textBuffer = new StringBuffer();
                ArrayList<String> authors = new ArrayList<String>();
                ArrayList<Sentence> content = new ArrayList<Sentence>();
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    textBuffer.setLength(0);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase(listTag.TAG_TITLE.toString())) {
                        _parsedDocument.setTitle(textBuffer.toString().trim());
                    } else if (qName.equalsIgnoreCase(listTag.TAG_AUTHOR.toString())) {
                        authors.add(textBuffer.toString().trim());
                    } else if (qName.equalsIgnoreCase(listTag.TAG_PAPER.toString())) {
                        _parsedDocument.setAuthors(authors);
                        _parsedDocument.setContent(content);
                    } else if (qName.equalsIgnoreCase(listTag.TAG_AIM.toString())) {
                       Sentence sentence = new Sentence(textBuffer.toString().trim(), listTag.TAG_AIM.toString());
                       content.add(sentence);
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

    public Document getParsedDocument() {
        return _parsedDocument;
    }
    
}