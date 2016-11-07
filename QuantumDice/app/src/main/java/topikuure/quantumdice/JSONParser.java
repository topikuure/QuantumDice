package topikuure.quantumdice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Topi on 06/11/2016.
 */
public class JSONParser {

    public interface JSONParserCallbackInterface {
        void onCallBack(JSONObject json);
    }

    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String jsonString = "";

    public void getJSONFromUrl(String url, JSONParserCallbackInterface callbackInterface) {
        new GetJSONFromUrlTask(callbackInterface).execute(url);
    }

    private class GetJSONFromUrlTask extends AsyncTask<String, Void, JSONObject> {

        private JSONParserCallbackInterface callbackInterface;

        public GetJSONFromUrlTask(JSONParserCallbackInterface callbackInterface) {
            this.callbackInterface = callbackInterface;
        }

        @Override
        protected JSONObject doInBackground(String[] params) {
            final String urlString = params[0];

            //Haetaan data netistä
            URL url;

            try {
                url = new URL(urlString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
                inputStream = httpsURLConnection.getInputStream();
            }
            catch(Exception exception) {
                exception.printStackTrace();
                return null;
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
                jsonString = stringBuilder.toString();
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }

            // try parse the string to a JSON object
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException exception) {
                exception.printStackTrace();
                return null;
            }

            return jsonObject;

/*
            //Testi JSON local String->
            final String onSuccess = "{\"type\":\"uint8\",\"length\":10,\"data\":[48,223,28,238,228,72,151,179,168,2],\"success\":true}";
            final String onFailure = "{\"success\":false}";

            try {
                jsonObject = new JSONObject(onSuccess);
            }
            catch(JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jsonObject;
            //<-Testi JSON local String
*/
        }

        protected void onPostExecute(JSONObject result) {
            callbackInterface.onCallBack(result);
        }
    }
}