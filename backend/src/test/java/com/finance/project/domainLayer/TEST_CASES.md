# Lab 04 — Tabla de Casos de Prueba (Módulo `Person` del Dominio)

> Curso: Ingeniería de Software II — D.Sc. Edgar Sarmiento Calisaya
> Frameworks: **JUnit 5** (xUnit) + **Mockito** (dobles) + **JaCoCo** (coverage)
> Integrante: tests del módulo **Person** (`domainEntities.aggregates.person`)

Este documento especifica los casos de prueba implementados sobre el agregado raíz
`Person` y sus Value Objects propios (`Name`, `Email`*, `Birthdate`, `Birthplace`,
`Address`). Se cubren **invariantes** del constructor y **comportamiento** público
(adders, verificadores de parentesco, equals/hashCode).

\* `Email` vive en `vosShared` y se usa indirectamente vía `Person` y `PersonID`.

---

## 1. Agregado `Person`

| Clase/Método | Caso Prueba | Valores de Prueba | Resultado Esperado |
|---|---|---|---|
| `Person.createPerson` | Crear persona válida (sin padres ni dirección) | email="ana@x.com", name="Ana", birthdate=2000-01-01, birthplace="Lima" | Devuelve instancia no nula; getters retornan VOs equivalentes |
| `Person.createPersonWithoutParents` | Crear persona válida con dirección y ledger | email, name, fecha, birthplace, Address, LedgerID | Instancia no nula; `getAddress()` y `getLedgerID()` no nulos |
| `Person.createPersonWithParents` | Crear persona con madre y padre | + PersonID(mother), PersonID(father) | `getMother()` y `getFather()` devuelven los IDs |
| `Person` constructor | INVARIANTE: name nulo | name=null | Lanza `IllegalArgumentException` |
| `Person` constructor | INVARIANTE: name vacío | name="" | Lanza `IllegalArgumentException` |
| `Person` constructor | INVARIANTE: email nulo | email=null | Lanza `IllegalArgumentException` |
| `Person` constructor | INVARIANTE: email vacío | email="" | Lanza `IllegalArgumentException` |
| `Person` constructor | INVARIANTE: birthdate nulo | birthdate=null | Lanza `IllegalArgumentException` |
| `Person` constructor | INVARIANTE: birthplace vacío | birthplace="" | Lanza `IllegalArgumentException` |
| `Person` constructor (con address) | INVARIANTE: address nulo | address=null | Lanza `IllegalArgumentException` |
| `Person` constructor (con address) | INVARIANTE: ledgerID nulo | ledgerID=null | Lanza `IllegalArgumentException` |
| `Person.addSibling` | Agregar hermano nuevo | PersonID válido | Retorna `true`; `getListOfSiblings()` lo contiene |
| `Person.addSibling` | Agregar hermano duplicado | mismo PersonID dos veces | Segunda llamada retorna `false` |
| `Person.addCategory` | Agregar categoría nueva / duplicada | CategoryID | `true` la primera vez, `false` la segunda |
| `Person.addAccount` | Agregar cuenta nueva / duplicada | AccountID | `true` / luego `false` |
| `Person.addSchedule` | Agregar schedule nuevo / duplicado | ScheduleID | `true` / luego `false` |
| `Person.addMother` | Asignar madre cuando no existe | PersonID | Retorna `true` |
| `Person.addMother` | Intentar reasignar madre | PersonID nuevo cuando ya hay | Retorna `false` |
| `Person.addFather` | Asignar / reasignar padre | PersonID | `true` / luego `false` |
| `Person.addAddress` | Asignar address cuando no existe | calle, nº, código, ciudad, país | Retorna `true` y `getAddress()` ya no es null |
| `Person.verifySiblingsOrHalfSiblings` | Misma madre | dos personas con misma madre | Retorna `true` |
| `Person.verifySiblingsOrHalfSiblings` | Mismo padre | dos personas con mismo padre | Retorna `true` |
| `Person.verifySiblingsOrHalfSiblings` | Hermanos por lista | personA tiene a personB en su lista | Retorna `true` |
| `Person.verifySiblingsOrHalfSiblings` | Sin parentesco | personas sin padres comunes | Retorna `false` |
| `Person.verifySiblingsOrHalfSiblings` | Misma persona | comparar consigo misma | Retorna `false` |
| `Person.verifySameSiblings` | Listas idénticas | mismo set de hermanos | Retorna `true` |
| `Person.verifySameSiblings` | Tamaños distintos | listas con tamaño distinto | Retorna `false` |
| `Person.checkPersonID` | ID coincide | el mismo PersonID | Retorna `true` |
| `Person.checkPersonID` | ID distinto | otro PersonID | Retorna `false` |
| `Person.checkIfPersonHasAccount` | Cuenta presente | AccountID previamente añadido | Retorna `true` |
| `Person.checkIfPersonHasAccount` | Cuenta ausente | AccountID no añadido | Retorna `false` |
| `Person.checkIfPersonHasCategory` | Categoría presente / ausente | CategoryID | `true` / `false` |
| `Person.equals` | Mismo email | dos `Person` con mismo email | Retorna `true` |
| `Person.equals` | Distinto email | dos `Person` con emails distintos | Retorna `false` |
| `Person.equals` | Comparar con null | other=null | Retorna `false` |
| `Person.hashCode` | Consistencia con equals | dos personas iguales | mismos `hashCode()` |

