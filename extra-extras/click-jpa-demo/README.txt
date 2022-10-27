Before getting started you need to sign-up for a GAE account here:
http://code.google.com/appengine/

Once your account is ready, create a new application and specify a unique
application id.

The easiest way to build the project is to use the Eclipse GAE plugin.

Import this project into your Eclipse workspace. 

Then add your GAE application id to the <application> element in the file
'/war/WEB-INF/appengine-web.xml'. 

You can now run the application locally: Run -> Run As -> Web Application

To upload the application to GAE, click on the GAE icon and follow the
"Add Engine project settings" link to specify your application id. Then
enter your password to upload the project.