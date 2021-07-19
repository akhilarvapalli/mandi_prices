import okhttp3.*;
import org.json.*;
import java.io.IOException;

public class Enum {
    public static void main(String[] args)throws IOException {
        System.out.println("test");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("language", "en")
                .add("stateName", "ANDHRA PRADESH")
                .add("apmcName","ADONI")
                .add("commodityName","CASTOR SEED")
                .add("fromDate","2021-07-12")
                .add("toDate","2021-07-12")
                .build();
        Request request = new Request.Builder()
                .header("Accept","application/json, text/javascript, */*; q=0.01")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Origin","https://enam.gov.in")
                .header("Referer","https://enam.gov.in/web/dashboard/trade-data")
                .url("https://enam.gov.in/web/Ajax_ctrl/trade_data_list")
                .post(requestBody)
                .build();
            Response response = client.newCall(request).execute();
            response.header("Content-Type","text/html");
            response.header("Server","Apache");
            response.header("Content-Length","290");
        System.out.println(response.isSuccessful());
        String str=response.body().string();
            System.out.println(str);

//        String str = "{\"data\":[{\"id\":\"6610145\",\"state\":\"ANDHRA PRADESH\",\"apmc\":\"ADONI\",\"commodity\":\"CASTOR SEED\",\"min_price\":\"4556\",\"modal_price\":\"4556\",\"max_price\":\"4556\",\"commodity_arrivals\":\"5\",\"commodity_traded\":\"3\",\"created_at\":\"2021-07-12\",\"status\":\"1\",\"Commodity_Uom\":\"Qui\"}],\"status\":200}";
        JSONObject example = new JSONObject(str);
        System.out.println("Final JSONObject: " + example);

    }
}
