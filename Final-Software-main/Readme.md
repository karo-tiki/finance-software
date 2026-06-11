# Finance Software - Gestión de Finanzas Personales

## Equipo de Trabajo
| Integrante | Módulo |
|---|---|
| Angelica | Person |
| Karoline | Group |
| Jossein | Ledger/Transaction |
| Joel | Account/Category |

## Propósito del Proyecto
Aplicación web de gestión de finanzas personales desarrollada con arquitectura DDD (Domain-Driven Design) y Clean Architecture. Permite gestionar cuentas, transacciones, categorías y grupos financieros.

## Funcionalidades
- Gestión de personas y datos personales
- Gestión de grupos financieros
- Registro de transacciones en el ledger
- Gestión de cuentas y categorías
- Programación de transacciones recurrentes

## Arquitectura
El proyecto sigue los principios de Domain-Driven Design (DDD) y Clean Architecture:

    backend/src/
    main/java/com/finance/project/
        domainLayer/          <- Entidades, Agregados, Value Objects
        applicationLayer/     <- Servicios de Aplicación
        persistenceLayer/     <- Repositorios JPA
        controllerLayer/      <- REST Controllers
        dtos/                 <- Data Transfer Objects
    test/java/com/finance/project/
        domainLayer/          <- Pruebas Unitarias

## Tecnologías
| Capa | Tecnología |
|---|---|
| Backend | Java 11 + Spring Boot 2.2 |
| ORM | JPA + Hibernate |
| Base de Datos | H2 (desarrollo) |
| Testing | JUnit 5 + Mockito |
| Cobertura | JaCoCo |
| Build | Maven |
| CI/CD | Jenkins |
| Contenedor | Docker |
| Análisis Estático | SonarQube |

## Módulos y Servicios REST

### Módulo Person
- POST /persons - Crear persona
- GET /persons/{id} - Obtener persona
- POST /persons/{id}/accounts - Agregar cuenta
- POST /persons/{id}/categories - Agregar categoría

### Módulo Group
- POST /groups - Crear grupo
- GET /groups/{id} - Obtener grupo
- POST /groups/{id}/members - Agregar miembro
- POST /groups/{id}/admins - Agregar administrador

### Módulo Ledger/Transaction
- POST /ledgers/{id}/transactions - Crear transacción
- GET /ledgers/{id}/transactions - Listar transacciones
- GET /ledgers/{id}/transactions/between - Por rango de fechas

### Módulo Account/Category
- POST /accounts - Crear cuenta
- POST /categories - Crear categoría
- GET /accounts/{id} - Obtener cuenta

## Pruebas Unitarias
| Módulo | Archivo | Suites | Tests |
|---|---|---|---|
| Person | PersonTest.java | 6 | 35 |
| Group | GroupTest.java | 6 | 40 |
| Ledger | LedgerTest.java | 6 | 25 |
| Transaction | TransactionTest.java | 3 | 15 |
| Account | AccountTest.java | 2 | 10 |
| Category | CategoryTest.java | 2 | 10 |

## Pipeline CI/CD
1. Construcción Automática - Maven clean package
2. Análisis Estático - SonarQube
3. Pruebas Unitarias - JUnit5 + Mockito + JaCoCo
4. Pruebas Funcionales - Selenium
5. Pruebas de Performance - JMeter
6. Pruebas de Seguridad - OWASP ZAP
7. Despliegue - Docker

## Ramas del Repositorio
- main -> código estable
- desarrollo -> integración de features
- feature/person-tests -> módulo Person
- feature/group-tests -> módulo Group
- feature/ledger-tests -> módulo Ledger/Transaction
- feature/account-category-tests -> módulo Account/Category

## Gestión de Tareas
Gestionadas en GitHub Project con flujo:
TODO -> IN PROGRESS -> ITERATION -> FIX VALIDATION -> DONE

## Ejecución
    git clone https://github.com/karo-tiki/finance-software.git
    cd finance-software/Final-Software-main
    mvn clean package
    mvn test
    mvn jacoco:report
