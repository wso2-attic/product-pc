package org.wso2.carbon.pc.core;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.common.SolrException;
import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.indexing.AsyncIndexer;
import org.wso2.carbon.registry.indexing.IndexingConstants;
import org.wso2.carbon.registry.indexing.indexer.Indexer;
import org.wso2.carbon.registry.indexing.solr.IndexDocument;

import java.io.*;
import java.util.*;



/**
 * Created by sathya on 3/16/16.
 */
public class ProcessPDFIndexer implements Indexer{

    private static final Log log = LogFactory.getLog(ProcessPDFIndexer.class);

    @Override
    public IndexDocument getIndexedDocument(AsyncIndexer.File2Index fileData) throws SolrException, RegistryException {

        try {
            PDFParser e = new PDFParser(new ByteArrayInputStream(fileData.data));
            e.parse();
            COSDocument msg1 = e.getDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            String docText = stripper.getText(new PDDocument(msg1));
            //System.out.println(docText);
            msg1.close();
            IndexDocument indexDocument = new IndexDocument(fileData.path, docText, (String)null);
            Map<String, java.util.List<String>> fields = new HashMap<String, java.util.List<String>>();
            fields.put("path", Arrays.asList(fileData.path));
            if (fileData.mediaType != null) {
                fields.put(IndexingConstants.FIELD_MEDIA_TYPE, Arrays.asList(fileData.mediaType));
            } else {
                fields.put(IndexingConstants.FIELD_MEDIA_TYPE, Arrays.asList("application/pdf"));
            }

            indexDocument.setFields(fields);

            return indexDocument;
        } catch (IOException ex) {
            String msg = "Failed to write to the index";
            log.error(msg, ex);
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, msg);
        }
    }
    public String processPDF(InputStream pdfStream){

        try {

                PdfReader reader = new PdfReader(pdfStream);
                int numberOfPages = reader.getNumberOfPages();
                String str=PdfTextExtractor.getTextFromPage(reader, 2); //Extracting the content from a particular page.
               // System.out.println(str);
                reader.close();

        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

}
