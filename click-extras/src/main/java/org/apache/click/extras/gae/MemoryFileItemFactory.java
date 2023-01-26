package org.apache.click.extras.gae;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

/**
 * Provides a FileItemFactory implementation that creates {@link MemoryFileItem}
 * instances which always keep their content in memory.
 */
public class MemoryFileItemFactory implements FileItemFactory {

  /**
   * Create a new {@link MemoryFileItem} instance from the supplied parameters.
   *
   * @param fieldName the name of the form field
   * @param contentType the content type of the form field
   * @param isFormField true if this is a plain form field, false otherwise
   * @param fileName the name of the uploaded file, if any, as supplied by the browser or other client
   * @return the newly created file item
   */
  @Override public FileItem createItem (String fieldName, String contentType, boolean isFormField, String fileName){
    return new MemoryFileItem(fieldName, contentType, isFormField, fileName);
  }
}