spring-boot-web-mvc-tiles3
==========================

Spring Boot Web MVC configured to prouce an executable WAR and demonstrating Tiles 3 configuration


# Overview
No frills project that demonstrates configuring a spring-boot-starter-web project to build as an executable WAR file and demonstrate configuration of Spring Web MVC with Apache Tiles 3 framework.

I use Spring Source Tool Suite for develop and this generates a starter project that was used as the basis for this application. The notes that follow identify what I changed to get it working.

# The Changes
The changes outlined below were noted as things were developed.

* Start by creating an application based on spring-boot-starter-web (1.1.4.RELEASE)
* Add dependencies to the POM to pull in Tomcat, Jasper and Apache Tiles
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.apache.tiles</groupId>
        <artifactId>tiles-jsp</artifactId>
        <version>3.0.4</version>
    </dependency>
```
* Change the packaging to WAR in the POM
```
    <packaging>war</packaging>
```
* Add properties to redefine the application base in the POM
```
    <main.basedir>${basedir}/../..</main.basedir>
    <m2eclipse.wtp.contextRoot>/</m2eclipse.wtp.contextRoot>
```
* Create Web application structure the Maven way (tree showing all files):
```
        D:.
        │   pom.xml
        │   README.txt
        ├───src
        │   ├───main
        │   │   ├───java
        │   │   │   └───com
        │   │   │       └───mvmlabs
        │   │   │           └───springboot
        │   │   │               │   Application.java
        │   │   │               │   ConfigurationForTiles.java
        │   │   │               └───web
        │   │   │                       GreetingController.java
        │   │   ├───resources
        │   │   │       application.properties
        │   │   └───webapp
        │   │       ├───static
        │   │       │       index.html
        │   │       └───WEB-INF
        │   │           └───tiles
        │   │               │   tiles.xml
        │   │               ├───layout
        │   │               │       basic.jsp
        │   │               └───view
        │   │                   │   footer.jsp
        │   │                   │   header.jsp
        │   │                   └───home
        │   │                           greeting.jsp
        │   │                           home.jsp
        │   └───test
        │       └───java
        │           └───com
        │               └───mvmlabs
        │                   └───springboot
        │                           ApplicationTests.java
        │                           TestConfigurtaion.java
        └───target
```
* Configure tiles by adding a new configuration class - it will be pulled in by classpath scanning:
```
    package com.mvmlabs.springboot;
    
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
    import org.springframework.web.servlet.view.tiles3.TilesView;
    import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
    
    @Configuration
    public class ConfigurationForTiles {
    
        @Bean
        public TilesConfigurer tilesConfigurer() {
            final TilesConfigurer configurer = new TilesConfigurer();
            configurer.setDefinitions(new String[] { "WEB-INF/tiles/tiles.xml" });
            configurer.setCheckRefresh(true);
            return configurer;
        }
    
        @Bean
        public TilesViewResolver tilesViewResolver() {
            final TilesViewResolver resolver = new TilesViewResolver();
            resolver.setViewClass(TilesView.class);
            return resolver;
        }
    }
```
* Create the tiles configuration file: WEB-INF/tiles/tiles.xml
```
    <?xml version="1.0" encoding="ISO-8859-1" ?>
    <!DOCTYPE tiles-definitions PUBLIC
           "-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN"
           "http://tiles.apache.org/dtds/tiles-config_3_0.dtd">
    <tiles-definitions>
    
        <!-- Templates -->
        
        <definition name="layout.basic" template="/WEB-INF/tiles/layout/basic.jsp">
            <put-attribute name="title" value="Spring Web MVC with Tiles 3" />
            <put-attribute name="header" value="/WEB-INF/tiles/view/header.jsp" />
            <put-attribute name="body" value="" />
            <put-attribute name="footer" value="/WEB-INF/tiles/view//footer.jsp" />
        </definition>
        
        <!-- Pages -->  
        
        <definition name="site.homepage" extends="layout.basic">
            <put-attribute name="body" value="/WEB-INF/tiles/view/home/home.jsp" />
        </definition>
        
        <definition name="site.greeting" extends="layout.basic">
            <put-attribute name="body" value="/WEB-INF/tiles/view/home/greeting.jsp" />
        </definition>
    </tiles-definitions>
