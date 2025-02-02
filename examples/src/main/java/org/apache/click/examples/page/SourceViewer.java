package org.apache.click.examples.page;

import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

/**
 * Provides a Java source code, HTML and XML examples rendering page.
 *
 * @author Malcolm Edgar
 */
public class SourceViewer extends BorderPage {
  @Serial private static final long serialVersionUID = 1761309857296092283L;

  private static final String[] HTML_KEYWORDS = { "html", "head", "style",
      "script", "title", "link", "body", "h1", "h2", "h3", "h4", "h5",
      "h6", "p", "hr", "br", "span", "table", "tr", "th", "td", "a", "b",
      "i", "u", "ul", "ol", "li", "form", "div", "input", "fieldset",
      "pre", "tt", "ajax-response", "response", "%@", "%@taglib",
      "jsp:include", "c:forEach", "c:choose", "c:when", "c:otherwise",
      "fmt:formatNumber", "fmt:formatDate", "center" };

  private static final String[] XML_KEYWORDS = { "click-app", "pages",
      "page", "excludes", "headers", "header", "format", "mode", "type",
      "filter-name", "filter-class", "filter-mapping", "filter",
      "web-app", "display-name", "description", "servlet-mapping",
      "servlet-name", "servlet-class", "init-param", "param-name",
      "param-value", "servlet", "load-on-startup", "security-constraint",
      "web-resource-collection", "auth-constraint", "role-name",
      "login-config", "auth-method", "realm-name", "security-role",
      "url-pattern", "welcome-file-list", "welcome-file", "Context",
      "ResourceLink", "menu", "?xml", "controls", "control",
      "listener-class", "listener", "beans",  "bean", "context-param",
      "context:component-scan", "property", "constructor-arg",
      "list", "value", "ref", "beans:beans", "beans:bean", "http",
      "intercept-url", "form-login", "logout", "beans:property",
      "beans:list", "beans:ref", "authentication-provider",
      "page-interceptor", "property-service" };

  private static final String[] VELOCITY_KEYWORDS = { "#if", "#if(",
      "#elseif", "#elseif(", "#else", "#else(", "#end", "#set", "#set(",
      "#include", "#include(", "#parse", "#parse(", "#stop", "#macro",
      "#macro(", "#foreach", "#foreach(", "##", "#*", "*#", "#" };

  private boolean isJava = false;
  private boolean isXml = false;
  private boolean isHtml = false;

  /** @see Page#onGet() */
  @Override
	public void onGet (){
    HttpServletRequest request = getContext().getRequest();

    String filename = request.getParameter("filename");

    if (StringUtils.isNotBlank(filename)){
      loadFilename(filename.trim().replace('\\','/'));
      getModel().put("title", "Source Viewer : "+ filename); // ?
    } else {
      addModel("error", "filename not defined");
    }
  }

  private void loadFilename (String fileName){
    ServletContext context = getContext().getServletContext();

		if (fileName.charAt(0) != '/'){
			fileName = '/'+ fileName;// Orion server requires '/' prefix to find resources
		}

    InputStream in = null;
    try {
      //1. web root /
      in = context.getResourceAsStream(fileName);

      //2. web root, but .htm→.jsp?
      if (in == null && fileName.endsWith(".htm")){
        in = context.getResourceAsStream(fileName.substring(0, fileName.length()-4) +".jsp");
      }

      //3. in classpath?
      if (in == null){// && filename.endsWith(".java")
				fileName = fileName.substring(1);// cut leading / for ClassPath
        in = ClickUtils.getResourceAsStream(fileName, getClass());
      }

      if (in == null){// WEB-INF/classes/net/sf/click/examples/page/SourceViewer.java
				//4. in class-path without WEB-INF/classes prefix?
        if (fileName.startsWith("WEB-INF/classes/")){
					fileName = fileName.substring(16);
					in = ClickUtils.getResourceAsStream(fileName, getClass());
        }
				if (in == null){
					in = ClickUtils.getResourceAsStream("static/"+fileName, getClass());// resources/static are served by Spring from ClassPath, not by Tomcat
				}

        //6. in subproject - in file system?
        if (in == null){// ok. There are no sources... Maybe we are still under gradle?
          try {
            in = new FileInputStream("./src/main/java/"+fileName);
          } catch (IOException ignore){}// just best-effort last attempt..
        }
      }

      if (in != null){
        loadResource(in, fileName);
      } else {
        addModel("error", "File "+ fileName +" not found");
      }

    } catch (IOException e){
      addModel("error", "Could not read "+ fileName+" with error "+e);
    } finally {
      ClickUtils.close(in);
    }
  }

