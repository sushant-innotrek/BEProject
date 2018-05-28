package www.SRTS.in;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import www.SRTS.in.FareData.Transaction;
import www.SRTS.in.PaymentGateway.Payment;
import www.SRTS.in.naviation.Game;

import static java.lang.System.gc;

public class HomeActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageButton home, wallet, booking, setting;
    private TextView userName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;

    private int mIndex;
    Integer btnStatus = 0;
    Integer currentFrag = 0;
    private String Name;
    HashMap<Integer,Product> map= new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_home);
        home = findViewById(R.id.imageButton9);
        wallet = findViewById(R.id.imageButton8);
        booking = findViewById(R.id.imageButton7);
        setting = findViewById(R.id.imageButton10);
        final HomeFragment homeActivity = new HomeFragment();
        final SettingFragment settingFragment = new SettingFragment();
        mIndex = 0;

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetNavigation(home);
                mIndex -= 0;
                currentFrag = 0;
                ApplyFragment(homeActivity);
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this,Game.class));
            }
        });
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetNavigation(booking);
                mIndex -= 2;
                currentFrag = 2;

                ApplyFragment(new BookingFragment());
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetNavigation(setting);
                mIndex -= 3;
                currentFrag = 3;
                ApplyFragment(settingFragment);
            }
        });
        home.performClick();
    }

    public void ApplyFragment(Fragment fragment) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (mIndex < 0)
            fragmentTransaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left);
        else if (mIndex > 0)
            fragmentTransaction.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right);
        mIndex = currentFrag;
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }

    public void resetNavigation(ImageButton btn) {
        home.setBackground(Drawable.createFromPath("@android:color/transparent"));
        wallet.setBackground(Drawable.createFromPath("@android:color/transparent"));
        booking.setBackground(Drawable.createFromPath("@android:color/transparent"));
        setting.setBackground(Drawable.createFromPath("@android:color/transparent"));


        btn.setBackgroundResource(R.color.colorPrimaryDark);
    }

    public void ComingSoon(View view) {
        Intent i = new Intent(this,MyList.class);
        startActivity(i);
    }

    public void BookTicket(View view) {
        Intent i = new Intent(this, QRScannerActivity.class);
        gc();
        startActivityForResult(i, 0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    final Barcode barcode = data.getParcelableExtra("Result");
                    Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                    //Check barcode in database
                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    //Get Details of Product related to Barcode


                    database.child("Cart").child(firebaseAuth.getUid()).orderByKey().equalTo(barcode.displayValue).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.exists()){
                                //Toast.makeText(HomeActivity.this, "Snapshop is null..new Data Inserted", Toast.LENGTH_SHORT).show();
                                database.child("Products").orderByKey().equalTo(barcode.displayValue).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Product product = ds.getValue(Product.class);
                                            //Toast.makeText(HomeActivity.this, product.getName(), Toast.LENGTH_SHORT).show();

                                            database.child("Cart").child(firebaseAuth.getUid()).child(barcode.displayValue).setValue(product);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            }
                            else{

                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    Product p = ds.getValue(Product.class);

                                    database.child("Cart").child(firebaseAuth.getUid()).child(barcode.displayValue).child("count").setValue(p.getCount() + 1);
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                } else {
                    Toast.makeText(HomeActivity.this, "No Barcode Scanned!!!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void Logout(View view) {
        firebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    public void Payment(View view) {
        startActivity(new Intent(HomeActivity.this, Payment.class));
    }


}
