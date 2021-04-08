package com.tmnt;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Car {

    @Property()
    private final String id;
    @Property()
    private final String brand;
    @Property()
    private final String model;
    @Property()
    private final String color;
    @Property()
    private final String owner;


    public Car(final String id, final String brand, final String model, final String color, final String owner) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Car other = (Car) obj;

        return Objects.deepEquals(new String[]{getId(), getBrand(), getModel(), getColor(), getOwner()},
                new String[]{other.getId(), other.getBrand(), other.getModel(), other.getColor(), other.getOwner()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrand(), getModel(), getColor(), getOwner());
    }

}