```
* Create the tiles themselves
   basic.jsp:
```
        <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
        <html>
            <head>
                <title><tiles:getAsString name="title" /></title>
            </head>
            <body>
                <tiles:insertAttribute name="header" />
                <tiles:insertAttribute name="body" />
                <tiles:insertAttribute name="footer" />
            </body>
        </html>
```    
  * header.jsp:
```
        <div>The Footer</div>
```
  * footer.jsp:
```
        <div>The Header</div>
```
    
  * home.jsp:
```
        <div>
            Main content would go here. Lets try.
        </div>
```
  * greeting.jsp:
```
        <div>
            Hello ${name}
        </div>
```
* Add a controller class (GreetingController.java) into a directory under that containing Application.java
```
    package com.mvmlabs.springboot.web;
    
    import org.apache.commons.logging.Log;
    import org.apache.commons.logging.LogFactory;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.ModelAndView;
    
    @Controller
    public class GreetingController {
        private Log log = LogFactory.getLog(this.getClass());
    
        @RequestMapping(value = "/home", method=RequestMethod.GET)
        public String home() {
            return "site.homepage";
        }
        
        @RequestMapping(value = "/greet", method=RequestMethod.GET)
        public ModelAndView greet(@RequestParam(value = "name", required=false, defaultValue="World!")final String name, final Model model) {
            log.info("Controller has been invoked with Request Parameter name = '" + name + "'.");
            return new ModelAndView("site.greeting", "name", name);
        }
    
        @RequestMapping(value = "/greet/{name}", method=RequestMethod.GET)
        public ModelAndView greetTwoWays(@PathVariable(value="name") final String name, final Model model) {
            log.info("Controller has been invoked with Path Variable name = '" + name + "'.");
            return new ModelAndView("site.greeting", "name", name);
        }
    }
```
* Fix the unit tests, add a new test configuration class to replace tiles configuration with one that reads the config from filesystem rather than trying to use a URL
```
        package com.mvmlabs.springboot;
        
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
        
        @Configuration
        public class TestConfigurtaion {
        
            @Bean
            public TilesConfigurer tilesConfigurer() {
                TilesConfigurer configurer = new TilesConfigurer();
                configurer.setDefinitions(new String[] { "file:src/main/webapp/WEB-INF/tiles/tiles.xml" });
                configurer.setCheckRefresh(true);
                return configurer;
            }
        }
```
* Modify the application test to include the above test configuration after application has loaded its configuration, thus ensuring that the tiles configurer is switched out.
```
        package com.mvmlabs.springboot;
        
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.test.context.web.WebAppConfiguration;
        import org.springframework.boot.test.SpringApplicationConfiguration;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
        
        import com.mvmlabs.springboot.Application;
        
        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringApplicationConfiguration(classes = {Application.class, TestConfigurtaion.class})
        @WebAppConfiguration
        public class ApplicationTests {
        
            @Test
            public void contextLoads() {
            }
        }
```
* Build the project using Maven
```
    mvn clean package
```
* Run it, the --debug flag is to display Springs Auto-Configuration Report
```
    java -jar target\spring-boot-web-mvc-tiles3-1.0.war --debug
```
* Confirm static content can be accessed:
```
    http://localhost:8080/static/index.html
    http://localhost:8080/index.html
```
>Note: The first URL above demostrates Spring Boot mapping of static resources
>      The second URL above demonstrates default index.html mapping provided by Spring Boot
* Confirm that Spring MVC has been configured as expected
```
    http://localhost:8080/home
    http://localhost:8080/greet?name=Mark
    http://localhost:8080/greet/Mark
```

All done here.

