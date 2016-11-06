package topikuure.quantumdice;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Topi on 06/11/2016.
 */
public class JSONParser {

    public interface JSONParserCallbackInterface {
        void onCallBack(JSONObject json);
    }

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public void getJSONFromUrl(String url, JSONParserCallbackInterface callbackInterface) {
        new GetJSONFromUrlTask(callbackInterface).execute(url);
    }

    private class GetJSONFromUrlTask extends AsyncTask<String, Void, JSONObject> {

        private JSONParserCallbackInterface callbackInterface;

        public GetJSONFromUrlTask(JSONParserCallbackInterface callbackInterface) {
            this.callbackInterface = callbackInterface;
        }

        @Override
        protected JSONObject doInBackground(String[] params) {//TODO implementoi
/*
            final String url = params[0];

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("IntegerStack Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            return jObj;
*/
            //Testi JSON local String->
            final String onSuccess = "{\"type\":\"uint8\",\"length\":10,\"data\":[48,223,28,238,228,72,151,179,168,2],\"success\":true}";
            final String onFailure = "{\"success\":false}";

            try {
                jObj = new JSONObject(onSuccess);
            }
            catch(JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jObj;
            //<-Testi JSON local String
        }

        protected void onPostExecute(JSONObject result) {
            callbackInterface.onCallBack(result);
        }
    }
}