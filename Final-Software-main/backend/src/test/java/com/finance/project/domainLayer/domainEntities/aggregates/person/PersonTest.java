package com.finance.project.domainLayer.domainEntities.aggregates.person;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Person Aggregate — Domain Tests")
class PersonTest {

    private LocalDate birthdate;
    private Address address;
    private LedgerID ledgerID;
    private Person basicPerson;

    @BeforeEach
    void setUp() {
        birthdate   = LocalDate.of(2000, 1, 1);
        address     = Address.createAddress("Av. Sol", "123", "15000", "Lima", "Peru");
        ledgerID    = LedgerID.createLedgerID();
        basicPerson = Person.createPerson("ana@x.com", "Ana", birthdate, "Lima");
    }

    @AfterEach
    void tearDown() {
        birthdate   = null;
        address     = null;
        ledgerID    = null;
        basicPerson = null;
    }

    @Nested
    @DisplayName("Suite 1 — Creación de Persona")
    class CreacionPersona {

        @Test
        @DisplayName("createPerson — retorna persona con VOs correctos")
        void createPerson_ValidInputs_ReturnsPerson() {
            Person person = Person.createPerson("ana@x.com", "Ana", birthdate, "Lima");
            assertNotNull(person);
            assertEquals("Ana", person.getName().getName());
            assertEquals("ana@x.com", person.getEmail().getEmail());
            assertEquals(birthdate, person.getBirthdate().getBirthdate());
            assertEquals("Lima", person.getBirthplace().getBirthplace());
            assertNull(person.getAddress());
            assertNull(person.getLedgerID());
        }

        @Test
        @DisplayName("createPersonWithoutParents — setea address y ledger")
        void createPersonWithoutParents_ValidInputs_ReturnsPerson() {
            Person person = Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "Lima", address, ledgerID);
            assertNotNull(person);
            assertEquals(address, person.getAddress());
            assertEquals(ledgerID, person.getLedgerID());
        }

