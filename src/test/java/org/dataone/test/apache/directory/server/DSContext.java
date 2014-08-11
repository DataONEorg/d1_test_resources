/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dataone.test.apache.directory.server;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 *
 * For testing the ApacheDS Suite Runner The hardcoded values should reflect the annotations of the Suite class that
 * implements ApacheDSSuiteRunner
 *
 * @author waltz
 */
public class DSContext {

    static final private String server = "ldap://localhost:10389";
    static final private String admin = "cn=admin,dc=dataone,dc=org";
    static final private String password = "password";

    static LdapContext ctx = null;

    public static LdapContext getDefaultContext() throws NamingException {
        if (ctx == null) {
            Hashtable<String, String> env = new Hashtable<String, String>();
            /*
             * Specify the initial context implementation to use. This could also be
             * set by using the -D option to the java program. For example, java
             * -Djava.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory \
             * Modattrs
             */
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            /* Specify host and port to use for directory service */
            env.put(Context.PROVIDER_URL, server);
            /* specify authentication information */
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, admin);
            env.put(Context.SECURITY_CREDENTIALS, password);

            /* get a handle to an Initial DirContext */
            ctx = new InitialLdapContext(env,
                    new Control[0]);

        }
        return ctx;
    }

    public static Map<String, String> getAttributesMap(SearchResult si) throws NamingException {

                //return dn;
        // or we could double check
        HashMap<String, String> attributesMap = new HashMap<String, String>();
        Attributes attributes = si.getAttributes();
        NamingEnumeration<? extends Attribute> values = attributes.getAll();
        while (values.hasMore()) {
            Attribute attribute = values.next();
            String attributeName = attribute.getID().toLowerCase();
            String attributeValue = (String) attribute.get();
            attributesMap.put(attributeName, attributeValue);
        }
        return attributesMap;
    }
}
