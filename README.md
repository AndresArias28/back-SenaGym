# 🏋️‍♂️ Gym SENA – Backend (Spring Boot)

Este es el **backend** del sistema Gym SENA (**HealthU**), desarrollado con **Spring Boot** y arquitectura por capas para gestionar usuarios, rutinas, desafíos y autenticación.

---

## 📦 Stack Tecnológico

- **Lenguaje:** Java 17+  
- **Framework principal:** Spring Boot 3.x  
- **Dependencias principales:**
  - Spring Web (API REST)
  - Spring Security + JWT
  - Spring Data JPA (persistencia)
  - Base de datos: MySQL (puede adaptarse a PostgreSQL)
  - Maven (gestión de dependencias)
- **Otros:**  
  - OpenAI (integraciones de IA)  
  - Manejo de excepciones centralizado  

---

## ⚙️ Requisitos previos

Antes de empezar, asegúrate de tener instalado:

- **Java JDK 17 o superior**  
- **Maven 3.9+**  
- **MySQL** (o base de datos compatible)  
- **IDE recomendado:** IntelliJ IDEA o VS Code

---

## 🚀 Instalación y ejecución

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/tu-usuario/tu-repo-backend.git
   cd tu-repo-backend

2. Configurar variables de entorno en .env:

   ```bash
  
   PORT=6090
   SECRET_KEY=tu_clave_secreta_aqui
   DATABASE_URL=jdbc:postgresql://localhost:5432/rutas_sena
   JDBC_DATABASE_USERNAME=postgres
   JDBC_DATABASE_PASSWORD=postgres_password
   DB_URL=jdbc:mysql://<host>:3306/gym_sena?useSSL=false&allowPublicKeyRetrieval=true
   DB_PASSWORD=tu_password_mysql
   EMAIL_USERNAME=tu_correo@gmail.com
   GOOGLE_CLIENT_SECRET=tu_google_client_secret
   SPRING_PROFILES_ACTIVE=prod
   CLOUDINARY_CLOUD_NAME=tu_cloud_name
   CLOUDINARY_API_KEY=tu_api_key
   CLOUDINARY_API_SECRET=tu_api_secret
   OPENAI_API_KEY=tu_api_key_openai
  
3. Compilar y ejecutar:

   ```bash
   mvn clean install
   mvn spring-boot:run

4. 📂 Estructura del proyecto

   ```bash

   src/main/java/com/gym/gym_ver2/
    ├── aplicacion.service/          # Lógica de negocio / capa de aplicación
    ├── domain.model/                # Modelos de dominio
    │   ├── dto/                     # Data Transfer Objects
    │   ├── entity/                  # Entidades JPA
    │   └── requestModels/           # Modelos para requests
    ├── infraestructura/             # Adaptadores / capa de infraestructura
    │   ├── auth/                    # Seguridad y autenticación
    │   ├── config/                  # Configuraciones generales
    │   ├── controller/              # Controladores REST
    │   ├── exceptions/              # Manejo de excepciones
    │   ├── jwt/                     # Generación y validación de JWT
    │   ├── openai/                  # Integraciones con OpenAI
    │   └── persistence.repository/  # Repositorios (Spring Data JPA)
    └── GymVer2Application.java      # Clase principal


