package com.finance.project.domainLayer.domainEntities.aggregates.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Birthplace VO — Domain Tests")
class BirthplaceTest {

    @Test
    void createBirthplace_valid_returnsVO() {
        Birthplace bp = Birthplace.createBirthplace("Lima");
        assertNotNull(bp);
        assertEquals("Lima", bp.getBirthplace());
    }

    @Test
    void createBirthplace_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> Birthplace.createBirthplace(null));
    }

    @Test
    void equals_caseInsensitive_returnsTrue() {
        assertEquals(Birthplace.createBirthplace("Lima"), Birthplace.createBirthplace("LIMA"));
    }

    @Test
    void equals_differentCities_returnsFalse() {
        assertNotEquals(Birthplace.createBirthplace("Lima"), Birthplace.createBirthplace("Cusco"));
    }
}
