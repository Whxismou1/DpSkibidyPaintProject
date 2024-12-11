# Planificador de Lotes PPG

Este proyecto ha sido desarrollado como parte de la asignatura de Dirección de Proyectos de 3º de Ingeniería Informática en la Universidad de León. Consiste en una aplicación creada para PPG que, dado un archivo de entrada en formato Excel, genera una planificación de varios lotes de pintura y exporta un archivo Excel con dos hojas:
- Una hoja con la planificación calculada.
- Una hoja con un diagrama de Gantt.

## Requisitos Previos

1. Tener [Java JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) (versión 17 o superior) instalado.
2. Tener [Apache Maven](https://maven.apache.org/) instalado.
3. Crear un archivo `.env` en el directorio raíz del proyecto con el siguiente contenido:
   ```env
   DB_URL=
   USER_DB=
   PASS_DB=
   ```
## Tecnologías y Dependencias

Este proyecto utiliza las siguientes tecnologías y bibliotecas:
- **JavaFX**: Para la interfaz gráfica.
- **dotenv-java**: Para gestionar variables de entorno desde un archivo `.env`.
- **MySQL Connector**: Para la conexión con la base de datos MySQL.
- **Apache POI**: Para trabajar con archivos Excel.
- **jBCrypt**: Para gestionar el cifrado de contraseñas.
- **Log4j**: Para la gestión de logs.

## Instalación y Uso

### Clonar el Proyecto

1. Clona este repositorio ejecutando el siguiente comando:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd <NOMBRE_DEL_REPOSITORIO>
   ```

### Compilar y Ejecutar el Proyecto

2. Para compilar y ejecutar el proyecto, utiliza Maven:
   ```bash
   mvn clean install
   mvn javafx:run
   ```

### Crear el Archivo JAR

3. Si se desea empaquetar el proyecto en un archivo JAR, ejecutar:
   ```bash
   mvn package
   ```
   Esto generará un archivo JAR en el directorio `target`. Se puede ejecutar con:
   ```bash
   java -jar target/<NOMBRE_DEL_JAR>.jar
   ```

---
