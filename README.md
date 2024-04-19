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

## Acceder desde un IDE

Editar el archivo `/etc/hosts` utilizando un editor de texto (por ejemplo, `nano`):
   
    sudo nano /etc/hosts


Agregar tu ip en la línea al final del archivo:
 
    192.168.0.1	dev.com.sodep.py

Para saber tu ip desde linux, puedes usar el comando:

    ifconfig
    
## Ejecutar el Proyecto Captura desde un IDE:

    - Abrir el proyecto "captura-formserver-community" desde su IDE.
    - Ejecutar el proyecto desde su IDE.
    
## Acceder a la Aplicación Web:

En un navegador web, acceder a la siguiente URL:
    
    dev.com.sodep.py:8080/mf/login/login.mob
    

Iniciar sesión con las siguientes credenciales:
    
    - Usuario: root@mobileforms.sodep.com.py
    - Contraseña: 123456

## Editar Usuario:

    Ir a la sección de administración y editar el usuario "chake", asignándole una nueva contraseña.

## Configuración en Android Studio:

    - Abrir Android Studio.
    - Abrir el proyecto "captura-android-community" que se clonó.
    - En la parte superior, seleccionar la opción "Pixel 3a" como dispositivo de emulación.
    - Ejecutar el proyecto desde Android Studio. Esto iniciará el emulador.

## Acceder a la Aplicación Android:

    Iniciar sesión en la aplicación Android con las siguientes credenciales:
    
    - Usuario: chake@feltesq.com
    - Contraseña: [la contraseña asignada anteriormente]

