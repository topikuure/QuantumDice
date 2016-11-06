package topikuure.quantumdice;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by Topi on 06/11/2016.
 */
public class MainView extends View implements View.OnClickListener {

    private Die die;

    public MainView(Context context, int screenWidth, int screenHeight) {
        super(context);
        setOnClickListener(this);

        float dieSize;

        if(screenWidth <= screenHeight) dieSize = (float)(screenWidth - 40);
        else dieSize = (float)(screenHeight - 40);

        die = new Die(20f, 20f, dieSize);
    }

    @Override
    public void onClick(View view) {
        die.roll();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        die.draw(canvas);
    }
}
