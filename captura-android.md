# Instrucciones para Levantar el Proyecto Captura-Android

## Requisitos Previos

- Android Studio instalado en tu sistema.
- Haber levantado previamente el proyecto "captura-web".

## Pasos para Levantar el Proyecto

1. **Clonar el Repositorio:**

    Clonar el repositorio "captura-android-community" desde GitHub:
    
    ```
    git clone https://github.com/jokoframework/captura-android-community.git
    ```

2. **Configuración de Hosts:**

    Editar el archivo `/etc/hosts` utilizando un editor de texto (por ejemplo, `nano`):
    
    ```
    sudo nano /etc/hosts
    ```

    Agregar la siguiente línea al final del archivo:
    
    ```
    tu-ip	dev.com.sodep.py
    ```

    Guardar los cambios y salir del editor.

3. **Levantar el Docker:**

    Desde el directorio donde se encuentra el proyecto "captura-community-docker", levantar los contenedores Docker.

    ```
    sudo docker-compose up
    ```

4. **Ejecutar el Proyecto Captura desde IntelliJ:**

    - Abrir el proyecto "captura-formserver-community" en IntelliJ.
    - Ejecutar el proyecto desde IntelliJ.
    
5. **Acceder a la Aplicación Web:**

    En un navegador web, acceder a la siguiente URL:
    
    ```
    dev.com.sodep.py:8080/mf/login/login.mob
    ```

    Iniciar sesión con las siguientes credenciales:
    
    - Usuario: root@mobileforms.sodep.com.py
    - Contraseña: 123456

6. **Editar Usuario:**

    Ir a la sección de administración y editar el usuario "chake", asignándole una nueva contraseña.

7. **Configuración en Android Studio:**

    - Abrir Android Studio.
    - Abrir el proyecto "captura-android-community" que se clonó.
    - En la parte superior, seleccionar la opción "Pixel 3a" como dispositivo de emulación.
    - Ejecutar el proyecto desde Android Studio. Esto iniciará el emulador.

8. **Acceder a la Aplicación Android:**

    Iniciar sesión en la aplicación Android con las siguientes credenciales:
    
    - Usuario: chake@feltesq.com
    - Contraseña: [la contraseña asignada en el paso 6]

