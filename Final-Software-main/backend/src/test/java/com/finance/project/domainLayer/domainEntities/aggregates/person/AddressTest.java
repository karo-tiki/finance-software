package com.finance.project.domainLayer.domainEntities.aggregates.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Address VO — Domain Tests")
class AddressTest {

    private Address baseAddress() {
        return Address.createAddress("Av. Sol", "123", "15000", "Lima", "Peru");
    }

    @Test
    void createAddress_valid_returnsAddress() {
        Address a = baseAddress();
        assertNotNull(a);
        assertEquals("Av. Sol", a.getStreet());
        assertEquals("123", a.getDoorNumber());
        assertEquals("15000", a.getPostCode());
        assertEquals("Lima", a.getCity());
        assertEquals("Peru", a.getCountry());
    }

    @Test
    void createAddress_streetNull_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Address.createAddress(null, "123", "15000", "Lima", "Peru"));
    }

    @Test
    void createAddress_doorNumberNull_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Address.createAddress("Av. Sol", null, "15000", "Lima", "Peru"));
    }

    @Test
    void createAddress_postCodeNull_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Address.createAddress("Av. Sol", "123", null, "Lima", "Peru"));
    }

    @Test
    void createAddress_cityNull_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Address.createAddress("Av. Sol", "123", "15000", null, "Peru"));
    }

    @Test
    void createAddress_countryNull_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Address.createAddress("Av. Sol", "123", "15000", "Lima", null));
    }

    @Test
    void equals_sameValues_returnsTrue() {
        assertEquals(baseAddress(), baseAddress());
    }

    @Test
    void equals_differentStreet_returnsFalse() {
        Address other = Address.createAddress("Otra", "123", "15000", "Lima", "Peru");
        assertNotEquals(baseAddress(), other);
    }

    @Test
    void copyConstructor_producesEqualAddress() {
        Address original = baseAddress();
        Address copy = new Address(original);
        assertEquals(original, copy);
    }

    @Test
    void hashCode_consistentWithEquals() {
        assertEquals(baseAddress().hashCode(), baseAddress().hashCode());
    }
}
