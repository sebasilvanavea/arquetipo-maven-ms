# Indice

 1. [Descripcion](#descripcion)
 2. [Prerequisitos](#prerequisitos)
 3. [Componentes](#componentes)
 4. [Propiedades Maven en POM.xml](#propiedades-maven-en-pomxml)
 5. [Propiedades de la aplicación](#propiedades-de-la-aplicación)
 6. [Integración con repositorio de datos](#integración-con-repositorio-de-datos)
 7. [Integración con API REST](#integración-con-api-rest)
 8. [Logging](#logging)
 9. [Estructura](#estructura)
10. [Pruebas](#pruebas)
11. [F.A.Q - Preguntas frecuentes](#faq---preguntas-frecuentes)

---
## Descripcion

Este código fuente contiene el template para una aplicación que expone servicios REST utilizando el framework de [Spring Boot 3][spring_boot_url]. Considerar este proyecto como una guía para el desarrollo de Microservicios y BFF.
El proyecto trabaja con [Maven][maven_url] como sistema de automatización de la construcción.

---
## Prerequisitos

El primer prerequisito es tener un kit de desarrollo Java o Java JDK instalado. Este proyecto fue construido con _Java SE 17_ utiliando alguno de los JDK compatibles (por ejemplo: _Eclipse Temurin, Amazon Coretto, Bellsoft Liberica_)

El segundo prerequisito es contar con Maven. Una guía para la instalación de Maven se puede encontrar en la documentación oficial de [Apache Maven Project - Installing Apache maven][maven_install_url].

---
## Componentes

1. Aplicación Java construida con [Spring Boot 3][spring_boot_url].
2. Dependencias y configuración para integración con una base de datos embebida tipo [H2 database][h2_database_url].
3. Definición de dependencias BCH mediante BOM
4. Test unitarios construidos con [Spring Testing][spring_test_url], [Junit][junit_url], [Mockito][mockito_url], y algunas herramientas utilitarias como [AssertJ][assertj_url] y [Java Hamcrest][hamcrest_java_url].
5. Documentación de API y endpoints mediante [Swagger][swagger_url].
6. Definición de umbrales de cobertura vía [Maven][maven_url] en POM.xml, ejecución y verificación de cobertura con [Jacoco][jacoco_url].
7. Validación de reglas de buenas prácticas de desarrollo via [Maven][maven_url] en POM.xml con ejecución de [PMD][pmd_url]
8. Validación de reglas de estructura via [Maven][maven_url] en POM.xml con ejecución de Maven Enforcer

---
## Propiedades Maven en POM.xml

Algunas propiedades definidas para Maven en el archivo POM.xml relevantes de mencionar son:

---
- Descriptores y versión del proyecto, utilizando la especificación de [SemVer][semver_url].
```xml
<groupId>cl.bch.cloud</groupId>
<artifactId>test</artifactId>
<packaging>jar</packaging>
<version>0.0.1</version>
<name>test</name>
```
---
- Versión del framework de Spring Boot 3, mediante POM parent Banco de Chile.
```xml
<parent>
  <groupId>cl.bch.cloud</groupId>
  <artifactId>bch-ms-parent</artifactId>
  <version>1.0.0</version>
  <relativePath/>
</parent>
```
---
- Dependencias del proyecto con versiones definidas en el BOM Banco de Chile.
```xml
<dependencies>
    ...
    <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    </dependency>
    ...
</dependencies>
```

---
- Las pruebas con [Jacoco][jacoco_url] se encuentra definida en el POM Parent. Exclusiones y reglas de cobertura pueden ser realizadas mediante properties.
```xml
<properties>
  ...
  <sonar.coverage.exclusions>**/Application.*, **/config/*.*, **/exceptions/*.*, **/enums/*.*, **/dtos/*.*,
    **/entities/*.*, **/controllers/ExceptionHandlerController.*, **/services/impl/HelloServiceImpl.*
  </sonar.coverage.exclusions>
  <jacoco.coverage.line.min>0.8</jacoco.coverage.line.min>
  <jacoco.coverage.branch.min>0.4</jacoco.coverage.branch.min>
  ...
</properties>
```

---
## Propiedades de la aplicación

Las propiedades de la aplicación se establecen en los archivos `src/main/resources/application.yaml` (ver [especificación YAML][yaml_specification_url]), `src/main/resources/application.properties`, `src/main/resources/osb.properties`, `src/main/resources/rest.properties` y `src/main/resources/bbdd.properties`. Estos archivos tiene algunos puntos relevantes a mencionar:

## `src/main/resources/bootstrap.yml`
Este archivo de propiedades establece las propiedades que se cargan previo al arranque del contexto de la aplicación. __Este archivo no debe ser modificado, salvo para ajustar el nombre de la aplicación, previa revisión con el arquitecto encargado.__

---
```yml
spring:
    application:
        name: test
```
- Establece el nombre de la aplicación. Dada la configuración de la dependencia `spring-cloud-starter-kubernetes-config`, este nombre establece la búsqueda de un 
[ConfigMap en Kubernetes][kubernetes_configmap_url] con el nombre de la aplicación.
---

```yaml
...
spring:
  cloud:
    kubernetes:
      config:
        enabled: true
        sources:
          - name: @project.artifactId@
          - name: bbdd
          - name: osb
          - name: rest
          - name: context-shared
```
- Se establecen las propiedades para el lookup de ConfigMaps desde Kubernetes:
  * De acuerdo a valores por defecto, el restart y pooling de configmaps para detectar cambios está desactivado
  * `spring.cloud.kubernetes.config.enabled`: Habilita recuperar los configmaps indicados durante el bootstrap de la aplicación
  * `spring.cloud.kubernetes.config.sources`: Listado de ConfigMaps a consumir. __Notar como este corresponde a un ejemplo, y la definición de los ConfigMaps a consumir por su proyecto puede variar. En caso de existir segmentación de propiedades en estos u otros ConfigMaps, será necesario incorporar los archivos de propiedades correspondientes como parte de los recursos de la aplicación (_src/main/resources_) e incorporar su uso como _PropertySource_ en la clase Application.java. Se debe revisar con el arquitecto responsable esta definición.__

---

```yaml
management:
  endpoint:
    health:
      enabled: true
      show-details: ALWAYS
    info:
      enabled: true
    prometheus:
      enabled: true
  endpoints:
    enabled-by-default: false
    web:
      base-path: /
      exposure:
        include:
          - health
          - info
          - prometheus
```
- Define la habilitación de endpoints para `spring-boot-starter-actuator`. Estos endpoints son requeridos como parte de la arquitectura, en especial para la integración con Kubernetes, la lectura de ConfigMaps y para la integración con la herramienta de monitoreo Grafana.
---

## `src/main/resources/application.yaml`

```properties
startup.beans.inspect=false
```
- Propiedad utilizada en la clase `src/main/java/cl/bch/cloud/Application.java` para imprimir los Beans cargados en el contexto de la aplicación al momento de iniciarlo. Es de utilidad para temas de desarrollo y/o profiling.
---

```properties
logging.level.root=INFO
```
- Establece el nivel de registro de eventos para el logging.
---

```yaml
swagger:
  info:
    name: @project.artifactId@
    description: @project.description@
    version: @project.version@
    contact:
      name: Lider Técnico
      mail: lidertecnico@bancochile.cl
      
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    try-it-out-enabled: false
```
- Habilita/Deshabilita la UI de Swagger, y configura la descripción del API documentada.
---

## `src/main/resources/bbdd.properties`
```properties
spring.datasource.driver-class-name=org.h2.jdbcx.JdbcDataSource
spring.datasource.url=jdbc:h2:mem:db
spring.datasource.username=
spring.datasource.password=

spring.datasource.hikari.maximumPoolSize=10
spring.datasource.hikari.connectionTimeout=30000
spring.datasource.hikari.idleTimeout=600000
spring.datasource.hikari.maxLifetime=0

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.database=H2
spring.jpa.openInView=false
spring.jpa.show_sql=true
spring.jpa.generate-ddl=true

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.EJB3NamingStrategy
spring.jpa.properties.hibernate.cache.use_query_cache=false
spring.jpa.properties.hibernate.generate_statistics=true
```
- Establece propiedades de configuración para integración con un repositorio de datos. Estas configuraciones son parte de la definición del DataSource y de JPA, que para el caso de este proyecto se configuran para integrar con una base de datos embebida [H2 database][h2_database_url]. Definiciones del DataSource y configuraciones de JPA deberán modificarse de acuerdo a los requerimientos de cada implementación.

---

```properties
feign.client.config.default.connectTimeout=5000
feign.client.config.default.readTimeout=5000
feign.client.config.default.loggerLevel=basic
```
- Establece las configuraciones para los clientes Feign (ver [documentación de referencia de Feign][feign_reference_url]) para en consumo de API REST.
---

```yaml
server:
  port: 8080
```
- Establece el puerto de exposición para el servidor Tomcat. Mas configuraciones para el servidor son realizables de acuerdo a lo definido en la documentación oficial de Spring Boot. En el caso de este proyecto, la documentación se basa en la versión [Spring Boot 2.2.6 - Howto embedded servers][spring_boot_servers_url].

---
## Integración con repositorio de datos

Como parte del proyecto se provee una base de datos embebida tipo H2. Como se menciona en secciones anteriores, la configuración de conexión a esta base de datos en memoria se realiza en el archivo `src/main/resources/bbdd.properties`. El objetivo de esta integración es documentar la integración a otros posibles repositorios de datos. Las propiedades de configuración deberán actualizarse según corresponda. Lo importante a destacar en este punto, es que como parte de la dependencia de `spring-boot-starter-data-jpa` y la configuración de Spring Boot en este proyecto (ver archivo POM.xml), las conexiones JDBC se realizan mediante [HikariCP][hikaricp_url].

```properties
spring.datasource.hikari.maximumPoolSize=10
spring.datasource.hikari.connectionTimeout=30000
spring.datasource.hikari.idleTimeout=600000
spring.datasource.hikari.maxLifetime=0
```
- Tal como se indica en la sección [Propiedades de la aplicación](#propiedades-de-la-aplicación), el archivo `bbdd.properties` contiene propiedades de configuración de HikariCP para el pool de conexiones al repositorio de datos. Estas y otras configuraciones (_ver [HikariCP - Configuration][hikaricp_configuration_url]_) deben manejarse de forma independiente por cada perfil de configuración, y los valores deben evaluarse para cada implementación junto al equipo responsable.

```properties
spring.datasource.driver-class-name=org.h2.jdbcx.JdbcDataSource
spring.datasource.url=jdbc:h2:mem:db
spring.datasource.username=
spring.datasource.password=
```
- Estas propiedades son requeridas para la integración JPA vía JDBC a un repositorio de datos, el que para este ejemplo corresponde a una base de datos embebida [H2 database][h2_database_url]. Conexiones a otros motores de bases de datos (por ejemplo, Oracle DataBase, PostreSQL, MySQL, SQL Server, otros) requerirán incorporar la configuración del driver correspondiente. _Nota: Es posible que dicho driver deba ser incorporado como dependencia en el archivo POM.xml._

Para este proyecto, la integración trabaja junto al script SQL ubicado en el archivo `src/main/resources/data.sql`, el cual opera mediante [Hibernate][hibernate_url] como parte de la dependencia `spring-boot-starter-data-jpa`. Este archivo ejecuta al inicio de la aplicación, por lo que puede ser utilizado para ejecutar ajustes iniciales.

---
## Integración con API REST

Este proyecto incorpora la capacidad de consumir APIs REST mediante [Feign][feign_url] de Spring Cloud. Esta integración de ejemplo se realiza consumiendo el API de pruebas provista por [http://www.jsonschema2pojo.org/](http://www.jsonschema2pojo.org/).

El objeto PostDTO para la integración se define en el package `cl.bch.cloud.dtos` y la interfaz JsonPlaceHolderClient de Feign en el package `cl.bch.cloud.restclients`. Finalmente, la ejecución del consumo de un API Rest se ejecuta en la clase `cl.bch.cloud.repositories.JsonPlaceHolderRepository` como parte del servicio `noGreetings` en la clase `cl.bch.cloud.services.impl.HelloServiceImpl`.

---
## Estructura

Este proyecto utiliza la estructura de _Controller-Service-Repository_. Todas las clases Java que implementan estas capas de la aplicación se encuentran en los paquetes que definen su responsabilidad. Por ejemplo, para estos componentes y las clases que los implementan, existen los paquetes `cl.bch.cloud.controllers`, `cl.bch.cloud.services` y `cl.bch.cloud.repositories` en las carpetas con similar ruta bajo `src/main/java`.

De similar manera, clases de soporte a esta estructuración de capas de la aplicación se encuentran en los paquetes `cl.bch.cloud.entities`, `cl.bch.cloud.dtos` y `cl.bch.cloud.config`.

Adicionales clases y objetos que sean incorporados al proyecto deberán seguir una definición de paquetes similar, según corresponda.

---
## Logging

El registro de logs se realiza mediante [Logback][logback_url] que es la libreria por defecto utilizada con los starters de Spring Boot. La configuración de Logback y los appenders se encuentra en el archivo `src/main/resources/logback-spring.xml` en donde para este proyecto se definen appenders de archivos con política de rolling. Un punto importante a mencionar es la configuración de la ruta de escritura del archivo:
```xml
...
<file>${LOGS_APP_BANCO}/archetype-v1.log</file>
...
```

La variable de sistema `LOGS_APP_BANCO` establece la ruta de escritura de los archivos de log, la cual debe ser indicada en la ejecución de la aplicación:
```sh
java -jar -DLOGS_APP_BANCO=<ruta-escritura-de-logs> <nombre-empaquetado-jar-generado>
```

---
## Pruebas

Pruebas unitarias se encuentran en la carpeta `src/test`. Estas se construyen utilizando los paquetes [org.springframework.test.context][springboot_test_context_url] y el [org.springframework.test.web][springboot_test_web_url] del framwork de Spring, [Junit][junit_url] y [Mockito][mockito_url], y algunas utilidades adicionales como [AssertJ][assertj_url] y [Java Hamcrest][hamcrest_java_url].

Los reportes de pruebas unitarias generados por Jacoco son utilizados para evaluar reglas de cobertura y aceptar el proceso de compilación, tal como se define en la sección [Propiedades Maven en POM.xml](#propiedades-maven-en-pomxml). Utilizando el ciclo de vida de Maven, el paso de testing genera los reportes HTML en la carpeta `target/site/jacoco`.

Las reglas de cobertura y aceptación se definen en el archivo `POM.xml` bajo el plugin de Jacoco. Los umbrales siguen la definición de [Jacoco - Coverage Counters][jacoco_coverage_url].

```xml
<rules>
    <rule>
        <element>PACKAGE</element>
        <limits>
            <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.8</minimum>
            </limit>
        </limits>
    </rule>
    <rule>
        <element>PACKAGE</element>
        <limits>
            <limit>
                <counter>BRANCH</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.4</minimum>
            </limit>
        </limits>
    </rule>
</rules>
```
__Notar como los valores de aceptación de este proyecto guía no necesariamente establecen los umbrales definidos para otros desarrollos.__

---
## F.A.Q - Preguntas frecuentes
#### ¿Puedo ejecutar la aplicación en ambiente local?

Si, es posible probar la aplicacón en ambiente local. Para ello es necesario cumplir con los prerequisitos mencionados en la sección de [Prerequisitos](#prerequisitos). No es necesario contar con Docker para contenerización ni con Kubernetes para generar clústers de contenedores.

* ¿Como compilo la aplicación?
El uso de Maven sigue un ciclo de vida de las aplicaciones (ver [Maven Build Lifecycle][maven_lifecycle_url]). Para pruebas en ambiente local, el objetivo es proceder con este ciclo hasta la etapa de package o empaquetado. Para ello se opera de la siguiente manera:
  - Abrir una consola en la misma ruta donde se encuentre el archivo `pom.xml` del proyecto.
  
  - Ejecutar el comando `maven clean package`.


* ¿Cómo ejecuto la aplicación?
Ejecutando el empaquetado JAR de Spring Boot con el servidor Tomcat embebido.
  - Abrir una consola en la misma ruta del archivo `pom.xml` que haga sido utilizado para el ciclo de empaquetado maven.
  
  - Usar el comando `java -jar target/<nombre-empaquetado-jar-generado>`. Notar como este proyecto de ejemplo utiliza la variable de sistema _LOGS_APP_BANCO_, la cual se inyecta al momento de ejecución con el siguiente comando `java -jar -DAPP_BANCO_LOGS=<ruta-escritura-de-logs> target/<nombre-empaquetado-jar-generado> `.

#### ¿Cómo comienzo a desarrollar?

1. Lo primero es realizar los ajustes en el archivo POM.xml de acuerdo a los descriptores del proyecto de acuerdo al desarrollo a iniciar:
    ```xml
    <groupId>cl.bch.cloud</groupId>
    <artifactId>test</artifactId>
    <packaging>jar</packaging>
    <version>0.0.1</version>
    <name>test</name>
    ```
   
2. En segundo lugar, se deberán eliminar desde el mismo archivo POM.xml aquellas dependencias de las cuales el nuevo desarrollo no hará uso. Muchas de las dependencias de ejemplo se encuentran con comentarios asociados.
 
3. En tercer lugar, se deben renombrar los paquetes o _packages_ de la aplicación a los paquetes que correspondan de acuerdo al desarrollo a iniciar.

4. Adicionalmente, se deben eliminar las clases de ejemplo que se proveen junto a este proyecto de guía. Es posible reutilizar algunas de estas clases ya existentes.

5. Se deberán ajustar las propiedad que usa el proyecto. Para ello es necesario ajustar los archivos ubicados en la carpeta `src/main/resources`. Notar que el archivo `bootstrap.yml` no debe modificarse, salvo para ajustar el nombre de la aplicación.

6. También se debe completar el archivo `README.md` con la información del proyecto, y borrar el archivo `DELETEME.md` que contiene las instrucciones de uso del proyecto. 

7. Como parte del proceso de gestión de código e integración continua, se debe generar el repositorio en el SCM correspondiente,, e incorporar el código fuente de la primera versión del desarrollo.

8. A su vez, se debe generar la rama `build-ci` para asignar el pipeline de compilación y despliegue según corresponda. Se deben asignar los permisos adecuados a esta rama, permitiendo el ingreso de código sólo al responsable (por ejemplo,, al Technical Lead del equipo de desarrollo).

9. Finalmente, se puede comenzar a codificar e incorporar funcionalidades siguiendo la estrategia de branching definida y registrando la evolución del desarrollo en el archivo `CHANGELOG.md`.

#### ¿Cómo cambio el mecanismo de integración a otras bases de datos?

Tal como se indica en la sección [Integración con repositorio de datos](#integración-con-repositorio-de-datos), el proyecto cuenta con la dependencia _spring-boot-starter-data-jpa_ la que provisiona a su vez la dependencia _spring-boot-starter-jdbc_. Esta última dependencia permite, con pocas configuraciones, establecer una conexión JDBC con un motor de base de datos. Dependiendo del tipo de base de datos con la cual se desee conectar (MySQL, PostreSQL, Oracle Database, SQL Server, otro), es necesario realizar algunas configuraciones.

En el archivo **pom.xml** se deberá proveer el driver de conexión correspondiente a cada tipo de base de datos.

Por ejemplo, para conectar con una base de datos Oracle:
```xml
<oracle.ojdbc.version>[19,20)</oracle.ojdbc.version>
...
<dependency>
    <groupId>com.oracle.ojdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>${oracle.ojdbc.version}</version>
</dependency>
```
- Aquí se incorpora como dependencia el artefacto _ojdbc8_ en versión 19.x.x de acuerdo a la disponibilidad en el repositorio de artefactos, ya sea Maven u otro repositorio de artefactos autorizado. Notar como se indica la versión en formato _[19,20)_, lo que señala versión disponible entre versión 19 inclusive y versión 20 excluyente.

Para el caso de base de datos PostgreSQL, el archivo **pom.xml** debiese contener algo como esto:
```xml
<postgresql.jdbc.version>AQUI_LA_VERSION_CORRESPONDIENTE</postgresql.jdbc.version>
...
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>${postgresql.jdbc.version}</version>
</dependency>
```

Y para el caso de MySQL:
```xml
<mysql.connector.version>AQUI_LA_VERSION_CORRESPONDIENTE</mysql.connector.version>
...
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.connector.version}</version>
</dependency>
```

Finalmente, en el archivo **bbdd.properties** o equivalente, modificamos los datos de conexión y el driver a utilizar.

Por ejemplo, para conectar con una base de datos Oracle:
```properties
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=AQUI_LA_URL_DE_CONEXION
spring.datasource.username=AQUI_EL_USUARIO_DE_CONEXION
spring.datasource.password=AQUI_LA_PASSWORD_DE_CONEXION
```

Para el caso de base de datos PostgreSQL:
```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=AQUI_LA_URL_DE_CONEXION
spring.datasource.username=AQUI_EL_USUARIO_DE_CONEXION
spring.datasource.password=AQUI_LA_PASSWORD_DE_CONEXION
```

Y para el caso de base de datos MySQL:
```properties
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=AQUI_LA_URL_DE_CONEXION
spring.datasource.username=AQUI_EL_USUARIO_DE_CONEXION
spring.datasource.password=AQUI_LA_PASSWORD_DE_CONEXION
```
o, dependiendo de la versión del dirver:
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=AQUI_LA_URL_DE_CONEXION
spring.datasource.username=AQUI_EL_USUARIO_DE_CONEXION
spring.datasource.password=AQUI_LA_PASSWORD_DE_CONEXION
```

---
[alpine_linux_url]: https://alpinelinux.org/
[assertj_url]: http://joel-costigliola.github.io/assertj/
[docker_ce_url]: https://docs.docker.com/install/
[docker_url]: https://www.docker.com/
[dockerfile_url]: https://docs.docker.com/engine/reference/builder/
[feign_url]: https://spring.io/projects/spring-cloud-openfeign
[feign_reference_url]: https://cloud.spring.io/spring-cloud-openfeign/reference/html/
[h2_database_url]: http://www.h2database.com
[hamcrest_java_url]: http://hamcrest.org/JavaHamcrest/
[hibernate_url]: http://hibernate.org/
[hibernate_url]: https://hibernate.org/
[hikaricp_configuration_url]: https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
[hikaricp_url]: https://github.com/brettwooldridge/HikariCP
[jacoco_coverage_url]: https://www.eclemma.org/jacoco/trunk/doc/counters.html
[jacoco_url]: https://www.eclemma.org/jacoco/
[java_jdk_jse_url]: https://www.oracle.com/technetwork/java/javase/downloads/index.html
[junit_url]: https://junit.org/
[kubernetes_configmap_url]: https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/
[logback_url]: http://logback.qos.ch/
[maven_install_url]: https://maven.apache.org/install.html
[maven_lifecycle_url]: https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html
[maven_plugins_url]: https://maven.apache.org/plugins/
[maven_url]: https://maven.apache.org/
[mockito_url]: https://site.mockito.org/
[semver_url]: https://semver.org/
[spring_boot_servers_url]: https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/html/howto.html#howto-embedded-web-servers
[spring_boot_url]: https://spring.io/projects/spring-boot
[spring_test_url]: https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html
[springboot_test_context_url]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/package-summary.html
[springboot_test_web_url]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/package-summary.html
[swagger_url]: https://swagger.io/
[yaml_specification_url]: https://yaml.org/spec/
[pmd_url]: https://docs.pmd-code.org/latest/
