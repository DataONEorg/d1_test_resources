# Java development (testing really):

![Java CI](https://github.com/DataONEorg/d1_test_resources/workflows/Java%20CI/badge.svg)

This maven package is set up as the location for DataONE common test resources
to be made available for unit and integration tests.  It will be listed as a
dependency of d1_common_java, for transparent incorporation to java developers
using the `d1_*_java packages`. (`d1_common_java` is a dependency of most, if
not all so far. See `d1_common_java pom.xml` dependency section otherwise.)

## Java Resources

It is meant that callers would use
`this.getClass().getResourceAsStream("relative/path/to/resource")`  to access
the resources, and that resources will be appropriately organized in
subdirectories according to usage and perhaps type.

Special rules apply to `getResourceAsStream()` for accessing files, so for
example, to access the file:

```
d1_test_resources/src/main/resources/D1shared/selfTest/simpleDummyResource.txt
```

one would invoke the method thusly:

```
this.getClass().getResourceAsStream("D1shared/selfTest/simpleDummyResource.txt");
```
    
## Java Classes

Classes may be added in order to supply Resource collections to unit tests or to extend Junit tests that may be used by all DataONE java components.

## PackageFiles

If a collection of resources must be used instead of a single file, then the
`PackageFiles.java` class is available for extension. As background,
Introspection can be used to discover classes in a  java package, but it cannot
be used to discover other resources, such as xml files or ldif files. The
`PackageFiles.java` class is a mechanism to discover resources other than
classes in a package. `The PackageFiles.java` class to work must have a java
class in the same package as the other resources to discover. Therefore, when D1 needed a collection of files from the KNB node to be used for unit testing, The `PackageFiles.java` class was extended in the maven directory `src/main/java` in the package:

```
org.dataone.test.knb.data
```

with the class:

```
KnbRecords.java
```

The xml resources where placed in the maven directory `src/main/resources` in the same package:

```
org.dataone.test.knb.data
```

with the xml files:

```
Blandy.76.2
Blandy.77.1
etc
```

The extension of the class also allowed for a collection of systemMetadata files to be found in the specified package:

```
org.dataone.test.knb.sysmeta
```

## ApacheDSSuiteRunner

ApacheDSSuiteRunner is an extension of Junit's Suite class and is
based on `org.apache.directory.server.core.integ.FrameworkRunner`.

The `ApacheDSSuiteRunner` allows for ApacheDS annotations such as `@CreateDS`, 
`@ApplyLdifFiles` and `@CreateLdapServer` to be applied to a Junit suite of
tests.  This allows for ApacheDS to be setup and shutdown and run only once  for multiple junit test  classes in a java component rather than having to setup and shutdown ApacheDS for each class executed.

ApacheDS will load a `dataone-schema.ldif` file in order to create a schema
that ApacheDS  will use as its LDAP tree.  In order to generate thie
dataone-schema.ldif file, you must install Apache Directory Studio (minimal
2.0.0-M9).  Open up Apache Directory Studio and  from the File Menu, choose
New. You should choose Schema Editor and then New  Schema Project. Specify the
Project Name as DataONETest and confirm the option Offline Schema is choosen.
Click Next. The next screen confirm that you are viewing the schemas for
ApacheDS and  press the button 'Select All' followed by pressing the button
Finish.

You will now need to import the DataONE OpenLDAP compliant schema into
ApacheDS Studio. From the File Menu, choose Import.  On the Import wizard
window, choose Schema Editor > Schemas from Open Ldap Files. The file you will
import is in the dataone-cn-os-core package. Make certain you have 
`https://repository.dataone.org/software/cicore/trunk/cn-buildout/dataone-cn-os-core/usr/share/dataone-cn-os-core/debian/ldap/dataone.schema`
downloaded. Press the Next button on the Import Wizard, on the next screen,
using the Browse Button, find the directory that holds the dataone.schema
file. Click on the dataone.schema file that was found. Press the Finish
button.

If everything worked well, then you should see 'dataone' in the Schema Editor
perspective in the Schema view.  You will next Export the Dataone Schema as an
Active Directory schema. From the File Menu, choose Export.  On the Export
wizard window,  choose Schema Editor > Schemas for Apache DS. Press the Next
button. Check the 'dataone' schema. You should 'Export each schema as a
separate file' and choose the directory in which the dataone-schema.ldif file
is located in `d1_test_resources` package. 
`d1_test_resources/src/main/resources/org/dataone/test/apache/directory/server`
Press Finish button.

A dataone.ldif should show up in the org.dataone.test.apache.directory.server
package for the main resources of the project. Diff the dataone.ldif to the
data-schema.ldif file to confirm changes. Then move the dataone.ldif to the
data-schema.ldif. Commit the changes and make certain to rebuild the project
to test out the changes.



