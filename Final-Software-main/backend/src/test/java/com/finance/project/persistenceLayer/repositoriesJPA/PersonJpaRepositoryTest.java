package com.finance.project.persistenceLayer.repositoriesJPA;

import com.finance.project.dataModel.dataAssemblers.PersonDomainDataAssembler;
import com.finance.project.dataModel.dataModel.PersonJpa;
import com.finance.project.domainLayer.domainEntities.aggregates.person.Person;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.infrastructureLayer.repositories.PersonRepository;
import com.finance.project.persistenceLayer.repositoriesJPA.AddressJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonRepository — Repository Tests (Mockito)")
class PersonJpaRepositoryTest {

    @Mock
    private PersonJpaRepository personJpaRepository;
    @Mock
    private AddressJpaRepository addressJpaRepository;
    @Mock
    private PersonDomainDataAssembler personAssembler;

    @InjectMocks
    private PersonRepository personRepository;

    private Person person;
    private PersonJpa personJpa;
    private PersonID personID;

    @BeforeEach
    void setUp() {
        personID = PersonID.createPersonID("ana@x.com");
        person   = Person.createPerson("ana@x.com", "Ana", LocalDate.of(2000, 1, 1), "Lima");
        personJpa = new PersonJpa("ana@x.com", "Ana", "2000-01-01", "Lima");
    }

    // ------------------------------------------------------------------
    // save
    // ------------------------------------------------------------------

    @Test
    @DisplayName("save: convierte a JPA, persiste y devuelve dominio")
    void save_person_persistsAndReturnsDomain() {
        when(personAssembler.toData(person)).thenReturn(personJpa);
        when(personJpaRepository.save(personJpa)).thenReturn(personJpa);
        when(personAssembler.toDomain(personJpa)).thenReturn(person);

        Person result = personRepository.save(person);

        assertEquals(person, result);
        verify(personJpaRepository).save(personJpa);
    }

    // ------------------------------------------------------------------
    // findById
    // ------------------------------------------------------------------

    @Test
    @DisplayName("findById: ID existente devuelve Optional con la persona")
    void findById_existing_returnsOptionalPerson() {
        when(personJpaRepository.findById(personID)).thenReturn(Optional.of(personJpa));
        when(personAssembler.toDomain(personJpa)).thenReturn(person);

        Optional<Person> result = personRepository.findById(personID);

        assertTrue(result.isPresent());
        assertEquals(person, result.get());
    }

    @Test
    @DisplayName("findById: ID inexistente devuelve Optional vacío")
    void findById_nonExisting_returnsEmpty() {
        PersonID ghostID = PersonID.createPersonID("ghost@x.com");
        when(personJpaRepository.findById(ghostID)).thenReturn(Optional.empty());

        Optional<Person> result = personRepository.findById(ghostID);

        assertFalse(result.isPresent());
    }

    // ------------------------------------------------------------------
    // exists
    // ------------------------------------------------------------------

    @Test
    @DisplayName("exists: ID existente devuelve true")
    void exists_existing_returnsTrue() {
        when(personJpaRepository.existsById(personID)).thenReturn(true);

        assertTrue(personRepository.exists(personID));
    }

    @Test
    @DisplayName("exists: ID inexistente devuelve false")
    void exists_nonExisting_returnsFalse() {
        PersonID unknownID = PersonID.createPersonID("nobody@x.com");
        when(personJpaRepository.existsById(unknownID)).thenReturn(false);

        assertFalse(personRepository.exists(unknownID));
    }

    // ------------------------------------------------------------------
    // count
    // ------------------------------------------------------------------

    @Test
    @DisplayName("count: delega en personJpaRepository.count()")
    void count_delegatesToJpaRepository() {
        when(personJpaRepository.count()).thenReturn(3L);

        assertEquals(3L, personRepository.count());
    }

    // ------------------------------------------------------------------
    // findAll
    // ------------------------------------------------------------------

    @Test
    @DisplayName("findAll: convierte cada JPA a dominio y devuelve la lista")
    void findAll_returnsMappedList() {
        PersonJpa personJpa2 = new PersonJpa("beto@x.com", "Beto", "1998-05-15", "Cusco");
        Person person2 = Person.createPerson("beto@x.com", "Beto",
                LocalDate.of(1998, 5, 15), "Cusco");

        when(personJpaRepository.findAll()).thenReturn(Arrays.asList(personJpa, personJpa2));
        when(personAssembler.toDomain(personJpa)).thenReturn(person);
        when(personAssembler.toDomain(personJpa2)).thenReturn(person2);

        List<Person> result = personRepository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(person));
        assertTrue(result.contains(person2));
    }

    // ------------------------------------------------------------------
    // delete
    // ------------------------------------------------------------------

    @Test
    @DisplayName("delete: convierte a JPA y llama a personJpaRepository.delete()")
    void delete_person_callsJpaDelete() {
        when(personAssembler.toData(person)).thenReturn(personJpa);

        personRepository.delete(person);

        verify(personJpaRepository).delete(personJpa);
    }

    // ------------------------------------------------------------------
    // addAndSaveAddress
    // ------------------------------------------------------------------

    @Test
    @DisplayName("addAndSaveAddress: guarda la persona con la dirección y retorna true")
    void addAndSaveAddress_savesAndReturnsTrue() {
        when(personAssembler.toData(person)).thenReturn(personJpa);
        when(personJpaRepository.save(personJpa)).thenReturn(personJpa);

        boolean result = personRepository.addAndSaveAddress(person);

        assertTrue(result);
        verify(personJpaRepository).save(personJpa);
    }

    // ------------------------------------------------------------------
    // addAndSaveCategory / addAndSaveLedger
    // ------------------------------------------------------------------

    @Test
    @DisplayName("addAndSaveCategory: guarda la persona y retorna true")
    void addAndSaveCategory_savesAndReturnsTrue() {
        when(personAssembler.toData(person)).thenReturn(personJpa);
        when(personJpaRepository.save(personJpa)).thenReturn(personJpa);

        boolean result = personRepository.addAndSaveCategory(person);

        assertTrue(result);
        verify(personJpaRepository).save(personJpa);
    }

    @Test
    @DisplayName("addAndSaveLedger: guarda la persona y retorna true")
    void addAndSaveLedger_savesAndReturnsTrue() {
        when(personAssembler.toData(person)).thenReturn(personJpa);
        when(personJpaRepository.save(personJpa)).thenReturn(personJpa);

        boolean result = personRepository.addAndSaveLedger(person);

        assertTrue(result);
        verify(personJpaRepository).save(personJpa);
    }
}
