/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.test.app.pageobject;

import es.us.isa.ideas.test.app.utils.TestProperty;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
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
    protected static final Logger LOG = Logger.getLogger(TestCase.class.getName());

    @Rule
    public Timeout globalTimeout = Timeout.seconds(30000); // 30 seconds max per method tested

    @BeforeClass
    public static void beforeClass() {
        LOG.info(TestProperty.getTestProperties().getProperty("test.environment"));
    }

    @Before
    public void before() {
        TEST_RESULT = false;
    }

}
