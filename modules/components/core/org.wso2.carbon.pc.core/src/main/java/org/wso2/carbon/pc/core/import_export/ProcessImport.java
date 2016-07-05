package org.wso2.carbon.pc.core.import_export;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.api.UserStoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ProcessImport {
    private static final Log log = LogFactory.getLog(ProcessImport.class);

    public void importProcesses(InputStream processZipInputStream, String user)
            throws IOException, RegistryException, ProcessCenterException {

        byte[] buffer = new byte[2048];
        ZipInputStream zipInputStream = new ZipInputStream(processZipInputStream);
       // String outdir = "Imports";
        new File(ImportExportConstants.IMPORTS_DIR).mkdirs();

        try {
            ZipEntry entry;
            int counter = 0;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                counter++;
                String outpath = ImportExportConstants.IMPORTS_DIR + "/" + entry.getName();
                String dirPath = outpath.substring(0, outpath.lastIndexOf("/"));
                new File(dirPath).mkdirs();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(outpath);
                    int len = 0;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                } finally {
                    if (fileOutputStream != null)
                        fileOutputStream.close();
                }
            }
        } finally {
            zipInputStream.close();
        }

        //check if the importing processes are already available
        boolean processesAlreadyAvaiableInPC = checkIsAlreadyAvailble();
        if (processesAlreadyAvaiableInPC){
            return;
        }

    }

    private boolean checkIsAlreadyAvailble() throws IOException, ProcessCenterException, RegistryException {
        File folder = new File(ImportExportConstants.IMPORTS_DIR);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles[0].isDirectory() && listOfFiles.length == 1) {
            String zipHomeDirectoryName = listOfFiles[0].getPath();
            File packageFolder = new File(zipHomeDirectoryName);
            File[] listOfProcessDirs = packageFolder.listFiles();
            ArrayList<String> processListinPC = getProcessList();

            for (File file : listOfProcessDirs) {
                if (file.isDirectory()) {
                    String fileName = file.getName();
                    if(processListinPC.contains(fileName)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<String> getProcessList() throws ProcessCenterException, RegistryException {

        ArrayList<String> processList = new ArrayList<String>();
        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            UserRegistry reg = registryService.getGovernanceSystemRegistry();

            String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
            for (String processPath : processPaths) {
                String processName = processPath.split("/")[2];
                String processVersion = processPath.split("/")[3];
                processList.add(processName + "-" + processVersion);
            }
        } else {
            String msg = "Registry service not available for retrieving processes.";
            throw new ProcessCenterException(msg);
        }
        return processList;
    }

    public void putProcessRxt(String userName) throws RegistryException, UserStoreException {
       /* File fXmlFile = new File("Imports/staff.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            UserRegistry reg = registryService.getGovernanceUserRegistry(userName);
            RegPermissionUtil.setPutPermission(registryService, userName, ProcessCenterConstants.AUDIT.PROCESS_PATH);
            String processAssetContent = xmlToString(doc);
            Resource processAsset = reg.newResource();
            processAsset.setContent(processAssetContent);
            processAsset.setMediaType("application/vnd.wso2-process+xml");
            String processAssetPath = "processes/" + processName + "/" + processVersion;
            reg.put(processAssetPath, processAsset);
        }*/
    }
}
