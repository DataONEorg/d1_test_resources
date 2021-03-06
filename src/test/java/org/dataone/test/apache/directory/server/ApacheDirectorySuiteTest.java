/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dataone.test.apache.directory.server;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateAuthenticator;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.authn.SimpleAuthenticator;
import org.dataone.test.apache.directory.server.integ.ApacheDSSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author waltz
 */
@RunWith(ApacheDSSuiteRunner.class)
@SuiteClasses({ DSSuiteCase1TestUnit.class, DSSuiteCase2TestUnit.class })
@CreateDS(allowAnonAccess = false, enableAccessControl=true,  authenticators ={@CreateAuthenticator(type = SimpleAuthenticator.class)} ,name = "org", partitions = { @CreatePartition(name = "org", suffix = "dc=org") })
@ApplyLdifFiles({"org/dataone/test/apache/directory/server/dataone-schema.ldif", "org/dataone/test/apache/directory/server/dataone-base-data.ldif", "org/dataone/test/services/types/v1/nodes/ldif/devNodeList.ldif", "org/dataone/test/services/types/v1/person/ldif/devTestPrincipal.ldif"})
@CreateLdapServer(transports = { @CreateTransport(address = "localhost", protocol = "LDAP", port=10389) })
public class ApacheDirectorySuiteTest {
/* To Run With Spring
    Test Suite

1) Have a Base test Class which has @Runwith() and @Contextconfiguration() .
    @RunWith(SpringJUnit4ClassRunner.class) (????)
    @ContextConfiguration(locations = { "classpath:/org/dataone/configuration/testApplicationContext.xml" }) (???)
    Extend this base class to all the test cases required. Make sure the sub class test case should not have @Runwith and @ContextConfiguration annotations
    
2) Create a test suite with
    @Runwith (Suite.class)
    @Suiteclasses({})

now the the context will be loaded and Autowire will work .

Above way worked for me . Just wanted to share . 
    */
}
