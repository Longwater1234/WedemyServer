# WedemyServer

(Backend repo). Clone of Udemy, an e-learning platform, built using Springboot + Vue 3 + Typescript. With CreditCard and
PayPal checkout (both powered by **Braintree Payments**). Uses Spring Security, Spring Session Redis & Server-Side
Cookies[^1] (see footnote) for auth,
_instead of_ stateless JWT Tokens. CSRF protection is enabled. Maximum 2 *concurrent* login sessions per user. You can
easily customize these settings in [SecurityConfig](src/main/java/com/davistiba/wedemyserver/config/SecurityConfig.java)
.

## Frontend & Live Demo

Click to view [Frontend Repo](https://github.com/Longwater1234/WedemyClient) built using Vue 3 and Typescript.

## Requirements

- Java 11 or higher
- MySQL 8
- Redis Server 5.0+ (native / Cloud / Docker)
- [Google OAuth Credentials](https://console.developers.google.com/apis/credentials) (for _Google Login_)
- [Braintree Developer](https://developer.paypal.com/braintree/docs) Account + API Keys.
- (OPTIONAL) Free PayPal Business Account.

### Environmental Variables

You MUST set these ENV variables on your System or Container before you launch this Springboot app. **💡TIP**: During
dev/test, you can easily set them up within your IDE: In either Eclipse or IntelliJ IDEA, in the top toolbar, find
the **Run** menu > **Edit/Run Configuration** > **Environment** > **Environmental Variables**. Add (+) each key and its
value, then click **Apply**.

```bash
#below are for Google OAuth
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
#below are for Braintree Payments
BT_MERCHANT_ID=
BT_PUBLIC_KEY=
BT_PRIVATE_KEY=
#... in production, activate "prod" profile
SPRING_PROFILES_ACTIVE=prod
```

## Important ⚠

Please examine the file [application.yml](src/main/resources/application.yml) inside src/main/resources/ folder. Place
all your necessary Spring Application properties there. Notice property `frontend.root.url`; replace value with yours.
But for _sensitive_ info (like Passwords or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY**❌ . I suggest store them as
Environmental Variables instead (see above), then either declare them as `property.name = ${ENV_KEY_NAME}`, OR call
directly in your source code as shown
in [BraintreeConfig](src/main/java/com/davistiba/wedemyserver/config/BraintreeConfig.java).

## Extras

For your convenience, I have included a Dockerfile, and a mysqldump
file [data_wedemy.sql](src/main/resources/data_wedemy.sql) which contains sample data for testing. Take a look at
the [ERD diagram](src/main/resources/wedemy_erd.png) of this DB. All DateTimes are stored and queried in UTC only.
Handle timezone conversion on your Frontend!

- To maintain consistent time-zone (UTC) with your Java app, ensure your MySQL connection URL has
  parameter `connectionTimeZone=UTC`. See example below. For native @Query's, use UTC_TIMESTAMP() or UTC_DATE().
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   # OR, use this
   spring.jpa.properties.hibernate.jdbc.time_zone=UTC
   ```
- Download Redis: [MacOS & Linux](https://redis.io/download) | Windows
  (.msi) [installer](https://github.com/tporadowski/redis/releases)
- Official Redis GUI client for all platforms. [RedisInsight](https://redis.com/redis-enterprise/redis-insight/)
- Download MySQL 8.0.x (LATEST) https://dev.mysql.com/downloads/mysql/
- Official Braintree Payments [Docs.](https://developer.paypal.com/braintree/docs)

***
[^1]: In production, for Browser clients, ensure both your Backend and Frontend share the same ROOT domain (same-site
policy), and set `session.cookie.Secure=true` (strictly https), for Cookies to work properly. For other REST clients,
you may replace cookies with special Header X-AUTH-TOKEN (expires like cookies too); see SecurityConfig.java. Learn
more: [WebDev](https://web.dev/samesite-cookies-explained/)  
