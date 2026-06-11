package com.finance.project.applicationLayer.applicationServices.groupServices;

import com.finance.project.domainLayer.domainEntities.vosShared.GroupID;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.domainLayer.repositoriesInterfaces.IGroupRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.IPersonRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.IAccountRepository;
import com.finance.project.dtos.dtos.CreateGroupDTO;
import com.finance.project.dtos.dtos.AddPersonToGroupDTO;
import com.finance.project.dtos.dtos.CreateGroupAccountDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
public class GroupServicesTest {

    @Mock private IGroupRepository groupRepository;
    @Mock private IPersonRepository personRepository;
    @Mock private IAccountRepository accountRepository;

    @InjectMocks private CreateGroupService createGroupService;
    @InjectMocks private AddPersonToGroupService addPersonService;
    @InjectMocks private CreateGroupAccountService createGroupAccountService;

    @Nested
    @DisplayName("Suite 1 — CreateGroupService")
    class CreateGroupServiceTests {

        @Test
        @DisplayName("G1: Fallar si la persona no existe")
        void createGroup_PersonNotFound_ThrowsException() {
            CreateGroupDTO dto = new CreateGroupDTO("ghost@test.com", "Group", "ghost@test.com", "Desc");
            when(personRepository.exists(any(PersonID.class))).thenReturn(false);
            assertThrows(Exception.class, () ->
                createGroupService.createGroupAsPersonInCharge(dto)
            );
        }

        @Test
        @DisplayName("G2: Fallar si el grupo ya existe")
        void createGroup_AlreadyExists_ThrowsException() {
            CreateGroupDTO dto = new CreateGroupDTO("admin@test.com", "Existing Group", "admin@test.com", "Desc");
            when(personRepository.exists(any(PersonID.class))).thenReturn(true);
            when(groupRepository.exists(any(GroupID.class))).thenReturn(true);
            assertThrows(Exception.class, () ->
                createGroupService.createGroupAsPersonInCharge(dto)
            );
        }
    }

    @Nested
    @DisplayName("Suite 2 — AddPersonToGroupService")
    class AddPersonToGroupServiceTests {

        @Test
        @DisplayName("G3: Error si la persona no existe")
        void addPersonToGroup_PersonNotFound() {
            AddPersonToGroupDTO dto = new AddPersonToGroupDTO("ghost@test.com", "Project Alpha");
            when(personRepository.exists(any(PersonID.class))).thenReturn(false);
            assertThrows(Exception.class, () ->
                addPersonService.addPersonToGroup(dto)
            );
        }

        @Test
        @DisplayName("G4: Error si el grupo no existe")
        void addPersonToGroup_GroupNotFound() {
            AddPersonToGroupDTO dto = new AddPersonToGroupDTO("user@test.com", "Fake Group");
            when(personRepository.exists(any(PersonID.class))).thenReturn(true);
            when(groupRepository.exists(any(GroupID.class))).thenReturn(false);
            assertThrows(Exception.class, () ->
                addPersonService.addPersonToGroup(dto)
            );
        }
    }

    @Nested
    @DisplayName("Suite 3 — CreateGroupAccountService")
    class CreateGroupAccountServiceTests {

        @Test
        @DisplayName("G5: Fallar si el grupo no existe")
        void createGroupAccount_GroupNotFound() {
            CreateGroupAccountDTO dto = new CreateGroupAccountDTO("admin@test.com", "Fake Group", "Savings", "Desc");
            when(groupRepository.exists(any(GroupID.class))).thenReturn(false);
            assertThrows(Exception.class, () ->
                createGroupAccountService.createAccountAsPeopleInCharge(dto)
            );
        }
    }
}
