 /*
 * Based on org.apache.directory.server.core.integ.FrameworkRunner
 * http://svn.apache.org/repos/asf/directory/apacheds/branches/apacheds-txns/test-framework/src/main/java/org/apache/directory/server/core/integ/FrameworkRunner.java

 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.dataone.test.apache.directory.server.integ;

import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.changelog.ChangeLog;
import org.apache.directory.server.core.factory.DSAnnotationProcessor;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.core.factory.PartitionFactory;
import org.apache.directory.server.factory.ServerAnnotationProcessor;
import org.apache.directory.server.i18n.I18n;
import org.apache.directory.server.kerberos.kdc.KdcServer;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.log4j.Logger;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Based on org.apache.directory.server.core.integ.FrameworkRunner
 * http://svn.apache.org/repos/asf/directory/apacheds/branches/apacheds-txns/test-framework/src/main/java/org/apache/directory/server/core/integ/FrameworkRunner.java
 *
 * The ApacheDSSuiteRunner allows for ApacheDS annotations such as CreateDS, ApplyLdifFiles and CreateLdapServer to be
 * applied to a Junit suite of tests. This allows for ApacheDS to be setup and shutdown and run only once for multiple
 * junit test classes in a java component rather than having to setup and shutdown ApacheDS for each class executed.
 *
 *
 * @author waltz
 * @see org.dataone.test.apache.directory.server.ApacheDirectorySuiteTest
 *
 */
public class ApacheDSSuiteRunner extends Suite {

    /**
     * The class responsible for running all the tests. it reads the annotations, initializes the DirectoryService, call
     * each test and do the cleanup at the end.
     *
     * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
     */
    
    // A logger for this class
    static Logger logger = Logger.getLogger(ApacheDSSuiteRunner.class);


    // The 'service' field in the run tests

    private static final String SET_SERVICE_METHOD_NAME = "setService";


    // The 'ldapServer' field in the run tests

    private static final String SET_LDAP_SERVER_METHOD_NAME = "setLdapServer";


    // The 'kdcServer' field in the run tests

    private static final String SET_KDC_SERVER_METHOD_NAME = "setKdcServer";


    // The DirectoryService for this class, if any

    private DirectoryService classDS = null;


    // The LdapServer for this class, if any

    private static LdapServer classLdapServer = null;


    // The KdcServer for this class, if any

    private  KdcServer classKdcServer = null;

    /**
     * Called reflectively on classes annotated with <code>@RunWith(Suite.class)</code>
     *
     * @param klass the root class
     * @param builder builds runners for classes in the suite
     */
    public ApacheDSSuiteRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    /**
     * Call this when there is no single root class (for example, multiple class names passed on the command line to
     * {@link org.junit.runner.JUnitCore}
     *
     * @param builder builds runners for classes in the suite
     * @param classes the classes in the suite
     */
    public ApacheDSSuiteRunner(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(builder, classes);
    }

    /**
     * Call this when the default builder is good enough. Left in for compatibility with JUnit 4.4.
     *
     * @param klass the root of the suite
     * @param suiteClasses the classes in the suite
     */
    protected ApacheDSSuiteRunner(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }

    /**
     * Called by this class and subclasses once the classes making up the suite have been determined
     *
     * @param builder builds runners for classes in the suite
     * @param klass the root of the suite
     * @param suiteClasses the classes in the suite
     */
    protected ApacheDSSuiteRunner(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(builder, klass, suiteClasses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final RunNotifier notifier) {
        // Before running any test, check to see if we must create a class DS
        // Get the LdapServerBuilder, if any
        CreateLdapServer classLdapServerBuilder = getDescription().getAnnotation(CreateLdapServer.class);

        try {
            classDS = DSAnnotationProcessor.getDirectoryService(getDescription());
            long revision = 0L;
            DirectoryService directoryService = null;

            if (classDS != null) {
                // We have a class DS defined, update it
                directoryService = classDS;

                DSAnnotationProcessor.applyLdifs(getDescription(), classDS);
            } else {
                // No : define a default class DS then
                DirectoryServiceFactory dsf = DefaultDirectoryServiceFactory.class.newInstance();

                directoryService = dsf.getDirectoryService();
                // enable CL explicitly cause we are not using DSAnnotationProcessor
                directoryService.getChangeLog().setEnabled(true);

                dsf.init("default" + UUID.randomUUID().toString());

                // Stores the defaultDS in the classDS
                classDS = directoryService;

                // Load the schemas
                DSAnnotationProcessor.loadSchemas(getDescription(), directoryService);

                // Apply the class LDIFs
                DSAnnotationProcessor.applyLdifs(getDescription(), directoryService);
            }

            // check if it has a LdapServerBuilder
            // then use the DS created above
            if (classLdapServerBuilder != null) {
                classLdapServer = ServerAnnotationProcessor.createLdapServer(getDescription(), directoryService);
            }

            if (classKdcServer == null) {
                int minPort = getMinPort();

                classKdcServer = ServerAnnotationProcessor.getKdcServer(getDescription(), directoryService,
                        minPort + 1);
            }

            // print out information which partition factory we use
            DirectoryServiceFactory dsFactory = DefaultDirectoryServiceFactory.class.newInstance();
            PartitionFactory partitionFactory = dsFactory.getPartitionFactory();
            logger.debug("Using partition factory " + partitionFactory.getClass().getSimpleName());

            // Now run the class
            super.run(notifier);

            if (classLdapServer != null) {
                classLdapServer.stop();
            }

            if (classKdcServer != null) {
                classKdcServer.stop();
            }

            // cleanup classService if it is not the same as suite service or
            // it is not null (this second case happens in the absence of a suite)
            if (classDS != null) {
                logger.debug("Shuting down DS for " + classDS.getInstanceId());
                classDS.shutdown();
                FileUtils.deleteDirectory(classDS.getInstanceLayout().getInstanceDirectory());
            } else {
                // Revert the ldifs
                // We use a class or suite DS, just revert the current test's modifications
                revert(directoryService, revision);
            }
        } catch (Exception e) {
            logger.error(I18n.err(I18n.ERR_181, getTestClass().getName()));
            logger.error(e.getLocalizedMessage());
            notifier.fireTestFailure(new Failure(getDescription(), e));
        } finally {
            // help GC to get rid of the directory service with all its references
            classDS = null;
            classLdapServer = null;
            classKdcServer = null;
        }
    }

    /**
     * Get the lower port out of all the transports
     */
    private int getMinPort() {
        int minPort = 0;

        return minPort;
    }

    private long getCurrentRevision(DirectoryService dirService) throws Exception {
        if ((dirService != null) && (dirService.getChangeLog().isEnabled())) {
            long revision = dirService.getChangeLog().getCurrentRevision();
            logger.debug("Create revision " + revision);

            return revision;
        }

        return 0;
    }

    private void revert(DirectoryService dirService, long revision) throws Exception {
        if (dirService == null) {
            return;
        }

        ChangeLog cl = dirService.getChangeLog();
        if (cl.isEnabled() && (revision < cl.getCurrentRevision())) {
            logger.debug("Revert revision " + revision);
            dirService.revert(revision);
        }
    }

    public static LdapServer getLdapServer() {
        return classLdapServer;
    }
    
}
