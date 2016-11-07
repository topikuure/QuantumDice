package topikuure.quantumdice;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Topi on 06/11/2016.
 *
 * QuantumRandom-luokka käyttää QRNG@ANU JSON API -rajapintaa
 * https://qrng.anu.edu.au/API/api-demo.php
 */
public class QuantumRandom implements JSONParser.JSONParserCallbackInterface {

    private class IntegerStack {

        public int id;

        private int topIndex = -1;
        private int buffer[];

        private IntegerStack(int id, int size) {
            this.id = id;
            buffer = new int[size];
        }

        public void push(int value) {
            //Ei tarkista onko topIndex liian suuri
            buffer[++topIndex] = value;
        }

        public int pop() {
            //Palauttaa alimman luvun jos pino on tyhjä
            if(topIndex < 0) topIndex = 0;
            return buffer[topIndex--];
        }

        public int getSize() {
            return topIndex + 1;
        }

        public void clear() {
            topIndex = -1;
        }
    }

    private static final int STACK_SIZE = 10;
    private static final String arrayLength = Integer.toString(STACK_SIZE);

    private IntegerStack stack1 = new IntegerStack(1, STACK_SIZE);
    private IntegerStack stack2 = new IntegerStack(2, STACK_SIZE);
    private IntegerStack currentStack = stack1;

    public QuantumRandom() {
        fillBackStack();
        swapStacks();
        fillBackStack();
    }

    public int getRandomNumber(int min, int max) {
        if(currentStack.getSize() <= 0) {
            swapStacks();
            fillBackStack();
        }

        //Testipalautus
        //return currentStack.pop();

        //Oikea palautus
        return (currentStack.pop() % (max - min + 1)) + min;
    }

    private boolean fillBackStack() {
        Log.d("QuantumRandom", "fillBackStack");

        String url = "https://qrng.anu.edu.au/API/jsonI.php?length=" + arrayLength + "&type=uint8";

        JSONParser jsonParser = new JSONParser();
        jsonParser.getJSONFromUrl(url, this);

        return false;
    }

    public void swapStacks() {
        Log.d("QuantumRandom", "swapStacks");

        if(currentStack.id == stack1.id) currentStack = stack2;
        else if(currentStack.id == stack2.id) currentStack = stack1;
    }

    @Override
    public void onCallBack(JSONObject jsonObject) {
        Log.d("QuantumRandom", "onCallBack");

        IntegerStack backStack;

        if(currentStack.id == stack1.id) backStack = stack2;
        else if(currentStack.id == stack2.id) backStack = stack1;
        else {
            Log.e("QuantumRandom", "Invalid IntegerStack.id");
            return;
        }

        try {
            if(jsonObject.getString("success").equals("true")) {
                backStack.clear();
                JSONArray data = jsonObject.getJSONArray("data");

                for(int i = 0; i < STACK_SIZE; ++i) {
                    backStack.push(data.getInt(i));
                }
            }
            else Log.e("QuantumRandom", "JSON call failed");//TODO jos serveri kaatunut tms., käytä Javan pseudo random generaattoria
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}