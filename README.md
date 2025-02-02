Welcome to Click
================
[![Java CI with Gradle](https://github.com/magicprinc/Apache-Click/actions/workflows/gradle.yml/badge.svg)](https://github.com/magicprinc/Apache-Click/actions/workflows/gradle.yml)
[![Release](https://jitpack.io/v/magicprinc/Apache-Click.svg)](https://jitpack.io/#magicprinc/Apache-Click)

Apache Click™ is a modern JEE web application framework, providing a natural rich client style programming model.
Apache Click is designed to be very easy to learn and use, with developers getting up and running within a day.

Documentation
=============

Comprehensive HTML documentation is available online at:

[http://click.apache.org/](http://click.apache.org/)

The documentation is also available from the directory:

  `documentation/`


Release Notes
=============

Detailed release notes are available here:

  `documentation/docs/roadmap-changes.html`


Distribution Jars
=================

Distributed Click JAR files include:

   `dist/click-xx.jar`        - Click runtime JAR including dependencies

   `dist/click-nodeps-xx.jar` - Click runtime JAR with no dependencies

   `dist/click-extras-xx.jar` - Click Extras JAR

   `dist/click-mock-xx.jar`   - Click Mock Utilities JAR


Examples
========

Example pre-built web application include:

   `dist/click-examples.war`


Build Information
=================

Click is built using the J2SE 1.5.0 and Apache Ant

The Ant `build.xml` and `build.properties` files are located in
the `build/` directory.

The main Ant targets include:

    build-all             build framework, extras, examples
    build-distribution    build distribution ZIP file
    build-examples        build click-examples WAR file
    build-extras          build click-extras JAR file
    build-framework       build click framework JAR file
    build-maven-bundles   build Maven repository upload bundles
    build-mock            build mock JAR file
    build-sources         build source ZIP files for use with IDEs
    checkstyle            run checkstyle report on Java source
    deploy-examples       copy example WAR files to app server
    get-deps              download JAR dependencies
    get-deps-proxy        download JAR dependencies via proxy
    help                  display the Help message
    javadoc               create Javadoc HTML files
    project-quick-start   build application template
    test-all              run all unit tests


Before building the framework, all third-party library dependencies must be
downloaded using the command:

    ant get-deps

or in the case of running behind a proxy, by using the command:

    ant get-deps-proxy

To run all tests execute the command:

    ant test-all

To build a new distribution run:

    ant build-distribution

To build the core library, click-x.x.x.jar, run:

    ant build-framework

To build the extras library, click-extras.x.x.x.jar, run:

    ant build-extras

To build the documentation (PDF and HTML) please see:

    documentation/xdocs/README.txt

Further information on building Click is available here:

   `documentation/docs/developer-guide/building.html`


## 2022-2023 Gradle build, Java 8-17, upgraded dependencies

Main site

https://click.apache.org/

User guide, documents, book

- https://click.apache.org/docs/user-guide.html
- https://click.apache.org/docs/user-guide/htmlsingle/click-book.html
- https://click.apache.org/docs/user-guide/html/index.html
- https://click.apache.org/docs/user-guide/pdf/click-book.pdf

Why Click?

https://click.apache.org/docs/why-click.html

FAQ

https://click.apache.org/docs/faq.html

GitHub

- https://github.com/magicprinc/Apache-Click
- https://github.com/apache/click

Run examples

💡 If you have *gradle* installed, you can use command `gradle` otherwise use `gradlew` in project's root folder.

💡 You will probably need to stop gradle's daemon, to successfully start next example:
`gradle --stop`

- $ `gradle :examples:clean :examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`
- $ `gradle :extra-clickclick-examples:clean :extra-clickclick-examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`
- $ `gradle :click-charts-examples:clean :click-charts-examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`
- $ `gradle :click-jquery-examples:clean :click-jquery-examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`
- $ `gradle :extra-ajax4click-examples:clean :extra-ajax4click-examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`
- $ `gradle :extra-click-calendar-examples:clean :extra-click-calendar-examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`
- $ `gradle :extra-click-charts-enhanced-examples:clean :extra-click-charts-enhanced-examples:tomcatRunWar --stacktrace --info --warning-mode all --continue`

(see also https://docs.gradle.org/current/userguide/command_line_interface.html)