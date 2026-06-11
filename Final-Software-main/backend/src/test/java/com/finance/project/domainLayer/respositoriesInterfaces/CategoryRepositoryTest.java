package com.finance.project.repositories;

import com.finance.project.domainLayer.domainEntities.aggregates.category.Category;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;
import com.finance.project.domainLayer.repositoriesInterfaces.ICategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryRepositoryTest {

    @Test
    void shouldSaveCategory() {

        // Arrange
        ICategoryRepository repository = mock(ICategoryRepository.class);

        PersonID personID = PersonID.createPersonID("test@gmail.com");
        Category category = Category.createCategory("Food", personID);

        when(repository.save(category)).thenReturn(category);

        // Act
        Category savedCategory = repository.save(category);

        // Assert
        assertNotNull(savedCategory);
        assertEquals(category, savedCategory);

        verify(repository, times(1)).save(category);
    }

    @Test
    void shouldFindCategoryById() {

        // Arrange
        ICategoryRepository repository = mock(ICategoryRepository.class);

        PersonID personID = PersonID.createPersonID("test@gmail.com");
        Category category = Category.createCategory("Food", personID);

        when(repository.findById("test@gmail.com", "Food"))
                .thenReturn(Optional.of(category));

        // Act
        Optional<Category> result =
                repository.findById("test@gmail.com", "Food");

        // Assert
        assertTrue(result.isPresent());

        verify(repository, times(1))
                .findById("test@gmail.com", "Food");
    }

    @Test
    void shouldReturnTrueWhenCategoryExists() {

        // Arrange
        ICategoryRepository repository = mock(ICategoryRepository.class);

        PersonID personID = PersonID.createPersonID("test@gmail.com");
        Category category = Category.createCategory("Food", personID);

        when(repository.existsById(category.getCategoryID()))
                .thenReturn(true);

        // Act
        boolean result =
                repository.existsById(category.getCategoryID());

        // Assert
        assertTrue(result);

        verify(repository, times(1))
                .existsById(category.getCategoryID());
    }

    @Test
    void shouldDeleteCategory() {

        // Arrange
        ICategoryRepository repository = mock(ICategoryRepository.class);

        PersonID personID = PersonID.createPersonID("test@gmail.com");
        Category category = Category.createCategory("Food", personID);

        // Act
        repository.delete(category.getCategoryID());

        // Assert
        verify(repository, times(1))
                .delete(category.getCategoryID());
    }
}