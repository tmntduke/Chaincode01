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
        name = "FabWarehouse",
        info = @Info(
                title = "FabWarehouse contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "FabWarehouse",
                        url = "https://hyperledger.example.com")))

@Default
public final class FabWarehouse implements ContractInterface {
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

        ArrayList<Resources> classifications1 = new ArrayList<>();
        ArrayList<Resources> classifications2 = new ArrayList<>();
        ArrayList<Resources> classifications3 = new ArrayList<>();
        classifications1.add(new Resources("11000001", "霍尼韦尔h80-A-1", "10001", "10000001", "1", "20210415224000"));
        classifications1.add(new Resources("11000002", "霍尼韦尔h80-A-2", "10001", "10000001", "1", "20210415224102"));
        classifications1.add(new Resources("11000004", "霍尼韦尔h80-A-4", "10001", "10000001", "1", "20210415224102"));
        classifications1.add(new Resources("11000003", "霍尼韦尔h80-A-3", "10001", "10000001", "1", "20210415224102"));
        classifications1.add(new Resources("11000005", "霍尼韦尔h80-A-5", "10001", "10000001", "1", "20210415224102"));
        classifications1.add(new Resources("11000006", "霍尼韦尔h80-A-6", "10001", "10000001", "1", "20210415224102"));
        classifications2.add(new Resources("11000007", "霍尼韦尔h80-A-7", "10002", "10000001", "1", "20210415224102"));
        classifications2.add(new Resources("11000008", "霍尼韦尔h80-A-8", "10002", "10000001", "1", "20210415224102"));
        classifications2.add(new Resources("11000009", "霍尼韦尔h80-A-9", "10002", "10000001", "1", "20210415224102"));
        classifications2.add(new Resources("11000010", "霍尼韦尔h80-A-10", "10002", "10000001", "1", "20210415224102"));
        classifications2.add(new Resources("12000001", "3M9501-A-1", "10002", "10000003", "1", "20210415224102"));
        classifications2.add(new Resources("12000002", "3M9501-A-2", "10002", "10000003", "1", "20210415224102"));
        classifications2.add(new Resources("12000003", "3M9501-A-3", "10002", "10000003", "1", "20210415224102"));
        classifications2.add(new Resources("12000004", "3M9501-A-4", "10002", "10000003", "1", "20210415224102"));
        classifications2.add(new Resources("12000005", "3M9501-A-5", "10002", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("12000006", "3M9501-A-6", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("12000007", "3M9501-A-7", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("12000008", "3M9501-A-8", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("12000009", "3M9501-A-9", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("120000010", "3M9501-A-10", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("120000011", "3M9501-A-11", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("120000012", "3M9501-A-12", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("120000013", "3M9501-A-13", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("120000014", "3M9501-A-14", "10003", "10000003", "1", "20210415224102"));
        classifications3.add(new Resources("120000015", "3M9501-A-14", "10003", "10000003", "1", "20210415224102"));





        createAsset(ctx, "10001", "北京顺义南彩工业园仓", classifications1,"北京顺义南彩工业园区彩祥西路9号","13812341234");
        createAsset(ctx, "10002", "广州白云石井石潭路大基围工业区仓", classifications2,"广州白云石井石潭路大基围工业区","18843215678");
        createAsset(ctx, "10003", "香港北区仓",classifications3 ,"香港北区文锦渡路（红桥新村）","12312344321");


    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createAsset(final Context ctx, final String id, final String name, final List<Resources> classifications
            , final String address, final String tel) {
        ChaincodeStub stub = ctx.getStub();
        if (AssetExists(ctx, id)) {
            String errorMessage = String.format("Asset %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabClassificationErrors.CLASSIFICATION_ALREADY_EXISTS.toString());
        }

        Warehouse w = new Warehouse(id, name, classifications, address, tel);

        String claJson = GsonUtil.object2json(w);
        stub.putStringState(id, claJson);

        return claJson;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public boolean AssetExists(final Context ctx, final String id) {

        ChaincodeStub stub = ctx.getStub();
        String claJson = stub.getStringState(id);

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

        List<Warehouse> queryResults = new ArrayList<Warehouse>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            String value = result.getStringValue();
            Warehouse warehouse = GsonUtil.json2object(value, Warehouse.class);
            queryResults.add(warehouse);
            System.out.println(warehouse.toString());
        }

        String s = GsonUtil.object2json(queryResults);

        return s;
    }
}



