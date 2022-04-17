# WedemyServer

(Backend repo). A Springboot + Vue 3 + Typescript clone of Udemy, an e-learning platform. With PayPal and CreditCard
checkout (both powered by **Braintree Payments**). Uses Spring Security, Spring Session Redis, and Cookies (httpOnly)
for auth, _instead of_ stateless JWT Tokens. CSRF protection is ENABLED. For simplicity, both UserDetails and UserRole (
enum) are stored in the same table. Max 2 login **sessions** per user at any time. If same user logs in third time,
first session is revoked. You can easily customize these settings
in [SecurityConfig](src/main/java/com/davistiba/wedemyserver/config/SecurityConfig.java)

## Requirements

- JDK 11+
- MySQL 8.0.x
- Redis Server 6.0+ (local or Cloud)
- [Google OAuth Credentials](https://console.developers.google.com/apis/credentials) (for _Google Login_)
- [Braintree](https://developer.paypal.com/braintree/docs) Developer Account + API Keys.
- (OPTIONAL) PayPal Developer Account.

## Important ⚠

Please examine the file [application.yml](src/main/resources/application.yml) inside src/main/resources/ folder. Place
all your necessary Spring Application properties there. Notice property `frontend.root.url`; replace value with yours.
But for _sensitive_ info (like Secrets or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY** ❌. I suggest store them
as [Environmental Variables](https://www.baeldung.com/properties-with-spring) instead, then simply declare them
as `property.name = ${ENV_KEY_NAME}`, OR use directly in your code as `Environment.getProperty("ENV_KEY_NAME")`
as shown [here](src/main/java/com/davistiba/wedemyserver/config/BraintreeConfig.java).

| Tip 💡 | For IntelliJ users, I highly recommend the free plugin JPABuddy, it will make dealing with Spring Data JPA so much EASIER and CLEANER! |
|---------|:---------------------------------------------------------------------|

## Databases Used

### MySQL 8.0.x

This is the primary database. All DateTimes are stored and queried in UTC only❗ (**Hint:
USE `java.time.Instant` as Type for ALL Datetime fields. Also see point 6 below.**) Handle timezone Conversion on your
Frontend! For your convenience, I have included a mysqldump file `wedemy.sql`
inside [src/main/resources](src/main/resources) which contains sample data for some tables. You can take a look at
the [ERD diagram](src/main/resources/wedemy_db_erd.png) of this DB. To get QUICKLY STARTED:

1. Make sure you have MySQL 8.0.x. installed. (Verify, in terminal: `mysql --version`)
2. CREATE new database called `wedemy` or whatever you like.
3. Replace the values of `DB_HOST` `DB_USERNAME` and `DB_PASSWORD` inside _application.yml_ to match your database.
4. If everything is set, you may now launch the SpringBoot app for Hibernate to auto-generate all tables and indexes.
5. Now you can finally IMPORT file [wedemy.sql](src/main/resources/wedemy.sql) with sample data into your db.
6. To maintain consistent time-zone (UTC) with your Java app, ensure your MySQL connection URL has
   parameter `connectionTimeZone=UTC`. See example below. For custom @Query's, use UTC_TIMESTAMP() or UTC_DATE()
   ```properties
   spring.datasource.url= jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   ```

### Redis v6.0

This project uses Redis for 2 things: Caching, and Storing User sessions. You can download latest Redis (macOS & Linux)
from https://redis.io/download. Windows users may download the latest stable .msi
from [this Github repo](https://github.com/tporadowski/redis/releases). Or you could set it up with Docker. If you
prefer the Cloud instead, you could try Redis Cloud at: https://redis.com/try-free/. Remember to replace _host, password
and port_ for redis inside `application.yml` to match your running Redis instance.

| Tip 💡 | Redis now has an OFFICIAL cross-platform desktop GUI: RedisInsight. Download it free from [here](https://redis.com/redis-enterprise/redis-insight/) |
|---------|:---------------------------------------------------------------------|

## Payments Handling

All payments are securely handled by **Braintree Payments** (owned by PayPal), which also supports Apple Pay, GooglePay,
Venmo and many other methods. This project has been configured with PayPal and Credit-Card ONLY, in SANDBOX (test) mode.
Make sure you obtain a set of 3 API Keys from your own Braintree Account dashboard and store them as ENV
variables: `BT_MERCHANT_ID`, `BT_PUBLIC_KEY` and `BT_PRIVATE_KEY`. For Braintree tutorials and examples, please check
the [official docs](https://developer.paypal.com/braintree/docs).

