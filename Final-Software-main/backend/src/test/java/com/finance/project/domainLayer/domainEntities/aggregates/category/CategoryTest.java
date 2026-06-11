package com.finance.project.domainLayer.domainEntities.aggregates.category;
import com.finance.project.domainLayer.domainEntities.vosShared.GroupID;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("Should create category successfully for person")
    void shouldCreateCategoryForPerson() {

        // Arrange
        String denomination = "Food";
        PersonID personID = PersonID.createPersonID("test@gmail.com");

        // Act
        Category category = Category.createCategory(denomination, personID);

        // Assert
        assertNotNull(category);
        assertNotNull(category.getCategoryID());
    }

    @Test
    @DisplayName("Should create category successfully for group")
    void shouldCreateCategoryForGroup() {

        // Arrange
        String denomination = "Health";
        GroupID groupID = GroupID.createGroupID("Team");

        // Act
        Category category = Category.createCategory(denomination, groupID);

        // Assert
        assertNotNull(category);
        assertNotNull(category.getCategoryID());
    }

    @Test
    @DisplayName("Should throw exception when denomination is null")
    void shouldThrowExceptionWhenDenominationIsNull() {

        // Arrange
        PersonID personID = PersonID.createPersonID("test@gmail.com");

        // Act + Assert
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Category.createCategory(null, personID)
        );

        assertEquals(
                "Category not created due to the fact that the denomination parameter hasn't a valid argument",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Should throw exception when denomination is empty")
    void shouldThrowExceptionWhenDenominationIsEmpty() {

        // Arrange
        GroupID groupID = GroupID.createGroupID("Team");

        // Act + Assert
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Category.createCategory("", groupID)
        );

        assertEquals(
                "Category not created due to the fact that the denomination parameter hasn't a valid argument",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Should throw exception when ownerID is null")
    void shouldThrowExceptionWhenOwnerIDIsNull() {

        // Act + Assert
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Category.createCategory("Food", null)
        );

        assertEquals(
                "Category not created due to the fact that the ownerID parameter hasn't a valid argument",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Should return true when categories are equal")
    void shouldReturnTrueWhenCategoriesAreEqual() {

        // Arrange
        PersonID personID = PersonID.createPersonID("test@gmail.com");

        Category category1 = Category.createCategory("Food", personID);
        Category category2 = Category.createCategory("Food", personID);

        // Act
        boolean result = category1.equals(category2);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when categories are different")
    void shouldReturnFalseWhenCategoriesAreDifferent() {

        // Arrange
        PersonID personID = PersonID.createPersonID("test@gmail.com");

        Category category1 = Category.createCategory("Food", personID);
        Category category2 = Category.createCategory("Health", personID);

        // Act
        boolean result = category1.equals(category2);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should have same hashCode for equal objects")
    void shouldHaveSameHashCodeForEqualObjects() {

        // Arrange
        PersonID personID = PersonID.createPersonID("test@gmail.com");

        Category category1 = Category.createCategory("Food", personID);
        Category category2 = Category.createCategory("Food", personID);

        // Assert
        assertEquals(category1.hashCode(), category2.hashCode());
    }
}