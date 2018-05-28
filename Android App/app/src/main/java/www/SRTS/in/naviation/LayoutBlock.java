package www.SRTS.in.naviation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import static www.SRTS.in.naviation.MainThread.canvas;

public class LayoutBlock {
    public Player block = null;
    public Point point = null;
    public int X=0;
    public int Y=0;
    public int Type=0;


    public void draw(Canvas canvas){
        block.draw(canvas);
    }
    public void update(){
        block.update(point);
    }
    public void WriteText(int ID){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(75);
        canvas.drawText(Integer.toString(ID), point.x, point.y, paint);

    }
}


