package com.finance.project.infrastructureLayer.repositories;

import com.finance.project.dataModel.dataAssemblers.GroupDomainDataAssembler;
import com.finance.project.dataModel.dataModel.AdminJpa;
import com.finance.project.dataModel.dataModel.GroupJpa;
import com.finance.project.domainLayer.domainEntities.aggregates.group.Group;
import com.finance.project.domainLayer.domainEntities.vosShared.GroupID;
import com.finance.project.domainLayer.domainEntities.vosShared.LedgerID;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.persistenceLayer.repositoriesJPA.GroupJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupRepositoryTest {

    @Mock
    GroupJpaRepository groupJpaRepository;

    @Mock
    GroupDomainDataAssembler groupAssembler;

    @InjectMocks
    GroupRepository groupRepository;

    private Group group;
    private GroupJpa groupJpa;
    private GroupID groupID;
    private PersonID adminID;
    private LedgerID ledgerID;

    @BeforeEach
    void setUp() {
        adminID  = PersonID.createPersonID("admin@email.com");
        ledgerID = LedgerID.createLedgerID();
        groupID  = GroupID.createGroupID("Amigos");
        group    = Group.createGroupAsPersonInCharge("Amigos", adminID, "Grupo de amigos", LocalDate.of(2024, 1, 1), ledgerID);
        groupJpa = mock(GroupJpa.class);
    }

    @Test
    void addAndSaveAdmin_ReturnsTrue() {
        when(groupAssembler.toData(group)).thenReturn(groupJpa);
        when(groupJpaRepository.save(groupJpa)).thenReturn(groupJpa);

        boolean result = groupRepository.addAndSaveAdmin(group, adminID);

        assertTrue(result);
        verify(groupJpaRepository).save(groupJpa);
    }

    @Test
    void findById_GroupExists_ReturnsOptionalGroup() {
        when(groupJpaRepository.findById(groupID)).thenReturn(Optional.of(groupJpa));
        when(groupAssembler.toDomain(groupJpa)).thenReturn(group);

        Optional<Group> result = groupRepository.findById(groupID);

        assertTrue(result.isPresent());
        assertEquals(group, result.get());
    }

    @Test
    void findById_GroupNotExists_ReturnsEmptyOptional() {
        when(groupJpaRepository.findById(groupID)).thenReturn(Optional.empty());

        Optional<Group> result = groupRepository.findById(groupID);

        assertFalse(result.isPresent());
    }

    @Test
    void addAndSaveLedger_ReturnsTrue() {
        when(groupAssembler.toData(group)).thenReturn(groupJpa);
        when(groupJpaRepository.save(groupJpa)).thenReturn(groupJpa);

        boolean result = groupRepository.addAndSaveLedger(group);

        assertTrue(result);
    }

    @Test
    void findAdminsById_GroupExists_ReturnsAdminList() {
        AdminJpa adminJpa = mock(AdminJpa.class);
        when(adminJpa.getPersonID()).thenReturn(adminID);
        List<AdminJpa> adminsJpa = new ArrayList<>(List.of(adminJpa));

        when(groupJpaRepository.findById(groupID)).thenReturn(Optional.of(groupJpa));
        when(groupJpa.getAdministrators()).thenReturn(adminsJpa);

        List<PersonID> result = groupRepository.findAdminsById(groupID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(adminID, result.get(0));
    }

    @Test
    void findAdminsById_GroupNotExists_ReturnsNull() {
        when(groupJpaRepository.findById(groupID)).thenReturn(Optional.empty());

        List<PersonID> result = groupRepository.findAdminsById(groupID);

        assertNull(result);
    }

    @Test
    void addAndSaveMember_ReturnsTrue() {
        PersonID memberID = PersonID.createPersonID("member@email.com");
        when(groupAssembler.toData(group)).thenReturn(groupJpa);
        when(groupJpaRepository.save(groupJpa)).thenReturn(groupJpa);

        boolean result = groupRepository.addAndSaveMember(group, memberID);

        assertTrue(result);
        verify(groupJpaRepository).save(groupJpa);
    }

    @Test
    void addAndSaveCategory_ReturnsTrue() {
        when(groupAssembler.toData(group)).thenReturn(groupJpa);
        when(groupJpaRepository.save(groupJpa)).thenReturn(groupJpa);

        boolean result = groupRepository.addAndSaveCategory(group);

        assertTrue(result);
    }

    @Test
    void exists_GroupExists_ReturnsTrue() {
        when(groupJpaRepository.existsById(groupID)).thenReturn(true);

        assertTrue(groupRepository.exists(groupID));
    }

    @Test
    void exists_GroupNotExists_ReturnsFalse() {
        when(groupJpaRepository.existsById(groupID)).thenReturn(false);

        assertFalse(groupRepository.exists(groupID));
    }

    @Test
    void count_ReturnsNumberOfGroups() {
        when(groupJpaRepository.count()).thenReturn(5L);

        assertEquals(5L, groupRepository.count());
    }

    @Test
    void findAll_ReturnsListOfGroups() {
        List<GroupJpa> groupJpaList = new ArrayList<>(List.of(groupJpa));
        when(groupJpaRepository.findAll()).thenReturn(groupJpaList);
        when(groupAssembler.toDomain(groupJpa)).thenReturn(group);

        List<Group> result = groupRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(group, result.get(0));
    }

    @Test
    void findAll_EmptyRepository_ReturnsEmptyList() {
        when(groupJpaRepository.findAll()).thenReturn(new ArrayList<>());

        List<Group> result = groupRepository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
