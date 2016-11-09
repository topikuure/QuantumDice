package topikuure.quantumdice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Topi on 06/11/2016.
 *
 * 6-sivuinen noppa, joka pyörii roll-metodilla, ja piirtyy draw-metodilla.
 * Noppa piirtää myös ilmoituksen jos ei onnistuttu hakemaan kvanttifysiikan lakien avulla generoituja randomlukuja netistä.
 * Tällöin noppa hakee luvun Random-luokasta, joka generoi pseudorandomeja lukuja.
 */
public class Die {

    private int sides = 6;
    private int currentNumber;

    private QuantumRandom quantumRandom;

    private Paint backgroundPaint = new Paint();
    private Paint numberPaint = new Paint();
    private Paint textPaint = new Paint();

    private boolean usingQuantumRandom = true;
    private RectF destinationRect;

    public Die(QuantumRandom quantumRandom, float x, float y, float size) {
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

        textPaint.setColor(Color.YELLOW);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(size / 18f);

        destinationRect = new RectF(x, y, x + size, y + size);
    }

    public void roll() {
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

    public void draw(Canvas canvas) {
        if(!quantumRandom.isInitialized()) {
            canvas.drawText("LOADING...",
                destinationRect.centerX(), destinationRect.centerY() - ((numberPaint.descent() + numberPaint.ascent()) / 2),
                textPaint);
            return;
        }
        canvas.drawRect(destinationRect, backgroundPaint);
        canvas.drawText(Integer.toString(currentNumber),
            destinationRect.centerX(), destinationRect.centerY() - ((numberPaint.descent() + numberPaint.ascent()) / 2),
            numberPaint);

        if(!usingQuantumRandom) {//TODO järkevämpi virheilmoitus(?) ja tekstit resource stringeiksi.
            canvas.drawText("Could not get quantum random numbers",
                    destinationRect.centerX(), destinationRect.bottom + textPaint.getTextSize(),
                    textPaint);
            canvas.drawText("Using pseudo random numbers instead!",
                destinationRect.centerX(), destinationRect.bottom + (textPaint.getTextSize() * 2f),
                    textPaint);
        }
    }
}