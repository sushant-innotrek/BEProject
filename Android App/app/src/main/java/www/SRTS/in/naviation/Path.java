package www.SRTS.in.naviation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by HP on 2/13/2018.
 */

public class Path {
    private Point P1;
    private Point P2;

    public Path(Point P,Point Q ){
        P1=P;
        P2=Q;
    }
    public void draw(final Canvas canvas) {
        final Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);

        canvas.drawLine(P1.x, P1.y, P2.x, P2.y, paint);

    }

}
