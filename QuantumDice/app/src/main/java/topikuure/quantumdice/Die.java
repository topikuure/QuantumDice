package topikuure.quantumdice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Topi on 06/11/2016.
 */
public class Die {

    private int currentNumber = 1;
    private QuantumRandom quantumRandom = new QuantumRandom();
    private Paint backgroundPaint = new Paint();
    private Paint numberPaint = new Paint();

    private Paint errorPaint = new Paint();
    private boolean usingQuantumRandom = true;

    private int sides = 6;
    private RectF destinationRect;

    public Die(float x, float y, float size) {
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
        numberPaint.setTextSize(size / 2f);

        errorPaint.setColor(Color.YELLOW);
        errorPaint.setStyle(Paint.Style.STROKE);
        errorPaint.setTextAlign(Paint.Align.CENTER);
        errorPaint.setAntiAlias(true);
        errorPaint.setTextSize(size / 14f);

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
        canvas.drawRect(destinationRect, backgroundPaint);
        canvas.drawText(Integer.toString(currentNumber),
            destinationRect.centerX(), destinationRect.centerY(),
            numberPaint);

        if(!usingQuantumRandom) {//TODO järkevämpi virheilmoitus ja tekstit resource stringeiksi.
            canvas.drawText("Out of quantum random numbers",
                    destinationRect.centerX(), destinationRect.bottom + errorPaint.getTextSize(),
                    errorPaint);
            canvas.drawText("Using pseudo random numbers!",
                destinationRect.centerX(), destinationRect.bottom + errorPaint.getTextSize() * 2,
                errorPaint);
        }
    }
}