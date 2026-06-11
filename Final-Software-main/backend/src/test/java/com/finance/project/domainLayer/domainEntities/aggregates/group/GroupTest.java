package com.finance.project.domainLayer.domainEntities.aggregates.group;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private PersonID admin;
    private PersonID member1;
    private PersonID member2;
    private LedgerID ledgerID;
    private Group group;

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

    @Test
    void createGroupAsPersonInCharge_Success() {
        assertNotNull(group);
    }

    @Test
    void createGroupAsPersonInCharge_NullPersonThrowsException() {
        assertThrows(NullPointerException.class, () ->
            Group.createGroupAsPersonInCharge("Amigos", null, "desc", LocalDate.now(), ledgerID)
        );
    }

    @Test
    void createGroup_WithLists_Success() {
        List<PersonID> admins  = new ArrayList<>(List.of(admin));
        List<PersonID> members = new ArrayList<>(List.of(member1));
        Group g = Group.createGroup("Trabajo", admins, members, "Equipo", LocalDate.now(), ledgerID);
        assertNotNull(g);
    }

    @Test
    void createGroup_WithLists_NullAdminsThrowsException() {
        List<PersonID> members = new ArrayList<>();
        assertThrows(NullPointerException.class, () ->
            Group.createGroup("Trabajo", null, members, "Equipo", LocalDate.now(), ledgerID)
        );
    }

    @Test
    void createGroup_WithLists_NullMembersThrowsException() {
        List<PersonID> admins = new ArrayList<>(List.of(admin));
        assertThrows(NullPointerException.class, () ->
            Group.createGroup("Trabajo", admins, null, "Equipo", LocalDate.now(), ledgerID)
        );
    }

    @Test
    void createGroup_WithStringDate_Success() {
        Group g = Group.createGroup("Familia", "Familia cercana", "2023-05-10", admin);
        assertNotNull(g);
    }

    @Test
    void addMember_NewMember_ReturnsTrue() {
        assertTrue(group.addMember(member1));
    }

    @Test
    void addMember_DuplicateMember_ReturnsFalse() {
        group.addMember(member1);
        assertFalse(group.addMember(member1));
    }

    @Test
    void addPersonInCharge_NewPerson_ReturnsTrue() {
        assertTrue(group.addPersonInCharge(member1));
    }

    @Test
    void addPersonInCharge_Duplicate_ReturnsFalse() {
        assertFalse(group.addPersonInCharge(admin));
    }

    @Test
    void isPersonAlreadyMember_AdminIsAlreadyMember() {
        assertTrue(group.isPersonAlreadyMember(admin));
    }

    @Test
    void isPersonAlreadyMember_NewPersonIsNotMember() {
        assertFalse(group.isPersonAlreadyMember(member1));
    }

    @Test
    void isPersonAlreadyMember_AfterAddingMember_ReturnsTrue() {
        group.addMember(member1);
        assertTrue(group.isPersonAlreadyMember(member1));
    }

    @Test
    void isPersonPeopleInCharge_Admin_ReturnsTrue() {
        assertTrue(group.isPersonPeopleInCharge(admin));
    }

    @Test
    void isPersonPeopleInCharge_RegularMember_ReturnsFalse() {
        group.addMember(member1);
        assertFalse(group.isPersonPeopleInCharge(member1));
    }

    @Test
    void addCategory_NewCategory_ReturnsTrue() {
        CategoryID cat = CategoryID.createCategoryID("Comida", group.getGroupID());
        assertTrue(group.addCategory(cat));
    }

    @Test
    void addCategory_Duplicate_ReturnsFalse() {
        CategoryID cat = CategoryID.createCategoryID("Comida", group.getGroupID());
        group.addCategory(cat);
        assertFalse(group.addCategory(cat));
    }

    @Test
    void checkIfGroupHasCategory_ExistingCategory_ReturnsTrue() {
        CategoryID cat = CategoryID.createCategoryID("Transporte", group.getGroupID());
        group.addCategory(cat);
        assertTrue(group.checkIfGroupHasCategory(cat));
    }

    @Test
    void checkIfGroupHasCategory_NonExisting_ReturnsFalse() {
        CategoryID cat = CategoryID.createCategoryID("Salud", group.getGroupID());
        assertFalse(group.checkIfGroupHasCategory(cat));
    }

    @Test
    void addAccount_NewAccount_ReturnsTrue() {
        AccountID acc = AccountID.createAccountID("Banco", group.getGroupID());
        assertTrue(group.addAccount(acc));
    }

    @Test
    void addAccount_Duplicate_ReturnsFalse() {
        AccountID acc = AccountID.createAccountID("Banco", group.getGroupID());
        group.addAccount(acc);
        assertFalse(group.addAccount(acc));
    }

    @Test
    void checkIfGroupHasAccount_ExistingAccount_ReturnsTrue() {
        AccountID acc = AccountID.createAccountID("Efectivo", group.getGroupID());
        group.addAccount(acc);
        assertTrue(group.checkIfGroupHasAccount(acc));
    }

    @Test
    void getAllMembers_ContainsAdminAndMembers() {
        group.addMember(member1);
        List<PersonID> all = group.getAllMembers();
        assertTrue(all.contains(admin));
        assertTrue(all.contains(member1));
    }

    @Test
    void getPeopleInCharge_ContainsAdmin() {
        assertTrue(group.getPeopleInCharge().contains(admin));
    }

    @Test
    void getMembers_AfterAddingMember_ContainsMember() {
        group.addMember(member2);
        assertTrue(group.getMembers().contains(member2));
    }

    @Test
    void checkGroupID_SameID_ReturnsTrue() {
        assertTrue(group.checkGroupID(group.getGroupID()));
    }

    @Test
    void checkGroupID_DifferentID_ReturnsFalse() {
        GroupID otherID = GroupID.createGroupID("OtroGrupo");
        assertFalse(group.checkGroupID(otherID));
    }

    @Test
    void equals_SameDenomination_ReturnsTrue() {
        Group g2 = Group.createGroupAsPersonInCharge("Amigos", member1, "Otro desc", LocalDate.now(), ledgerID);
        assertEquals(group, g2);
    }

    @Test
    void equals_DifferentDenomination_ReturnsFalse() {
        Group g2 = Group.createGroupAsPersonInCharge("Trabajo", admin, "desc", LocalDate.now(), ledgerID);
        assertNotEquals(group, g2);
    }

    @Test
    void equals_SameObject_ReturnsTrue() {
        assertEquals(group, group);
    }

    @Test
    void equals_Null_ReturnsFalse() {
        assertNotEquals(group, null);
    }

    @Test
    void hashCode_SameGroup_SameHash() {
        Group g2 = Group.createGroupAsPersonInCharge("Amigos", member1, "desc", LocalDate.now(), ledgerID);
        assertEquals(group.hashCode(), g2.hashCode());
    }

    @Test
    void addLedgerID_ReturnsTrue() {
        LedgerID newLedger = LedgerID.createLedgerID();
        assertTrue(group.addLedgerID(newLedger));
    }

    @Test
    void addScheduling_NewScheduling_IsAdded() {
        ScheduleID scheduleID = ScheduleID.createScheduleID("Pago mensual", LocalDate.of(2024, 1, 1), "monthly", "debit");
        group.addScheduling(scheduleID);
        assertTrue(group.getSchedulings().contains(scheduleID));
    }

    @Test
    void addScheduling_Duplicate_NotAddedTwice() {
        ScheduleID scheduleID = ScheduleID.createScheduleID("Pago mensual", LocalDate.of(2024, 1, 1), "monthly", "debit");
        group.addScheduling(scheduleID);
        group.addScheduling(scheduleID);
        assertEquals(1, group.getSchedulings().size());
    }
}
