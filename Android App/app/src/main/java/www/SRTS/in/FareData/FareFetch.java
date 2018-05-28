package www.SRTS.in.FareData;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harshit on 26-Feb-18.
 */

public class FareFetch {
    public String Stations[];

    public FareFetch(){
        Stations = new String[]{"Vashi","Belapur Rd","Kharghar","Panvel","Thane","Kurla","Chembur","Ghatkopar","Bhandup","CSTM","Dadar","Kalyan","Badlapur","Karjat","Dahanu"};
    }

}
