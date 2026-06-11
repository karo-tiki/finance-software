package com.finance.project.domainLayer.domainEntities.aggregates.group;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Group Aggregate — Domain Tests")
class GroupTest {

    private PersonID admin;
    private PersonID member1;
    private PersonID member2;
    private LedgerID ledgerID;
    private Group group;

    // =========================================================
    // SetUp & TearDown
    // =========================================================

    @BeforeEach
    void setUp() {
        admin    = PersonID.createPersonID("admin@email.com");
        member1  = PersonID.createPersonID("member1@email.com");
        member2  = PersonID.createPersonID("member2@email.com");
        ledgerID = LedgerID.createLedgerID();
        group    = Group.createGroupAsPersonInCharge(
            "Amigos", admin, "Grupo de amigos", LocalDate.of(2024, 1, 1), ledgerID
        );
    }

    @AfterEach
    void tearDown() {
        admin    = null;
        member1  = null;
        member2  = null;
        ledgerID = null;
        group    = null;
    }

    // =========================================================
    // Test Suite 1: Creación del Grupo
    // =========================================================

    @Nested
    @DisplayName("Suite 1 — Creación del Grupo")
    class CreacionGrupo {

        @Test
        @DisplayName("Crear grupo con admin — retorna grupo no nulo")
        void createGroupAsPersonInCharge_Success() {
            assertNotNull(group);
        }

        @Test
        @DisplayName("Crear grupo con admin — GroupID correcto")
        void createGroupAsPersonInCharge_GroupIDCorrect() {
            GroupID expectedID = GroupID.createGroupID("Amigos");
            assertEquals(expectedID, group.getGroupID());
        }

        @Test
        @DisplayName("Crear grupo con admin — descripción correcta")
        void createGroupAsPersonInCharge_DescriptionCorrect() {
            assertNotNull(group.getDescription());
        }

        @Test
        @DisplayName("Crear grupo con admin — fecha de creación correcta")
        void createGroupAsPersonInCharge_DateOfCreationCorrect() {
            assertNotNull(group.getDateOfCreation());
        }

        @Test
        @DisplayName("Crear grupo con admin — ledgerID correcto")
        void createGroupAsPersonInCharge_LedgerIDCorrect() {
            assertEquals(ledgerID, group.getLedgerID());
        }

        @Test
        @DisplayName("Crear grupo con admin nulo — lanza NullPointerException")
        void createGroupAsPersonInCharge_NullPersonThrowsException() {
            assertThrows(NullPointerException.class, () ->
                Group.createGroupAsPersonInCharge("Amigos", null, "desc", LocalDate.now(), ledgerID)
            );
        }

        @Test
        @DisplayName("Crear grupo con listas — retorna grupo no nulo")
        void createGroup_WithLists_Success() {
            List<PersonID> admins  = new ArrayList<>(List.of(admin));
            List<PersonID> members = new ArrayList<>(List.of(member1));
            Group g = Group.createGroup("Trabajo", admins, members, "Equipo", LocalDate.now(), ledgerID);
            assertNotNull(g);
        }

        @Test
        @DisplayName("Crear grupo con listas — admins nulos lanza NullPointerException")
        void createGroup_WithLists_NullAdminsThrowsException() {
            List<PersonID> members = new ArrayList<>();
            assertThrows(NullPointerException.class, () ->
                Group.createGroup("Trabajo", null, members, "Equipo", LocalDate.now(), ledgerID)
            );
        }

        @Test
        @DisplayName("Crear grupo con listas — members nulos lanza NullPointerException")
        void createGroup_WithLists_NullMembersThrowsException() {
            List<PersonID> admins = new ArrayList<>(List.of(admin));
            assertThrows(NullPointerException.class, () ->
                Group.createGroup("Trabajo", admins, null, "Equipo", LocalDate.now(), ledgerID)
            );
        }

        @Test
        @DisplayName("Crear grupo con fecha en String — retorna grupo no nulo")
        void createGroup_WithStringDate_Success() {
            Group g = Group.createGroup("Familia", "Familia cercana", "2023-05-10", admin);
            assertNotNull(g);
        }
    }

    // =========================================================
    // Test Suite 2: Gestión de Miembros
    // =========================================================

    @Nested
    @DisplayName("Suite 2 — Gestión de Miembros")
    class GestionMiembros {

        @Test
        @DisplayName("Agregar miembro nuevo — retorna true")
        void addMember_NewMember_ReturnsTrue() {
            assertTrue(group.addMember(member1));
        }

