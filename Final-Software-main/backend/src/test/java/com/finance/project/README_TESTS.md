# Carpeta de Tests — Por implementar

Esta carpeta contiene la estructura lista para que implementes los tests del proyecto.

## Lo que debes hacer (requisitos del curso):

### 1. Framework xUnit → JUnit 5
Descomenta las dependencias de JUnit 5 en `pom.xml` y agrega tests en esta carpeta.
Estructura sugerida (espeja la de `main/`):
```
test/
 └── java/com/finance/project/
      ├── domainLayer/          ← tests unitarios de entidades y VOs
      ├── applicationLayer/     ← tests unitarios de servicios
      ├── controllerLayer/
      │    ├── unitTests/       ← tests unitarios de controllers (con mock del servicio)
      │    └── integrationTests/← tests de integración REST
      ├── dataModel/            ← tests de assemblers JPA
      ├── dtos/                 ← tests de DTOs y sus assemblers
      └── infrastructureLayer/  ← tests de repositorios
```

### 2. Code Coverage → JaCoCo
Descomenta el plugin JaCoCo en `pom.xml`.
Ejecuta con: `mvn test jacoco:report`
El reporte aparece en: `target/site/jacoco/index.html`

### 3. Framework de Mocking → Mockito
Descomenta la dependencia de Mockito en `pom.xml`.
Úsalo en los unit tests de controllers y services para mockear dependencias.

### Separación main / test
- Código de implementación → `backend/src/main/`
- Código de pruebas       → `backend/src/test/`   ← estás aquí
