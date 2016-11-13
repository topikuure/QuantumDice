package topikuure.quantumdice;

import android.app.Application;

/**
 * Created by Topi on 07/11/2016.
 */
public class QuantumDiceApplication extends Application {

    //quantumRandom-instanssi halutaan pitää elossa mahdollisimman kauan koska sen alustus netin
    //kautta kestää kauan
    public QuantumRandom quantumRandom;

    @Override
    public void onCreate() {
        super.onCreate();
        quantumRandom = new QuantumRandom();
    }
}