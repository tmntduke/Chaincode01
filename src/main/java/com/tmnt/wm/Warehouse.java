package com.tmnt.wm;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;
import java.util.Objects;

@DataType
public class Warehouse {
    @Property()
    private final String warehouseId;
    @Property()
    private final String name;
    @Property()
    private final List<Resources> classifications;
    @Property
    private final String address;
    @Property
    private final String tel;

    public Warehouse(final String warehouseId, final String name, final List<Resources> classifications
            , final String address, final String tel) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.classifications = classifications;
        this.address = address;
        this.tel = tel;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getName() {
        return name;
    }

    public List<Resources> getClassifications() {
        return classifications;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Warehouse warehouse = (Warehouse) o;
        return getWarehouseId().equals(warehouse.getWarehouseId()) &&
                getName().equals(warehouse.getName()) &&
                getClassifications().equals(warehouse.getClassifications());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWarehouseId(), getName(), getClassifications());
    }
}
