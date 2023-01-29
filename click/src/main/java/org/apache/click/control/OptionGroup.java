package org.apache.click.control;

import org.apache.click.util.HtmlStringBuffer;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 Provides a select Option Group element: &nbsp; &lt;optgroup&gt;&lt;/optgroup&gt;.
 <p/>
 The OptionGroup class uses an immutable design so Option instances can be
 shared by multiple Pages in the multi-threaded Servlet environment.
 This enables OptionGroup instances to be cached as static variables.
 <p/>
 For an OptionGroup code example see the {@link Option} Javadoc example.
 <p/>
 See also the W3C HTML reference:
 <a class="external" target="_blank" title="W3C HTML 4.01 Specification"
 href="http://www.w3.org/TR/html401/interact/forms.html#h-17.6">OPTGROUP</a>

 @see Select
 @see Option */
public class OptionGroup implements Serializable {
  @Serial private static final long serialVersionUID = -5230003770456184292L;

  /** The label for the OptionGroup. */
  protected final String label;
  /** The groups child Option/OptGroup objects. */
  protected final List children = new ArrayList();


  /**
   Create an OptionGroup with the given display label.

   @param label the display label for the OptionGroup
   */
  public OptionGroup (String label) {
    this.label = label;
  }

  // -------------------------------------------------- Public Attributes


  /**
   Return the OptionGroup's html tag: <tt>optgroup</tt>.

   @return the OptionGroup's html tag
   */
  public String getTag () {
    return "optgroup";
  }


  /**
   Add the given Option or OptionGroup object to this group.

   @param object the Option or OptionGroup to add
   */
  public void add (Object object) {
    getChildren().add(object);
  }


  /**
   Return the OptionGroup children.

   @return the OptionGroup children
   */
  public List getChildren () {
    return children;
  }


  /**
   Return the display label.

   @return the display label
   */
  public String getLabel () {
    return label;
  }

  // ----------------------------------------------------- Public Methods


  /**
   Return a HTML rendered Option string.

   @param select the parent Select
   @param buffer the specified buffer to render to
   */
  public void render (Select select, HtmlStringBuffer buffer) {
    buffer.elementStart(getTag());
    buffer.appendAttribute("label", getLabel());
    buffer.closeTag();

    List list = getChildren();
    for (Object object : list){
      if (object instanceof Option option){
        option.render(select, buffer);
      } else if (object instanceof OptionGroup optionGroup){
        optionGroup.render(select, buffer);
      } else {
        String msg = "Select option class not instance of Option"
            + " or OptionGroup: " + object.getClass().getName();
        throw new IllegalArgumentException(msg);
      }
    }

    buffer.elementEnd(getTag());
  }


  /**
   Return a HTML rendered OptionGroup string.

   @param select the parent Select

   @return a rendered HTML OptionGroup string

   @deprecated use {@link #render(org.apache.click.control.Select, org.apache.click.util.HtmlStringBuffer)} instead
   */
  @Deprecated
  public String renderHTML (Select select) {
    HtmlStringBuffer buffer = new HtmlStringBuffer();
    render(select, buffer);
    return buffer.toString();
  }
}