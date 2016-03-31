/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dataone.test.apache.directory.server;

import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author waltz
 */
public class DSSuiteCase2TestUnit extends BaseTestContext {

    private static final Logger log = LoggerFactory.getLogger(DSSuiteCase2TestUnit.class);

    @Autowired
    @Qualifier("ldapContext")
    private LdapContext ldapCtx;

    @Test
    public void testNodeSearch() throws Exception {

        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = ldapCtx.search("dc=org", "(cn=urn:node:testcn)", searchControls);
        assertTrue(results.hasMore());

//		SearchResult firstResultItem = results.next();
        //allEntriesMap will hold a DN + the key/value pairs of the ldap entries
        HashMap<String, Map<String, String>> allEntriesMap = new HashMap<String, Map<String, String>>();

        while (results != null && results.hasMore()) {
            SearchResult si = results.next();
            String entryDN = si.getNameInNamespace();

            //return dn;
            // or we could double check
            // the key/value pairs of an ldap entry
            HashMap<String, String> attributesMap = new HashMap<String, String>();
            Attributes attributes = si.getAttributes();
            NamingEnumeration<? extends Attribute> values = attributes.getAll();
            while (values.hasMore()) {
                Attribute attribute = values.next();
                String attributeName = attribute.getID().toLowerCase();
                String attributeValue = (String) attribute.get();
                attributesMap.put(attributeName, attributeValue);
            }
            allEntriesMap.put(entryDN, attributesMap);
        }
        // Confirm that there are 1 entries
        assertTrue(allEntriesMap.size() == 1);
        log.info("AllEntrys = " + allEntriesMap.size());

    }
}
