package www.SRTS.in.PaymentGateway;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import www.SRTS.in.FareData.Transaction;
import www.SRTS.in.HomeActivity;
import www.SRTS.in.NewUser;
import www.SRTS.in.R;

public class Payment extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    private  EditText Amount;

    @Override
    protected void onDestroy() {
        stopService(new Intent(Payment.this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Amount = findViewById(R.id.Amount);

        Intent intent = new Intent(Payment.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

    }

    public void AddMoney(View view){
        String strAmount = Amount.getText().toString();
        double amount = Double.parseDouble( strAmount );
        if(amount < 10){
            Toast.makeText(getApplicationContext(),"Minimum Amount is Rs. 10",Toast.LENGTH_LONG).show();
        }
        else {
            //Start Payment

            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),
                    "USD",
                    "ADD TO SRTS WALLET",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent =new Intent(Payment.this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation != null) {
                    database.child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    NewUser CurrUser = dataSnapshot.getValue(NewUser.class);
                                    double newBalance = CurrUser.getWalletBalance() + Double.parseDouble(Amount.getText().toString());
                                    database.child("Users").child(firebaseAuth.getUid()).child("walletBalance").setValue(newBalance);
                                    Transaction transaction = new Transaction();
                                    transaction.setTask("Added Money");
                                    transaction.setType("CREDIT");
                                    transaction.setAmount(Double.parseDouble(Amount.getText().toString()));
                                    database.child("Users").child(firebaseAuth.getUid()).child("Transactions").push().setValue(transaction);
                                    Toast.makeText(getApplicationContext(), "Money Added Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
                else if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(Payment.this,"Payment Cancelled",Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(Payment.this,"Invalid Request",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
