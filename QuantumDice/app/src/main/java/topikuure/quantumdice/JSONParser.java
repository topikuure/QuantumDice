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

    public interface CallbackInterface {

        void onParsed(JSONObject json);
    }

    private static InputStream inputStream = null;
    private static JSONObject jsonObject = null;
    private static String jsonString = "";

    public void getJSONFromUrl(String url, CallbackInterface callbackInterface) {
        new GetJSONFromUrlTask(callbackInterface).execute(url);
    }

    private class GetJSONFromUrlTask extends AsyncTask<String, Void, JSONObject> {

        private CallbackInterface callbackInterface;

        public GetJSONFromUrlTask(CallbackInterface callbackInterface) {
            this.callbackInterface = callbackInterface;
        }

        //Palauttaa null jos mikään menee pieleen
        @Override
        protected JSONObject doInBackground(String[] params) {
            final String urlString = params[0];
            URL url;

            //Haetaan netistä json-data inputStreamiin
            try {
                url = new URL(urlString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
                inputStream = httpsURLConnection.getInputStream();
            }
            catch(Exception exception) {
                exception.printStackTrace();
                return null;
            }

            //inputStream -> String
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

            //String -> JSONObject
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException exception) {
                exception.printStackTrace();
                return null;
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            callbackInterface.onParsed(result);
        }
    }
}