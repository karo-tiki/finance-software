package com.finance.project.domainLayer.domainEntities.aggregates.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Name VO — Domain Tests")
class NameTest {

    @Test
    void createName_valid_returnsName() {
        Name name = Name.createName("Ana");
        assertNotNull(name);
        assertEquals("Ana", name.getName());
    }

    @Test
    void createName_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> Name.createName(null));
    }

    @Test
    void createName_empty_throws() {
        assertThrows(IllegalArgumentException.class, () -> Name.createName(""));
    }

    @Test
    void equals_caseInsensitive_returnsTrue() {
        assertEquals(Name.createName("Ana"), Name.createName("ANA"));
    }

    @Test
    void equals_differentValues_returnsFalse() {
        assertNotEquals(Name.createName("Ana"), Name.createName("Beto"));
    }

    @Test
    void equals_null_returnsFalse() {
        assertNotEquals(null, Name.createName("Ana"));
    }
}
