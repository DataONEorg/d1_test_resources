/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dataone.test.services.types.v1.node;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

/**
 * This is a test of Junit Suite cooperating with Spring Junit Testing
 * @author waltz
 */

public class NodeRecordsTest  {
    

    private NodeRecords readNodeRecords = new NodeRecords();
    @Test
    public void retrieveANodeList() throws URISyntaxException, IOException {
        List<String> nodeRecords = readNodeRecords.getFiles();
        assert(!nodeRecords.isEmpty());
    }
}
