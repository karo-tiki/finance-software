package com.finance.project.domainLayer.domainEntities.aggregates.account;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Aggregate — Domain Tests")
class AccountTest {

    private PersonID personID;
    private Account account;

    @BeforeEach
    void setUp() {
        personID = PersonID.createPersonID("user@email.com");
        account  = Account.createAccount("Cuenta principal", "Banco", personID);
    }

    @AfterEach
    void tearDown() {
        personID = null;
        account  = null;
    }

    @Nested
    @DisplayName("Suite 1 — Creación de Cuenta")
    class CreacionCuenta {

        @Test
        @DisplayName("Crear cuenta — retorna no nulo")
        void createAccount_NotNull() {
            assertNotNull(account);
        }

        @Test
        @DisplayName("Crear cuenta — AccountID no nulo")
        void createAccount_AccountIDNotNull() {
            assertNotNull(account.getAccountID());
        }

        @Test
        @DisplayName("Crear cuenta — descripción no nula")
        void createAccount_DescriptionNotNull() {
            assertNotNull(account.getDescription());
        }

        @Test
        @DisplayName("Crear cuenta — description nula lanza excepción")
        void createAccount_NullDescription_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Account.createAccount(null, "Banco", personID)
            );
        }

        @Test
        @DisplayName("Crear cuenta — denomination nula lanza excepción")
        void createAccount_NullDenomination_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Account.createAccount("Cuenta", null, personID)
            );
        }

        @Test
        @DisplayName("Crear cuenta — ownerID nulo lanza excepción")
        void createAccount_NullOwnerID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Account.createAccount("Cuenta", "Banco", null)
            );
        }
    }

    @Nested
    @DisplayName("Suite 2 — Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("equals — misma denominación y owner retorna true")
        void equals_SameData_ReturnsTrue() {
            Account account2 = Account.createAccount("Otra desc", "Banco", personID);
            assertEquals(account, account2);
        }

        @Test
        @DisplayName("equals — diferente denominación retorna false")
        void equals_DifferentDenomination_ReturnsFalse() {
            Account account2 = Account.createAccount("Cuenta", "Efectivo", personID);
            assertNotEquals(account, account2);
        }

        @Test
        @DisplayName("equals — mismo objeto retorna true")
        void equals_SameObject_ReturnsTrue() {
            assertEquals(account, account);
        }

        @Test
        @DisplayName("equals — comparar con null retorna false")
        void equals_Null_ReturnsFalse() {
            assertNotEquals(account, null);
        }

        @Test
        @DisplayName("hashCode — misma cuenta genera mismo hash")
        void hashCode_SameAccount_SameHash() {
            Account account2 = Account.createAccount("Otra desc", "Banco", personID);
            assertEquals(account.hashCode(), account2.hashCode());
        }

        @Test
        @DisplayName("hashCode — diferente cuenta genera diferente hash")
        void hashCode_DifferentAccount_DifferentHash() {
            Account account2 = Account.createAccount("Cuenta", "Efectivo", personID);
            assertNotEquals(account.hashCode(), account2.hashCode());
        }
    }
}
