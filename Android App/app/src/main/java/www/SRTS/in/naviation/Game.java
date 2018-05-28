package www.SRTS.in.naviation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import www.SRTS.in.naviation.Algorithm.floyds;


public class Game extends Activity {
    private GamePanel gamePanel;
    ArrayList<Integer> pathNodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("list").child(firebaseAuth.getUid());

        database.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List Racks = new ArrayList<Integer>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            final int len = (int) dataSnapshot.getChildrenCount();
                            String barcode = ds.getValue(String.class);
                            Query db = FirebaseDatabase.getInstance().getReference().child("RacksLayout").orderByKey().equalTo(barcode);
                            db.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot RDS : dataSnapshot.getChildren()){
                                                int rack = Integer.parseInt(RDS.getValue(String.class));
                                                Racks.add(rack);
                                            }
                                            if(Racks.toArray().length==len) {
                                                int[] array = new int[len];

                                                for(int i=0;i<Racks.size();i++){
                                                    array[i] = (int) Racks.get(i);
                                                }
                                                pathNodes = new floyds(54).calculate(array);
                                                gamePanel = new GamePanel(Game.this,pathNodes);
                                                setContentView(new GamePanel(Game.this,pathNodes));
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
