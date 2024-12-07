package org.apache.click.extras.hibernate;

import io.ebean.DB;
import io.ebean.Transaction;
import io.ebeaninternal.api.SpiEbeanServer;
import io.ebeaninternal.server.deploy.BeanProperty;
import jakarta.persistence.PersistenceException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;

/**
 * Provides Hibernate data aware Form control: &nbsp; &lt;form method='POST'&gt;.
 *
 * <table class='htmlHeader' cellspacing='10'>
 * <tr>
 * <td>
 *
 * <table class='fields'>
 * <tr>
 * <td align='left'><b><label>First Name:</label></b></td>
 * <td align='left'><input type='text' name='name' value='' size='20' /></td>
 * </tr>
 * <tr>
 * <td align='left'><label>Middle Names:</label></td>
 * <td align='left'><input type='text' name='name' value='' size='20' /></td>
 * </tr>
 * <tr>
 * <td align='left'><b><label>Family Name:</label></b></td>
 * <td align='left'><input type='text' name='name' value='' size='20' /></td>
 * </tr>
 * </table>
 * <table class="buttons" align='right'>
 * <tr><td>
 * <input type='submit' name='ok' value='   OK   '/>&nbsp;<input type='submit' name='cancel' value='Cancel'/>
 * </td></tr>
 * </table>
 *
 * </td>
 * </tr>
 * </table>
 *
 * <a href="http://www.hibernate.org/">Hibernate</a> is an Object Relational
 * Mapping (ORM) framework. The HibernateForm supports creating (inserting) and
 * saving (updating) POJO instances. This form will automatically apply the
 * given objects property required validation constraints to the form fields.
 * <p/>
 * The HibernateForm uses the thread local <tt>Session</tt> obtained via
 * <tt>SessionContext.getSession()</tt> for all object for persistence
 * operations. To use an alternative Session source override set the forms
 * getSession() method.
 * <p/>
 * The example below provides a <tt>User</tt> data object creation
 * and editing page. To edit an existing user object, the object is passed
 * to the page as a request parameter. Otherwise a new user object will
 * be created when {@link #saveChanges()} is called.
 *
 * <pre class="codeJava">
 * <span class="kw">public class</span> UserEdit <span class="kw">extends</span> Page {
 *
 *   <span class="kw">private</span> HibernateForm form = <span class="kw">new</span> HibernateForm(<span class="st">"form"</span>, User.<span class="kw">class</span>);
 *
 *    <span class="kw">public</span> UserEdit() {
 *        form.add(<span class="kw">new</span> TextField(<span class="st">"firstName"</span>));
 *        form.add(<span class="kw">new</span> TextField(<span class="st">"middleNames"</span>));
 *        form.add(<span class="kw">new</span> TextField(<span class="st">"FamilyName"</span>));
 *
 *        form.add(<span class="kw">new</span> Submit(<span class="st">"ok"</span>, <span class="st">"   OK   "</span>, <span class="kw">this</span>, <span class="st">"onOkClicked"</span>));
 *        form.add(<span class="kw">new</span> Submit(<span class="st">"cancel"</span>, <span class="kw">this</span>, <span class="st">"onCancelClicked"</span>));
 *
 *        form.setButtonAlign(<span class="st">"right"</span>);
 *        form.setLabelRequiredPrefix(<span class="st">"&lt;b&gt;"</span>);
 *        form.setLabelRequiredSuffix(<span class="st">"&lt;/b&gt;"</span>);
 *        addControl(form);
 *    }
 *
 *    <span class="kw">public void</span> setUser(User user) {
 *        form.setValueObject(user);
 *    }
 *
 *    <span class="kw">public boolean</span> onOkClicked() {
 *        <span class="kw">if</span> (form.isValid()) {
 *           <span class="kw">if</span> (form.saveChanges()) {
 *               setRedirect(<span class="st">"user-list.htm"</span>);
 *           }
 *        }
 *        <span class="kw">return true</span>;
 *    }
 *
 *    <span class="kw">public boolean</span> onCancelClicked() {
 *        setRedirect(<span class="st">"user-list.htm"</span>);
 *        <span class="kw">return false</span>;
 *    }
 * } </pre>
 @see io.ebeaninternal.server.deploy.BeanDescriptor
 @see io.ebeaninternal.server.deploy.BeanDescriptorManager
 @see io.ebean.bean.EntityBean
 @see io.ebean.DB
 */
public class HibernateForm extends Form {
  @Serial private static final long serialVersionUID = -7134198516606088333L;

  /** The form value object classname parameter name. */
  protected static final String FO_CLASS = "FO_CLASS";

  /** The form value object id parameter name. */
  protected static final String FO_ID = "FO_ID";

  /** The value object class name hidden field. */
  protected final HiddenField classField;

  /** The value object identifier hidden field. */
  protected final HiddenField oidField;

  /** The flag specifying that object validation meta data has been applied to the form fields. */
  protected boolean metaDataApplied = false;

