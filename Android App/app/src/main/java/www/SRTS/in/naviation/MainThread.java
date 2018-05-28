package www.SRTS.in.naviation;

import android.graphics.Canvas;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by HP on 2/3/2018.
 */

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private boolean running;
    private GamePanel gamePanel;
    public static Canvas canvas;

    MainThread(SurfaceHolder surfaceHolder,GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

    }
    public void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run(){
        Looper.prepare();

        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            timeMillis = (System.nanoTime()-startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try{
                if(waitTime>0){
                    this.sleep(waitTime);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
//                Log.i("AverageFPS",Double.toString(averageFPS));
            }
        }
    }
}
