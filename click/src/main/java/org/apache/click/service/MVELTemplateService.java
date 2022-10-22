package org.apache.click.service;

import org.apache.click.Page;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

// src\main\java\org\apache\click\service\MVELTemplateService.java
public class MVELTemplateService implements TemplateService {

  @Override public void onInit (ServletContext servletContext) throws Exception {
    throw new IllegalStateException("todo");
  }


  @Override public void onDestroy () {
    throw new IllegalStateException("todo");
  }


  @Override
  public void renderTemplate (Page page, Map<String, ?> model, Writer writer) throws IOException, TemplateException {
    throw new IllegalStateException("todo");
  }


  @Override
  public void renderTemplate (String templatePath, Map<String, ?> model, Writer writer) throws IOException, TemplateException {
    throw new IllegalStateException("todo");
  }

}