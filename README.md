# WedemyServer

(Backend repo). A Springboot + Vue 3 + Typescript clone of Udemy, an e-learning platform. With PayPal and CreditCard
checkout (powered by **Braintree** Payments). Uses Spring Session Redis + Spring Security + Cookies, for handling auth,
_instead of_ stateless JWT Tokens. CSRF protection is ENABLED. Max 2 login **sessions** per user at any time. If same
user logs in third time, first session is revoked. You can customize these settings
in [SecurityConfig](src/main/java/com/davistiba/wedemyserver/config/SecurityConfig.java)

## Requirements

- JDK 11+
- MySQL 8.0.x
- Redis Server v5.0+ (local or Cloud)
- [Google OAuth Credentials](https://console.developers.google.com/apis/credentials) (for _Sign In With Google_)
- [Braintree](https://developer.paypal.com/braintree/docs) Developer Account (for payments handling)
- (OPTIONAL) PayPal Developer Account.

## Important ⚠

Please examine the file [application.yml](src/main/resources/application.yml) inside *src/main/resources/*
folder. Place all your necessary Spring Application properties there. Notice property `frontend.root.url`; replace value
with yours. But for _sensitive_ info (like Secrets or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY** ❌. I recommend
store them as [Environmental Variables](https://www.baeldung.com/properties-with-spring) instead, then simply declare
them as `property.name = ${ENV_KEY_NAME}`, OR use directly in your code as `Environment.getProperty ("ENV_KEY_NAME")`
as shown [here](src/main/java/com/davistiba/wedemyserver/config/BraintreeConfig.java).

## Databases Used

### MySQL 8.0.17

This is the primary database. All DateTimes are stored and queried in UTC only❗ (**Hint:
USE `java.time.Instant` as Type for ALL Datetime fields. Also see point #6 below.**) Handle timezone Conversion on your
Frontend! I have included a mysqldump file `wedemy.sql` inside [src/main/resources/](src/main/resources) which contains
sample data for few tables. Take a look at the [ERD diagram](src/main/resources/wedemy_db_erd.png) of this DB. To get
QUICKLY STARTED:

1. Make sure you have MySQL 8.0.x. installed. (in terminal or CMD, enter: `mysql --version`)
2. Next, Install MySQL Workbench v8.x (OPTIONAL)
3. CREATE new database called `wedemy`.
4. Then IMPORT data file [data_wedemy.sql](src/main/resources/data_wedemy.sql) with sample data into it.
5. Replace the values of `DB_HOST` `DB_USERNAME` and `DB_PASSWORD` inside _application.yml_ to match your database
   setup.
6. To maintain consistent time-zone (UTC) with your Java app, ensure your JDBC connection URL has
   parameter `connectionTimeZone=UTC`. See example below. For custom @Query's, use UTC_TIMESTAMP() or UTC_DATE()
   ```properties
   spring.datasource.url= jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   ```

### Redis v5.0.14

This project uses Redis for 2 things: Caching, and Storing User sessions. You can download latest Redis (macOS & Linux)
from https://redis.io/download. Windows users may download the latest .msi from https://github.com/tporadowski/redis. If
you prefer the Cloud instead, you could try Redis Cloud at: https://redis.com/try-free/. Or you could set it up with
docker. Remember to replace _host, password and port_ for redis inside `application.yml` to match your running Redis
instance.

## Payments Handling

All payments are securely handled by **Braintree Payments** (owned by PayPal), which also supports Apple Pay, GooglePay,
Venmo and many other methods. This project has been configured with PayPal and Credit-Card ONLY, in SANDBOX (test) mode.
Make sure you obtain a set of 3 API Keys from your own Braintree Account dashboard and store them as ENV
variables: `BT_MERCHANT_ID`, `BT_PUBLIC_KEY` and `BT_PRIVATE_KEY`. For Braintree tutorials and examples, see
the [official docs](https://developer.paypal.com/braintree/docs). Here is a sample GitHub project.
