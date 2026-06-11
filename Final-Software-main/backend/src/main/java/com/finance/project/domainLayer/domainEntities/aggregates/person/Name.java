package com.finance.project.domainLayer.domainEntities.aggregates.person;

import com.finance.project.domainLayer.entitiesInterfaces.ValueObject;
import lombok.Getter;

import java.util.Objects;


@Getter
public class Name implements ValueObject {

    private final String name;

    // Constructor

    public static Name createName(String name){
        return new Name(name);
    }

    private Name(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name not created due to the fact that the name parameter hasn't a valid argument");
        } else {
            this.name = name;
        }
    }

    // Getters


    // Equals & hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name.toUpperCase(), name1.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
