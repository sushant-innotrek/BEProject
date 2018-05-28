package www.SRTS.in;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import www.SRTS.in.Encrypt.Decryption;
import www.SRTS.in.Encrypt.Stations;
import www.SRTS.in.FareData.Transaction;

public class VerifyDetails extends AppCompatActivity {
    private TextView Source,Destination,Adults,Children,Fare,Trip,Tclass;
    private ProgressDialog pd;
    private Button Pay;
    private Ticket ticket;
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_details);
//
//     Declare Database Reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//      Initialize TextViews
        Source = findViewById(R.id.Source);
        Destination = findViewById(R.id.Destination);
        Adults = findViewById(R.id.Adults);
        Children= findViewById(R.id.Children);
        Fare = findViewById(R.id.Fare);
        Trip = findViewById(R.id.Trip);
        Tclass = findViewById(R.id.Tclass);

//
//      Initialize Button
        Pay = findViewById(R.id.Pay);
        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FARE = Fare.getText().toString();
                if(FARE.contains("Calculating...")){
                    //IF APP IS STILL CALCULATING THE FARE
                    Toast.makeText(getApplicationContext(),"Wait Till The App Calculates Your Fare",Toast.LENGTH_LONG).show();
                }
                else{
                    //IF FARE IS CALCULATED BY THE APP
                    long minutes = System.currentTimeMillis()/60000;
                    minutes -= time;
                    if(minutes>3){
                        Toast.makeText(getApplicationContext(),"Session Expired! Book Again",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    NewUser CurrUser = dataSnapshot.getValue(NewUser.class);
                                    if(CurrUser!=null) {
                                        double fare = Double.parseDouble(Fare.getText().toString());
                                        if(ticket.getReturn().equals("Return")){
                                            fare*=2;
                                        }
                                        double balance = CurrUser.getWalletBalance();

                                        if(fare>balance){
                                            Toast.makeText(getApplicationContext(),"Insufficient Funds",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                        else {
                                            Pay.setEnabled(false);
                                            double newBalance = balance-fare;
                                            database.child("Users").child(firebaseAuth.getUid()).child("walletBalance").setValue(newBalance);
                                            Transaction transaction = new Transaction();
                                            transaction.setTask("Ticket "+ticket.getSource()+" TO "+ticket.getDestination());
                                            transaction.setType("DEBIT");
                                            transaction.setAmount(fare);
                                            database.child("Users").child(firebaseAuth.getUid()).child("Transactions").push().setValue(transaction);
                                            DatabaseReference booking = database.child("Users").child(firebaseAuth.getUid()).child("Bookings").push();
                                            booking.setValue(ticket);
                                            Toast.makeText(getApplicationContext(),"Ticket Booked Successfully",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }
            }
        });

//
//      Decrypting QRCode and storing in String variable "Code"
        Decryption D = new Decryption();
        String Code = D.decrypt(getIntent().getStringExtra("QRcode"));

//
//      Validating QRcode
        Stations s=new Stations();
        if(!s.contains(Code)){
            Toast.makeText(this,"Invalid QR code",Toast.LENGTH_LONG).show();
            finish();
        }

//
//      Retrieving Ticket Info from BookingActivity Intent
        ticket = (Ticket) getIntent().getSerializableExtra("Ticket");

//
//      Setting Source Retrieved from Decrypted Text
        ticket.setSource(Code.substring(2));

//
//      Retrieve Values from Ticket Object
        final int adults = ticket.getAdults();
        final int children = ticket.getChildren();
        String source = ticket.getSource();
        String destination = ticket.getDestination();
        String trip = ticket.getReturn();
        String tclass = ticket.getCategory();

//
//      Setting all Fields on activity
        Source.setText(source);
        Destination.setText(destination.toUpperCase());
        Adults.setText(adults + "");
        Children.setText(children + "");
        Trip.setText(trip);
        Tclass.setText(tclass);

//
//      Start Progress Dialog
        pd = new ProgressDialog(this);
        pd.setTitle("Calculating Total Fare");
        pd.show();

//
//      Display Final Ticket Details for Verifying
//      Generate final ticket and store in Database
//      Deduct Fare from User's Wallet
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Gson G = new Gson();
                    String jsonTickets = G.toJson(dataSnapshot.getValue());
                    // Log.e("123kapil",jsonTickets);
                    try {
                        JSONArray Fares;
                        Fares = new JSONObject(jsonTickets).getJSONArray("TicketRates");
                        int i;
                        for (i = 1; i < Fares.length(); i++) {
                            JSONObject Curr = Fares.getJSONObject(i);
                            String SOURCE = Curr.getString("SOURCE");
                            String DESTINATION = Curr.getString("DESTINATION");

                            if (SOURCE.equalsIgnoreCase(ticket.getSource()) && DESTINATION.equalsIgnoreCase(ticket.getDestination())) {


                                float aFare = Float.parseFloat(Curr.getString("COST"));
                                float cFare = aFare/2;
                                if(((int)aFare) % 2 == 1){
                                    cFare = (aFare+5)/2;
                                }
                                float fare = adults*aFare + children*cFare;
                                Fare.setText(fare + "");
                                Log.e("cFare aFare",cFare + " " + aFare);
                                ticket.setFare(fare);
                                pd.dismiss();
                                time = System.currentTimeMillis()/1000;
                                time/=60;
                                Toast.makeText(getApplicationContext(),"You Have 3 minutes to Verify and Pay",Toast.LENGTH_LONG).show();
                                break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
