/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dataone.test.knb.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.dataone.test.PackageFiles;

/**
 * This class will provide a list of files from the same package in which it is compiled
 * Since this class 
 * @author waltz
 */
public class KnbRecords {
        private static final String dataDir= "org/dataone/test/knb/data/";
        private static final String sysMetaDataDir= "org/dataone/test/knb/sysmeta/";
        public List<String> getFiles() throws URISyntaxException, IOException {
            PackageFiles packageFiles = new PackageFiles();
            List<String> pids = new ArrayList<String>();
            String[] pidArray =  packageFiles.getFileNameListing(this.getClass(), dataDir);
            for (int i = 0; i < pidArray.length; ++i) {
                if ((!pidArray[i].isEmpty()) && (!pidArray[i].endsWith(".class"))) {
                    pids.add(pidArray[i]);
                }
            }
            return pids;
        }
        
        public InputStream getData(String fileName){
            return this.getClass().getResourceAsStream("/" + dataDir + fileName);
        }
        public InputStream getSystemMetadata(String fileName) {
            return this.getClass().getResourceAsStream("/" + sysMetaDataDir + fileName);
        }
}
