# WedemyServer

Backend repo. A Springboot + Vue.js 3 clone of Udemy. Uses Spring Session (stored in Redis) + Spring Security, for
handling auth, _instead of_ stateless JWT Tokens. Please note, CSRF protection is ENABLED in the Security Config.

## Requirements

- JDK 11+
- MySQL 8
- Redis Server (local or Cloud)
- [Google OAuth Credentials](https://console.developers.google.com/apis/credentials) (for _Sign In With Google_)
- Lombok

## Important ⚠

Please see the file [application.yml](src/main/resources/application.yml) inside `src/main/resources/`
folder. Place all your necessary Spring application values there. But for _super-sensitive_
info (like Secrets or API Keys), **DON'T PASTE THEM IN THERE DIRECTLY** 🚫. I recommend store them
as [Environmental Variables](https://www.baeldung.com/properties-with-spring) instead, then simply pass them by
reference as `property.name = ${ENV_KEY_NAME}`. Also notice my custom ENV variable `frontend.server.url`

## Databases Used

### MySQL 8

This application uses MySQL 8 (InnoDB Engine) as main database. NOTE: All DateTimes are saved and fetched in UTC
only❗. (**MUST MAKE SURE ONLY USE `java.time.Instant` as Type for ALL Datetime fields. Also see point #6 below.**).
Handle Timezone Conversion on your Frontend. I have included a mysqldump file `wedemy.sql`
inside [src/main/resources/](src/main/resources) which contains and schema for ALL tables and sample data for COURSES,
LESSONS, OBJECTIVES. Simply do the following to get started:

1. Make sure you have MySQL 8.x installed. (in terminal or CMD, enter: `mysql --version`)
2. Next, Install MySQL Workbench version 8. (OPTIONAL)
3. CREATE a database called `wedemy`.
4. Then **Import** the file [wedemy.sql](src/main/resources/wedemy.sql) into it.
5. Replace the values of `DB_HOST` `DB_USERNAME` and `DB_PASSWORD` inside _application.yml_ to match your database
   setup. That's it, DONE.
6. To maintain consistent time-zone (UTC) with your Java app, ensure your MySQL connection URL has
   parameter `connectionTimeZone=UTC`. See _application.yml_ file (line 23), or example below.
   ```properties
   spring.datasource.url= jdbc:mysql://localhost:3306/wedemy?connectionTimeZone=UTC
   ```

### Redis

This app uses Redis for 2 things: Caching, and Storing user sessions. I prefer managing sessions server-side using
Spring Session rather than using Stateless JWTs. For local development, you could download Redis (macOS & Linux)
from https://redis.io/download. Windows users should download from https://github.com/tporadowski/redis. If you prefer
the Cloud instead, you could try Redis Cloud at: https://redis.com/try-free/, for a very generous free trial.
(**no credit card required**). Or, you could just use Docker. Just make sure you change the _url, password and port_
inside `application.yml` to point your running Redis instance.

