package com.finance.project.domainLayer.domainEntities.aggregates.ledger;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ledger Aggregate — Domain Tests")
class LedgerTest {

    private Ledger ledger;
    private CategoryID categoryID;
    private AccountID debitAccountID;
    private AccountID creditAccountID;
    private PersonID personID;

    @BeforeEach
    void setUp() {
        ledger          = Ledger.createLedger();
        personID        = PersonID.createPersonID("user@email.com");
        categoryID      = CategoryID.createCategoryID("Comida", personID);
        debitAccountID  = AccountID.createAccountID("Banco", personID);
        creditAccountID = AccountID.createAccountID("Efectivo", personID);
    }

    @AfterEach
    void tearDown() {
        ledger          = null;
        personID        = null;
        categoryID      = null;
        debitAccountID  = null;
        creditAccountID = null;
    }

    @Nested
    @DisplayName("Suite 1 — Creación del Ledger")
    class CreacionLedger {

        @Test
        @DisplayName("Crear ledger — retorna no nulo")
        void createLedger_NotNull() {
            assertNotNull(ledger);
        }

        @Test
        @DisplayName("Crear ledger — LedgerID no nulo")
        void createLedger_LedgerIDNotNull() {
            assertNotNull(ledger.getLedgerID());
        }

        @Test
        @DisplayName("Crear ledger — lista de registros vacía")
        void createLedger_RecordsInitiallyEmpty() {
            assertTrue(ledger.getRecords().isEmpty());
        }

        @Test
        @DisplayName("Crear ledger con LedgerID — retorna no nulo")
        void createLedgerWithID_NotNull() {
            LedgerID id = LedgerID.createLedgerID();
            Ledger l = new Ledger(id);
            assertNotNull(l);
        }
    }

    @Nested
    @DisplayName("Suite 2 — Agregar Transacciones")
    class AgregarTransacciones {

        @Test
        @DisplayName("createAndAddTransaction — retorna true")
        void createAndAddTransaction_ReturnsTrue() {
            assertTrue(ledger.createAndAddTransaction(categoryID, "debit", "Almuerzo", 50.0, debitAccountID, creditAccountID));
        }

        @Test
        @DisplayName("createAndAddTransaction — aumenta tamaño de registros")
        void createAndAddTransaction_IncreasesRecordsSize() {
            ledger.createAndAddTransaction(categoryID, "debit", "Almuerzo", 50.0, debitAccountID, creditAccountID);
            assertEquals(1, ledger.getRecords().size());
        }

        @Test
        @DisplayName("createAndAddTransactionWithDate — retorna true")
        void createAndAddTransactionWithDate_ReturnsTrue() {
            assertTrue(ledger.createAndAddTransactionWithDate(categoryID, "debit", "Cena", 80.0,
                LocalDate.of(2024, 1, 10), debitAccountID, creditAccountID));
        }

        @Test
        @DisplayName("addTransaction — transacción válida se agrega")
        void addTransaction_ValidTransaction_IsAdded() {
            Transaction t = Transaction.createTransaction(categoryID, "debit", "Test", 30.0,
                LocalDate.of(2024, 3, 1), debitAccountID, creditAccountID);
            ledger.addTransaction(t);
            assertEquals(1, ledger.getRecords().size());
        }

        @Test
        @DisplayName("addTransaction — transacción nula no se agrega")
        void addTransaction_NullTransaction_NotAdded() {
            ledger.addTransaction(null);
            assertTrue(ledger.getRecords().isEmpty());
        }
    }

    @Nested
    @DisplayName("Suite 3 — Actualizar Transacciones")
    class ActualizarTransacciones {

        @Test
        @DisplayName("updateTransaction — retorna true")
        void updateTransaction_ReturnsTrue() {
            ledger.createAndAddTransaction(categoryID, "debit", "Almuerzo", 50.0, debitAccountID, creditAccountID);
            assertTrue(ledger.updateTransaction(1, categoryID, "credit", "Cena", 90.0, debitAccountID, creditAccountID));
        }

        @Test
        @DisplayName("updateTransaction — cambia la transacción correctamente")
        void updateTransaction_ChangesTransaction() {
            ledger.createAndAddTransaction(categoryID, "debit", "Almuerzo", 50.0, debitAccountID, creditAccountID);
            ledger.updateTransaction(1, categoryID, "credit", "Cena actualizada", 90.0, debitAccountID, creditAccountID);
            assertEquals(1, ledger.getRecords().size());
        }
    }

    @Nested
    @DisplayName("Suite 4 — Consultar por Fechas")
    class ConsultarPorFechas {

