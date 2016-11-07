package topikuure.quantumdice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.View;

/**
 * Created by Topi on 06/11/2016.
 */
public class MainView extends View implements View.OnClickListener {

    private class QuantumRandomInitializationTask extends AsyncTask<Void, Void, Void> {

        private QuantumRandom quantumRandom;
        private MainView mainView;

        public QuantumRandomInitializationTask(QuantumRandom quantumRandom, MainView mainView) {
            this.quantumRandom = quantumRandom;
            this.mainView = mainView;
        }

        @Override
        protected Void doInBackground(Void[] params) {
            while(!quantumRandom.isInitialized()) {}
            return null;
        }

        protected void onPostExecute(Void result) {
            mainView.quantumRandomInitialized();
        }
    }

    private Die die;
    private Vibrator vibrator;
    private boolean vibratorIsOn = false;

    public MainView(Context context, QuantumRandom quantumRandom, int screenWidth, int screenHeight) {
        super(context);

        setOnClickListener(this);

        float dieSize;

        if(screenWidth <= screenHeight) dieSize = (float)(screenWidth - 80);
        else dieSize = (float)(screenHeight - 80);

        die = new Die(quantumRandom, 40f, 40f, dieSize);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


        if(!quantumRandom.isInitialized()) new QuantumRandomInitializationTask(quantumRandom, this).execute();
        else quantumRandomInitialized();
    }

    @Override
    public void onClick(View view) {
        if(vibratorIsOn) vibrator.vibrate(80);
        die.roll();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        die.draw(canvas);
    }

    void quantumRandomInitialized() {
        vibratorIsOn = true;
        die.roll();
        invalidate();
    }
}