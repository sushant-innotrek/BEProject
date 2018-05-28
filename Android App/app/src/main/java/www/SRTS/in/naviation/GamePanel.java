package www.SRTS.in.naviation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by HP on 2/3/2018.
 */

class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private RacksLayout racksLayout;
    private MainThread thread;
    private Point size;
    private ArrayList<Integer> pathNodes;
    public GamePanel(Context context, ArrayList<Integer> pathNodes) {
        super(context);
        this.pathNodes = pathNodes;
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);

        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        racksLayout = new RacksLayout();
        for(int i=0;i<60;i++)
        {
            plotRack(racksLayout.Block.get(i));
        }
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
        
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            }catch(Exception e){e.printStackTrace();}
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }


    /////////////////////////////////////////////////////////////
    public void update()
    {
        racksLayout.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);


        canvas.drawColor(Color.WHITE);

        racksLayout.draw(canvas);
        //Log.e("path",pathNodes.toString());
        racksLayout.DrawPath(canvas,pathNodes);
    }

    /////////////////////////////////////////////////////////////


    public void plotRack(LayoutBlock layoutBlock){
        int maxX =size.x - size.x%6 ;
        int maxY = size.y - size.y%10;

        int x=layoutBlock.X;
        int y=layoutBlock.Y;

        int RackW = (int) Math.floor(maxX/6);
        int RackH = (int) Math.floor(maxY/10);

        int unitX = RackW/2,unitY=RackH/2;
        int color=Color.WHITE;
        int border=Color.WHITE;
        if(layoutBlock.Type==0) {
            color = Color.GRAY;
            border =Color.BLACK;
        }
        if(layoutBlock.Type==2){
            color = Color.BLACK;
            border = Color.GREEN;
        }
        if(layoutBlock.Type==3){
            color = Color.GREEN;
            border = Color.BLUE;
        }
        layoutBlock.block = new Player(new Rect(0,0,RackW,RackH),color,border);
        layoutBlock.point = new Point(unitX + x*RackW,unitY + y*RackH);

    }

}
