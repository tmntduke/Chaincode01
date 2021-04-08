package com.tmnt;

import com.google.gson.GsonBuilder;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Default;

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
public final class GsonUtil {

    private GsonUtil() {
    }

    public static <T> T json2object(final String s, final Class<T> clz) {
        T o = new GsonBuilder().disableHtmlEscaping()
                .create()
                .fromJson(s, clz);

        return o;
    }

    public static String object2json(final Object o) {
        String s = new GsonBuilder().disableHtmlEscaping().create()
                .toJson(o);
        return s;
    }
}
