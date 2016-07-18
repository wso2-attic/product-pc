/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.pc.core.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.solr.common.SolrException;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.indexing.AsyncIndexer;
import org.wso2.carbon.registry.indexing.IndexingConstants;
import org.wso2.carbon.registry.indexing.indexer.Indexer;
import org.wso2.carbon.registry.indexing.solr.IndexDocument;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class for indexing documents with doc, docx file formats
 */
public class DocumentIndexer implements Indexer {
    public static final Log log = LogFactory.getLog(DocumentIndexer.class);

    @Override public IndexDocument getIndexedDocument(AsyncIndexer.File2Index fileData)
            throws SolrException, RegistryException {
        try {
            String wordText = null;
            try {
                //Extract MSWord 2003 document files
                POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(fileData.data));

                WordExtractor msWord2003Extractor = new WordExtractor(fs);
                wordText = msWord2003Extractor.getText();

            } catch (OfficeXmlFileException e) {
                //if 2003 extraction failed, try with MSWord 2007 document files extractor
                XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(fileData.data));

                XWPFWordExtractor msWord2007Extractor = new XWPFWordExtractor(doc);
                wordText = msWord2007Extractor.getText();

            } catch (Exception e) {
                //The reason for not throwing an exception is that since this is an indexer that runs in the background
                //throwing an exception might lead to adverse behaviors in the client side and might lead to
                //other files not being indexed
                String msg = "Failed to extract the document while indexing";
                log.error(msg, e);
            }
            IndexDocument indexDoc = new IndexDocument(fileData.path, wordText, null);

            Map<String, List<String>> fields = new HashMap<String, List<String>>();
            fields.put("path", Arrays.asList(fileData.path));
            if (fileData.mediaType != null) {
                fields.put(IndexingConstants.FIELD_MEDIA_TYPE, Arrays.asList(fileData.mediaType));
            } else {
                fields.put(IndexingConstants.FIELD_MEDIA_TYPE, Arrays.asList("application/pdf"));
            }

            indexDoc.setFields(fields);

            return indexDoc;

        } catch (IOException e) {
            String msg = "Failed to write to the index";
            log.error(msg, e);
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, msg);
        }
    }
}
