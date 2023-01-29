package net.sf.clickclick.control;

import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * The StyledOption class adds the <tt>id</tt> attribute to the html option
 * element thus allowing you to add specific css makeup to individual html
 * instances.
 * <p/>
 * The id is generated as follows: <code>formName-selectName-optionName</code>
 * Please note that the possible styles that can be applied is limited and
 * rendering differs greatly between browser.
 * <p/>
 * Tested elements:
 * <p/>
 * Safe on all browsers: <tt>background-color</tt> and <tt>color</tt>.
 * <p/>
 * Firefox also supports background-image, font, border and padding.
 * <p/>
 * Firefox however does not apply the style of the selected element to the
 * Select control. You can simulate this behaviour with Javascript if you so
 * wish (see samplepage).
 * <p/>
 * See the samplepage for inspiration.
 */
public class StyledOption extends Option {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------- Variables

    /** The option name. */
    private final String name;

    /** The option id. */
    private String id;

    /**
     * Create a new StyledOption for the given value.
     * <p/>
     * The value will be used as the StyledOption's name and label.
     *
     * @param value the value of the StyledOption
     */
    public StyledOption(String value) {
        super(value);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Value cannot be null if it is also to be used as Name.");
        }
        name = value;
    }

    /**
     * Create a new StyledOption for the given value and name.
     * <p/>
     * The name will be used as the StyledOption's label.
     *
     * @param value the value of the StyledOption
     * @param name the name of the StyledOption
     */
    public StyledOption(Object value, String name) {
        super(value, name);
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Please provide a name for this option.");
        }
        this.name = name;
    }

    /**
     * Create a new StyledOption for the given value, name and label.
     *
     * @param value the value of the StyledOption
     * @param name the name of the StyledOption
     * @param label the label of the StyledOption
     */
    public StyledOption(Object value, String name, String label) {
        super(value, label);
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Please provide a name for this option.");
        }
        this.name = name;
    }

    // ------------------------------------------------------------------

    /**
     * Return the option name.
     *
     * @return name of the option
     */
    public String getName() {
        return name;
    }

    /**
     * Return the custom id set for this option.
     *
     * @return the HTML id attribute
     */
    public String getId() {
        return id;
    }

    /**
     * The id of this option, leave empty to let StyledOption generate the
     * default id (formName-selectName-optionName).
     *
     * @param id the HTML id attribute
     */
    public void setId(String id) {
        this.id = id;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the HTML representation of the Option.
     * <p/>
     * Copied everything from Option, just adding the id attribute.
     */
    @Override public void render(Select select, HtmlStringBuffer buffer) {
        buffer.elementStart(getTag());

        if (select.isMultiple()) {
            if (!select.getSelectedValues().isEmpty()) {

                // Search through selection list for matching value
                List values = select.getSelectedValues();
                for (int i = 0, size = values.size(); i < size; i++) {
                    String value = values.get(i).toString();
                    if (getValue().equals(value)) {
                        buffer.appendAttribute("selected", "selected");
                        break;
                    }
                }
            }

        } else {
            if (getValue().equals(select.getValue())) {
                buffer.appendAttribute("selected", "selected");
            }
        }

        buffer.appendAttribute("value", getValue());
        buffer.appendAttribute("id", getIdAttr(select));

        buffer.closeTag();
        buffer.appendEscaped(getLabel());
        buffer.elementEnd(getTag());
    }

    /**
     * Option is not a field so we need to pass the parent manually.
     *
     * @param select the parent Select control
     * @return the ID attribute value
     */
    String getIdAttr(Select select) {
        if (StringUtils.isEmpty(id)) {
            String result = null;
            if (select != null) result = select.getId();
            if (StringUtils.isEmpty(result))
                result = "";
            else
                result += "_";
            return result + name;
        } else {
            return id;
        }
    }
}