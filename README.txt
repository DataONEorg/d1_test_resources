This maven package is set up as the location for DataONE common test resources to be made
available for unit and integration tests.  It will be listed as a dependency of d1_common_java,
for transparent incorporation to java developers using the d1_*_java packages. (d1_common_java is
a dependency of most, if not all so far. See d1_common_java pom.xml dependency section otherwise.)

Only files under src/main/resources will be available outside this package.


Java development (testing really):
==================================
It is meant that callers would use this.getClass().getResourceAsStream("relative/path/to/resource") 
to access the resources, and that resources will be appropriately organized in subdirectories
according to usage and perhaps type.

Special rules apply to getResourceAsStream() for accessing files, so for example,
to access the file:
    d1_test_resources/src/main/resources/D1shared/selfTest/simpleDummyResource.txt
 
one would invoke the method thusly:
    this.getClass().getResourceAsStream("D1shared/selfTest/simpleDummyResource.txt");
    
    


