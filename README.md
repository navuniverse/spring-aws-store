# spring-aws-store

Spring Boot Integration Application With AWS Parameter Store as Config Source.

The application uses Amazon Access Key and Secret key to access the Parameter store rather than IAM roles as we can use the Aceess keys method on non-EC2 environments as well.

# changes to do to use

1. You have to update your AWS Access Key and Secret Key in application.properties before use.
2. You must create Parameter /config/awsstore/hello in your AWS.
3. Run the application as Spring Boot and open swagger at http://localhost:8083/awsstore/swagger-ui.html in order to verify and test the integration. 
4. The APIs provided will help you access properties from AWS Parameter Store in multiple ways - using @Value, using @Autowired ConfigurableEnvironment and using AWS SSM client.

