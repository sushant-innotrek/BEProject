package www.SRTS.in.naviation;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP on 2/8/2018.
 */

public class RacksLayout {

    HashMap<Integer,LayoutBlock> Block;

    RacksLayout(){
        Block = new HashMap<>();
        int k=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<6;j++){
                LayoutBlock layoutBlock = new LayoutBlock();
                layoutBlock.X = j;
                layoutBlock.Y = i;

                if((j==3||j==2||j==0||j==5)&&(i>=1&&i<=7))
                    layoutBlock.Type = 0;   //Rack
                else
                    layoutBlock.Type = 1;   //Path

                if(j==5&&(i==0||i==8)){
                    layoutBlock.Type = 0;   //Rack
                }
                if(i==9&&(j==2||j==3)){
                   layoutBlock.Type = 3;    //Billing Counter
                }
                if((i==9)&&j==0)
                {
                    layoutBlock.Type = 2;   //DOOR
                }

                Block.put(k,layoutBlock);
                k++;
            }
        }
    }

    public  void update(){
        for(int i=0;i<60;i++){
            Block.get(i).update();
        }
    }

    public void draw(Canvas canvas){
        for(int i=0;i<60;i++){
            Block.get(i).draw(canvas);
            //Block.get(i).WriteText(i);
        }
    }

    public void DrawPath(Canvas canvas, ArrayList<Integer> Racks){

        for(int i=0;i<Racks.size()-1;i++){

            LayoutBlock lb1 = Block.get(Racks.get(i));
            LayoutBlock lb2 = Block.get(Racks.get(i + 1));
            Path P = new Path(lb1.point, lb2.point);
            P.draw(canvas);
        }
    }


}
