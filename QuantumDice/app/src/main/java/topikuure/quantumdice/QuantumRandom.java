package topikuure.quantumdice;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Topi on 06/11/2016.
 *
 * QuantumRandom-luokka käyttää QRNG@ANU JSON API -rajapintaa
 * https://qrng.anu.edu.au/API/api-demo.php
 *
 * QuantumStack hakee kahteen pinoon random-lukuja JSON-APIn avulla.
 * Kun käytössä oleva pino tyhjenee, vaihdetaan toinen pino sen paikalle,
 * ja haetaan tyhjään pinoon uudet luvut omassa säikeessä.
 */
public class QuantumRandom implements JSONParser.CallbackInterface {

    private class IntegerStack {

        private int topIndex = -1;
        private int buffer[];

        public int id;

        private IntegerStack(int id, int size) {
            this.id = id;
            buffer = new int[size];
        }

        //Ei tarkista onko pino täynnä
        public void push(int value) {
            buffer[++topIndex] = value;
        }

        //Palauttaa alimman luvun jos pino on tyhjä
        public int pop() {
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
    private static final Object lock = new Object();

    private boolean isInitialized = false;
    private IntegerStack stack1 = new IntegerStack(1, STACK_SIZE);
    private IntegerStack stack2 = new IntegerStack(2, STACK_SIZE);
    private IntegerStack currentStack = stack1;

    public QuantumRandom() {
        Log.d("QuantumRandom", "constructor");

        final String url = "https://qrng.anu.edu.au/API/jsonI.php?length=" + Integer.toString(STACK_SIZE * 2) + "&type=uint8";

        JSONParser jsonParser = new JSONParser();
        jsonParser.getJSONFromUrl(url, new JSONParser.CallbackInterface() {
            @Override
            public void onParsed(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < STACK_SIZE; ++i) {
                            stack1.push(data.getInt(i));
                        }
                        for (int i = STACK_SIZE; i < (STACK_SIZE * 2); ++i) {
                            stack2.push(data.getInt(i));
                        }
                    } else {
                        Log.e("QuantumRandom", "JSON call failed");
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                synchronized (lock) {
                    isInitialized = true;
                }
            }
        });
    }

    public boolean isInitialized() {
        synchronized (lock) {
            return isInitialized;
        }
    }

    public int getRandomNumber(int min, int max) throws Exception {
        if(currentStack.getSize() <= 0) {
            swapStacks();
            fillBackStack();

            if(currentStack.getSize() <= 0) throw new Exception("Empty stacks");
        }
        return (currentStack.pop() % (max - min + 1)) + min;
    }

    private void fillBackStack() {
        Log.d("QuantumRandom", "fillBackStack");

        String url = "https://qrng.anu.edu.au/API/jsonI.php?length=" + arrayLength + "&type=uint8";

        JSONParser jsonParser = new JSONParser();
        jsonParser.getJSONFromUrl(url, this);
    }

    private void swapStacks() {
        Log.d("QuantumRandom", "swapStacks");

        if(currentStack.id == stack1.id) currentStack = stack2;
        else if(currentStack.id == stack2.id) currentStack = stack1;
    }

    @Override
    public void onParsed(JSONObject jsonObject) {
        Log.d("QuantumRandom", "onParsed");

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
            else Log.e("QuantumRandom", "JSON call failed");
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}