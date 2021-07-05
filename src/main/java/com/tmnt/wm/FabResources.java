package com.tmnt.wm;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "FabResources",
        info = @Info(
                title = "FabResources contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "FabResources",
                        url = "https://hyperledger.example.com")))

@Default
public final class FabResources implements ContractInterface {
    private enum FabCarErrors {
        CAR_NOT_FOUND,
        CAR_ALREADY_EXISTS
    }

    /**
     * 待定
     *
     * @param ctx
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        createAsset(ctx, "11000001", "霍尼韦尔h80-A-1", "10001", "10000001", "1", "20210415224000");
        createAsset(ctx, "11000002", "霍尼韦尔h80-A-2", "10001", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000004", "霍尼韦尔h80-A-4", "10001", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000003", "霍尼韦尔h80-A-3", "10001", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000005", "霍尼韦尔h80-A-5", "10001", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000006", "霍尼韦尔h80-A-6", "10001", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000007", "霍尼韦尔h80-A-7", "10002", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000008", "霍尼韦尔h80-A-8", "10002", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000009", "霍尼韦尔h80-A-9", "10002", "10000001", "1", "20210415224102");
        createAsset(ctx, "11000010", "霍尼韦尔h80-A-10", "10002", "10000001", "1", "20210415224102");
        createAsset(ctx, "12000001", "3M9501-A-1", "10002", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000002", "3M9501-A-2", "10002", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000003", "3M9501-A-3", "10001", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000004", "3M9501-A-4", "10003", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000005", "3M9501-A-5", "10001", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000006", "3M9501-A-6", "10002", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000007", "3M9501-A-7", "10003", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000008", "3M9501-A-8", "10002", "10000003", "1", "20210415224102");
        createAsset(ctx, "12000009", "3M9501-A-9", "10001", "10000003", "1", "20210415224102");
        createAsset(ctx, "120000010", "3M9501-A-10", "10002", "10000003", "1", "20210415224102");
        createAsset(ctx, "120000011", "3M9501-A-11", "10003", "10000003", "1", "20210415224102");
        createAsset(ctx, "120000012", "3M9501-A-12", "10003", "10000003", "1", "20210415224102");
        createAsset(ctx, "120000013", "3M9501-A-13", "10003", "10000003", "1", "20210415224102");
        createAsset(ctx, "120000014", "3M9501-A-14", "10003", "10000003", "1", "20210415224102");
        createAsset(ctx, "120000015", "3M9501-A-14", "10003", "10000003", "1", "20210415224102");

    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createAsset(final Context ctx, final String id
            , final String name
            , final String warehouseId
            , final String classificationId
            , final String state
            , final String timestamp) {
        ChaincodeStub stub = ctx.getStub();
        if (AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_ALREADY_EXISTS.toString());
        }


        Resources resources = new Resources(id, name, warehouseId, classificationId, state, timestamp);

        String carJson = GsonUtil.object2json(resources);
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
    public String UpdateAsset(final Context ctx, final String id, final String name, final String warehouseId, final String classificationId, final String state, final String timestamp) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Resources resources = new Resources(id, name, warehouseId, classificationId, state, timestamp);
        String json = GsonUtil.object2json(resources);

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
    public String TransferAsset(final Context ctx, final String assetID, final String newWare, String timestamp) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Resources resources = GsonUtil.json2object(assetJSON, Resources.class);

        Resources newResources = new Resources(resources.getResourceId(), resources.getName(), newWare, resources.getClassificationId(), resources.getState(), timestamp);

        String json = GsonUtil.object2json(newResources);

        stub.putStringState(assetID, json);

        return json;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Resources> queryResults = new ArrayList<Resources>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            String value = result.getStringValue();
            Resources resources = GsonUtil.json2object(value, Resources.class);
            queryResults.add(resources);
            System.out.println(resources.toString());
        }

        String s = GsonUtil.object2json(queryResults);

        return s;
    }
}

