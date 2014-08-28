/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dataone.test.knb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import org.dataone.test.knb.data.KnbRecords;
import org.junit.Test;

/**
 *
 * @author waltz
 */
public class KnbDataRetrievalTest {
    
    @Test
    public void listPackageContents() throws URISyntaxException, IOException {
        KnbRecords emlRecords = new KnbRecords();
        List<String> fileNames = emlRecords.getFiles();
        for (String filename : fileNames) {
            InputStream in = emlRecords.getSystemMetadata(filename);
            System.out.println(filename);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
            byte[] bytes = new byte[512];
             
             // Read bytes from the input stream in bytes.length-sized chunks and write 
             // them into the output stream
             int readBytes; 
             while ((readBytes = in.read(bytes)) > 0) { 
                 outputStream.write(bytes, 0,readBytes); 
             } 
             String strBuffer = new String(outputStream.toByteArray()); 
             System.out.println(strBuffer);

             System.out.println("\n");
        }
        
    }
}
