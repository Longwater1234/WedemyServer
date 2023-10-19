# WedemyServer

[![Static Badge](https://img.shields.io/badge/API_docs-v1.2-red)](https://longwater1234.github.io/WedemyServer/)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/Longwater1234/WedemyServer/graphs/commit-activity)
[![License: MIT](https://img.shields.io/github/license/Longwater1234/WedemyServer)](https://github.com/Longwater1234/WedemyServer/blob/master/LICENSE)
[![Static Badge](https://img.shields.io/badge/reference-help.md-orange)](HELP.md)

(Backend repo). Clone of Udemy, an e-learning platform, built using SpringBoot + Vue 3 + Typescript. With CreditCard and
PayPal checkout (both powered by **Braintree Payments**). Uses Spring Security & Spring Session Redis (via cookies[^1]
or sessionID Headers) for auth, instead of stateless JWT Tokens. CSRF protection is enabled. You can easily customize
these settings in [SecurityConfig](src/main/java/com/davistiba/wedemyserver/config/SecurityConfig.java). By default, the app runs on port 9000.

## Frontend & Live Demo

Click to view [Frontend Repo](https://github.com/Longwater1234/WedemyClient) and live Demo built using Vue 3, Vite and
Typescript. However, you can still use any frontend stack with this project. See
the [API Docs](https://longwater1234.github.io/WedemyServer/) for this project.

## Requirements

- Java 11 or higher
- MySQL 8.0
- Redis Server (latest stable)
- [Google OAuth Credentials](https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid) (for Google
  Login)
- [Braintree Developer](https://developer.paypal.com/braintree/docs) Account + API Keys. (for Payments)
- (OPTIONAL) Free PayPal Business Account.

### Environmental Variables

You MUST set these variables on your Local or Cloud ENV before you launch this SpringBoot app. **💡TIP**: During
dev/test, you can pass them via `args`, OR store inside your IDE: e.g. In either Eclipse or IntelliJ IDE, in the top
toolbar, find the **"Run"** menu > **Edit/Run Configuration** > **Environment** > **Environmental Variables**. Add (+)
each key and its value, then click **Apply**. If using Docker CLI, follow this quick
[official guide.](https://docs.docker.com/engine/reference/commandline/run/#env)

```properties
MYSQL_PASSWORD=
# below are for Google OAuth
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
# below are for Braintree Payments
BT_MERCHANT_ID=
BT_PUBLIC_KEY=
BT_PRIVATE_KEY=
# For production, set these:
SPRING_PROFILES_ACTIVE=prod
PORT=#{for Spring server}
```

## Important ⚠

Please examine the files [application.yml](src/main/resources/application.yml) (default),
and [application-prod.yml](src/main/resources/application-prod.yml) (meant for _production_). Replace all the necessary
Spring Application properties with yours. But for sensitive info (like Passwords or API Keys), **DON'T PASTE THEM IN
THERE DIRECTLY**❌ . It's safer to store them as Environmental Variables instead (see section above), then either
declare them as `property.name = ${ENV_KEY_NAME}`, OR refer them directly in your source code as shown
in [BraintreeConfig](src/main/java/com/davistiba/wedemyserver/config/BraintreeConfig.java).

## Database Setup

Read carefully the instructions in [HELP.md](HELP.md#database-setup-info), for both MySQL and Redis (_TODO: ADD spring
will auto-create tables_)

## Quick Start 🚀

### With Maven (natively)

I assume you have requirements listed above. And both your DB's are running. Using your terminal, execute the commands
below. That's it! Server will be available at http://localhost:9000

```bash
./mvnw clean package
java -jar target/wedemyserver.jar
```

### With Docker

I have attached [Dockerfile](Dockerfile) for the Java server only. You will need to set up MySQL & Redis
separately. First build the image in your terminal:

```bash
  docker build -t wedemy-server .
```

If using Docker Desktop (latest), before starting the container, simply fill in the ENV Variables in the GUI directly:

![docker_env_gui](src/main/resources/docker_env.PNG)

Otherwise, you may use your terminal to start the container:

```bash
 docker run --name "wedemy" -d -p 9000:9000 wedemy-server
```

## Payments Handling

All payments are securely handled by **Braintree Payments** (owned by PayPal), which also supports cards, Apple Pay,
GooglePay, Venmo and many other methods. This project implements Credit-Card and PayPal Checkout only, in _Sandbox_
(Dev) mode: **No actual money is deducted at Checkout**. Make sure you obtain a set of 3 API Keys from
your own Braintree Dev Account and store them as ENV variables: `BT_MERCHANT_ID`, `BT_PUBLIC_KEY` and `BT_PRIVATE_KEY`.
For Braintree tutorials and samples, please check their [official docs](https://developer.paypal.com/braintree/docs).

## License

[MIT License](LICENSE)

---

[^1]: In production, for Browser clients, ensure both your Backend and Frontend share the same _ROOT_ domain (same-site
policy), AND set property `session.cookie.Secure=true` (strictly https) for Session Cookies to work properly. Learn
more at [WebDev](https://web.dev/samesite-cookies-explained/). Alternatively, you can replace Cookies **entirely** with
special Header X-AUTH-TOKEN (by Spring; expires too). See file SecurityConfig.java.
