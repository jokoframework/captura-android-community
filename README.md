# Captura Android

## Ambiente de Desarrollo

El stack tecnológico es fijo durante todo el desarrollo. Sólo en casos excepcionales, y programados; se hacen upgrades de versiones.

    - Android Stack tecnológico
    - Requiere JAVA 8.
    - Gradle 4.4
    - Gradle Plugin 3.1.4
    - Android Studio 3.5
    - Sonarqube 7.9.1 (build 27448)

## Generación del APK

Para generar el apk, son necesarias estas configuraciones en el `gradle.properties` general del usuario que ejecuta el build. En Unix sería el archivo: `$HOME/.gradle/gradle.properties`:

    org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
