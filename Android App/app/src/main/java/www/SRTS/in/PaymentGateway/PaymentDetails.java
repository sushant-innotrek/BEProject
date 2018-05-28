package www.SRTS.in.PaymentGateway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import www.SRTS.in.R;

public class PaymentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        TextView textView = findViewById(R.id.Result);
        Intent i = getIntent();
        String Res = i.getStringExtra("PaymentDetails") +"\n AMOUNT: "+i.getStringExtra("Amount");
        textView.setText(Res);
    }
}
