#

**NOTE:** You will need a development branch from Apache Shiro which supports Bearer Tokens: https://github.com/apache/shiro/pull/129

## Clone the project

```bash
git clone https://github.com/bdemers/shiro-via-gateway.git
cd shiro-via-gateway
mvn install
```

## Configuration

You also need to gather the following information from the Okta Developer Console:

- **Client ID** and **Client Secret** - These can be found on the "General" tab of a Web application that you created in the Okta Developer Console.

- **Issuer** - This is the URL of the authorization server that will perform authentication.  All Developer Accounts have a "default" authorization server.  The issuer is a combination of your Org URL (found in the upper right of the console home page) and `/oauth2/default`. For example, `https://dev-123456.okta.com/oauth2/default`.

## Start the Servlet Application

The "Servlet" application represents any existing application, typically with some other form of authentication (LDAP, DB, etc). Converting an existing application to an resource server may be an easier task than implementing an OAuth2 Authorization Code Flow.  In this example use an Apache Shiro Realm to handle the authentication and authorization.

**NOTE:** An embedded Jetty server is used make starting this example easy.

```bash
cd servlet-application
mvn -Dokta.oauth2.issuer=https://{yourOktaDomain}/oauth2/default
```
 
## Start the Gateway Application

This gateway uses Spring Cloud Gateway to perform an OIDC authorization code flow login and use an access token to securely communicate with the above servlet application.  

Plug the above values into the `mvn` commands used to start the application.

```bash
cd gateway-application
mvn -Dokta.oauth2.issuer=https://{yourOktaDomain}/oauth2/default \
    -Dokta.oauth2.client-id={clientId} \
    -Dokta.oauth2.client-secret={clientSecret}
```

> **NOTE:** Putting secrets on the command line should ONLY be done for examples, do NOT do this in production. Instead update the projects `application.yml`

## See it in Action!

Now navigate to http://localhost:8080/profile in your browser.  If everything is working you will be redirected to Okta to login, and then back to `/profile` where you should see the current user's information.
