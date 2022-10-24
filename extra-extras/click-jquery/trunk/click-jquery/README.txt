Click-jQuery
============

Click-jQuery depends on Click 2.3.0-M1 or greater. At the time of this writing
Click 2.3.0-M1 has not been released yet, so you need to either:

- build Click from trunk
- use a nightly build from http://hudson.zones.apache.org/hudson/view/Click/job/Click/ws/

Building with ANT
-----------------
1. build Click from trunk (or grab a nightly copy), and copy manually click.X.X.X.jar, click-extras.X.X.X.jar and click-mock.X.X.X.jar to /click-jquery/lib
   (X.X.X must be 2.3.0-M1 or greater)

2. run
 > ant get-deps
   -> to get all the required dependencies
3. run
 > ant javadoc
   -> to build the javadocs
4. run
 > ant build-examples
   -> to build click-jquery.jar and click-jquery-examples.war

Building with Maven
-------------------
1. Since Click-jQuery is still using Click directly from trunk, you need to build Click from trunk (or grab a nightly copy)
   and install it in your local maven repo, using (X.X.X must be 2.3.0-M1 or greater):
     mvn install:install-file -Dfile=c:\click-X.X.X.jar -DgroupId=org.apache.click -DartifactId=click -Dversion=X.X.X -Dpackaging=jar
     mvn install:install-file -Dfile=c:\click-extras-X.X.X.jar -DgroupId=org.apache.click -DartifactId=click-extras -Dversion=X.X.X -Dpackaging=jar
     mvn install:install-file -Dfile=c:\click-mock-X.X.X.jar -DgroupId=org.apache.click -DartifactId=click-mock -Dversion=X.X.X -Dpackaging=jar
2. run
  > cd click-jquery
3. run
  > mvn clean install
    -> to build the click-jquery.jar. The result should be in the 'target' directory.
       This JAR has the required pom.xml in it's META-INF so it can be upload to a public Maven Repo.
4. run (optional)
  > mvn site:site
    -> to generate the Maven based site.
