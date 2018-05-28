package www.SRTS.in.FareData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import www.SRTS.in.R;


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionHolder> {
    private ArrayList<Transaction> transactions;
    private Context context;
    private View view;
    public TransactionsAdapter(ArrayList<Transaction> transactions,Context context){
        this.transactions = transactions;
        this.context = context;
    }
    @Override
    public TransactionsAdapter.TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(context).inflate(R.layout.transaction, parent,false);
        TransactionHolder Holder = new TransactionHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(TransactionsAdapter.TransactionHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        if(transaction != null) {
            holder.Task.setText(transaction.getTask());
            if (transaction.getType().equals("DEBIT")) {
                holder.Amount.setText("-" + transaction.getAmount());
                holder.Amount.setTextColor(view.getResources().getColor(R.color.invalid));
            } else if (transaction.getType().equals("CREDIT")) {
                holder.Amount.setText("+" + transaction.getAmount());
                holder.Amount.setTextColor(view.getResources().getColor(R.color.valid));
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionHolder  extends RecyclerView.ViewHolder{
        TextView Task;
        TextView Amount;
        public TransactionHolder(View itemView) {
            super(itemView);
            Task = itemView.findViewById(R.id.Task);
            Amount = itemView.findViewById(R.id.Amount);
        }
    }
}
