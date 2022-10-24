package net.sf.clickclick.control;

import junit.framework.TestCase;
import org.apache.click.control.Form;
import org.apache.click.control.Select;

public class StyledOptionTest extends TestCase {

    /**
     * test if Id is correctly set (form_select_option)
     */
    public void testGetIdAttr() {
        Form form = new Form("formName");
        Select select = new Select("selectName");
        StyledOption option = new StyledOption("", "optionName");
        form.add(select);
        select.add(option);

        // with id property set
        String expectedId = "customId";
        option.setId("customId");
        assertEquals(expectedId, option.getIdAttr(select));

        // full path available
        String fullId = "formName_selectName_optionName";
        option.setId(null);
        assertEquals(fullId, option.getIdAttr(select));

        // path up to select available
        String partialId = "selectName_optionName";
        form.remove(select);
        assertEquals(partialId, option.getIdAttr(select));

        // only option available
        String onlyOptionId = "optionName";
        assertEquals(onlyOptionId, option.getIdAttr(null));
    }
}
