package www.SRTS.in;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Checkout extends AppCompatActivity {
    TextView txt_amount;
    TextView txt_otp;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        txt_amount = findViewById(R.id.text_amount);
        txt_otp = findViewById(R.id.text_otp);
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            txt_amount.setText("Payable Amount : Rs. "+value);
        }

        mDatabase.child("OTP").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String otp = ds.getValue(String.class);
                        if(!otp.equals("done"))
                            txt_otp.setText(otp);
                    }
                }
                else{
                    String val = ""+((int)(Math.random()*9000)+1000);

                    mDatabase.child("OTP").child(firebaseAuth.getUid()).setValue(val);
                    txt_otp.setText(val);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Check for done
        mDatabase.child("OTP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String status = ds.getValue(String.class);
                        if(status.equals("done")){
                            ImageView imageView = findViewById(R.id.imageView5);
                            imageView.setVisibility(View.VISIBLE);
                            Button button = findViewById(R.id.button2);
                            button.setVisibility(View.VISIBLE);
                            TextView textView = findViewById(R.id.textView5);
                            textView.setVisibility(View.VISIBLE);
                            mDatabase.child("OTP").setValue(null);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void exit(View view) {
        finish();
    }
}
