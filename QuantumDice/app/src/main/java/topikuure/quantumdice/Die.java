package topikuure.quantumdice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.Random;

/**
 * Created by Topi on 06/11/2016.
 *
 * 6-sivuinen noppa, joka pyörii roll-metodilla, ja piirtyy draw-metodilla.
 * Noppa piirtää myös ilmoituksen jos ei onnistuttu hakemaan kvanttifysiikan lakien avulla
 * generoituja randomlukuja netistä. Tällöin noppa hakee luvun Random-luokasta, joka generoi
 * pseudorandomeja lukuja.
 */
public class Die {

    private class RollAnimation implements Runnable {

        private View view;
        private Die die;

        public RollAnimation(View view, Die die) {
            this.view = view;
            this.die = die;
        }

        @Override
        public void run() {
            synchronized (lock) {
                die.rolling = true;
                view.postInvalidate();
            }
            try {
                Thread.sleep(80);
            }
            catch (InterruptedException exception) {
            }
            synchronized (lock) {
                die.rolling = false;
                view.postInvalidate();
            }
        }
    }

    private final static Object lock = new Object();

    private int sides = 6;
    private int currentNumber;
    private boolean rolling = false;
    private QuantumRandom quantumRandom;
    private boolean usingQuantumRandom = true;
    private View view;
    private Paint backgroundPaint = new Paint();
    private Paint numberPaint = new Paint();

    public RectF destinationRect;

    public Die(View view, QuantumRandom quantumRandom, float x, float y, float size) {
        this.view = view;
        this.quantumRandom = quantumRandom;

        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        numberPaint.setColor(Color.BLACK);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setAntiAlias(true);
        numberPaint.setTextSize(size / 2f);

        numberPaint.setColor(Color.BLACK);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setAntiAlias(true);
        numberPaint.setTextSize(size / 1.5f);

        destinationRect = new RectF(x, y, x + size, y + size);
    }

    public void roll() {
        new Thread(new RollAnimation(view, this)).start();

        synchronized (lock) {
            rolling = true;
        }
        try {
            currentNumber = quantumRandom.getRandomNumber(1, sides);
            usingQuantumRandom = true;
        }
        catch(Exception exception) {
            Random random = new Random();
            currentNumber = random.nextInt(sides) + 1;
            usingQuantumRandom = false;
        }
    }

    public boolean usingQuantumRandom() {
        return usingQuantumRandom;
    }

    //Palauttaa Y-koordinaatin, mihin teksti piirretään, jos se halutaan piirtää nopan keskelle
    public float getNumberCenterY() {
        return destinationRect.centerY() - ((numberPaint.descent() + numberPaint.ascent()) / 2);

    }

    public void draw(Canvas canvas) {
        canvas.drawRect(destinationRect, backgroundPaint);

        synchronized (lock) {
            if(rolling) return;
        }
        canvas.drawText(Integer.toString(currentNumber),
            destinationRect.centerX(), getNumberCenterY(),
            numberPaint);
    }
}