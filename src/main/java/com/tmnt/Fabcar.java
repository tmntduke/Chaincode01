package com.tmnt;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "FabCar",
        info = @Info(
                title = "FabCar contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "F Carr",
                        url = "https://hyperledger.example.com")))

@Default
public final class Fabcar implements ContractInterface {
    private enum FabCarErrors {
        CAR_NOT_FOUND,
        CAR_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        createAsset(ctx, "car01", "BMW", "x3", "white", "tom");
        createAsset(ctx, "car02", "Benz", "ct60", "black", "john");
        createAsset(ctx, "car03", "Audi", "a8", "blue", "zz");
        createAsset(ctx, "car04", "lots", "mm", "yellow", "duke");
        createAsset(ctx, "car05", "hafu", "qq", "white", "ada");

    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createAsset(final Context ctx, final String id, final String brand, final String model, final String color, final String owner) {
        ChaincodeStub stub = ctx.getStub();
        if (AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_ALREADY_EXISTS.toString());
        }

        Car car = new Car(id, brand, model, color, owner);
        String carJson = GsonUtil.object2json(car);
        stub.putStringState(id, carJson);

        return carJson;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public boolean AssetExists(final Context ctx, final String id) {

        ChaincodeStub stub = ctx.getStub();
        String carJson = stub.getStringState(id);

        return (carJson != null && !carJson.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String ReadAsset(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(id);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        return assetJSON;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String UpdateAsset(final Context ctx, final String id, final String brand, final String model, final String color, final String owner) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Car newAsset = new Car(id, brand, model, color, owner);
        String json = GsonUtil.object2json(newAsset);

        stub.putStringState(id, json);

        return json;
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteAsset(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        stub.delState(id);
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String TransferAsset(final Context ctx, final String assetID, final String newOwner) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Car car = GsonUtil.json2object(assetJSON, Car.class);
        Car newCar = new Car(car.getId(), car.getBrand(), car.getModel(), car.getColor(), newOwner);

        String json = GsonUtil.object2json(newCar);

        stub.putStringState(assetID, json);

        return json;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Car> queryResults = new ArrayList<Car>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            String value = result.getStringValue();
            Car car = GsonUtil.json2object(value, Car.class);
            queryResults.add(car);
            System.out.println(car.toString());
        }

        String s = GsonUtil.object2json(queryResults);

        return s;
    }
}
