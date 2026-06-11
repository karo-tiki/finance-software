@ExtendWith(MockitoExtension.class)
class GroupServicesTestSuite {

    @Mock private IGroupRepository groupRepository;
    @Mock private IPersonRepository personRepository;
    @Mock private IAccountRepository accountRepository;

    @InjectMocks private AddPersonToGroupService addPersonService;
    @InjectMocks private CreateGroupService createGroupService;
    @InjectMocks private CreateGroupAccountService createGroupAccountService;

    // ============================================================
    // TESTS PARA CreateGroupService (Módulo Group)
    // ============================================================

    @Test
    @DisplayName("G1: Crear grupo exitosamente")
    void createGroup_Success() {
        when(groupRepository.existsById(any())).thenReturn(false);
        when(groupRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        boolean result = createGroupService.createGroup("Project Alpha", "Description");

        assertTrue(result);
        verify(groupRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("G2: Fallar si el grupo ya existe")
    void createGroup_AlreadyExists_ThrowsException() {
        when(groupRepository.existsById(any())).thenReturn(true);

        assertThrows(InvalidArgumentsBusinessException.class, () -> 
            createGroupService.createGroup("Existing Group", "Desc")
        );
        verify(groupRepository, never()).save(any());
    }

    // ============================================================
    // TESTS PARA AddPersonToGroupService (Relaciones)
    // ============================================================

    @Test
    @DisplayName("G3: Agregar persona a grupo exitosamente")
    void addPersonToGroup_Success() {
        // Arrange: Ambos existen
        when(personRepository.existsById(any())).thenReturn(true);
        when(groupRepository.findById(any())).thenReturn(Optional.of(mock(Group.class)));
        when(groupRepository.addMember(any(), any())).thenReturn(true);

        // Act
        boolean result = addPersonService.addPerson("user@test.com", "group-id");

        // Assert
        assertTrue(result);
        verify(groupRepository).addMember(any(), any());
    }

    @Test
    @DisplayName("G4: Error al agregar si la persona no existe")
    void addPersonToGroup_PersonNotFound() {
        when(personRepository.existsById(any())).thenReturn(false);

        assertThrows(NotFoundArgumentsBusinessException.class, () -> 
            addPersonService.addPerson("ghost@test.com", "group-id")
        );
        verify(groupRepository, never()).addMember(any(), any());
    }

    @Test
    @DisplayName("G5: Error si el grupo no existe")
    void addPersonToGroup_GroupNotFound() {
        when(personRepository.existsById(any())).thenReturn(true);
        when(groupRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class, () -> 
            addPersonService.addPerson("user@test.com", "fake-group")
        );
    }

    // ============================================================
    // TESTS PARA CreateGroupAccountService (Cuentas Grupales)
    // ============================================================

    @Test
    @DisplayName("G6: Crear cuenta grupal exitosamente")
    void createGroupAccount_Success() {
        when(groupRepository.existsById(any())).thenReturn(true);
        when(accountRepository.existsById(any())).thenReturn(false);
        when(groupRepository.addAndSaveAccount(any(), any())).thenReturn(true);

        boolean result = createGroupAccountService.createAccount("group-1", "Shared Savings");

        assertTrue(result);
        verify(groupRepository).addAndSaveAccount(any(), any());
    }

    @Test
    @DisplayName("G7: Fallar si la cuenta ya existe en el grupo")
    void createGroupAccount_DuplicateAccount() {
        when(groupRepository.existsById(any())).thenReturn(true);
        when(accountRepository.existsById(any())).thenReturn(true);

        assertThrows(InvalidArgumentsBusinessException.class, () -> 
            createGroupAccountService.createAccount("group-1", "Shared Savings")
        );
    }

    // ============================================================
    // TEST DE RESILIENCIA (Infraestructura)
    // ============================================================

    @Test
    @DisplayName("G8: El servicio debe retornar false si el repositorio falla silenciosamente")
    void createGroup_PersistenceFailure_ReturnsFalse() {
        when(groupRepository.existsById(any())).thenReturn(false);
        when(groupRepository.save(any())).thenReturn(null); // Simula fallo en el retorno

        boolean result = createGroupService.createGroup("Fail Group", "Desc");

        assertFalse(result);
    }
}