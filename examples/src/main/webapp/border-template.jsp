<!doctype html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>${title} : Click Examples</title>
<link type="text/css" rel="stylesheet" href="${context}/assets/css/style.css" title="Style"/>
${headElements}
<link rel="shortcut icon" href="$context/favicon.ico" type="image/ico"/>
</head>
<body>

<div class="page">
  <div class="header">

    <%-- Title Header --%>
    <div class="title-icon">
      <a target="blank" href="http://click.apache.org/"><img src="${context}/assets/images/click-icon-blue-32.png" border="0" alt="Click"/></a>
    </div>
		<div class="title-left">
      Click Examples
    </div>
    <div class="title-hosted">
      <p>Version ${messages.version}</p>
      <a target="blank" href="https://github.com/magicprinc/Apache-Click">A.Fink presents</a>
    </div>
    <div class="header-color">
    </div>

    <%-- Menu --%>
    <table id="menuTable" border="0" width="100%" cellspacing="0" cellpadding="0" style="margin-top: 2px;">
    <tr>
      <td>
        <div class="menustyle" id="menu">
          <ul class="menubar" id="dmenu">
            <c:forEach items="${rootMenu.children}" var="topMenu">
              <li class="topitem">${topMenu}
                <ul class="submenu"
                  <c:forEach items="${topMenu.children}" var="subMenu">
                    ><li>${subMenu}</li
                  </c:forEach>
                ></ul>
              </li>
            </c:forEach>
            <li class="topitem"><a target="_blank" href="${context}/source-viewer.htm?filename=WEB-INF/classes/${srcPath}" title="Page Java source"><img border="0" class="link" alt="" src="${context}/assets/images/lightbulb1.png"/> Page Java</a>
            </li>
            <li class="topitem"><a target="_blank" href="${context}/source-viewer.htm?filename=${path}" title="Page Content source"><img border="0" class="link" alt="" src="${context}/assets/images/lightbulb2.png"/> Page HTML</a>
            </li>
          </ul>
        </div>
      </td>
    </tr>
    </table>
  </div>

  <%-- Page Content --%>
  <div class="content">
	  <h2>${title}</h2>
    <p/>
    <jsp:include page='${forward}' flush="true"/>
	</div>

</div>

${jsElements}

</body>
</html>