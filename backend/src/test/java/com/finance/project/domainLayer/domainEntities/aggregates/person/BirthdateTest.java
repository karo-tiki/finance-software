package com.finance.project.domainLayer.domainEntities.aggregates.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Birthdate VO — Domain Tests")
class BirthdateTest {

    @Test
    void createBirthdate_valid_returnsVO() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        Birthdate birthdate = Birthdate.createBirthdate(date);

        assertNotNull(birthdate);
        assertEquals(date, birthdate.getBirthdate());
    }

    @Test
    void createBirthdate_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> Birthdate.createBirthdate(null));
    }

    @Test
    void equals_sameDate_returnsTrue() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        assertEquals(Birthdate.createBirthdate(date), Birthdate.createBirthdate(date));
    }

    @Test
    void equals_differentDate_returnsFalse() {
        Birthdate a = Birthdate.createBirthdate(LocalDate.of(2000, 1, 1));
        Birthdate b = Birthdate.createBirthdate(LocalDate.of(2001, 1, 1));
        assertNotEquals(a, b);
    }

    @Test
    void hashCode_isConsistentWithEquals() {
        LocalDate date = LocalDate.of(1999, 6, 15);
        assertEquals(
                Birthdate.createBirthdate(date).hashCode(),
                Birthdate.createBirthdate(date).hashCode());
    }
}
