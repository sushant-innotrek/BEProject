package www.SRTS.in.naviation;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by HP on 2/4/2018.
 */

public class Player implements GameObject {
    private Rect rectangle;
    private int color;
    private int border;
    Player(Rect rectangle,int color,int border){
        this.rectangle = rectangle;
        this.color = color;
        this.border = border;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(border);
        canvas.drawRect(rectangle, paint);

    }


    public Point getCenter(){
        return new Point(this.rectangle.centerX(),this.rectangle.centerY());
    }
    @Override
    public void update() {

    }



    public void update(Point point){
        //LTRB
        rectangle.set(point.x - rectangle.width()/2,point.y-rectangle.height()/2,point.x
                + rectangle.width()/2,point.y+rectangle.height()/2);
    }
}