  /**
   * Create a new HibernateForm with the given form name and value object class.
   *
   * @param name the form name
   * @param valueClass the value object class
   */
  public HibernateForm (String name, @NonNull Class<?> valueClass, @NonNull Class<?> idClass) {
    super(name);
    classField = new HiddenField(FO_CLASS, String.class);
    classField.setValue(getClassname(valueClass));
    add(classField);

    oidField = new HiddenField(FO_ID, idClass);
    add(oidField);
  }

	/**
   * Return a Hibernate value object from the form with the form field values
   * copied into the object's properties.
   *
   * @return the Hibernate object from the form with the form field values
   * applied to the object properties.
   */
  @Nullable  @SneakyThrows
	public Object getValueObject() {
    if (StringUtils.isNotBlank(classField.getValue())){
			Class<?> valueClass = ClickUtils.classForName(classField.getValue());

			Serializable oid = (Serializable) oidField.getValueObject();

			Object valueObject = oid != null
					? DB.find(valueClass, oid)
					: valueClass.newInstance();

			copyTo(valueObject);

			return valueObject;
    }
    return null;
  }

  /**
   * Set the given Hibernate value object in the form, copying the object's
   * properties into the form field values.
   *
   * @param valueObject the Hibernate value object to set
   */
  public void setValueObject (Object valueObject) {
		if (valueObject != null){
			// Extract the identifier value
			Object identifier = DB.beanId(valueObject);
			oidField.setValueObject(identifier);

			// Perform a custom copy operation
			copyFrom(valueObject);
		}
	}

  /**
   * Save or update the object to the database and return true.
   * If a <tt>HibernateException</tt> occurs the <tt>Transaction</tt> will be
   * rolled back the exception will be raised.
   * <p/>
   * If no object is added to the form using <tt>setValueObject()</tt>
   * then this method will: <ul>
   * <li>create a new instance of the Class</li>
   * <li>copy the form's field values to the objects properties</li>
   * <li>save the new object to the database</li>
   * </ul>
   * <p/>
   * If an existing persistent object is added to the form using
   * <tt>setValueObject()</tt> then this method will: <ul>
   * <li>load the persistent object record from the database</li>
   * <li>copy the form's field values to the objects properties</li>
   * <li>update the object in the database</li>
   * </ul>
   *
   * @return true if the object was saved or false otherwise
   * @throws HibernateException if a persistence error occurred
   */
  public boolean saveChanges () {
    Object valueObject = getValueObject();

    Transaction transaction = DB.beginTransaction();
    try {
      DB.save(valueObject);

      return true;

    } catch (PersistenceException he) {
      transaction.rollback();
      throw he;
    } finally {
			transaction.commit();
		}
  }

  /**
   * This method applies the object meta data to the form fields and then
   * invokes the <tt>super.onProcess()</tt> method.
   *
   * @see Form#onProcess()
   *
   * @return true to continue Page event processing or false otherwise
   */
  @Override
  public boolean onProcess() {
    applyMetaData();
    return super.onProcess();
  }

  /**
   * Render the HTML representation of the HibernateForm.
   * <p/>
   * This method applies the object meta data to the form fields and then
   * invokes the <tt>super.toString()</tt> method.
   *
   * @see #toString()
   *
   * @param buffer the specified buffer to render the control's output to
   */
  @Override
  public void render(HtmlStringBuffer buffer) {
    applyMetaData();
    super.render(buffer);
  }


  /**
   * Applies the <tt>ClassMetadata</tt> validation database meta data to the
   * form fields.
   * <p/>
   * The field validation attributes include:
   * <ul>
   * <li>required - is a mandatory field and cannot be null</li>
   * </ul>
   */
	@SneakyThrows // ClassNotFoundException
  protected void applyMetaData() {
    if (metaDataApplied){
      return;
    }
		Class<?> valueClass = ClickUtils.classForName(classField.getValue());
		String classname = getClassname(valueClass);

//		var server = (SpiServer) DB.getDefault();
//		BeanType<?> beanType = server.beanType(valueClass);
		var server = (SpiEbeanServer) DB.getDefault();

		val beanDescriptor = server.descriptor(valueClass);
	 	for (BeanProperty p : beanDescriptor.propertiesAll()){
			Field field = getField(p.name());
			if (field != null){
				boolean isMandatory = !p.isNullable();
				if (!field.isRequired() && isMandatory){
					if (!(field instanceof Checkbox)){
						field.setRequired(true);
					}
				}
			}
		}
    metaDataApplied = true;
  }

  /**
   * Return the original classname for the given class removing any CGLib proxy information.
   *
   * @param aClass the class to get the original name from
   * @return the original classname for the given class
   */
  protected String getClassname(Class<?> aClass) {
    String classname = aClass.getName();
    if (classname.contains("$$")) {
      classname = classname.substring(0, classname.indexOf('$'));
    }
    return classname;
  }
}