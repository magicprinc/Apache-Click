package org.apache.click.util;

import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class ClickTestUtils {

  public static final String XML_HEADER = "<?xml version='1.0' encoding=\"UTF-8\" standalone=\"yes\"?>";


  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static File makeTmpDir () throws IOException {
    File tmpdir = File.createTempFile("click", "");
    try { tmpdir.delete(); } catch (Throwable ignore) {}
    tmpdir.mkdir();
    return tmpdir;
  }

  public static File makeFile (File dir, String filename) {
    File file = new File(dir, filename);
    file.getParentFile().mkdirs();
    return file;
  }

  public static PrintStream makeXmlStream (File dir, String filename) throws FileNotFoundException {
    File file = makeFile(dir, filename);
    PrintStream pstr = new PrintStream(file);
    pstr.println(XML_HEADER);
    return pstr;
  }


  public static void deleteDir (@NonNull File tmpdir) throws IOException {
    for (File f : tmpdir.listFiles()) {
      if (f.isDirectory()) {
        deleteDir(f);// recur
      }
      f.delete();
    }
    tmpdir.delete();
  }

  static void usedToTest (@NonNull Object value) {}

  public static Class<Throwable> detectNonNullException () {
    try {
      usedToTest(null);
    } catch (Throwable e) {
      return ClickUtils.castUnsafe(e.getClass());
    }
    throw new AssertionError("Lombok.@NonNull is not working");
  }

}