package com.tmnt.wm;

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
        name = "FabClassification",
        info = @Info(
                title = "FabClassification contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "FarClassification",
                        url = "https://hyperledger.example.com")))

@Default
public final class FabClassification implements ContractInterface {
    private enum FabClassificationErrors {
        CLASSIFICATION_NOT_FOUND,
        CLASSIFICATION_ALREADY_EXISTS
    }


    /**
     * 待定
     *
     * @param ctx
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        createAsset(ctx, "10000001", "霍尼韦尔h80", "100","69","10/包");
        createAsset(ctx, "10000002", "霍尼韦尔h80", "100","138","20/包");
        createAsset(ctx, "10000003", "3M9501", "100","80","15/包");
        createAsset(ctx, "10000004", "3M9001", "100","72","10/包");
        createAsset(ctx, "10000005", "3M9001", "100","75","15/包");
        createAsset(ctx, "10000006", "3M防护服", "100","1500","10/箱");

    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createAsset(final Context ctx, final String classificationId, final String name
            , final String count, final String price, final String detail) {
        ChaincodeStub stub = ctx.getStub();
        if (AssetExists(ctx, classificationId)) {
            String errorMessage = String.format("Asset %s already exists", classificationId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabClassificationErrors.CLASSIFICATION_ALREADY_EXISTS.toString());
        }

        Classification classification = new Classification(classificationId, name, count, price, detail);

        String claJson = GsonUtil.object2json(classification);
        stub.putStringState(classificationId, claJson);

        return claJson;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public boolean AssetExists(final Context ctx, final String classificationId) {

        ChaincodeStub stub = ctx.getStub();
        String claJson = stub.getStringState(classificationId);

        return (claJson != null && !claJson.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String ReadAsset(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(id);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabClassificationErrors.CLASSIFICATION_NOT_FOUND.toString());
        }

        return assetJSON;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String ReadAssetByName(final Context ctx, final String name) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(name);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", name);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabClassificationErrors.CLASSIFICATION_NOT_FOUND.toString());
        }

        return assetJSON;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String UpdateAsset(final Context ctx, final String id, final String name
            , final String count, final String price, final String detail) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabClassificationErrors.CLASSIFICATION_NOT_FOUND.toString());
        }

        Classification classification = new Classification(id, name, count, price, detail);
        String json = GsonUtil.object2json(classification);

        stub.putStringState(id, json);

        return json;
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteAsset(final Context ctx, final String id) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabClassificationErrors.CLASSIFICATION_NOT_FOUND.toString());
        }

        stub.delState(id);
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Classification> queryResults = new ArrayList<Classification>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            String value = result.getStringValue();
            Classification car = GsonUtil.json2object(value, Classification.class);
            queryResults.add(car);
            System.out.println(car.toString());
        }

        String s = GsonUtil.object2json(queryResults);

        return s;
    }
}


