# WedemyServer

Backend repo. A Springboot + Vue.js 3 clone of Udemy.

## Requirements

- Java 11+
- MySQL 8
- Hibernate
- Redis Server (local or Cloud)
- Google OAuth Credentials (for _Sign In With Google_) 
- Lombok-enabled IDE.

## Important

Make sure you create a file named **application.properties** INSIDE
`src/main/resources/` folder. Place all the necessary application values. For super-secret values, I recommend use Environmental Variables in your system. For an example, see the template
file [`src/main/resources/local.properties`](src/main/resources/local.properties)