        @Test
        @DisplayName("Agregar miembro duplicado — retorna false")
        void addMember_DuplicateMember_ReturnsFalse() {
            group.addMember(member1);
            assertFalse(group.addMember(member1));
        }

        @Test
        @DisplayName("Agregar nuevo admin — retorna true")
        void addPersonInCharge_NewPerson_ReturnsTrue() {
            assertTrue(group.addPersonInCharge(member1));
        }

        @Test
        @DisplayName("Agregar admin duplicado — retorna false")
        void addPersonInCharge_Duplicate_ReturnsFalse() {
            assertFalse(group.addPersonInCharge(admin));
        }

        @Test
        @DisplayName("Verificar si admin es miembro — retorna true")
        void isPersonAlreadyMember_AdminIsAlreadyMember() {
            assertTrue(group.isPersonAlreadyMember(admin));
        }

        @Test
        @DisplayName("Verificar si persona nueva es miembro — retorna false")
        void isPersonAlreadyMember_NewPersonIsNotMember() {
            assertFalse(group.isPersonAlreadyMember(member1));
        }

        @Test
        @DisplayName("Verificar si persona recién agregada es miembro — retorna true")
        void isPersonAlreadyMember_AfterAddingMember_ReturnsTrue() {
            group.addMember(member1);
            assertTrue(group.isPersonAlreadyMember(member1));
        }

        @Test
        @DisplayName("Verificar si admin es PersonInCharge — retorna true")
        void isPersonPeopleInCharge_Admin_ReturnsTrue() {
            assertTrue(group.isPersonPeopleInCharge(admin));
        }

        @Test
        @DisplayName("Verificar si miembro regular es PersonInCharge — retorna false")
        void isPersonPeopleInCharge_RegularMember_ReturnsFalse() {
            group.addMember(member1);
            assertFalse(group.isPersonPeopleInCharge(member1));
        }

        @Test
        @DisplayName("getAllMembers — contiene admin y miembros")
        void getAllMembers_ContainsAdminAndMembers() {
            group.addMember(member1);
            List<PersonID> all = group.getAllMembers();
            assertTrue(all.contains(admin));
            assertTrue(all.contains(member1));
        }

        @Test
        @DisplayName("getPeopleInCharge — contiene admin")
        void getPeopleInCharge_ContainsAdmin() {
            assertTrue(group.getPeopleInCharge().contains(admin));
        }

        @Test
        @DisplayName("getMembers — contiene miembro recién agregado")
        void getMembers_AfterAddingMember_ContainsMember() {
            group.addMember(member2);
            assertTrue(group.getMembers().contains(member2));
        }
    }

    // =========================================================
    // Test Suite 3: Gestión de Categorías
    // =========================================================

    @Nested
    @DisplayName("Suite 3 — Gestión de Categorías")
    class GestionCategorias {

        @Test
        @DisplayName("Agregar categoría nueva — retorna true")
        void addCategory_NewCategory_ReturnsTrue() {
            CategoryID cat = CategoryID.createCategoryID("Comida", group.getGroupID());
            assertTrue(group.addCategory(cat));
        }

        @Test
        @DisplayName("Agregar categoría duplicada — retorna false")
        void addCategory_Duplicate_ReturnsFalse() {
            CategoryID cat = CategoryID.createCategoryID("Comida", group.getGroupID());
            group.addCategory(cat);
            assertFalse(group.addCategory(cat));
        }

        @Test
        @DisplayName("Verificar categoría existente — retorna true")
        void checkIfGroupHasCategory_ExistingCategory_ReturnsTrue() {
            CategoryID cat = CategoryID.createCategoryID("Transporte", group.getGroupID());
            group.addCategory(cat);
            assertTrue(group.checkIfGroupHasCategory(cat));
        }

        @Test
        @DisplayName("Verificar categoría no existente — retorna false")
        void checkIfGroupHasCategory_NonExisting_ReturnsFalse() {
            CategoryID cat = CategoryID.createCategoryID("Salud", group.getGroupID());
            assertFalse(group.checkIfGroupHasCategory(cat));
        }
    }

    // =========================================================
    // Test Suite 4: Gestión de Cuentas
    // =========================================================

    @Nested
    @DisplayName("Suite 4 — Gestión de Cuentas")
    class GestionCuentas {

        @Test
        @DisplayName("Agregar cuenta nueva — retorna true")
        void addAccount_NewAccount_ReturnsTrue() {
            AccountID acc = AccountID.createAccountID("Banco", group.getGroupID());
            assertTrue(group.addAccount(acc));
        }

