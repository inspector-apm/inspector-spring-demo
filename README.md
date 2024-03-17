## How to install spring agent in a demo project

### 1. Add the following dependency to the pom.xml file

``` 
<dependency>
    <groupId>dev.inspector</groupId>
	<artifactId>springdemo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. Reload the maven dependencies

```
mvn clean install
```

### 3. Add the following configuration to the application.properties file

```
inspector.ingestion-key=81e6d4df93e1bfad8e9f3c062022e3a0d8a77dce
```

### 4. Start the application

Using the ide to start the application
