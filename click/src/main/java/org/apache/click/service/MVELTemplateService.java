package org.apache.click.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.click.Page;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

// src\main\java\org\apache\click\service\MVELTemplateService.java
@Slf4j
public class MVELTemplateService implements TemplateService {

  @Override public void onInit (ServletContext servletContext) throws Exception {
    // todo
    log.error("onInit: {}", servletContext);
  }


  @Override public void onDestroy () {
    // todo
    log.error("onDestroy: {}", this);
  }


  @Override
  public void renderTemplate (Page page, Map<String, ?> model, Writer writer) throws IOException, TemplateException {
    writer.write("todo!!!");
    writer.write(page.toString());
    writer.write(String.valueOf(model));
  }


  @Override
  public void renderTemplate (String templatePath, Map<String, ?> model, Writer writer) throws IOException, TemplateException {
    writer.write("todo!!!");
    writer.write(templatePath);
    writer.write(String.valueOf(model));
  }

}