        @Test
        @DisplayName("Agregar cuenta duplicada — retorna false")
        void addAccount_Duplicate_ReturnsFalse() {
            AccountID acc = AccountID.createAccountID("Banco", group.getGroupID());
            group.addAccount(acc);
            assertFalse(group.addAccount(acc));
        }

        @Test
        @DisplayName("Verificar cuenta existente — retorna true")
        void checkIfGroupHasAccount_ExistingAccount_ReturnsTrue() {
            AccountID acc = AccountID.createAccountID("Efectivo", group.getGroupID());
            group.addAccount(acc);
            assertTrue(group.checkIfGroupHasAccount(acc));
        }

        @Test
        @DisplayName("Verificar cuenta no existente — retorna false")
        void checkIfGroupHasAccount_NonExisting_ReturnsFalse() {
            AccountID acc = AccountID.createAccountID("Ahorros", group.getGroupID());
            assertFalse(group.checkIfGroupHasAccount(acc));
        }
    }

    // =========================================================
    // Test Suite 5: GroupID y Equals/HashCode
    // =========================================================

    @Nested
    @DisplayName("Suite 5 — GroupID, Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("checkGroupID con mismo ID — retorna true")
        void checkGroupID_SameID_ReturnsTrue() {
            assertTrue(group.checkGroupID(group.getGroupID()));
        }

        @Test
        @DisplayName("checkGroupID con diferente ID — retorna false")
        void checkGroupID_DifferentID_ReturnsFalse() {
            GroupID otherID = GroupID.createGroupID("OtroGrupo");
            assertFalse(group.checkGroupID(otherID));
        }

        @Test
        @DisplayName("equals — misma denominación retorna true")
        void equals_SameDenomination_ReturnsTrue() {
            Group g2 = Group.createGroupAsPersonInCharge("Amigos", member1, "Otro desc", LocalDate.now(), ledgerID);
            assertEquals(group, g2);
        }

        @Test
        @DisplayName("equals — diferente denominación retorna false")
        void equals_DifferentDenomination_ReturnsFalse() {
            Group g2 = Group.createGroupAsPersonInCharge("Trabajo", admin, "desc", LocalDate.now(), ledgerID);
            assertNotEquals(group, g2);
        }

        @Test
        @DisplayName("equals — mismo objeto retorna true")
        void equals_SameObject_ReturnsTrue() {
            assertEquals(group, group);
        }

        @Test
        @DisplayName("equals — comparar con null retorna false")
        void equals_Null_ReturnsFalse() {
            assertNotEquals(group, null);
        }

        @Test
        @DisplayName("hashCode — mismo grupo genera mismo hash")
        void hashCode_SameGroup_SameHash() {
            Group g2 = Group.createGroupAsPersonInCharge("Amigos", member1, "desc", LocalDate.now(), ledgerID);
            assertEquals(group.hashCode(), g2.hashCode());
        }
    }

    // =========================================================
    // Test Suite 6: Ledger y Schedulings
    // =========================================================

    @Nested
    @DisplayName("Suite 6 — Ledger y Schedulings")
    class LedgerSchedulings {

        @Test
        @DisplayName("addLedgerID — retorna true")
        void addLedgerID_ReturnsTrue() {
            LedgerID newLedger = LedgerID.createLedgerID();
            assertTrue(group.addLedgerID(newLedger));
        }

        @Test
        @DisplayName("Agregar scheduling nuevo — se agrega correctamente")
        void addScheduling_NewScheduling_IsAdded() {
            ScheduleID scheduleID = ScheduleID.createScheduleID(
                "Pago mensual", LocalDate.of(2024, 1, 1), "monthly", "debit"
            );
            group.addScheduling(scheduleID);
            assertTrue(group.getSchedulings().contains(scheduleID));
        }

        @Test
        @DisplayName("Agregar scheduling duplicado — no se agrega dos veces")
        void addScheduling_Duplicate_NotAddedTwice() {
            ScheduleID scheduleID = ScheduleID.createScheduleID(
                "Pago mensual", LocalDate.of(2024, 1, 1), "monthly", "debit"
            );
            group.addScheduling(scheduleID);
            group.addScheduling(scheduleID);
            assertEquals(1, group.getSchedulings().size());
        }

        @Test
        @DisplayName("getSchedulings — lista vacía al crear grupo")
        void getSchedulings_InitiallyEmpty() {
            assertTrue(group.getSchedulings().isEmpty());
        }
    }
}