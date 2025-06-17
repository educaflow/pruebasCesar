# 🧪 Axelor - Proyecto de Pruebas

Este repositorio contiene una instalación de pruebas de **Axelor**, diseñada para experimentar con personalizaciones de frontend como estilos CSS y scripts JavaScript propios.

---

## 🎨 Personalización de Frontend

Axelor permite sobrescribir el archivo `index.html` que utiliza por defecto, lo cual nos da libertad para inyectar nuestros propios archivos estáticos.

### 1. Crear un `index.html` personalizado

Crea un archivo en:

```
src/main/webapp/index.html
```

Copia el contenido original del `index.html` generado automáticamente por Axelor, ubicado en:

```
build/webapp/index.html
```

Luego, modifica este archivo para incluir tus recursos personalizados. Por ejemplo:

```html
<link rel="stylesheet" href="./css/eduflow.css" />
<script src="./js/eduflow.js"></script>
```

---

### 2. Añadir tus archivos CSS y JS

Puedes crear tus propios archivos en:

```
src/main/webapp/css/eduflow.css
src/main/webapp/js/eduflow.js
```

Y luego agregarlos al `index.html` como se indica arriba.

---

### 3. Copiar archivos al `build/`

Para evitar recompilar toda la aplicación con `build`, puedes simplemente copiar los recursos web con el siguiente comando:

```bash
./gradlew copyWebapp
```

Esto copiará tu `index.html`, CSS, JS, y otros archivos a `build/webapp`.

---

### 4. Arrancar la aplicación

Una vez copiados los archivos, puedes iniciar la aplicación con:

```bash
./gradlew --no-daemon run
```

---

### 5. Modificar pantalla de login

- Personalización avanzada del login usando `axelor-config.properties`


## 📋 Requisitos

- Java 11 o superior
- Gradle 7+ (puedes usar el wrapper incluido: `./gradlew`)
- Axelor 7.4 o compatible


