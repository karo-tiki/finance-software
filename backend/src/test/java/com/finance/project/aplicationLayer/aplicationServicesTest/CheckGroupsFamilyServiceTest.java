package com.finance.project.applicationLayer.applicationServices.otherServices;

import com.finance.project.domainLayer.domainEntities.aggregates.group.Group;
import com.finance.project.domainLayer.domainEntities.aggregates.person.Person;
import com.finance.project.domainLayer.domainEntities.vosShared.GroupID;
import com.finance.project.domainLayer.domainEntities.vosShared.LedgerID;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.repositoriesInterfaces.IGroupRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.IPersonRepository;
import com.finance.project.dtos.dtos.GroupDTO;
import com.finance.project.dtos.dtos.GroupMembersDTO;
import com.finance.project.dtos.dtos.GroupsThatAreFamilyDTO;
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
class CheckGroupsFamilyServiceTest {

    @Mock
    IGroupRepository groupRepository;

    @Mock
    IPersonRepository personRepository;

    @InjectMocks
    CheckGroupsFamilyService checkGroupsFamilyService;

    private Group group;
    private GroupID groupID;
    private PersonID adminID;
    private LedgerID ledgerID;

    @BeforeEach
    void setUp() {
        adminID  = PersonID.createPersonID("admin@email.com");
        ledgerID = LedgerID.createLedgerID();
        groupID  = GroupID.createGroupID("Amigos");
        group    = Group.createGroupAsPersonInCharge("Amigos", adminID, "Grupo de amigos", LocalDate.of(2024, 1, 1), ledgerID);
    }

    // groupsThatAreFamily

    @Test
    void groupsThatAreFamily_EmptyList_ReturnsEmptyDTO() {
        when(groupRepository.findAll()).thenReturn(new ArrayList<>());

        GroupsThatAreFamilyDTO result = checkGroupsFamilyService.groupsThatAreFamily();

        assertNotNull(result);
    }

    @Test
    void groupsThatAreFamily_GroupWithNoPersonsInRepo_ReturnsEmptyDTO() {
        List<Group> groups = new ArrayList<>(List.of(group));
        when(groupRepository.findAll()).thenReturn(groups);
        when(personRepository.findById(any(PersonID.class))).thenReturn(Optional.empty());

        GroupsThatAreFamilyDTO result = checkGroupsFamilyService.groupsThatAreFamily();

        assertNotNull(result);
    }

    // getGroupByDenomination

    @Test
    void getGroupByDenomination_GroupExists_ReturnsGroupDTO() {
        when(groupRepository.findById(groupID)).thenReturn(Optional.of(group));

        GroupDTO result = checkGroupsFamilyService.getGroupByDenomination("Amigos");

        assertNotNull(result);
    }

    @Test
    void getGroupByDenomination_GroupNotExists_ThrowsException() {
        when(groupRepository.findById(any(GroupID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class, () ->
            checkGroupsFamilyService.getGroupByDenomination("Fantasmas")
        );
    }

    // getGroupAllMembers

    @Test
    void getGroupAllMembers_GroupExists_ReturnsGroupMembersDTO() {
        when(groupRepository.findById(groupID)).thenReturn(Optional.of(group));

        GroupMembersDTO result = checkGroupsFamilyService.getGroupAllMembers("Amigos");

        assertNotNull(result);
    }

    @Test
    void getGroupAllMembers_GroupNotExists_ThrowsException() {
        when(groupRepository.findById(any(GroupID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class, () ->
            checkGroupsFamilyService.getGroupAllMembers("Fantasmas")
        );
    }
}
