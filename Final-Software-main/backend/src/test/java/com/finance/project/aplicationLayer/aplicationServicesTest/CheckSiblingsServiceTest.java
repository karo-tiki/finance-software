package com.finance.project.applicationLayer.applicationServices.otherServices;

import com.finance.project.domainLayer.domainEntities.aggregates.person.Person;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.repositoriesInterfaces.IPersonRepository;
import com.finance.project.dtos.dtos.BooleanDTO;
import com.finance.project.dtos.dtos.CheckIfSiblingsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckSiblingsServiceTest {

    @Mock
    IPersonRepository personRepository;

    @InjectMocks
    CheckSiblingsService checkSiblingsService;

    private PersonID personAID;
    private PersonID personBID;
    private Person personA;
    private Person personB;

    @BeforeEach
    void setUp() {
        personAID = PersonID.createPersonID("personA@email.com");
        personBID = PersonID.createPersonID("personB@email.com");
        personA   = mock(Person.class);
        personB   = mock(Person.class);
    }

    private CheckIfSiblingsDTO buildDTO() {
        CheckIfSiblingsDTO dto = mock(CheckIfSiblingsDTO.class);
        when(dto.getEmail()).thenReturn("personA@email.com");
        when(dto.getSiblingEmail()).thenReturn("personB@email.com");
        return dto;
    }

    @Test
    void checkIfSiblings_BothAreSiblings_ReturnsTrue() {
        CheckIfSiblingsDTO dto = buildDTO();
        when(personRepository.findById(personAID)).thenReturn(Optional.of(personA));
        when(personRepository.findById(personBID)).thenReturn(Optional.of(personB));
        when(personA.verifySiblingsOrHalfSiblings(personB)).thenReturn(true);

        BooleanDTO result = checkSiblingsService.checkIfSiblings(dto);

        assertNotNull(result);
        assertTrue(result.getResult());
    }

    @Test
    void checkIfSiblings_NotSiblings_ThrowsException() {
        CheckIfSiblingsDTO dto = buildDTO();
        when(personRepository.findById(personAID)).thenReturn(Optional.of(personA));
        when(personRepository.findById(personBID)).thenReturn(Optional.of(personB));
        when(personA.verifySiblingsOrHalfSiblings(personB)).thenReturn(false);

        assertThrows(InvalidArgumentsBusinessException.class, () ->
            checkSiblingsService.checkIfSiblings(dto)
        );
    }

    @Test
    void checkIfSiblings_PersonANotExists_ThrowsException() {
        CheckIfSiblingsDTO dto = buildDTO();
        when(personRepository.findById(personAID)).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class, () ->
            checkSiblingsService.checkIfSiblings(dto)
        );
    }

    @Test
    void checkIfSiblings_PersonBNotExists_ThrowsException() {
        CheckIfSiblingsDTO dto = buildDTO();
        when(personRepository.findById(personAID)).thenReturn(Optional.of(personA));
        when(personRepository.findById(personBID)).thenReturn(Optional.empty());

        assertThrows(NotFoundArgumentsBusinessException.class, () ->
            checkSiblingsService.checkIfSiblings(dto)
        );
    }

    @Test
    void successConstant_IsCorrect() {
        assertEquals("Siblings", CheckSiblingsService.SUCCESS);
    }

    @Test
    void failConstant_IsCorrect() {
        assertEquals("Not Siblings", CheckSiblingsService.FAIL);
    }

    @Test
    void notExist1Constant_IsCorrect() {
        assertEquals("First person does not exist", CheckSiblingsService.NOT_EXIST_1);
    }

    @Test
    void notExist2Constant_IsCorrect() {
        assertEquals("Second person does not exist", CheckSiblingsService.NOT_EXIST_2);
    }
}
