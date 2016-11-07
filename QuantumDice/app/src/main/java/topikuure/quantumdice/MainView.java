package topikuure.quantumdice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Topi on 06/11/2016.
 */
public class MainView extends View implements View.OnClickListener {

    private Die die;
    private int backgroundColor = Color.BLACK;

    public MainView(Context context, int screenWidth, int screenHeight) {
        super(context);
        setOnClickListener(this);

        float dieSize;

        if(screenWidth <= screenHeight) dieSize = (float)(screenWidth - 80);
        else dieSize = (float)(screenHeight - 80);

        die = new Die(context, 40f, 40f, dieSize);
    }

    @Override
    public void onClick(View view) {
        die.roll();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        die.draw(canvas);
    }
}