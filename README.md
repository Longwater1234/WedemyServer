# WedemyServer

Backend repo. A Springboot + Vue.js 3 clone of Udemy. With PayPal and CreditCard checkout (powered by **Braintree** ).
Uses Spring Session (stored in Redis) + Spring Security, for handling auth, _instead of_ stateless JWT Tokens. CSRF
protection is also ENABLED.

## Requirements

- JDK 11+
- MySQL 8
- Redis Server (local or Cloud)
- [Google OAuth Credentials](https://console.developers.google.com/apis/credentials) (for _Sign In With Google_)
- [Braintree](https://developer.paypal.com/braintree/docs) Developer Account + API Keys (for payments handling)

## Important ⚠

Please see the file [application.yml](src/main/resources/application.yml) inside `src/main/resources/`
folder. Place all your necessary Spring Application values there. But for _sensitive_
info (like Secrets or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY** 🚫. I recommend store them
as [Environmental Variables](https://www.baeldung.com/properties-with-spring) instead, then simply declare them
as `property.name = ${ENV_KEY_NAME}`, or use directly in your code as `Environment.getProperty("ENV_KEY_NAME")`

## Databases Used

### MySQL 8

This is the the primary database. All DateTimes are stored and queried in UTC only❗. (**Hint:
USE `java.time.Instant` as Type for ALL Datetime fields. Also see point #6 below.**) Handle timezone Conversion on your
Frontend! I have included a mysqldump file `wedemy.sql` inside [src/main/resources/](src/main/resources) which contains
schema for ALL tables and some sample data. To get quickly started:

1. Make sure you have MySQL 8.x installed. (in terminal or CMD, enter: `mysql --version`)
2. Next, Install MySQL Workbench version 8.x (OPTIONAL)
3. CREATE a database called `wedemy`.
4. Then **Import** the file [wedemy.sql](src/main/resources/wedemy.sql) into it.
5. Replace the values of `DB_HOST` `DB_USERNAME` and `DB_PASSWORD` inside _application.yml_ to match your database
   setup.
6. To maintain consistent time-zone (UTC) with your Java app, ensure your JDBC connection URL has
   parameter `connectionTimeZone=UTC`. See example below.
   ```properties
   spring.datasource.url= jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   ```

### Redis

This app uses Redis for 2 things: Caching, and Storing User sessions. I prefer managing sessions server-side using
Spring Session to using Stateless JWTs. You can download Redis (macOS & Linux) from https://redis.io/download. Windows
users should download latest from https://github.com/tporadowski/redis. If you prefer the Cloud instead, you could try
Redis Cloud at: https://redis.com/try-free/, (**no credit card required**). Remember to replace
_host, password and port_  inside `application.yml` to match your running Redis instance.

## Payments Handling

All payments are fully handled by Braintree Payments, a PayPal service. No credit card info is stored in my database;
only transaction ID after **successful** payment. Make sure you obtain a set of 3 API Keys from your own Braintree
Sandbox Account and store them as ENV variables: `BT_MERCHANT_ID`, `BT_PUBLIC_KEY`
and `BT_PRIVATE_KEY`. You may use test credit-card
numbers [listed here](https://developer.paypal.com/braintree/docs/guides/credit-cards/testing-go-live/java). For
tutorials and examples, see the [official docs](https://developer.paypal.com/braintree/docs).