        @Test
        @DisplayName("getRecordsBetweenTwoDates — retorna transacciones en el rango")
        void getRecordsBetweenTwoDates_ReturnsCorrectTransactions() {
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T1", 10.0, LocalDate.of(2024, 1, 5), debitAccountID, creditAccountID);
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T2", 20.0, LocalDate.of(2024, 1, 15), debitAccountID, creditAccountID);
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T3", 30.0, LocalDate.of(2024, 2, 1), debitAccountID, creditAccountID);
            var result = ledger.getRecordsBetweenTwoDates(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("getRecordsBetweenTwoDates — fecha inicio posterior a fin lanza excepción")
        void getRecordsBetweenTwoDates_InvalidRange_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                ledger.getRecordsBetweenTwoDates(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 1, 1))
            );
        }

        @Test
        @DisplayName("getEarliestTransactionDate — retorna fecha más temprana")
        void getEarliestTransactionDate_ReturnsCorrectDate() {
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T1", 10.0, LocalDate.of(2024, 3, 1), debitAccountID, creditAccountID);
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T2", 20.0, LocalDate.of(2024, 1, 1), debitAccountID, creditAccountID);
            assertEquals(LocalDate.of(2024, 1, 1), ledger.getEarliestTransactionDate());
        }

        @Test
        @DisplayName("getEarliestTransactionDate — ledger vacío lanza excepción")
        void getEarliestTransactionDate_EmptyLedger_ThrowsException() {
            assertThrows(IllegalStateException.class, () -> ledger.getEarliestTransactionDate());
        }

        @Test
        @DisplayName("getLatestTransactionDate — retorna fecha más reciente")
        void getLatestTransactionDate_ReturnsCorrectDate() {
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T1", 10.0, LocalDate.of(2024, 1, 1), debitAccountID, creditAccountID);
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T2", 20.0, LocalDate.of(2024, 6, 1), debitAccountID, creditAccountID);
            assertEquals(LocalDate.of(2024, 6, 1), ledger.getLatestTransactionDate());
        }

        @Test
        @DisplayName("getLatestTransactionDate — ledger vacío lanza excepción")
        void getLatestTransactionDate_EmptyLedger_ThrowsException() {
            assertThrows(IllegalStateException.class, () -> ledger.getLatestTransactionDate());
        }
    }

    @Nested
    @DisplayName("Suite 5 — Consultar por Cuenta")
    class ConsultarPorCuenta {

        @Test
        @DisplayName("getAccountRecords — retorna transacciones de la cuenta")
        void getAccountRecords_ReturnsCorrectTransactions() {
            ledger.createAndAddTransactionWithDate(categoryID, "debit", "T1", 10.0, LocalDate.of(2024, 1, 1), debitAccountID, creditAccountID);
            var result = ledger.getAccountRecords(debitAccountID);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("getAccountRecords — cuenta sin registros lanza excepción")
        void getAccountRecords_NoRecords_ThrowsException() {
            AccountID otherAccount = AccountID.createAccountID("Otro", personID);
            assertThrows(IllegalArgumentException.class, () -> ledger.getAccountRecords(otherAccount));
        }

        @Test
        @DisplayName("getAccountsOfRecordsSorted — ledger vacío lanza excepción")
        void getAccountsOfRecordsSorted_EmptyLedger_ThrowsException() {
            assertThrows(IllegalStateException.class, () -> ledger.getAccountsOfRecordsSorted());
        }

        @Test
        @DisplayName("getAccountsOfRecordsSorted — retorna cuentas del ledger")
        void getAccountsOfRecordsSorted_ReturnsAccounts() {
            ledger.createAndAddTransaction(categoryID, "debit", "T1", 10.0, debitAccountID, creditAccountID);
            assertFalse(ledger.getAccountsOfRecordsSorted().isEmpty());
        }
    }

    @Nested
    @DisplayName("Suite 6 — Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("equals — mismo objeto retorna true")
        void equals_SameObject_ReturnsTrue() {
            assertEquals(ledger, ledger);
        }

        @Test
        @DisplayName("equals — diferente LedgerID retorna false")
        void equals_DifferentLedgerID_ReturnsFalse() {
            Ledger ledger2 = Ledger.createLedger();
            assertNotEquals(ledger, ledger2);
        }

        @Test
        @DisplayName("equals — comparar con null retorna false")
        void equals_Null_ReturnsFalse() {
            assertNotEquals(ledger, null);
        }

        @Test
        @DisplayName("hashCode — mismo ledger genera mismo hash")
        void hashCode_SameLedger_SameHash() {
            assertEquals(ledger.hashCode(), ledger.hashCode());
        }
    }
}
