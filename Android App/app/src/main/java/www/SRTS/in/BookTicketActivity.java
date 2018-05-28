package www.SRTS.in;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import www.SRTS.in.Encrypt.Stations;
import www.SRTS.in.FareData.FareFetch;

import static java.lang.System.gc;

public class BookTicketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner Destination;
    private EditText Adults, Child;
    private RadioGroup Class, TripType;
    private RadioButton selectedClass, selectedTripType;
    private String destination, adults, child, tclass, triptype;
    private FareFetch fareFetch;
    private ArrayAdapter<String> adapter;
    private  Ticket ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);

        fareFetch = new FareFetch();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fareFetch.Stations);
        Destination = findViewById(R.id.editDestination);
        Destination.setAdapter(adapter);
        Destination.setOnItemSelectedListener(this);
        Adults = findViewById(R.id.editAdults);
        Child = findViewById(R.id.editChildren);
        Class = findViewById(R.id.groupClass);
        TripType = findViewById(R.id.groupTripType);
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Log.e("Position",position+"");
        destination = fareFetch.Stations[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        destination = fareFetch.Stations[0];
    }


    public void ScanQR(View view) {



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("Result");

                    Intent i = new Intent(this, VerifyDetails.class);
                    i.putExtra("QRcode", barcode.displayValue);
                    i.putExtra("Ticket", ticket);
                    startActivity(i);
                    gc();
                    finish();

                } else {
                    Toast.makeText(BookTicketActivity.this, "No QRCode Scanned!!!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}