/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dataone.test.services.types.v1.node;

import org.dataone.test.knb.data.*;
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
public class NodeRecords {
        private static final String dataDir= "org/dataone/test/services/types/v1/nodes/";

        public List<String> getFiles() throws URISyntaxException, IOException {
            PackageFiles packageFiles = new PackageFiles();
            List<String> nodeNames = new ArrayList<String>();
            String[] nodeArray =  packageFiles.getFileNameListing(this.getClass(), dataDir);
            for (int i = 0; i < nodeArray.length; ++i) {
                if ((!nodeArray[i].isEmpty()) && (!nodeArray[i].endsWith(".class"))) {
                    nodeNames.add(nodeArray[i]);
                }
            }
            return nodeNames;
        }
        
        public InputStream getNodeStream(String fileName){
            return this.getClass().getResourceAsStream("/" + dataDir + fileName);
        }

}
