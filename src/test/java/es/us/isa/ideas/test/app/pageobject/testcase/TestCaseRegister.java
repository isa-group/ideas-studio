package es.us.isa.ideas.test.app.pageobject.testcase;

import es.us.isa.ideas.test.app.pageobject.login.RegisterPage;
import es.us.isa.ideas.test.app.pageobject.login.RegisterSocialTwitterPage;
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
public class TestCaseRegister extends TestCase {

    @Test
    public void step01_registerFormFieldMaxLength() {
        String bigString = "1729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734172937498172389471928374981273498719283471982739841729384719823748917341729374981723894719283749812734987192834719827398417293847198237489173417293749817238947192837498127349871928347198273984172938471982374891734";
        String name = bigString;
        String email = bigString;
        String phone = bigString;
        String address = bigString;
        
        RegisterPage.testRegister(name, email, phone, address);
    }
    
    @Test
    public void step02_twitterRegister() {
        String twUser = "IDEAS_ISA";
        String twPass = "ideas.isa.us%TWITTER";
        RegisterSocialTwitterPage.testTwitterSocialLogin(twUser, twPass);
    }
    
}
