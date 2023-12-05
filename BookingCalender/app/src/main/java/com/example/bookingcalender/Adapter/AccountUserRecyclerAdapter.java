package com.example.bookingcalender.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class AccountUserRecyclerAdapter extends RecyclerView.Adapter<AccountUserRecyclerAdapter.AccountUserHolder>{

    Context context;
    ArrayList<Account> accountUserArrayList;

    public AccountUserRecyclerAdapter(ArrayList<Account> accountUserArrayList, Context context) {
        this.accountUserArrayList = accountUserArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recycler_view, parent, false);
        return new AccountUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountUserHolder holder, int position) {
        final Account account = accountUserArrayList.get(position);
        if (account == null){
            return;
        }
        holder.email.setText(account.getEmail());
        holder.password.setText(account.getPassword());
    }

    @Override
    public int getItemCount() {
        return accountUserArrayList.size();
    }

    static class AccountUserHolder extends RecyclerView.ViewHolder{

        TextView email;
        TextView password;

        public AccountUserHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.tv_account_email);
            password = itemView.findViewById(R.id.tv_account_password);
        }
    }
}
