package com.finance.project.applicationLayer.applicationServices.personServices;

import com.finance.project.domainLayer.domainEntities.aggregates.person.Address;
import com.finance.project.domainLayer.domainEntities.aggregates.person.Person;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.repositoriesInterfaces.IAccountRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.ICategoryRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.ILedgerRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.IPersonRepository;
import com.finance.project.dtos.dtos.CreatePersonDTO;
import com.finance.project.dtos.dtos.PersonDTO;
import com.finance.project.dtos.dtos.PersonEmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePersonService — Application Service Tests (Mockito)")
class CreatePersonServiceTest {

    @Mock
    private IPersonRepository personRepository;
    @Mock
    private ILedgerRepository ledgerRepository;
    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private CreatePersonService service;

    private LocalDate birthdate;
    private Person existingPerson;
    private PersonID personID;

    @BeforeEach
    void setUp() {
        birthdate = LocalDate.of(2000, 1, 1);
        personID  = PersonID.createPersonID("ana@x.com");
        existingPerson = Person.createPerson("ana@x.com", "Ana", birthdate, "Lima");
    }

    // ------------------------------------------------------------------
    // createPerson
    // ------------------------------------------------------------------

    @Test
    @DisplayName("createPerson: persona nueva — la guarda y devuelve PersonDTO")
    void createPerson_personDoesNotExist_savesAndReturnsDTO() {
        CreatePersonDTO dto = new CreatePersonDTO("ana@x.com", "Ana", birthdate, "Lima");

        when(personRepository.findById(personID)).thenReturn(Optional.empty());
        when(personRepository.save(any(Person.class))).thenReturn(existingPerson);

        PersonDTO result = service.createPerson(dto);

        assertNotNull(result);
        assertEquals("ana@x.com", result.getEmail());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    @DisplayName("createPerson: persona ya existe — lanza InvalidArgumentsBusinessException")
    void createPerson_personAlreadyExists_throws() {
        CreatePersonDTO dto = new CreatePersonDTO("ana@x.com", "Ana", birthdate, "Lima");

        when(personRepository.findById(personID)).thenReturn(Optional.of(existingPerson));

        InvalidArgumentsBusinessException ex = assertThrows(
                InvalidArgumentsBusinessException.class,
                () -> service.createPerson(dto));

        assertEquals(CreatePersonService.PERSON_ALREADY_EXIST, ex.getMessage());
        verify(personRepository, never()).save(any());
    }

    // ------------------------------------------------------------------
    // getPersonByEmail
    // ------------------------------------------------------------------

    @Test
    @DisplayName("getPersonByEmail: persona existe — devuelve PersonDTO con datos correctos")
    void getPersonByEmail_personExists_returnsDTO() {
        PersonEmailDTO emailDTO = new PersonEmailDTO("ana@x.com");

        when(personRepository.findById(personID)).thenReturn(Optional.of(existingPerson));

        PersonDTO result = service.getPersonByEmail(emailDTO);

        assertNotNull(result);
        assertEquals("ana@x.com", result.getEmail());
        assertEquals("Ana", result.getName());
    }

    @Test
    @DisplayName("getPersonByEmail: persona no existe — lanza NotFoundArgumentsBusinessException")
    void getPersonByEmail_personDoesNotExist_throws() {
        PersonEmailDTO emailDTO = new PersonEmailDTO("ghost@x.com");
        PersonID ghostID = PersonID.createPersonID("ghost@x.com");

        when(personRepository.findById(ghostID)).thenReturn(Optional.empty());

        NotFoundArgumentsBusinessException ex = assertThrows(
                NotFoundArgumentsBusinessException.class,
                () -> service.getPersonByEmail(emailDTO));

        assertEquals(CreatePersonService.PERSON_DOES_NOT_EXIST, ex.getMessage());
    }

    // ------------------------------------------------------------------
    // getPersonSiblings
    // ------------------------------------------------------------------

    @Test
    @DisplayName("getPersonSiblings: persona existe — devuelve SiblingsDTO (lista vacía)")
    void getPersonSiblings_personExists_returnsSiblingsDTO() {
        PersonEmailDTO emailDTO = new PersonEmailDTO("ana@x.com");

        when(personRepository.findById(personID)).thenReturn(Optional.of(existingPerson));

        assertNotNull(service.getPersonSiblings(emailDTO));
    }

    @Test
    @DisplayName("getPersonSiblings: persona no existe — lanza NotFoundArgumentsBusinessException")
    void getPersonSiblings_personDoesNotExist_throws() {
        PersonEmailDTO emailDTO = new PersonEmailDTO("nobody@x.com");
        PersonID nobodyID = PersonID.createPersonID("nobody@x.com");

        when(personRepository.findById(nobodyID)).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class,
                () -> service.getPersonSiblings(emailDTO));
    }

    // ------------------------------------------------------------------
    // addAddressToPerson
    // ------------------------------------------------------------------

    @Test
    @DisplayName("addAddressToPerson: persona existe sin dirección — guarda y retorna true")
    void addAddressToPerson_personExistsNoAddress_returnsTrue() {
        Address address = Address.createAddress("Av. Sol", "123", "15000", "Lima", "Peru");

        when(personRepository.findById(personID)).thenReturn(Optional.of(existingPerson));
        when(personRepository.addAndSaveAddress(any(Person.class))).thenReturn(true);

        boolean result = service.addAddressToPerson(personID, address);

        assertTrue(result);
        verify(personRepository).addAndSaveAddress(any(Person.class));
    }

    @Test
    @DisplayName("addAddressToPerson: persona no existe — lanza NotFoundArgumentsBusinessException")
    void addAddressToPerson_personDoesNotExist_throws() {
        PersonID ghostID = PersonID.createPersonID("ghost@x.com");
        Address address = Address.createAddress("Av. Sol", "123", "15000", "Lima", "Peru");

        when(personRepository.findById(ghostID)).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class,
                () -> service.addAddressToPerson(ghostID, address));

        verify(personRepository, never()).addAndSaveAddress(any());
    }

    // ------------------------------------------------------------------
    // addMotherToPerson
    // ------------------------------------------------------------------

    @Test
    @DisplayName("addMotherToPerson: ambas personas existen — asigna madre y retorna true")
    void addMotherToPerson_bothExist_returnsTrue() {
        Person mother = Person.createPerson("mom@x.com", "Mom", birthdate, "Lima");
        PersonID motherID = PersonID.createPersonID("mom@x.com");

        when(personRepository.findById(personID)).thenReturn(Optional.of(existingPerson));
        when(personRepository.findById(motherID)).thenReturn(Optional.of(mother));
        when(personRepository.addAndSaveMother(any(Person.class))).thenReturn(true);

        boolean result = service.addMotherToPerson(personID, motherID);

        assertTrue(result);
        verify(personRepository).addAndSaveMother(existingPerson);
    }

    @Test
    @DisplayName("addMotherToPerson: persona no existe — lanza NotFoundArgumentsBusinessException")
    void addMotherToPerson_personDoesNotExist_throws() {
        PersonID ghostID = PersonID.createPersonID("ghost@x.com");
        PersonID motherID = PersonID.createPersonID("mom@x.com");

        when(personRepository.findById(ghostID)).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class,
                () -> service.addMotherToPerson(ghostID, motherID));
    }
}
