package com.finance.project.applicationLayer.applicationServices.personServices;

import com.finance.project.domainLayer.domainEntities.aggregates.ledger.Ledger;
import com.finance.project.domainLayer.domainEntities.aggregates.ledger.Transaction;
import com.finance.project.domainLayer.domainEntities.aggregates.person.Person;
import com.finance.project.domainLayer.domainEntities.vosShared.*;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.repositoriesInterfaces.*;
import com.finance.project.dtos.dtos.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Suite — Person Application Services
 * Módulo   : personServices
 * Servicios: CreatePersonAccountService, CreatePersonCategoryService,
 *            CreatePersonService, CreatePersonTransactionService,
 *            PersonSearchAccountRecordsService
 * Framework: JUnit 5 + Mockito
 * Patrón   : Arrange – Act – Assert (AAA)
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    // ── Mocks de repositorios (dobles de prueba) ──────────────────────────────
    @Mock private IPersonRepository   personRepository;
    @Mock private IAccountRepository  accountRepository;
    @Mock private ICategoryRepository categoryRepository;
    @Mock private ILedgerRepository   ledgerRepository;
    @Mock private IGroupRepository    groupRepository;
    @Mock private Ledger              mockLedger;

    // ── Datos de prueba compartidos ───────────────────────────────────────────
    private static final String    EMAIL      = "ana@mail.com";
    private static final String    NAME       = "Ana Lima";
    private static final String    BIRTHPLACE = "Lima";
    private static final LocalDate BIRTHDATE  = LocalDate.of(1990, 5, 20);

    /** Persona real creada con el factory method del dominio */
    private Person realPerson;
    private PersonID personID;
    private LedgerID ledgerID;

    @BeforeEach
    void setUp() {
        personID   = PersonID.createPersonID(EMAIL);
        ledgerID   = LedgerID.createLedgerID();
        realPerson = Person.createPerson(EMAIL, NAME, BIRTHDATE, BIRTHPLACE);
        realPerson.addLedgerID(ledgerID);
    }


    // =========================================================================
    //  1. CreatePersonAccountService
    // =========================================================================
    @Nested
    @DisplayName("1 - CreatePersonAccountService")
    class CreatePersonAccountServiceTests {

        private CreatePersonAccountService service;

        @BeforeEach
        void init() {
            service = new CreatePersonAccountService(personRepository, accountRepository);
        }

        @Test
        @DisplayName("TC1 - Cuenta creada con éxito cuando persona existe y cuenta es nueva")
        void createAccount_success() {
            // Arrange
            CreatePersonAccountDTO dto =
                    new CreatePersonAccountDTO(EMAIL, "Mi billetera", "Wallet");
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(accountRepository.existsById(any(AccountID.class)))
                    .thenReturn(false);
            when(personRepository.addAndSaveAccount(realPerson, "Mi billetera"))
                    .thenReturn(true);

            // Act
            PersonDTO result = service.createAccount(dto);

            // Assert
            assertNotNull(result, "El PersonDTO no debe ser nulo");
            verify(personRepository).addAndSaveAccount(realPerson, "Mi billetera");
        }

        @Test
        @DisplayName("TC2 - Lanza NotFoundArgumentsBusinessException cuando persona NO existe")
        void createAccount_personNotFound() {
            // Arrange
            CreatePersonAccountDTO dto =
                    new CreatePersonAccountDTO(EMAIL, "Mi billetera", "Wallet");
            when(personRepository.findById(personID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundArgumentsBusinessException.class,
                    () -> service.createAccount(dto));
        }

        @Test
        @DisplayName("TC3 - Lanza InvalidArgumentsBusinessException cuando cuenta YA existe")
        void createAccount_accountAlreadyExists() {
            // Arrange
            CreatePersonAccountDTO dto =
                    new CreatePersonAccountDTO(EMAIL, "Mi billetera", "Wallet");
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(accountRepository.existsById(any(AccountID.class)))
                    .thenReturn(true);

            // Act & Assert
            assertThrows(InvalidArgumentsBusinessException.class,
                    () -> service.createAccount(dto));
        }

        @Test
        @DisplayName("TC4 - Constante PERSON_DOES_NOT_EXIST tiene el mensaje correcto")
        void constant_personDoesNotExist() {
            assertEquals("Person does not exist in the system",
                    CreatePersonAccountService.PERSON_DOES_NOT_EXIST);
        }
    }


    // =========================================================================
    //  2. CreatePersonCategoryService
    // =========================================================================
    @Nested
    @DisplayName("2 - CreatePersonCategoryService")
    class CreatePersonCategoryServiceTests {

        private CreatePersonCategoryService service;

        @BeforeEach
        void init() {
            service = new CreatePersonCategoryService(personRepository, categoryRepository);
        }

        @Test
        @DisplayName("TC1 - Categoría creada con éxito cuando persona existe y categoría es nueva")
        void createCategory_success() {
            // Arrange
            CreatePersonCategoryDTO dto = new CreatePersonCategoryDTO(EMAIL, "Comida");
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(categoryRepository.existsById(any(CategoryID.class)))
                    .thenReturn(false);
            when(personRepository.addAndSaveCategory(realPerson))
                    .thenReturn(true);

            // Act
            PersonDTO result = service.createCategory(dto);

            // Assert
            assertNotNull(result);
            verify(personRepository).addAndSaveCategory(realPerson);
        }

        @Test
        @DisplayName("TC2 - Lanza NotFoundArgumentsBusinessException cuando persona NO existe")
        void createCategory_personNotFound() {
            // Arrange
            CreatePersonCategoryDTO dto = new CreatePersonCategoryDTO(EMAIL, "Comida");
            when(personRepository.findById(personID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundArgumentsBusinessException.class,
                    () -> service.createCategory(dto));
        }

        @Test
        @DisplayName("TC3 - Lanza InvalidArgumentsBusinessException cuando categoría YA existe")
        void createCategory_categoryAlreadyExists() {
            // Arrange
            CreatePersonCategoryDTO dto = new CreatePersonCategoryDTO(EMAIL, "Comida");
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(categoryRepository.existsById(any(CategoryID.class)))
                    .thenReturn(true);

            // Act & Assert
            assertThrows(InvalidArgumentsBusinessException.class,
                    () -> service.createCategory(dto));
        }

        @Test
        @DisplayName("TC4 - Constantes de mensaje correctas")
        void constants_areCorrect() {
            assertEquals("Category created and added",
                    CreatePersonCategoryService.SUCCESS);
            assertEquals("Category already exists",
                    CreatePersonCategoryService.CATEGORY_ALREADY_EXIST);
            assertEquals("Person does not exist",
                    CreatePersonCategoryService.PERSON_DOES_NOT_EXIST);
        }
    }


    // =========================================================================
    //  3. CreatePersonService
    // =========================================================================
    @Nested
    @DisplayName("3 - CreatePersonService")
    class CreatePersonServiceTests {

        private CreatePersonService service;

        @BeforeEach
        void init() {
            service = new CreatePersonService(
                    personRepository, ledgerRepository, categoryRepository, accountRepository);
        }

        @Test
        @DisplayName("TC1 - Persona creada y guardada cuando el email no existe")
        void createPerson_success() {
            // Arrange
            CreatePersonDTO dto = new CreatePersonDTO(EMAIL, NAME, BIRTHDATE, BIRTHPLACE);
            when(personRepository.findById(personID)).thenReturn(Optional.empty());
            when(personRepository.save(any(Person.class))).thenReturn(realPerson);

            // Act
            PersonDTO result = service.createPerson(dto);

            // Assert
            assertNotNull(result);
            verify(personRepository).save(any(Person.class));
        }

        @Test
        @DisplayName("TC2 - Lanza InvalidArgumentsBusinessException cuando email YA registrado")
        void createPerson_alreadyExists() {
            // Arrange
            CreatePersonDTO dto = new CreatePersonDTO(EMAIL, NAME, BIRTHDATE, BIRTHPLACE);
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));

            // Act & Assert
            assertThrows(InvalidArgumentsBusinessException.class,
                    () -> service.createPerson(dto));
            verify(personRepository, never()).save(any());
        }

        @Test
        @DisplayName("TC3 - Constante PERSON_ALREADY_EXIST correcta")
        void constant_personAlreadyExist() {
            assertEquals("Person already exists", CreatePersonService.PERSON_ALREADY_EXIST);
        }
    }


    // =========================================================================
    //  4. CreatePersonTransactionService
    // =========================================================================
    @Nested
    @DisplayName("4 - CreatePersonTransactionService")
    class CreatePersonTransactionServiceTests {

        private CreatePersonTransactionService service;

        @BeforeEach
        void init() {
            service = new CreatePersonTransactionService(
                    personRepository, accountRepository, ledgerRepository, categoryRepository);
        }

        // Constructor real: (email, denominationCategory, type, description,
        //                    amount, denominationAccountDeb, denominationAccountCred, date)
        private CreatePersonTransactionDTO buildDTO() {
            return new CreatePersonTransactionDTO(
                    EMAIL, "Comida", "debit", "Almuerzo",
                    50.0, "Wallet", "Savings", "2024-06-01");
        }

        @Test
        @DisplayName("TC1 - Transacción creada con éxito cuando todos los recursos existen")
        void createTransaction_success() {
            // Arrange
            CreatePersonTransactionDTO dto = buildDTO();
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(categoryRepository.existsById(any(CategoryID.class)))
                    .thenReturn(true);
            when(accountRepository.existsById(any(AccountID.class)))
                    .thenReturn(true);
            when(ledgerRepository.findById(ledgerID))
                    .thenReturn(Optional.of(mockLedger));
            when(ledgerRepository.addAndSaveTransaction(mockLedger))
                    .thenReturn(true);

            // Act
            PersonDTO result = service.createTransaction(dto);

            // Assert
            assertNotNull(result);
            verify(ledgerRepository).addAndSaveTransaction(mockLedger);
        }

        @Test
        @DisplayName("TC2 - Lanza NotFoundArgumentsBusinessException cuando persona NO existe")
        void createTransaction_personNotFound() {
            // Arrange
            CreatePersonTransactionDTO dto = buildDTO();
            when(personRepository.findById(personID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundArgumentsBusinessException.class,
                    () -> service.createTransaction(dto));
        }

        @Test
        @DisplayName("TC3 - Lanza excepción CATEGORY_DOES_NOT_EXIST cuando categoría no existe")
        void createTransaction_categoryNotFound() {
            // Arrange
            CreatePersonTransactionDTO dto = buildDTO();
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(categoryRepository.existsById(any(CategoryID.class)))
                    .thenReturn(false);

            // Act & Assert
            NotFoundArgumentsBusinessException ex = assertThrows(
                    NotFoundArgumentsBusinessException.class,
                    () -> service.createTransaction(dto));
            assertEquals(CreatePersonTransactionService.CATEGORY_DOES_NOT_EXIST,
                    ex.getMessage());
        }

        @Test
        @DisplayName("TC4 - Lanza excepción ACCOUNT_DEB_DOES_NOT_EXIST cuando cuenta débito no existe")
        void createTransaction_debitAccountNotFound() {
            // Arrange
            CreatePersonTransactionDTO dto = buildDTO();
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(categoryRepository.existsById(any(CategoryID.class)))
                    .thenReturn(true);
            when(accountRepository.existsById(any(AccountID.class)))
                    .thenReturn(false);

            // Act & Assert
            NotFoundArgumentsBusinessException ex = assertThrows(
                    NotFoundArgumentsBusinessException.class,
                    () -> service.createTransaction(dto));
            assertEquals(CreatePersonTransactionService.ACCOUNT_DEB_DOES_NOT_EXIST,
                    ex.getMessage());
        }

        @Test
        @DisplayName("TC5 - Lanza excepción ACCOUNT_CRED_DOES_NOT_EXIST cuando cuenta crédito no existe")
        void createTransaction_creditAccountNotFound() {
            // Arrange
            CreatePersonTransactionDTO dto = buildDTO();
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(categoryRepository.existsById(any(CategoryID.class)))
                    .thenReturn(true);
            // débito = true, crédito = false
            when(accountRepository.existsById(any(AccountID.class)))
                    .thenReturn(true)
                    .thenReturn(false);

            // Act & Assert
            NotFoundArgumentsBusinessException ex = assertThrows(
                    NotFoundArgumentsBusinessException.class,
                    () -> service.createTransaction(dto));
            assertEquals(CreatePersonTransactionService.ACCOUNT_CRED_DOES_NOT_EXIST,
                    ex.getMessage());
        }
    }


    // =========================================================================
    //  5. PersonSearchAccountRecordsService
    // =========================================================================
    @Nested
    @DisplayName("5 - PersonSearchAccountRecordsService")
    class PersonSearchAccountRecordsServiceTests {

        private PersonSearchAccountRecordsService service;

        @BeforeEach
        void init() {
            service = new PersonSearchAccountRecordsService(
                    personRepository, accountRepository, ledgerRepository);
        }

        private PersonSearchAccountRecordsInDTO dto(
                String email, String account, String start, String end) {
            return new PersonSearchAccountRecordsInDTO(email, account, start, end);
        }

        @Test
        @DisplayName("TC1 - getOptPerson: persona NO existe → InvalidArgumentsBusinessException")
        void getOptPerson_personNotFound() {
            // Arrange
            when(personRepository.findById(personID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(InvalidArgumentsBusinessException.class,
                    () -> service.getOptPerson(
                            dto(EMAIL, "Wallet", "2024-01-01", "2024-12-31")));
        }

        @Test
        @DisplayName("TC2 - getOptPerson: persona existe → Optional<Person> presente")
        void getOptPerson_success() {
            // Arrange
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));

            // Act
            Optional<Person> result =
                    service.getOptPerson(dto(EMAIL, "Wallet", "2024-01-01", "2024-12-31"));

            // Assert
            assertTrue(result.isPresent());
        }

        @Test
        @DisplayName("TC3 - getAccountID: campo cuenta vacío → NotFoundArgumentsBusinessException")
        void getAccountID_emptyField() {


            // Act & Assert
            assertThrows(NotFoundArgumentsBusinessException.class,
                    () -> service.getAccountID(
                            dto(EMAIL, "", "2024-01-01", "2024-12-31")));
        }

        @Test
        @DisplayName("TC4 - getAccountID: cuenta NO existe en repo → InvalidArgumentsBusinessException")
        void getAccountID_accountNotFound() {
            // Arrange
            when(accountRepository.existsById(any(AccountID.class)))
                    .thenReturn(false);

            // Act & Assert
            assertThrows(InvalidArgumentsBusinessException.class,
                    () -> service.getAccountID(
                            dto(EMAIL, "Wallet", "2024-01-01", "2024-12-31")));
        }

        @Test
        @DisplayName("TC5 - getStartDate: campo vacío → NotFoundArgumentsBusinessException")
        void getStartDate_emptyField() {
            assertThrows(NotFoundArgumentsBusinessException.class,
                    () -> service.getStartDate(
                            dto(EMAIL, "Wallet", "", "2024-12-31")));
        }

        @Test
        @DisplayName("TC6 - getEndDate: campo vacío → NotFoundArgumentsBusinessException")
        void getEndDate_emptyField() {
            assertThrows(NotFoundArgumentsBusinessException.class,
                    () -> service.getEndDate(
                            dto(EMAIL, "Wallet", "2024-01-01", "")));
        }

        @Test
        @DisplayName("TC7 - getOptLedger: startDate > endDate → DATES_IN_REVERSE_ORDER")
        void getOptLedger_datesInReverseOrder() {
            // Arrange
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));

            // Act & Assert
            InvalidArgumentsBusinessException ex = assertThrows(
                    InvalidArgumentsBusinessException.class,
                    () -> service.getOptLedger(
                            dto(EMAIL, "Wallet", "2024-12-31", "2024-01-01")));
            assertEquals(PersonSearchAccountRecordsService.DATES_IN_REVERSE_ORDER,
                    ex.getMessage());
        }

        @Test
        @DisplayName("TC8 - getOptLedger: ledger vacío → EMPTY_LEDGER")
        void getOptLedger_emptyLedger() {
            // Arrange
            when(personRepository.findById(personID))
                    .thenReturn(Optional.of(realPerson));
            when(ledgerRepository.findById(ledgerID))
                    .thenReturn(Optional.of(mockLedger));
            when(mockLedger.getRecords()).thenReturn(new ArrayList<>());

            // Act & Assert
            NotFoundArgumentsBusinessException ex = assertThrows(
                    NotFoundArgumentsBusinessException.class,
                    () -> service.getOptLedger(
                            dto(EMAIL, "Wallet", "2024-01-01", "2024-12-31")));
            assertEquals(PersonSearchAccountRecordsService.EMPTY_LEDGER,
                    ex.getMessage());
        }

        @Test
        @DisplayName("TC9 - Constantes de mensaje correctas")
        void constants_areCorrect() {
            assertEquals("Person does not exist in the system",
                    PersonSearchAccountRecordsService.PERSON_DOES_NOT_EXIST);
            assertEquals("Account does not exist in the system",
                    PersonSearchAccountRecordsService.ACCOUNT_DOES_NOT_EXIST);
            assertEquals("Ledger is empty",
                    PersonSearchAccountRecordsService.EMPTY_LEDGER);
            assertEquals("Check the start and end dates for the period, since start date cannot be later than end date",
                    PersonSearchAccountRecordsService.DATES_IN_REVERSE_ORDER);
        }
    }
}
