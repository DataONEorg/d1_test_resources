/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dataone.test.spring;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.dataone.test.services.types.v1.node.NodeRecords;
import org.dataone.test.spring.base.BaseTestContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This is a test of Junit Suite cooperating with Spring Junit Testing
 * @author waltz
 */

public class SpringComponentB extends BaseTestContext {

    @Autowired
    @Qualifier("readNodeRecords")
    private NodeRecords readNodeRecords;

    @Test
    public void retrieveBNodeList() throws URISyntaxException, IOException {
        List<String> nodeRecords = readNodeRecords.getFiles();
        assert (!nodeRecords.isEmpty());

    }
}
