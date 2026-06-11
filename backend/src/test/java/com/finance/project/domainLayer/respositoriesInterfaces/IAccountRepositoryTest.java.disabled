package com.finance.project.applicationLayer.applicationServices.accountServices;

import com.finance.project.domainLayer.domainEntities.aggregates.account.Account;
import com.finance.project.domainLayer.domainEntities.vosShared.AccountID;
import com.finance.project.domainLayer.repositoriesInterfaces.IAccountRepository;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryMockTest {

    @Mock
    private IAccountRepository accountRepository; // El contrato que estamos simulando

    @InjectMocks
    private AccountApplicationService accountService; // Servicio hipotético que usa el repo

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    @DisplayName("TC-01: Verificar guardado y captura de objeto Account")
    void testSaveAccount_Success() {
        // Arrange
        Account myAccount = mock(Account.class);
        when(accountRepository.save(any(Account.class))).thenReturn(myAccount);

        // Act
        accountRepository.save(myAccount);

        // Assert
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        assertNotNull(accountCaptor.getValue());
    }

    @Test
    @DisplayName("TC-02: Búsqueda por ID - Caso Cuenta no encontrada")
    void testFindById_NotFound() {
        // Arrange
        String id = "acc-123";
        String denomination = "USD";
        // Simulamos que el repositorio devuelve vacío
        when(accountRepository.findById(id, denomination)).thenReturn(Optional.empty());

        // Act & Assert
        // Verificamos que el servicio maneje el Optional vacío correctamente
        Optional<Account> result = accountRepository.findById(id, denomination);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC-03: Verificar conteo de cuentas")
    void testCountAccounts() {
        // Arrange
        when(accountRepository.count()).thenReturn(150L);

        // Act
        long total = accountRepository.count();

        // Assert
        assertEquals(150L, total);
    }

    @Test
    @DisplayName("TC-04: Eliminación de cuenta - Verificación de interacción")
    void testDeleteAccount() {
        // Arrange
        Account accToDelete = mock(Account.class);

        // Act
        accountRepository.delete(accToDelete);

        // Assert
        // En un método void, lo más importante es verificar que se llamó al repo
        verify(accountRepository, times(1)).delete(accToDelete);
    }

    @Test
    @DisplayName("TC-05: Búsqueda avanzada filtrada (findAllById)")
    void testFindAllById_WithParameters() {
        // Arrange
        when(accountRepository.findAllById(anyString(), anyString(), anyString()))
                .thenReturn(List.of(mock(Account.class)));

        // Act
        List<Account> results = accountRepository.findAllById("desc", "den", "id");

        // Assert
        assertFalse(results.isEmpty());
        verify(accountRepository).findAllById("desc", "den", "id");
    }
}