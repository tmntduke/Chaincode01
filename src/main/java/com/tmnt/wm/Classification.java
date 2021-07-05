package com.tmnt.wm;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType
public class Classification {
    @Property()
    private final String classificationId;
    @Property()
    private final String name;
    @Property
    private final String count;
    @Property
    private final String price;
    @Property
    private final String specification;


    public Classification(final String classificationId, final String name, final String count
            , final String price, final String specification) {
        this.classificationId = classificationId;
        this.name = name;
        this.count = count;
        this.price = price;
        this.specification = specification;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }

    public String getSpecification() {
        return specification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Classification that = (Classification) o;
        return getClassificationId().equals(that.getClassificationId()) &&
                getName().equals(that.getName()) &&
                getCount().equals(that.getCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClassificationId(), getName(), getCount());
    }
}
