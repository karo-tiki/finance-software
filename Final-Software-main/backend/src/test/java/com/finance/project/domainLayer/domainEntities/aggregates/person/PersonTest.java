package com.finance.project.domainLayer.domainEntities.aggregates.person;

import com.finance.project.domainLayer.domainEntities.vosShared.AccountID;
import com.finance.project.domainLayer.domainEntities.vosShared.CategoryID;
import com.finance.project.domainLayer.domainEntities.vosShared.LedgerID;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.domainLayer.domainEntities.vosShared.ScheduleID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Person Aggregate — Domain Tests")
class PersonTest {

    private LocalDate birthdate;
    private Address address;
    private LedgerID ledgerID;
    private Person basicPerson;

    @BeforeEach
    void setUp() {
        birthdate = LocalDate.of(2000, 1, 1);
        address = Address.createAddress("Av. Sol", "123", "15000", "Lima", "Peru");
        ledgerID = LedgerID.createLedgerID();
        basicPerson = Person.createPerson("ana@x.com", "Ana", birthdate, "Lima");
    }

    // ---------------------------------------------------------------------
    // Creación
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("createPerson — devuelve persona con VOs equivalentes")
    void creation_createPerson_validInputs_returnsPerson() {
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
    void creation_createPersonWithoutParents_validInputs_returnsPerson() {
        Person person = Person.createPersonWithoutParents(
                "ana@x.com", "Ana", birthdate, "Lima", address, ledgerID);

        assertNotNull(person);
        assertEquals(address, person.getAddress());
        assertEquals(ledgerID, person.getLedgerID());
    }

    @Test
    @DisplayName("createPersonWithParents — setea madre y padre")
    void creation_createPersonWithParents_validInputs_setsParents() {
        PersonID mother = PersonID.createPersonID("mom@x.com");
        PersonID father = PersonID.createPersonID("dad@x.com");

        Person person = Person.createPersonWithParents(
                "ana@x.com", "Ana", birthdate, mother, father,
                "Lima", address, ledgerID);

        assertEquals(mother, person.getMother());
        assertEquals(father, person.getFather());
    }

    // ---------------------------------------------------------------------
    // Invariantes
    // ---------------------------------------------------------------------

    @Test
    void invariant_name_null_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", null, birthdate, "Lima", address, ledgerID));
    }

