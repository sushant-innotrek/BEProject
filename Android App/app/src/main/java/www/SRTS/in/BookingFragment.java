package www.SRTS.in;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {
    ArrayList myDataset;
    RecyclerView recyclerView;
    MyAdapter adapter;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    Button clearCart;
    Button checkout;

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_booking, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Cart").child(firebaseAuth.getUid());
        mDatabase.keepSynced(true);
        recyclerView = rootView.findViewById(R.id.recyclerCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        //Total Calculation

        mDatabase.addValueEventListener(new ValueEventListener() {
            Double sum = 0.0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    sum += Double.valueOf(product.getCount()) * Double.valueOf(product.getPrice());
                    TextView txt_total = rootView.findViewById(R.id.txt_sum);
                    txt_total.setText(sum.toString());


                }
                if(dataSnapshot.exists()) {
                    TextView text_emptyCart = rootView.findViewById(R.id.text_emptyCart);
                    text_emptyCart.setVisibility(View.INVISIBLE);
                }
                else{
                    TextView text_emptyCart = rootView.findViewById(R.id.text_emptyCart);
                    text_emptyCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        clearCart = rootView.findViewById(R.id.button4);
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.getRef().setValue(null);
                Toast.makeText(rootView.getContext(), "Cart Cleared", Toast.LENGTH_SHORT).show();
                TextView textView = rootView.findViewById(R.id.txt_sum);
                textView.setText("0.0");
//                TextView text_emptyCart = rootView.findViewById(R.id.text_emptyCart);
//                text_emptyCart.setVisibility(View.VISIBLE);

            }
        });

        //Checkout
        checkout = rootView.findViewById(R.id.button3);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = rootView.findViewById(R.id.txt_sum);
                Intent i = new Intent(rootView.getContext(),Checkout.class);
                i.putExtra("key",textView.getText());
                startActivity(i);
            }
        });


        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product,ProductViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.item,ProductViewHolder.class,mDatabase) {

            @Override

            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setCount(model.getCount());
                viewHolder.setPrice(model.getPrice().doubleValue() * model.getCount().doubleValue());




            }

            @Override
            public void onBindViewHolder(final ProductViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

        }

        public void setName(String name){
            TextView item_name= mView.findViewById(R.id.item_Total);
            item_name.setText(name);
        }
        public void setCount(Long count){
            TextView item_count = mView.findViewById(R.id.item_count);
            item_count.setText(count.toString());
        }
        public void setPrice(Double price){
            TextView item_price = mView.findViewById(R.id.total_sum);
            item_price.setText(price.toString());
        }


    }
}
