package topikuure.quantumdice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;

/**
 * Created by Topi on 06/11/2016.
 */
public class MainView extends View implements View.OnClickListener {

    private class QuantumRandomInitializationTask extends AsyncTask<Void, Void, Void> {

        private final static int TIME_OUT = 15000;//ms
        private int time = 0;
        private QuantumRandom quantumRandom;
        private MainView mainView;

        public QuantumRandomInitializationTask(QuantumRandom quantumRandom, MainView mainView) {
            this.quantumRandom = quantumRandom;
            this.mainView = mainView;
        }

        @Override
        protected Void doInBackground(Void[] params) {
            while(!quantumRandom.isInitialized()) {
                SystemClock.sleep(1);
                if(time++ >= TIME_OUT) return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mainView.onQuantumRandomInitialized();
        }
    }

    private Die die;
    private Vibrator vibrator;
    private boolean vibratorIsOn = false;
    private QuantumRandom quantumRandom;
    private Paint textPaint = new Paint();

    public MainView(Context context, QuantumRandom quantumRandom, int screenWidth, int screenHeight) {
        super(context);

        this.quantumRandom = quantumRandom;

        setOnClickListener(this);

        float dieSize;

        if(screenWidth <= screenHeight) dieSize = (float)(screenWidth - 80);
        else dieSize = (float)(screenHeight - 80);

        die = new Die(quantumRandom, 40f, 40f, dieSize);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        textPaint.setColor(Color.YELLOW);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dieSize / 18f);

        if(!quantumRandom.isInitialized()) new QuantumRandomInitializationTask(quantumRandom, this).execute();
        else onQuantumRandomInitialized();
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

        if(quantumRandom.isInitialized()) {
            die.draw(canvas);
            if(!die.usingQuantumRandom()) drawNotUsingQuantumRandomMessage(canvas);
        }
        else  drawLoadingScreen(canvas);
    }

    private void drawLoadingScreen(Canvas canvas) {
        canvas.drawText("Loading random numbers from",
            die.destinationRect.centerX(), die.destinationRect.centerY(),
            textPaint);
        canvas.drawText("https://qrng.anu.edu.au",
            die.destinationRect.centerX(), die.destinationRect.centerY() + (textPaint.getTextSize() * 2f),
            textPaint);
    }

    private void drawNotUsingQuantumRandomMessage(Canvas canvas) {
        canvas.drawText("Could not load quantum random numbers",
                die.destinationRect.centerX(), die.destinationRect.bottom + textPaint.getTextSize(),
                textPaint);
        canvas.drawText("Using pseudo random numbers instead!",
                die.destinationRect.centerX(), die.destinationRect.bottom + (textPaint.getTextSize() * 2f),
                textPaint);
    }

    private void onQuantumRandomInitialized() {
        vibratorIsOn = true;
        die.roll();
        invalidate();
    }
}