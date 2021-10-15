# Captura Android

## Ambiente de Desarrollo

El stack tecnológico es fijo durante todo el desarrollo. Sólo en casos excepcionales, y programados; se hacen upgrades de versiones.

    - Android Stack tecnológico
    - JAVA 8.
    - Gradle 6.5 o superior
    - Gradle Plugin 3.1.4
    - Android Studio 4
    - Sonarqube 7.9.1 (build 27448)

## Pre-requisitos

1. Debe obtener la configuración para el Firebase Crashlytics y Google Services desde una cuenta de Developer de Google Play para poder compilar el proyecto. 
2. Instalar las dependencias en el repositorio maven local
* [Captura Exchange Community](https://github.com/jokoframework/captura-exchange-community)
* [Captura Form Definitions Community](https://github.com/jokoframework/captura-form_definitions-community)


## Generación del APK

Para generar el apk, son necesarias estas configuraciones en el `gradle.properties` general del usuario que ejecuta el build. En Unix sería el archivo: `$HOME/.gradle/gradle.properties`:

    org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
