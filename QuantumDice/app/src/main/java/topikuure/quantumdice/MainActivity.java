package topikuure.quantumdice;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        setContentView(new MainView(this, ((QuantumDiceApplication)getApplication()).quantumRandom,
            screenSize.x, screenSize.y));
    }
}