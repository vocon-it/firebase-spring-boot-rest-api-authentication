# Firebase Authentication for Spring boot [![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg)](https://opensource.org/licenses/MIT)

[![Open with ThePro](https://thepro.io/button.svg)](https://thepro.io/post/firebase-authentication-for-spring-boot-rest-api-5V)

Firebase is a backendless platform to run applications without dedicated backend. But, sometimes you may need to communicate with API of an exisiting backend or you may want a dedicated backend to perform operations that cannot be done through firebase infrastructure.

This **Spring Boot Starter** is perfect for such situations when you want to extend firebase's authentication menchanism with **Spring Security** to seamlessly create and use protected rest API's.

# vocon-IT is testing this Prepo
## Spring Boot Backend
### Step 1: Create a firebase project
[Firebase](https://firebase.google.com/) -> Go to Console -> Create Project and add name

### Step 2: Create a firebase service account and save the config to Spring boot
[Create a firebase service account](https://firebase.google.com/support/guides/service-accounts) via Project overview 
-> Settings icon right of project overview -> Service Account tab
-> Create new private key pair -> a json file is downloaded automatically
-> Save the json file to `src/main/resources/firebase-server-config.json`

### Step 3: Configure and run the Spring Boot Application
#### 3a: Run the Application using Maven

Configure $ run the application using maven:
```shell
export ENABLE_SERVER_SESSION=${ENABLE_SERVER_SESSION:=false}
export FIREBASE_DATABASE=${FIREBASE_DATABASE:="https://vocon-intellij-frontend.firebaseio.com"}
export CORS_DOMAIN=${CORS_DOMAIN:=localhost}
export GOOGLE_APPLICATION_CREDENTIALS=${GOOGLE_APPLICATION_CREDENTIALS:="src/main/resources/firebase-server-config.json"}

[ ! -r "${GOOGLE_APPLICATION_CREDENTIALS}" ] && echo "Please copy the firebase server config to ${GOOGLE_APPLICATION_CREDENTIALS}" >&2 && exit 1

echo "Runninf server. Press Ctrl-C for stopping it again..."
mvn spring-boot:run
```
You might need to change the variables (especially `FIREBASE_DATABASE`) to fit your case, though.

#### 3b: Run the Application within IntelliJ

For running the application and tests in IntelliJ, use the following content in
1. IntelliJ -> Run -> Edit configurations... -> Application --> ApiServiceApplicaton -> Environment variables
2. IntelliJ -> Run -> Edit configurations... -> JUnit --> ApiServiceApplicatonTests -> Environment variables
```shell
ENABLE_SERVER_SESSION=false;GOOGLE_APPLICATION_CREDENTIALS=src/main/resources/firebase-server-config.json;FIREBASE_DATABASE=https://vocon-intellij-frontend.firebaseio.com;CORS_DOMAIN=localhost
```
You might need to change the variables (especially `FIREBASE_DATABASE`) to fit your case, though.

Then right-click `src/main/java/io/thepro/apiservice/APIServiceApplication` and choose the green triangle or green bug
for running and debugging the application, respectively.

### Test the Application with curl
#### Public Access

Public access is allowed for any user:
```shell
curl localhost:8090/public/data

# output: 
You have accessed public data from spring boot
```
#### Protected Access

Protected access is forbidden, if we do not provide a valid JSON Web Token (JWT):
```shell
curl localhost:8090/protected/data
{"code":401,"message":"Unauthorized access of protected resource, invalid credentials","error":"UNAUTHORIZED","timestamp":"2021-02-18T19:27:54.698+00:00"}
```
Getting a token from firebase is possible by connecting to firebase with a frontend system and debugging the network 
traffic on the browser. I have not found any easy way to get a token with curl commands (but this should be possible somehow).

Instead, I use the Chrome in debug mode (F12) head over to 
https://intellij-frontend.vocon-it.com/products and filter for "ey", since every token starts with "ey".

You can find the key in the body of getAccountInfo or in the "channel" in the authorization header.
Copy the token and assign it to the TOKEN environment variable as shown below:

Now we can access the protected data with the valid token:
```shell
TOKEN=eyJhbGc...
curl -H "Authorization: Bearer $TOKEN" localhost:8090/protected/data

# output:
Oliver, you have accessed protected data from spring boot
```
### Role Based access
#### Define a super-user
Super-users must be defined via the environment variable SUPER_ADMINS (can be a list of users-IDs, but I have tested 
only with a single email addrss in SUPER_ADMINS).

```shell
SUPER_ADMINS=oliver.veits@vocon-it.com
```

#### Add a custom role
Custom roles can be defined via the main/java/security/roles/RoleController class

Only tested as super-user (see above):
```shell
curl -X PUT -H "Authorization: Bearer $TOKEN" "localhost:8090/role/role/add?uid=FKZlClsgcxZ3d1HcygDowscklfF2&role=ROLE_SELLER"

# or
TOKEN=ey...
FIREBASE_UID=FKZlClsgcxZ3d1HcygDowscklfF2
FIREBASE_ROLE=ROLE_SELLER
curl -X PUT -H "Authorization: Bearer $TOKEN" "localhost:8090/role/role/add?uid=${FIREBASE_UID}&role=${FIREBASE_ROLE}"
```
This produces following server log:
```shell
18:40:49 || DEBUG < o.s.web.servlet.DispatcherServlet   > PUT "/role/role/add?uid=FKZlClsgcxZ3d1HcygDowscklfF2&role=ROLE_SELLER", parameters={masked}
18:40:49 || DEBUG < .m.m.a.RequestMappingHandlerMapping > Mapped to io.thepro.apiservice.security.roles.RoleController#addRole(String, String)
18:40:50 || DEBUG < .RequestResponseBodyMethodProcessor > Using 'application/json', given [*/*] and supported [application/json, application/*+json, application/json, application/*+json]
18:40:50 || DEBUG < .RequestResponseBodyMethodProcessor > Nothing to write: null body
18:40:50 || DEBUG < o.s.web.servlet.DispatcherServlet   > Completed 200 OK
```

After that, the corresponding user can access the seller data:
```shell
curl -H "Authorization: Bearer $TOKEN" localhost:8090/seller/data

# output: 
You have accessed seller only data from spring boot
```
> NOTE: You need to claim a new TOKEN from firebase, so the new role is reflected in the token data. For that you might need
> to log out and log in to firebase in the frontend, where you get the token from.

> Note: custom roles are sent to firebase in lowercase, 
> ```shell
> # RoleServiceImpl.java
> claims.put(role.toLowerCase();
> ...
> firebaseAuth.setCustomUserClaims(uid, claims);
> ```
> while the corresponding claims are converted to uppercase before they are assigned
> to roles. 
> ```shell
> # SecurityFilter.java
> decodedToken.getClaims().forEach((k, v) -> authorities.add(new SimpleGrantedAuthority(k.toUpperCase())));
> ```
> The reason is that roles are preceded with uppercase ROLE_ prefix, therefore
> lowercase roles will not easily work. See [this stackoverflow question and its answer](https://stackoverflow.com/questions/33205236/spring-security-added-prefix-role-to-all-roles-name) for more information.
>
## UI Demo
See `ui-client-side-session-demo/README.md`.

### Configuration

- Be sure to add the following environment variable globally or project specific run configuration environment variable `GOOGLE_APPLICATION_CREDENTIALS=path_to_firebase_server_config.json`

- The starter can be configured to use firebase session as client side / strictly server side or both together.
- Htty Only / Secure enabled Session cookies may not work as expected in development hosts (localhost, 127.0.0.1). Adding self signed ssl certificate with reverse proxied host will work perfectly fine. Read this article => [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)
- Following application properties can edited to customize for your needs. Sample @ [application.yaml](src/main/resources/)

### Role Management

- Roles can be added through `SecurityRoleService` during registeration of user or manually managed by Super admins
- Super Admins are defined through application property `security.super-admins`
- With roles interated with spring security, spring authorization annotations like **`@Secured, @RolesAllowed, @PreAuthorize, @PostAuthorized`** etc will work out of the box.
- I personally like to define per role annotations like **`@IsSuper, @IsSeller`** etc for the sake of simplicity.

```
    @GetMapping("data")
	@isSeller
	public String getProtectedData() {
		return "You have accessed seller only data from spring boot";
	}
```

- UI useAuth hook also has utility properties like **_ `roles, hasRole, isSuper, isSeller `_** properties exposed accross the application to allow or restrict access to specific UI components

## Related Tutorials :

- [Firebase Authentication for Spring Boot Rest API](https://thepro.io/post/firebase-authentication-for-spring-boot-rest-api-5V)
- [Firebase and Spring Boot Based Role Management and Authorization](https://thepro.io/post/firebase-and-spring-boot-based-role-management-and-authorization-3D)
- [Firebase with Spring Boot for Kubernetes Deployment Configuration](https://thepro.io/post/firebase-with-spring-boot-kubernetes-deployment-configuration-RA)
- [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)

### UI Demo

#### UI Demo

Nextjs application demonstrating client side firebase session. [ui-demo](ui-client-side-session-demo/)

#### Screenshots

|                                                                        Logged out                                                                         |                                                                        Logged In                                                                         |
| :-------------------------------------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------------------------------------------: |
| ![Image of UI Loggedout](https://raw.githubusercontent.com/gladius/firebase-spring-boot-rest-api-authentication/master/ui-demo/screenshots/loggedout.png) | ![Image of UI LoggedIn ](https://raw.githubusercontent.com/gladius/firebase-spring-boot-rest-api-authentication/master/ui-demo/screenshots/loggedin.png) |

## Author

👤 **Gladius**

- Website: thepro.io/@/gladius
- Github: [@gladius](https://github.com/gladius)

## Show your support

Give a ⭐️ if this project helped you!

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