    @Test
    void invariant_name_empty_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "", birthdate, "Lima", address, ledgerID));
    }

    @Test
    void invariant_email_null_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents(null, "Ana", birthdate, "Lima", address, ledgerID));
    }

    @Test
    void invariant_email_empty_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("", "Ana", birthdate, "Lima", address, ledgerID));
    }

    @Test
    void invariant_birthdate_null_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", null, "Lima", address, ledgerID));
    }

    @Test
    void invariant_birthplace_empty_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "", address, ledgerID));
    }

    @Test
    void invariant_address_null_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "Lima", null, ledgerID));
    }

    @Test
    void invariant_ledgerID_null_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPersonWithoutParents("ana@x.com", "Ana", birthdate, "Lima", address, null));
    }

    @Test
    @DisplayName("createPerson (sin address) — name vacío también lanza")
    void invariant_simpleCreate_emptyName_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Person.createPerson("ana@x.com", "", birthdate, "Lima"));
    }

    // ---------------------------------------------------------------------
    // Adders
    // ---------------------------------------------------------------------

    @Test
    void adder_addSibling_new_returnsTrue_andStoresIt() {
        PersonID sibling = PersonID.createPersonID("beto@x.com");
        assertTrue(basicPerson.addSibling(sibling));
        assertTrue(basicPerson.getListOfSiblings().contains(sibling));
    }

    @Test
    void adder_addSibling_duplicate_returnsFalse() {
        PersonID sibling = PersonID.createPersonID("beto@x.com");
        basicPerson.addSibling(sibling);
        assertFalse(basicPerson.addSibling(sibling));
        assertEquals(1, basicPerson.getListOfSiblings().size());
    }

    @Test
    void adder_addCategory_new_then_duplicate() {
        CategoryID cat = CategoryID.createCategoryID("Food", basicPerson.getPersonID());
        assertTrue(basicPerson.addCategory(cat));
        assertFalse(basicPerson.addCategory(cat));
        assertTrue(basicPerson.checkIfPersonHasCategory(cat));
    }

    @Test
    void adder_addAccount_new_then_duplicate() {
        AccountID acc = AccountID.createAccountID("BCP", basicPerson.getPersonID());
        assertTrue(basicPerson.addAccount(acc));
        assertFalse(basicPerson.addAccount(acc));
        assertTrue(basicPerson.checkIfPersonHasAccount(acc));
    }

    @Test
    void adder_addSchedule_new_then_duplicate() {
        ScheduleID sched = ScheduleID.createScheduleID(
                "monthly-rent", LocalDate.of(2025, 1, 1), "MONTHLY", "DEBIT");
        assertTrue(basicPerson.addSchedule(sched));
        assertFalse(basicPerson.addSchedule(sched));
        assertTrue(basicPerson.getListOfSchedulings().contains(sched));
    }

    @Test
    void adder_addMother_whenAbsent_returnsTrue_andSets() {
        PersonID mom = PersonID.createPersonID("mom@x.com");
        assertTrue(basicPerson.addMother(mom));
        assertEquals(mom, basicPerson.getMother());
    }

    @Test
    void adder_addMother_whenAlreadySet_returnsFalse() {
        PersonID mom = PersonID.createPersonID("mom@x.com");
        basicPerson.addMother(mom);
        PersonID otherMom = PersonID.createPersonID("other@x.com");
        assertFalse(basicPerson.addMother(otherMom));
        assertEquals(mom, basicPerson.getMother());
    }

    @Test
    void adder_addFather_whenAbsent_returnsTrue_andSets() {
        PersonID dad = PersonID.createPersonID("dad@x.com");
        assertTrue(basicPerson.addFather(dad));
        assertEquals(dad, basicPerson.getFather());
    }

    @Test
    void adder_addFather_whenAlreadySet_returnsFalse() {
        PersonID dad = PersonID.createPersonID("dad@x.com");
        basicPerson.addFather(dad);
        assertFalse(basicPerson.addFather(PersonID.createPersonID("other@x.com")));
    }

    @Test
    void adder_addAddress_whenAbsent_returnsTrue() {
        assertNull(basicPerson.getAddress());
        assertTrue(basicPerson.addAddress("Av. Sol", "123", "15000", "Lima", "Peru"));
        assertNotNull(basicPerson.getAddress());
    }

    @Test
    void adder_addAddress_whenAlreadySet_returnsFalse() {
        basicPerson.setAddress(address);
        assertFalse(basicPerson.addAddress("Otra", "1", "00000", "Lima", "Peru"));
    }

    @Test
    void adder_addLedgerID_setsLedger_andReturnsTrue() {
        assertTrue(basicPerson.addLedgerID(ledgerID));
        assertEquals(ledgerID, basicPerson.getLedgerID());
    }

    // ---------------------------------------------------------------------
    // Verificación de parentesco
    // ---------------------------------------------------------------------

    @Test
    void sibling_verifySiblingsOrHalfSiblings_sameMother_returnsTrue() {
        PersonID mother = PersonID.createPersonID("mom@x.com");
        PersonID father = PersonID.createPersonID("dad@x.com");
        Person a = Person.createPersonWithParents("a@x.com", "A", birthdate, mother, father,
                "Lima", address, ledgerID);
        Person b = Person.createPersonWithParents("b@x.com", "B", birthdate, mother,
                PersonID.createPersonID("otherDad@x.com"),
                "Lima", address, ledgerID);

        assertTrue(a.verifySiblingsOrHalfSiblings(b));
    }

    @Test
    void sibling_verifySiblingsOrHalfSiblings_sameFather_returnsTrue() {
        PersonID mother = PersonID.createPersonID("mom@x.com");
        PersonID father = PersonID.createPersonID("dad@x.com");
        Person a = Person.createPersonWithParents("a@x.com", "A", birthdate, mother, father,
                "Lima", address, ledgerID);
        Person b = Person.createPersonWithParents("b@x.com", "B", birthdate,
                PersonID.createPersonID("otherMom@x.com"), father,
                "Lima", address, ledgerID);

        assertTrue(a.verifySiblingsOrHalfSiblings(b));
    }

    @Test
    void sibling_verifySiblingsOrHalfSiblings_inSiblingList_returnsTrue() {
        Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
        Person b = Person.createPerson("b@x.com", "B", birthdate, "Lima");
        a.addSibling(b.getPersonID());

        assertTrue(a.verifySiblingsOrHalfSiblings(b));
    }

    @Test
    void sibling_verifySiblingsOrHalfSiblings_noRelation_returnsFalse() {
        Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
        Person b = Person.createPerson("b@x.com", "B", birthdate, "Lima");

        assertFalse(a.verifySiblingsOrHalfSiblings(b));
    }

    @Test
    void sibling_verifySiblingsOrHalfSiblings_samePerson_returnsFalse() {
        Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
        assertFalse(a.verifySiblingsOrHalfSiblings(a));
    }

    @Test
    void sibling_verifySameSiblings_identicalLists_returnsTrue() {
        Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
        Person b = Person.createPerson("b@x.com", "B", birthdate, "Lima");
        PersonID sharedSibling = PersonID.createPersonID("shared@x.com");
        a.addSibling(sharedSibling);
        b.addSibling(sharedSibling);

        assertTrue(a.verifySameSiblings(b));
    }

    @Test
    void sibling_verifySameSiblings_differentSizes_returnsFalse() {
        Person a = Person.createPerson("a@x.com", "A", birthdate, "Lima");
        Person b = Person.createPerson("b@x.com", "B", birthdate, "Lima");
        a.addSibling(PersonID.createPersonID("x@x.com"));

        assertFalse(a.verifySameSiblings(b));
    }

    // ---------------------------------------------------------------------
    // Identidad / equals / hashCode
    // ---------------------------------------------------------------------

    @Test
    void identity_checkPersonID_matching_returnsTrue() {
        assertTrue(basicPerson.checkPersonID(PersonID.createPersonID("ana@x.com")));
    }

    @Test
    void identity_checkPersonID_different_returnsFalse() {
        assertFalse(basicPerson.checkPersonID(PersonID.createPersonID("b@x.com")));
    }

    @Test
    void identity_equals_sameEmail_returnsTrue_andHashCodeMatches() {
        Person p2 = Person.createPerson("ana@x.com", "Different", birthdate, "Cusco");

        assertEquals(basicPerson, p2);
        assertEquals(basicPerson.hashCode(), p2.hashCode());
    }

    @Test
    void identity_equals_differentEmail_returnsFalse() {
        Person p2 = Person.createPerson("b@x.com", "Ana", birthdate, "Lima");
        assertNotEquals(basicPerson, p2);
    }

    @Test
    void identity_equals_null_returnsFalse() {
        assertNotEquals(null, basicPerson);
    }

    @Test
    void identity_equals_self_returnsTrue() {
        assertEquals(basicPerson, basicPerson);
    }
}
