package com.finance.project.domainLayer.domainEntities.aggregates.ledger;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction — Domain Tests")
class TransactionTest {

    private CategoryID categoryID;
    private AccountID debitAccountID;
    private AccountID creditAccountID;
    private PersonID personID;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        personID        = PersonID.createPersonID("user@email.com");
        categoryID      = CategoryID.createCategoryID("Comida", personID);
        debitAccountID  = AccountID.createAccountID("Banco", personID);
        creditAccountID = AccountID.createAccountID("Efectivo", personID);
        date            = LocalDate.of(2024, 6, 15);
    }

    @AfterEach
    void tearDown() {
        personID        = null;
        categoryID      = null;
        debitAccountID  = null;
        creditAccountID = null;
        date            = null;
    }

    @Nested
    @DisplayName("Suite 1 — Creación con Fecha")
    class CreacionConFecha {

        @Test
        @DisplayName("Crear transacción con fecha — retorna no nulo")
        void createTransaction_ValidInputs_NotNull() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertNotNull(t);
        }

        @Test
        @DisplayName("Crear transacción con fecha — categoryID correcto")
        void createTransaction_CategoryIDCorrect() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertEquals(categoryID, t.getCategoryID());
        }

        @Test
        @DisplayName("Crear transacción con fecha — debitAccountID correcto")
        void createTransaction_DebitAccountIDCorrect() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertEquals(debitAccountID, t.getDebitAccountID());
        }

        @Test
        @DisplayName("Crear transacción con fecha — creditAccountID correcto")
        void createTransaction_CreditAccountIDCorrect() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertEquals(creditAccountID, t.getCreditAccountID());
        }

        @Test
        @DisplayName("Crear transacción — categoryID nulo lanza excepción")
        void createTransaction_NullCategoryID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransaction(null, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción — type nulo lanza excepción")
        void createTransaction_NullType_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransaction(categoryID, null, "Almuerzo", 50.0, date, debitAccountID, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción — description nula lanza excepción")
        void createTransaction_NullDescription_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransaction(categoryID, "debit", null, 50.0, date, debitAccountID, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción — fecha nula lanza excepción")
        void createTransaction_NullDate_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, null, debitAccountID, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción — debitAccountID nulo lanza excepción")
        void createTransaction_NullDebitAccountID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, null, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción — creditAccountID nulo lanza excepción")
        void createTransaction_NullCreditAccountID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, null)
            );
        }
    }

    @Nested
    @DisplayName("Suite 2 — Creación con Fecha del Sistema")
    class CreacionConFechaSistema {

        @Test
        @DisplayName("Crear transacción con fecha sistema — retorna no nulo")
        void createTransactionWithSystemDate_NotNull() {
            Transaction t = Transaction.createTransactionWithSystemDate(categoryID, "credit", "Pago", 100.0, debitAccountID, creditAccountID);
            assertNotNull(t);
        }

        @Test
        @DisplayName("Crear transacción con fecha sistema — fecha es hoy")
        void createTransactionWithSystemDate_DateIsToday() {
            Transaction t = Transaction.createTransactionWithSystemDate(categoryID, "credit", "Pago", 100.0, debitAccountID, creditAccountID);
            assertEquals(LocalDate.now(), t.getDate().getDate());
        }

        @Test
        @DisplayName("Crear transacción con fecha sistema — categoryID nulo lanza excepción")
        void createTransactionWithSystemDate_NullCategoryID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransactionWithSystemDate(null, "credit", "Pago", 100.0, debitAccountID, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción con fecha sistema — debitAccountID nulo lanza excepción")
        void createTransactionWithSystemDate_NullDebitAccountID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransactionWithSystemDate(categoryID, "credit", "Pago", 100.0, null, creditAccountID)
            );
        }

        @Test
        @DisplayName("Crear transacción con fecha sistema — creditAccountID nulo lanza excepción")
        void createTransactionWithSystemDate_NullCreditAccountID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Transaction.createTransactionWithSystemDate(categoryID, "credit", "Pago", 100.0, debitAccountID, null)
            );
        }
    }

    @Nested
    @DisplayName("Suite 3 — Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("equals — mismos datos retorna true")
        void equals_SameData_ReturnsTrue() {
            Transaction t1 = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            Transaction t2 = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertEquals(t1, t2);
        }

        @Test
        @DisplayName("equals — datos diferentes retorna false")
        void equals_DifferentData_ReturnsFalse() {
            Transaction t1 = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            Transaction t2 = Transaction.createTransaction(categoryID, "credit", "Cena", 80.0, date, debitAccountID, creditAccountID);
            assertNotEquals(t1, t2);
        }

        @Test
        @DisplayName("equals — mismo objeto retorna true")
        void equals_SameObject_ReturnsTrue() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertEquals(t, t);
        }

        @Test
        @DisplayName("equals — comparar con null retorna false")
        void equals_Null_ReturnsFalse() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertNotEquals(t, null);
        }

        @Test
        @DisplayName("hashCode — mismos datos generan mismo hash")
        void hashCode_SameData_SameHash() {
            Transaction t1 = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            Transaction t2 = Transaction.createTransaction(categoryID, "debit", "Almuerzo", 50.0, date, debitAccountID, creditAccountID);
            assertEquals(t1.hashCode(), t2.hashCode());
        }
    }
}
