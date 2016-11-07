package topikuure.quantumdice;

import android.app.Application;

/**
 * Created by Topi on 07/11/2016.
 */
public class QuantumDiceApplication extends Application {

    public QuantumRandom quantumRandom;

    @Override
    public void onCreate() {
        super.onCreate();
        quantumRandom = new QuantumRandom();
    }
}
