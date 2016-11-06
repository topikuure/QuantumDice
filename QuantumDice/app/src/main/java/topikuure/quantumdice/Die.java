package topikuure.quantumdice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Topi on 06/11/2016.
 */
public class Die {

    private int currentNumber = 1;
    private QuantumRandom quantumRandom = new QuantumRandom();
    private Paint backgroundPaint = new Paint();
    private Paint numberPaint = new Paint();

    public int sides = 6;
    public RectF destinationRect;

    public Die(float x, float y, float size) {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        numberPaint.setColor(Color.BLACK);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setAntiAlias(true);
        numberPaint.setTextSize(size / 2f);

        destinationRect = new RectF(x, y, x + size, y + size);
    }

    public void roll() {
        currentNumber = quantumRandom.getRandomNumber(1, sides);
    }

    public void draw(Canvas canvas) {//TODO eri muotoisia noppia
        canvas.drawRect(destinationRect, backgroundPaint);
        canvas.drawText(Integer.toString(currentNumber),
            destinationRect.centerX(), destinationRect.centerY(),
            numberPaint);
    }
}