  private void loadResource (InputStream inputStream, String name) throws IOException {
    isJava = name.endsWith(".java");
    isXml = name.endsWith(".xml");
    isHtml = name.endsWith(".htm") || name.endsWith(".html") || name.endsWith(".vm") || name.endsWith(".jsp");

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

    HtmlStringBuffer buffer = new HtmlStringBuffer();

    String line;

    while ((line = reader.readLine()) != null){
      buffer.append(getEncodedLine(line)).append("\n");
    }

    if (isHtml) {
      addModel("templateSource", buffer.toString());
    } else {
      addModel("source", buffer.toString());
    }

    addModel("name", name);
  }

  private String getEncodedLine (String line){
    if (isHtml) {
      line = ClickUtils.escapeHtml(line);

      for (String keyword : HTML_KEYWORDS){
        line = renderHtmlKeywords(line, keyword);
      }

      for (String keyword : VELOCITY_KEYWORDS){
        line = renderVelocityKeywords(line, keyword);
      }

      String renderedDollar = "<font color=\"red\">$</font>";

      line = StringUtils.replace(line, "$", renderedDollar);

    } else if (isXml) {
      line = ClickUtils.escapeHtml(line);

      for (String keyword : XML_KEYWORDS){
        line = renderXmlKeywords(line, keyword);
      }

    } else {
      line = ClickUtils.escapeHtml(line);
    }

    return line;
  }

  private String renderVelocityKeywords(String line, String token) {
    String markupToken = renderVelocityToken(token);

    line = StringUtils.replace(line, " " + token + " ", " " + markupToken + " ");

    if (line.startsWith(token)) {
      line = markupToken + line.substring(token.length());
    }

    if (line.endsWith(token)) {
      line = line.substring(0, line.length() - token.length())
          + markupToken;
    }

    return line;
  }

  private String renderHtmlKeywords(String line, String token) {

    String markupToken = "&lt;" + token + "&gt;";
    String renderedToken = "&lt;" + renderHtmlToken(token) + "&gt;";
    line = StringUtils.replace(line, markupToken, renderedToken);

    markupToken = "&lt;" + token + "/&gt;";
    renderedToken = "&lt;" + renderHtmlToken(token) + "/&gt;";
    line = StringUtils.replace(line, markupToken, renderedToken);

    markupToken = "&lt;/" + token + "&gt;";
    renderedToken = "&lt;/" + renderHtmlToken(token) + "&gt;";
    line = StringUtils.replace(line, markupToken, renderedToken);

    markupToken = "&lt;" + token + " ";
    renderedToken = "&lt;" + renderHtmlToken(token) + " ";
    line = StringUtils.replace(line, markupToken, renderedToken);

    return line;
  }

  private String renderXmlKeywords(String line, String token) {

    String markupToken = "&lt;" + token + "&gt;";
    String renderedToken = "&lt;" + renderXmlToken(token) + "&gt;";
    line = StringUtils.replace(line, markupToken, renderedToken);

    markupToken = "&lt;" + token + "/&gt;";
    renderedToken = "&lt;" + renderXmlToken(token) + "/&gt;";
    line = StringUtils.replace(line, markupToken, renderedToken);

    markupToken = "&lt;/" + token + "&gt;";
    renderedToken = "&lt;/" + renderXmlToken(token) + "&gt;";
    line = StringUtils.replace(line, markupToken, renderedToken);

    markupToken = "&lt;" + token + " ";
    renderedToken = "&lt;" + renderXmlToken(token) + " ";
    line = StringUtils.replace(line, markupToken, renderedToken);

    return line;
  }

  private String renderHtmlToken(String token) {
    return "<font color=\"#00029F\">" + token + "</font>";
  }

  private String renderXmlToken(String token) {
    return "<font color=\"#00029F\">" + token + "</font>";
  }

  private String renderVelocityToken(String token) {
    return "<font color=\"red\">" + token + "</font>";
  }

//private String renderComment(String comment){ return "<font color=\"#3F7F5F\">" + comment + "</font>";}
}