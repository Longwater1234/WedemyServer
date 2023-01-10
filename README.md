# WedemyServer

(Backend repo). Clone of Udemy, an e-learning platform, built using Springboot + Vue 3 + Typescript. With CreditCard and
PayPal checkout (both powered by **Braintree Payments**). Uses Spring Security & Spring Session Redis & Server-Side
Cookies[^1] (see footnote) for auth,
_instead of_ stateless JWT Tokens. For simplicity, both UserDetails and UserRole (enum) are stored in the same table.
Maximum 2 *concurrent* login sessions per user. You can easily customize these settings
in [SecurityConfig](src/main/java/com/davistiba/wedemyserver/config/SecurityConfig.java).


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
#... in production, REMEMBER to set this:
SPRING_PROFILES_ACTIVE=prod
# For others, see application-prod.yml
```

## Important ⚠

Please examine the file [application.yml](src/main/resources/application.yml) inside src/main/resources/ folder. Place
all your necessary Spring Application properties there. Notice property `frontend.root.url`; replace value with yours to avoid CORS error.
But for _sensitive_ info (like Passwords or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY** ❌. I suggest store them as
Environmental Variables instead (see above), then either declare them as `property.name = ${ENV_KEY_NAME}`, OR call
directly in your code as shown
in [BraintreeConfig](src/main/java/com/davistiba/wedemyserver/config/BraintreeConfig.java).

## Databases Used

### MySQL 8.0

This is the primary database. All DateTimes are stored and queried in UTC only❗ (**Hint: USE `java.time.Instant` as Type
for ALL Datetime fields**). Handle Timezone conversion on your Frontend! For your convenience, I have included a
mysqldump file `data_wedemy.sql` inside [src/main/resources](src/main/resources/data_wedemy.sql) which contains sample
data for some tables. Please take a look at the [ERD diagram](src/main/resources/wedemy_erd.png) of this DB.

- To maintain consistent time-zone (UTC) with your Java app, ensure your JDBC connection URL has
  parameter `connectionTimeZone=UTC`. See example below. For native @Query's, use UTC_TIMESTAMP() or UTC_DATE().
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   # OR, use this
   spring.jpa.properties.hibernate.jdbc.time_zone=UTC
   ```

### Redis v5.0 (or higher)

This project uses Redis for 2 main tasks: Caching, and Storing login sessions. You can download latest Redis (macOS &
Linux) from https://redis.io/download. Windows users may download the latest native installer (.msi)
from [this GitHub repo](https://github.com/tporadowski/redis/releases). Alternatively, you could run redis in Docker. If
you prefer the Cloud instead, you could try Redis Cloud at: https://redis.com/try-free/. Remember to replace redis
credentials inside `application.yml` to match your running Redis instance.

| Tip 💡 | Redis now has an OFFICIAL cross-platform desktop GUI client: RedisInsight. Download it free from [here](https://redis.com/redis-enterprise/redis-insight/) |
|---------|:---------------------------------------------------------------------|

## Payments Handling

All payments are securely handled by **Braintree Payments** (owned by PayPal), which also supports cards, Apple Pay,
GooglePay, Venmo and many other methods. This project has been configured with Credit-Card and PayPal Checkout only, in
SANDBOX (Dev) mode. Make sure you obtain a set of 3 API Keys from your own Braintree Dev Account and store them as ENV
variables: `BT_MERCHANT_ID`, `BT_PUBLIC_KEY` and `BT_PRIVATE_KEY`. For Braintree tutorials and examples, please check
their [official docs](https://developer.paypal.com/braintree/docs).

***

[^1]: In production, for BROWSER clients, ensure both your Backend and Frontend share the same ROOT domain (same-site), and set `session.cookie.Secure=true` (strictly https), for Cookies to work properly. Otherwise, replace cookies with token X-AUTH-TOKEN, see SecurityConfig.java. Learn
more: [WebDev](https://web.dev/samesite-cookies-explained/)  
