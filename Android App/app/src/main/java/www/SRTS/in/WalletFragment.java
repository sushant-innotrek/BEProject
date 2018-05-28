package www.SRTS.in;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import www.SRTS.in.FareData.Transaction;
import www.SRTS.in.FareData.TransactionsAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    private ArrayList<Transaction> myDataset;
    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View V = inflater.inflate(R.layout.fragment_wallet, container, false);

//     CREATE RECYCLER VIEW FOR TRANSACTIONS

        myDataset = new ArrayList<Transaction>();

        final TransactionsAdapter adapter = new TransactionsAdapter(myDataset, V.getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(V.getContext());


        //
        // Retrieve Wallet Balance Of the User
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(firebaseAuth.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NewUser CurrUser = dataSnapshot.getValue(NewUser.class);


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

      //
        // Retrieve All Transactions Of the User
        database.child("Users").child(firebaseAuth.getUid()).child("Transactions").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myDataset.clear();
                        for(DataSnapshot T : dataSnapshot.getChildren()){
                            Transaction transaction = T.getValue(Transaction.class);
                            myDataset.add(transaction);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return V;
    }


}
