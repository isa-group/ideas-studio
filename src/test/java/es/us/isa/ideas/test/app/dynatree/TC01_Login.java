package es.us.isa.ideas.test.app.dynatree;

import es.us.isa.ideas.test.app.pageobject.login.LoginPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TC01_Login extends es.us.isa.ideas.test.utils.TestCase {

    @Test
    public void dynatree_login() {
        LoginPage.testLogin("autotester", "autotester");
    }

}
