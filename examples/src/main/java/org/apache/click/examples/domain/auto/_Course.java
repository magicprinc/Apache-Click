package org.apache.click.examples.domain.auto;

import java.io.Serial;
import java.util.List;

/** Class _Course was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public class _Course extends org.apache.click.examples.domain.BaseEntity {
  @Serial private static final long serialVersionUID = -5052621563690605889L;

  public static final String NAME_PROPERTY = "name";
  public static final String STUDENTS_PROPERTY = "students";

  public static final String ID_PK_COLUMN = "id";


  public void setName(String name) {
    writeProperty("name", name);
  }
  public String getName() {
    return (String)readProperty("name");
  }


  public void addToStudents(org.apache.click.examples.domain.Student obj) {
    addToManyTarget("students", obj, true);
  }
  public void removeFromStudents(org.apache.click.examples.domain.Student obj) {
    removeToManyTarget("students", obj, true);
  }
  public List getStudents() {
    return (List)readProperty("students");
  }


}