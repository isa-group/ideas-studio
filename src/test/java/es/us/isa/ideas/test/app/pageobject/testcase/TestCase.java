/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject.testcase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class TestCase {

    protected static boolean TEST_RESULT;
    
    @Rule
    public Timeout globalTimeout = Timeout.seconds(30000); // 30 seconds max per method tested

    @Before
    public void testResult() {
        TEST_RESULT = false;
    }

}
