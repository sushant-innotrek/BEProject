package www.SRTS.in;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private ArrayList<Ticket> Tickets;
    private Context context;
    View v;
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.v = LayoutInflater.from(context).inflate(R.layout.ticket, parent,false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Ticket ticket = Tickets.get(position);
        holder.Source.setText("S: " + ticket.getSource());
        holder.Destination.setText("D: " + ticket.getDestination());
        holder.VIA.setText("VIA" + ticket.getVIA());
        holder.Fare.setText("FARE: Rs. " + ticket.getFare());
        holder.TimeStamp.setText(ticket.getTimeStamp());
        holder.Adult.setText("AD: " + ticket.getAdults());
        holder.Child.setText("CH: " + ticket.getChildren());
        holder.Category.setText(ticket.getCategory());
        holder.Return.setText("RET: " + ticket.getReturn());

        holder.Status.setText(ticket.getStatus());
        if(ticket.getStatus().equals("INVALID")){
            holder.Status.setTextColor(v.getResources().getColor(R.color.invalid));
        }
        if(ticket.getStatus().equals("VALID")){
            holder.Status.setTextColor(v.getResources().getColor(R.color.valid));
        }
    }

    @Override
    public int getItemCount() {
        return Tickets.size();
    }


    public MyAdapter(ArrayList myDataset,Context context){
        Tickets = myDataset;
        this.context=context;
    }



    class MyHolder extends RecyclerView.ViewHolder{
        TextView Source;
        TextView Destination;
        TextView VIA;
        TextView Fare;
        TextView TimeStamp;
        TextView Adult;
        TextView Child;
        TextView Category;
        TextView Return;
        TextView Status;


        public MyHolder(View itemView) {
            super(itemView);
            Source = itemView.findViewById(R.id.Source);
            Destination = itemView.findViewById(R.id.Destination);
            VIA = itemView.findViewById(R.id.VIA);
            Fare = itemView.findViewById(R.id.FARE);
            TimeStamp = itemView.findViewById(R.id.DATE_TIME);
            Adult = itemView.findViewById(R.id.Adults);
            Child = itemView.findViewById(R.id.Children);
            Category = itemView.findViewById(R.id.CLASS);
            Return = itemView.findViewById(R.id.RETURN);
            Status = itemView.findViewById(R.id.STATUS);
        }
    }
}