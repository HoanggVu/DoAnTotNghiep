package com.example.bookingcalender.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class AccountDoctorRecyclerAdapter extends RecyclerView.Adapter<AccountDoctorRecyclerAdapter.AccountDoctorHolder>{

    Context context;
    ArrayList<Account> accountDoctorArrayList;

    public AccountDoctorRecyclerAdapter(ArrayList<Account> accountDoctorArrayList, Context context) {
        this.accountDoctorArrayList = accountDoctorArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountDoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recycler_view, parent, false);
        return new AccountDoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountDoctorHolder holder, int position) {
        final Account account = accountDoctorArrayList.get(position);
        if (account == null){
            return;
        }
        holder.email.setText(account.getEmail());
        holder.password.setText(account.getPassword());
    }

    @Override
    public int getItemCount() {
        return accountDoctorArrayList.size();
    }

    static class AccountDoctorHolder extends RecyclerView.ViewHolder{
        TextView email;
        TextView password;
        RelativeLayout relativeLayout;
        public AccountDoctorHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.tv_account_email);
            password = itemView.findViewById(R.id.tv_account_password);
            relativeLayout = itemView.findViewById(R.id.item_account);
        }
    }
}
