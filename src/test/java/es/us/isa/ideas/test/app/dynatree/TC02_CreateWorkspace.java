package es.us.isa.ideas.test.app.dynatree;

import es.us.isa.ideas.test.app.pageobject.editor.WorkspaceSection;
import org.junit.Test;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class TC02_CreateWorkspace extends es.us.isa.ideas.test.utils.TestCase {

    @Test
    public void testCreateWorkspace() {
        
        String wsName = "wsTest";
        String wsDesc = "descTest";
        String wsTags = "tag1";
        
        WorkspaceSection.testCreateWorkspace(wsName, wsDesc, wsTags);
        
    }

}
