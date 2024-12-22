# Spring Agent Setup

## How to install spring agent in a demo project

### 1. Add the following dependency to the pom.xml file

```xml
<dependency>
    <groupId>dev.inspector</groupId>
	<artifactId>springagent</artifactId>
	<version>1.0.1</version>
</dependency>
```

### 2. Reload the maven dependencies

```
mvn clean install
```

### 3. Add the following configuration to the application.properties file

```properties
inspector.ingestion-key=81e6d4df93e1bfad8e9f3c062022e3a0d8a77dce
```

### 4. Start the application

Using the IDE to start the application


# Inspector Configuration Guide

This guide provides an overview of the necessary environment variables you need to set in your YAML configuration file to properly configure and use Inspector in your application.

## Environment Variables

To fully integrate Inspector, you need to set the following environment variables in your YAML file:

1. **INSPECTOR_INGESTION_KEY** - Your unique ingestion key for authenticating API requests.
    - Example: `INSPECTOR_INGESTION_KEY="your_ingestion_key_here"`

2. **INSPECTOR_ENABLE** - Determines whether Inspector is enabled or disabled.
    - Values: `true` or `false`
    - Example: `INSPECTOR_ENABLE=true`

3 **INSPECTOR_MAX_ITEMS** - Defines the maximum number of items Inspector should handle per session.
    - Example: `INSPECTOR_MAX_ITEMS=1000`

## Configuration Example

Here is a sample snippet from a YAML configuration file integrating these environment variables:

```yaml
environment:
  INSPECTOR_INGESTION_KEY: "your_ingestion_key_here"
  INSPECTOR_ENABLE: true
  INSPECTOR_MAX_ITEMS: 1000