        @Test
        @DisplayName("createPersonWithParents — setea madre y padre")
        void createPersonWithParents_ValidInputs_SetsParents() {
            PersonID mother = PersonID.createPersonID("mom@x.com");
            PersonID father = PersonID.createPersonID("dad@x.com");
            Person person = Person.createPersonWithParents("ana@x.com", "Ana", birthdate, mother, father, "Lima", address, ledgerID);
            assertEquals(mother, person.getMother());
            assertEquals(father, person.getFather());
        }
    }

    @Nested
    @DisplayName("Suite 2 — Invariantes")
    class Invariantes {

        @Test
        @DisplayName("name nulo — lanza excepción")
        void invariant_NullName_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", null, birthdate, "Lima", address, ledgerID));
        }

        @Test
        @DisplayName("name vacío — lanza excepción")
        void invariant_EmptyName_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "", birthdate, "Lima", address, ledgerID));
        }

        @Test
        @DisplayName("email nulo — lanza excepción")
        void invariant_NullEmail_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents(null, "Ana", birthdate, "Lima", address, ledgerID));
        }

        @Test
        @DisplayName("email vacío — lanza excepción")
        void invariant_EmptyEmail_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("", "Ana", birthdate, "Lima", address, ledgerID));
        }

        @Test
        @DisplayName("birthdate nulo — lanza excepción")
        void invariant_NullBirthdate_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", null, "Lima", address, ledgerID));
        }

        @Test
        @DisplayName("birthplace vacío — lanza excepción")
        void invariant_EmptyBirthplace_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "", address, ledgerID));
        }

        @Test
        @DisplayName("address nula — lanza excepción")
        void invariant_NullAddress_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "Lima", null, ledgerID));
        }

        @Test
        @DisplayName("ledgerID nulo — lanza excepción")
        void invariant_NullLedgerID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "Lima", address, null));
        }
    }

    @Nested
    @DisplayName("Suite 3 — Gestión de Relaciones")
    class GestionRelaciones {

        @Test
        @DisplayName("addSibling nuevo — retorna true y lo almacena")
        void addSibling_New_ReturnsTrueAndStores() {
            PersonID sibling = PersonID.createPersonID("beto@x.com");
            assertTrue(basicPerson.addSibling(sibling));
            assertTrue(basicPerson.getListOfSiblings().contains(sibling));
        }

        @Test
        @DisplayName("addSibling duplicado — retorna false")
        void addSibling_Duplicate_ReturnsFalse() {
            PersonID sibling = PersonID.createPersonID("beto@x.com");
            basicPerson.addSibling(sibling);
            assertFalse(basicPerson.addSibling(sibling));
            assertEquals(1, basicPerson.getListOfSiblings().size());
        }

        @Test
        @DisplayName("addMother sin madre — retorna true y la setea")
        void addMother_WhenAbsent_ReturnsTrueAndSets() {
            PersonID mom = PersonID.createPersonID("mom@x.com");
            assertTrue(basicPerson.addMother(mom));
            assertEquals(mom, basicPerson.getMother());
        }

        @Test
        @DisplayName("addMother cuando ya existe — retorna false")
        void addMother_WhenAlreadySet_ReturnsFalse() {
            PersonID mom = PersonID.createPersonID("mom@x.com");
            basicPerson.addMother(mom);
            assertFalse(basicPerson.addMother(PersonID.createPersonID("other@x.com")));
            assertEquals(mom, basicPerson.getMother());
        }

        @Test
        @DisplayName("addFather sin padre — retorna true y lo setea")
        void addFather_WhenAbsent_ReturnsTrueAndSets() {
            PersonID dad = PersonID.createPersonID("dad@x.com");
            assertTrue(basicPerson.addFather(dad));
            assertEquals(dad, basicPerson.getFather());
        }

        @Test
        @DisplayName("addFather cuando ya existe — retorna false")
        void addFather_WhenAlreadySet_ReturnsFalse() {
            PersonID dad = PersonID.createPersonID("dad@x.com");
            basicPerson.addFather(dad);
            assertFalse(basicPerson.addFather(PersonID.createPersonID("other@x.com")));
        }
    }

    @Nested
    @DisplayName("Suite 4 — Gestión de Cuentas y Categorías")
    class GestionCuentasCategorias {

        @Test
        @DisplayName("addCategory nuevo luego duplicado — comportamiento correcto")
        void addCategory_NewThenDuplicate() {
            CategoryID cat = CategoryID.createCategoryID("Food", basicPerson.getPersonID());
            assertTrue(basicPerson.addCategory(cat));
            assertFalse(basicPerson.addCategory(cat));
            assertTrue(basicPerson.checkIfPersonHasCategory(cat));
        }

        @Test
        @DisplayName("addAccount nuevo luego duplicado — comportamiento correcto")
        void addAccount_NewThenDuplicate() {
            AccountID acc = AccountID.createAccountID("BCP", basicPerson.getPersonID());
            assertTrue(basicPerson.addAccount(acc));
            assertFalse(basicPerson.addAccount(acc));
            assertTrue(basicPerson.checkIfPersonHasAccount(acc));
        }

        @Test
        @DisplayName("addSchedule nuevo luego duplicado — comportamiento correcto")
        void addSchedule_NewThenDuplicate() {
            ScheduleID sched = ScheduleID.createScheduleID("monthly-rent", LocalDate.of(2025, 1, 1), "MONTHLY", "DEBIT");
            assertTrue(basicPerson.addSchedule(sched));
            assertFalse(basicPerson.addSchedule(sched));
            assertTrue(basicPerson.getListOfSchedulings().contains(sched));
        }

        @Test
        @DisplayName("addAddress sin address — retorna true")
        void addAddress_WhenAbsent_ReturnsTrue() {
            assertNull(basicPerson.getAddress());
            assertTrue(basicPerson.addAddress("Av. Sol", "123", "15000", "Lima", "Peru"));
            assertNotNull(basicPerson.getAddress());
        }

        @Test
        @DisplayName("addAddress cuando ya existe — retorna false")
        void addAddress_WhenAlreadySet_ReturnsFalse() {
            basicPerson.setAddress(address);
            assertFalse(basicPerson.addAddress("Otra", "1", "00000", "Lima", "Peru"));
        }

        @Test
        @DisplayName("addLedgerID — setea ledger y retorna true")
        void addLedgerID_SetsLedgerAndReturnsTrue() {
            assertTrue(basicPerson.addLedgerID(ledgerID));
            assertEquals(ledgerID, basicPerson.getLedgerID());
        }
    }

    @Nested
    @DisplayName("Suite 5 — Verificación de Parentesco")
    class VerificacionParentesco {

        @Test
        @DisplayName("verifySiblingsOrHalfSiblings — misma madre retorna true")
        void verifySiblings_SameMother_ReturnsTrue() {
            PersonID mother = PersonID.createPersonID("mom@x.com");
            Person a = Person.createPersonWithParents("a@x.com", "A", birthdate, mother, PersonID.createPersonID("dad@x.com"), "Lima", address, ledgerID);
            Person b = Person.createPersonWithParents("b@x.com", "B", birthdate, mother, PersonID.createPersonID("otherDad@x.com"), "Lima", address, ledgerID);
            assertTrue(a.verifySiblingsOrHalfSiblings(b));
        }

        @Test
        @DisplayName("verifySiblingsOrHalfSiblings — mismo padre retorna true")
        void verifySiblings_SameFather_ReturnsTrue() {
            PersonID father = PersonID.createPersonID("dad@x.com");
            Person a = Person.createPersonWithParents("a@x.com", "A", birthdate, PersonID.createPersonID("mom@x.com"), father, "Lima", address, ledgerID);
            Person b = Person.createPersonWithParents("b@x.com", "B", birthdate, PersonID.createPersonID("otherMom@x.com"), father, "Lima", address, ledgerID);
            assertTrue(a.verifySiblingsOrHalfSiblings(b));
        }

        @Test
        @DisplayName("verifySiblingsOrHalfSiblings — en lista de hermanos retorna true")
        void verifySiblings_InSiblingList_ReturnsTrue() {
            Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
            Person b = Person.createPerson("b@x.com", "B", birthdate, "Lima");
            a.addSibling(b.getPersonID());
            assertTrue(a.verifySiblingsOrHalfSiblings(b));
        }

        @Test
        @DisplayName("verifySiblingsOrHalfSiblings — sin relación retorna false")
        void verifySiblings_NoRelation_ReturnsFalse() {
            Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
            Person b = Person.createPerson("b@x.com", "B", birthdate, "Lima");
            assertFalse(a.verifySiblingsOrHalfSiblings(b));
        }

        @Test
        @DisplayName("verifySiblingsOrHalfSiblings — misma persona retorna false")
        void verifySiblings_SamePerson_ReturnsFalse() {
            Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
            assertFalse(a.verifySiblingsOrHalfSiblings(a));
        }
    }

    @Nested
    @DisplayName("Suite 6 — Identidad, Equals y HashCode")
    class IdentidadEqualsHashCode {

        @Test
        @DisplayName("checkPersonID — mismo ID retorna true")
        void checkPersonID_Matching_ReturnsTrue() {
            assertTrue(basicPerson.checkPersonID(PersonID.createPersonID("ana@x.com")));
        }

        @Test
        @DisplayName("checkPersonID — diferente ID retorna false")
        void checkPersonID_Different_ReturnsFalse() {
            assertFalse(basicPerson.checkPersonID(PersonID.createPersonID("b@x.com")));
        }

        @Test
        @DisplayName("equals — mismo email retorna true y hashCode coincide")
        void equals_SameEmail_ReturnsTrueAndHashCodeMatches() {
            Person p2 = Person.createPerson("ana@x.com", "Different", birthdate, "Cusco");
            assertEquals(basicPerson, p2);
            assertEquals(basicPerson.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("equals — diferente email retorna false")
        void equals_DifferentEmail_ReturnsFalse() {
            Person p2 = Person.createPerson("b@x.com", "Ana", birthdate, "Lima");
            assertNotEquals(basicPerson, p2);
        }

        @Test
        @DisplayName("equals — comparar con null retorna false")
        void equals_Null_ReturnsFalse() {
            assertNotEquals(null, basicPerson);
        }

        @Test
        @DisplayName("equals — mismo objeto retorna true")
        void equals_SameObject_ReturnsTrue() {
            assertEquals(basicPerson, basicPerson);
        }
    }
}
