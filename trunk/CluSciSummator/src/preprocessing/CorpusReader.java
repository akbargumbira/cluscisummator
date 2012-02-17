/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preprocessing;

import data.Document;
import data.DocumentCollection;
import java.util.ArrayList;

/**
 *
 * @author Akbar Gumbira (akbargumbira@gmail.com)
 */
public class CorpusReader {
    private DocumentCollection _parsedDocumentCollection;
        
    public CorpusReader() {
        _parsedDocumentCollection = new DocumentCollection();
    }

    public DocumentCollection getParsedDocumentCollection() {
        return _parsedDocumentCollection;
    }
        
    /**
     * Method parseDocumentCollection : Melakukan parsing semua dokumen XML dari alamat-alamat di URIS
     * 
     * @param URIS 
     */
    public void parseDocumentCollection(ArrayList<String> URIS){
        int docID = 1;
        ArrayList<Document> documents = new ArrayList<Document>();
        for (String URI : URIS) {
           XMLParser parser = new XMLParser();
           parser.parseDocument(URI, docID);
           Document parsedDoc = parser.getParsedDocument();
           documents.add(parsedDoc);

           ++docID;
       }
        _parsedDocumentCollection.setDocumentCollection(documents);
    }
        
        
}
