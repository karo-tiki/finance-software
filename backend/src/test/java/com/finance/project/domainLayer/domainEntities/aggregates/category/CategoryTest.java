package com.finance.project.domainLayer.domainEntities.aggregates.category;

import com.finance.project.domainLayer.domainEntities.vosShared.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category Aggregate — Domain Tests")
class CategoryTest {

    private PersonID personID;
    private Category category;

    @BeforeEach
    void setUp() {
        personID = PersonID.createPersonID("user@email.com");
        category = Category.createCategory("Comida", personID);
    }

    @AfterEach
    void tearDown() {
        personID = null;
        category = null;
    }

    @Nested
    @DisplayName("Suite 1 — Creación de Categoría")
    class CreacionCategoria {

        @Test
        @DisplayName("Crear categoría — retorna no nulo")
        void createCategory_NotNull() {
            assertNotNull(category);
        }

        @Test
        @DisplayName("Crear categoría — CategoryID no nulo")
        void createCategory_CategoryIDNotNull() {
            assertNotNull(category.getCategoryID());
        }

        @Test
        @DisplayName("Crear categoría — denomination correcta")
        void createCategory_DenominationCorrect() {
            assertNotNull(category.getCategoryID().getDenomination());
        }

        @Test
        @DisplayName("Crear categoría — denomination nula lanza excepción")
        void createCategory_NullDenomination_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Category.createCategory(null, personID)
            );
        }

        @Test
        @DisplayName("Crear categoría — ownerID nulo lanza excepción")
        void createCategory_NullOwnerID_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () ->
                Category.createCategory("Comida", null)
            );
        }

        @Test
        @DisplayName("Crear categoría con GroupID — retorna no nulo")
        void createCategory_WithGroupID_NotNull() {
            GroupID groupID = GroupID.createGroupID("Amigos");
            Category cat = Category.createCategory("Transporte", groupID);
            assertNotNull(cat);
        }
    }

    @Nested
    @DisplayName("Suite 2 — Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("equals — misma denominación y owner retorna true")
        void equals_SameData_ReturnsTrue() {
            Category category2 = Category.createCategory("Comida", personID);
            assertEquals(category, category2);
        }

        @Test
        @DisplayName("equals — diferente denominación retorna false")
        void equals_DifferentDenomination_ReturnsFalse() {
            Category category2 = Category.createCategory("Transporte", personID);
            assertNotEquals(category, category2);
        }

        @Test
        @DisplayName("equals — mismo objeto retorna true")
        void equals_SameObject_ReturnsTrue() {
            assertEquals(category, category);
        }

        @Test
        @DisplayName("equals — comparar con null retorna false")
        void equals_Null_ReturnsFalse() {
            assertNotEquals(category, null);
        }

        @Test
        @DisplayName("hashCode — misma categoría genera mismo hash")
        void hashCode_SameCategory_SameHash() {
            Category category2 = Category.createCategory("Comida", personID);
            assertEquals(category.hashCode(), category2.hashCode());
        }

        @Test
        @DisplayName("hashCode — diferente categoría genera diferente hash")
        void hashCode_DifferentCategory_DifferentHash() {
            Category category2 = Category.createCategory("Transporte", personID);
            assertNotEquals(category.hashCode(), category2.hashCode());
        }
    }
}
