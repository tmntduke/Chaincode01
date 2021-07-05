package com.tmnt.wm;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

/**
 * @author amuxiaowu
 */
@DataType
public final class Resources {

    @Property()
    private final String resourceId;
    @Property()
    private final String name;
    @Property()
    private final String warehouseId;
    @Property()
    private final String classificationId;
    @Property()
    private final String state;
    @Property
    private final String timestamp;

    public Resources(final String resourceId,
                     final String name,
                     final String warehouseId,
                     final String classificationId,
                     final String state,
                     final String timestamp) {
        this.resourceId = resourceId;
        this.name = name;
        this.warehouseId = warehouseId;
        this.classificationId = classificationId;
        this.state = state;
        this.timestamp = timestamp;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public String getState() {
        return state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resources resources = (Resources) o;
        return getResourceId().equals(resources.getResourceId()) &&
                getName().equals(resources.getName()) &&
                getWarehouseId().equals(resources.getWarehouseId()) &&
                getClassificationId().equals(resources.getClassificationId()) &&
                getState().equals(resources.getState()) &&
                getTimestamp().equals(resources.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResourceId(), getName(), getWarehouseId(), getClassificationId(), getState(), getTimestamp());
    }
}
