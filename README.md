# WedemyServer

(Backend repo). Clone of Udemy, an e-learning platform, built using Springboot + Vue 3 + Typescript. With CreditCard and
PayPal checkout (both powered by **Braintree Payments**). Uses Spring Security, Spring Session Redis, and Cookies (
httpOnly) for auth, _instead of_ stateless JWT Tokens. CSRF protection is ENABLED. For simplicity, both UserDetails and
UserRole (enum) are stored in the same table. Maximum 2 *concurrent* login sessions per user. You can easily customize
these settings in [SecurityConfig](src/main/java/com/davistiba/wedemyserver/config/SecurityConfig.java).

## Frontend & Live Demo

Click to view [Frontend Repo](https://github.com/Longwater1234/WedemyClient) built using Vue 3 and Typescript.

## Requirements

- Java 11 or higher
- MySQL 8.0.x
- Redis Server 5.0+ (local or Cloud)
- [Google OAuth Credentials](https://console.developers.google.com/apis/credentials) (for _Google Login_)
- [Braintree Developer](https://developer.paypal.com/braintree/docs) Account + API Keys.
- (OPTIONAL) PayPal Business Account.
- (OPTIONAL) HCaptcha Dev Account

### Environmental Variables

You MUST set these ENV variables on your System before you launch this Springboot app. **💡TIP**: During dev/test, you
can easily set them up within your IDE (⚠ will be LOCAL only): In either Eclipse or IntelliJ IDEA, in the top toolbar,
find the **Run** menu > **Edit/Run Configuration** > **Environment** > **Environmental Variables**. Add (+) each key and
its value, then click **Apply**.

```shell
#below are for Google OAuth
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
#below are for Braintree Payments
BT_MERCHANT_ID=
BT_PUBLIC_KEY=
BT_PRIVATE_KEY=
#Below are for HCaptcha
HCAPTCHA_CLIENT_KEY=
HCAPTCHA_SECRET_KEY=
#...You may add DB credentials too...
```

## Important ⚠

Please examine the file [application.yml](src/main/resources/application.yml) inside src/main/resources/ folder. Place
all your necessary Spring Application properties there. Notice property `frontend.root.url`; replace value with yours.
But for _sensitive_ info (like Passwords or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY** ❌. I suggest store them as
Environmental Variables instead (see above), then either declare them as `property.name = ${ENV_KEY_NAME}`, OR call
directly in your code as shown
in [BraintreeConfig](src/main/java/com/davistiba/wedemyserver/config/BraintreeConfig.java).

## Databases Used

### MySQL 8.0.x

This is the primary database. All DateTimes are stored and queried in UTC only❗ (**Hint: USE `java.time.Instant` as Type
for ALL Datetime fields. Also see point #.7 below.**) Handle Timezone conversion on your Frontend! For your convenience,
I have included a mysqldump file `data_wedemy.sql` inside [src/main/resources](src/main/resources/data_wedemy.sql) which
contains sample data for some tables. Please take a look at the [ERD diagram](src/main/resources/wedemy_erd.png) of this
DB. To get QUICKLY STARTED:

1. Make sure you have MySQL 8.0.x. installed. (Verify, in terminal: `mysql --version`)
2. CREATE new database called `wedemy` or whatever you like.
3. Replace the values of `MYSQL_HOST`, `MYSQL_USERNAME` and `MYSQL_PASSWORD` inside _application.yml_ to match your db.
4. Ensure that _spring.jpa.hibernate.ddl-auto=_**update** inside application.yml
5. If everything is set, you may launch the SpringBoot app for Hibernate to auto-generate all tables and indexes.
6. You can now IMPORT file [data_wedemy.sql](src/main/resources/data_wedemy.sql) into your db (OPTIONAL).
7. To maintain consistent time-zone (UTC) with your Java app, ensure your MySQL connection URL has
   parameter `connectionTimeZone=UTC`. See example below. For native @Query's, use UTC_TIMESTAMP() or UTC_DATE().
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   # OR, use this
   spring.jpa.properties.hibernate.jdbc.time_zone=UTC
   ```

### Redis v5.0 (or higher)

This project uses Redis for 2 main tasks: Caching, and Storing login sessions. You can download latest Redis (macOS &
Linux) from https://redis.io/download. Windows users may download the latest native installer (.msi)
from [this Github repo](https://github.com/tporadowski/redis/releases). Alternatively, you could run redis in Docker.
If you prefer the Cloud instead, you could try Redis Cloud at: https://redis.com/try-free/. Remember to replace
redis credentials inside `application.yml` to match your running Redis instance.

| Tip 💡 | Redis now has an OFFICIAL cross-platform desktop GUI client: RedisInsight. Download it free from [here](https://redis.com/redis-enterprise/redis-insight/) |
|---------|:---------------------------------------------------------------------|

## Payments Handling

All payments are securely handled by **Braintree Payments** (owned by PayPal), which also supports cards, Apple Pay,
GooglePay, Venmo and many other methods. This project has been configured with Credit-Card and PayPal Checkout only,
in SANDBOX (test) mode. Make sure you obtain a set of 3 API Keys from your own Braintree Dev Account and store
them as ENV variables: `BT_MERCHANT_ID`, `BT_PUBLIC_KEY` and `BT_PRIVATE_KEY`. For Braintree tutorials and examples,
please check their well-written [official docs](https://developer.paypal.com/braintree/docs).

