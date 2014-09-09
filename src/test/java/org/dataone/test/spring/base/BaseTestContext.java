/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dataone.test.spring.base;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A base class that should provide the same spring context to each Test Class
 * 
 * This is a test of Junit Suite cooperating with Spring Junit Testing
 * 
 * @author waltz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/org/dataone/configuration/testApplicationContext.xml" })
public class BaseTestContext {
    
}
