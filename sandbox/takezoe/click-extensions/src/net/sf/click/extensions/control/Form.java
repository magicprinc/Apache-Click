package net.sf.click.extensions.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.arnx.jsonic.JSON;
import net.sf.click.control.Field;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.util.HtmlStringBuffer;

/**
 * Provides a extended Form control which has followng features:
 * <ul>
 *   <li>Show the confirmation dialog by JavaScript</li>
 *   <li>Field auto registration which declared in the subclasses</li>
 *   <li>Allows a Submit button which does not do JavaScript validation</li>
 * </ul>
 * 
 * <h2>Confirmation dialog by JavaScript</h2>
 * <p>
 *   You can specify confirmation message using {@link #addMessage(String, String)}.
 * </p>
 * <pre>
 * public SampleForm extends Form {
 *   ...
 *   private Submit register = new Submit("register");
 *   
 *   public SampleForm(){
 *     addMessage("register", "Could you register it?");
 *   }
 *   
 *   ...
 * } </pre>
 * 
 * <h2>Field auto registration</h2>
 * <p>
 *   If you set <code>true</code> to {@link #setFieldAutoRegisteration(boolean)},
 *   fields which are declared in the subclass are registered to this form automatically.
 *   You don't have to call <code>addField()</code> in the initialization code.
 * </p>
 * <pre>
 * public SampleForm extends Form {
 *   private TextField userId = new TextField("userId");
 *   private PasswordField password = new PasswordField("password");
 *   private Submit submit = new Submit("submit", this, "doSubmit");
 *   
 *   public SampleForm(){
 *     setFieldAutoRegisteration(true);
 *   }
 *   
 *   ...
 * } </pre>
 * 
 * <h2>Submit button which does not do JavaScript validation</h2>
 * <p>
 *   You can specify the button which does not do JavaScript validation
 *   using {@link #addNoJavaScriptValidateAction(String)}.
 * </p>
 * <pre>
 * public SampleForm extends Form {
 *   ...
 *   private Submit register = new Submit("register");
 *   private Submit cancel = new Submit("cancel");
 *   
 *   public SampleForm(){
 *     addNoJavaScriptValidateAction("register");
 *   }
 *   
 *   ...
 * } </pre>
 * 
 * @author Naoki Takezoe
 */
public class Form extends net.sf.click.control.Form {
	
	private static final long serialVersionUID = 1L;
	
	protected Map<String, String> confirmMessages = new HashMap<String, String>();
	protected List<String> noJavaScriptValidateActions = new ArrayList<String>();
	protected boolean fieldAutoRegisteration = false;
	
	/**
	 * Create a Form with no name defined.
	 */
	public Form() {
		super();
	}

	/**
	 * Construct the Form with the given name.
	 * 
	 * @param name the form name
	 */
	public Form(String name) {
		super(name);
	}
	
	public void setFieldAutoRegisteration(boolean fieldAutoRegisteration){
		this.fieldAutoRegisteration = fieldAutoRegisteration;
	}
	
	public boolean isFieldAutoRegistration(){
		return this.fieldAutoRegisteration;
	}

	public void addMessage(String action, String message){
		confirmMessages.put(action, message);
	}
	
	public void addNoJavaScriptValidateAction(String action){
		noJavaScriptValidateActions.add(action);
	}
	
	/**
	 * Initializes this form.
	 * <p>
	 * This method is called from {@link #onProcess()}.
	 */
	protected void init(){
		add(new HiddenField("action", ""));
		
		if(isFieldAutoRegistration()){
	    	for(java.lang.reflect.Field field: getClass().getDeclaredFields()){
	       		if(Field.class.isAssignableFrom(field.getType())){
	       			try {
	       				add((Field) field.get(this));
	       			} catch(IllegalAccessException ex){
	       				ex.printStackTrace();
	       			}
	    		}
	    	}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#add(net.sf.click.control.Field)
	 */
	@Override public Field add(Field field) {
		super.add(field);
		if(field instanceof Submit){
			field.setAttribute("onclick", getName() + ".action.value='" + field.getName() + "'");
		}
		return field;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#startTag()
	 */
	public String startTag(){
		HtmlStringBuffer buffer = new HtmlStringBuffer();
		buffer.append("<table width=\"100%\">");
		renderErrors(buffer, true);
		buffer.append("</table>");
		
		String script = "on_" + getId() + "_confirm()";
		if(getJavaScriptValidation()){
			script = "on_" + getId() + "_submit() && " + script;
		}
		setAttribute("onsubmit", "return " + script + ";");
		
		buffer.append("<script type=\"text/javascript\">\n");
		buffer.append("function on_" + getId() + "_confirm(){\n");
		buffer.append("  var action = " + getName() + ".action.value;\n");
		for(Map.Entry<String, String> entry: confirmMessages.entrySet()){
			buffer.append("  if(action == '" + entry.getKey() + "'){\n");
			buffer.append("    return confirm('" + entry.getValue() + "');\n");
			buffer.append("  }\n");
		}
		buffer.append("  return true;\n");
		buffer.append("}\n");
		buffer.append("</script>\n");
		
		return super.startTag() + buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#renderValidationJavaScript(net.sf.click.util.HtmlStringBuffer, java.util.List)
	 */
	@SuppressWarnings("unchecked")
    protected void renderValidationJavaScript(HtmlStringBuffer buffer, List formFields) {

        // Render JavaScript form validation code
        if (getValidate() && getJavaScriptValidation()) {
            List functionNames = new ArrayList();

            buffer.append("<script type=\"text/javascript\"><!--\n");

            // Render field validation functions & build list of function names
            for (Iterator i = formFields.iterator(); i.hasNext();) {
                Field field = (Field) i.next();
                String fieldJS = field.getValidationJavaScript();
                if (fieldJS != null) {
                    buffer.append(fieldJS);

                    StringTokenizer tokenizer = new StringTokenizer(fieldJS);
                    tokenizer.nextToken();
                    functionNames.add(tokenizer.nextToken());
                }
            }

            if (!functionNames.isEmpty()) {
                buffer.append("function on_");
                buffer.append(getId());
                buffer.append("_submit() {\n");
                
                buffer.append("   var noValidateActions = ");
                buffer.append(JSON.encode(noJavaScriptValidateActions));
                buffer.append(";\n");
                buffer.append("   for(var i=0;i<noValidateActions.length;i++){\n");
                buffer.append("      if(document." + getName() + ".action.value == noValidateActions[i]){\n");
                buffer.append("         return true;\n");
                buffer.append("      }\n");
                buffer.append("   }\n");
                
                buffer.append("   var msgs = new Array(");
                buffer.append(functionNames.size());
                buffer.append(");\n");
                for (int i = 0; i < functionNames.size(); i++) {
                    buffer.append("   msgs[");
                    buffer.append(i);
                    buffer.append("] = ");
                    buffer.append(functionNames.get(i).toString());
                    buffer.append(";\n");
                }
                buffer.append("   return validateForm(msgs, '");
                buffer.append(getId());
                buffer.append("', '");
                buffer.append(getErrorsAlign());
                buffer.append("', ");
                if (getErrorsStyle() == null) {
                    buffer.append("null");
                } else {
                    buffer.append("'" + getErrorsStyle() + "'");
                }
                buffer.append(");\n");
                buffer.append("}\n");

            } else {
                buffer.append("function on_");
                buffer.append(getId());
                buffer.append("_submit() { return true; }\n");
            }
            buffer.append("//--></script>\n");
        }
    }
	
	@Override public boolean onProcess() {
		init();
		return super.onProcess();
	}

}