## 2. VO `Name`

| Clase/Método | Caso Prueba | Valores | Esperado |
|---|---|---|---|
| `Name.createName` | Crear válido | "Ana" | Instancia no nula; `getName()` = "Ana" |
| `Name.createName` | INVARIANTE: nulo | null | `IllegalArgumentException` |
| `Name.createName` | INVARIANTE: vacío | "" | `IllegalArgumentException` |
| `Name.equals` | Case-insensitive | "Ana" vs "ANA" | `true` |
| `Name.equals` | Distintos | "Ana" vs "Beto" | `false` |

## 3. VO `Birthdate`

| Clase/Método | Caso Prueba | Valores | Esperado |
|---|---|---|---|
| `Birthdate.createBirthdate` | Crear válido | LocalDate.of(2000,1,1) | Instancia no nula |
| `Birthdate.createBirthdate` | INVARIANTE: nulo | null | `IllegalArgumentException` |
| `Birthdate.equals` | Misma fecha | dos VOs iguales | `true` |
| `Birthdate.equals` | Fechas distintas | 2000 vs 2001 | `false` |

## 4. VO `Birthplace`

| Clase/Método | Caso Prueba | Valores | Esperado |
|---|---|---|---|
| `Birthplace.createBirthplace` | Crear válido | "Lima" | Instancia no nula |
| `Birthplace.createBirthplace` | INVARIANTE: nulo | null | `IllegalArgumentException` |
| `Birthplace.equals` | Case-insensitive | "Lima" vs "LIMA" | `true` |

## 5. VO `Address`

| Clase/Método | Caso Prueba | Valores | Esperado |
|---|---|---|---|
| `Address.createAddress` | Crear válido | calle/nº/código/ciudad/país completos | Instancia no nula; getters consistentes |
| `Address.createAddress` | INVARIANTE: street nulo | street=null | `IllegalArgumentException` |
| `Address.createAddress` | INVARIANTE: doorNumber nulo | doorNumber=null | `IllegalArgumentException` |
| `Address.createAddress` | INVARIANTE: postCode nulo | postCode=null | `IllegalArgumentException` |
| `Address.createAddress` | INVARIANTE: city nulo | city=null | `IllegalArgumentException` |
| `Address.createAddress` | INVARIANTE: country nulo | country=null | `IllegalArgumentException` |
| `Address.equals` | Iguales | dos Address con mismos datos | `true` |
| `Address.equals` | Distinta calle | difiere en street | `false` |

---

## Estrategia de Setup / TearDown
- `@BeforeEach` (SetUp) instancia VOs y `Person` reutilizables para cada test
  ("sólo una vez" — Lab paso 7.f).
- No requiere teardown explícito: las entidades viven en memoria; JUnit limpia
  instancias entre tests.

## Cobertura objetivo
- **JaCoCo** sobre el paquete `domainEntities.aggregates.person` debe alcanzar
  > 90% de líneas y > 80% de ramas (los pocos paths no cubiertos son setters JPA
  no usados por el dominio).
