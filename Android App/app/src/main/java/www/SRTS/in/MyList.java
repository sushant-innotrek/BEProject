package www.SRTS.in;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyList extends AppCompatActivity {
    RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabase.keepSynced(true);
        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product,MyList.ProductViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Product, MyList.ProductViewHolder>(Product.class,R.layout.list_item,MyList.ProductViewHolder.class,mDatabase) {

            @Override

            protected void populateViewHolder(final MyList.ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.setName(model.getName());

                viewHolder.setKey(getRef(position).getKey());

                mDatabase.getRoot().child("list").child(firebaseAuth.getUid()).orderByKey().equalTo(String.valueOf(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            viewHolder.checkBox.setChecked(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onBindViewHolder(final MyList.ProductViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            mDatabase.getRoot().child("list").child(firebaseAuth.getUid()).child(String.valueOf(position)).setValue(viewHolder.text_barcode.getText());
                        }
                        else{

                            mDatabase.getRoot().child("list").child(firebaseAuth.getUid()).child(String.valueOf(position)).setValue(null);
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CheckBox checkBox;
        TextView text_barcode;
        public ProductViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            checkBox = mView.findViewById(R.id.list_checkbox);
            text_barcode = mView.findViewById(R.id.list_barcode);
        }

        public void setName(String name){
            TextView item_name= mView.findViewById(R.id.list_name);
            item_name.setText(name);
        }


        public void setKey(String key){
            TextView txt_barcode = mView.findViewById(R.id.list_barcode);
            txt_barcode.setText(key);
        }


    }
}
