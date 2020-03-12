package com.example.cazapatos.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cazapatos.R;
import com.example.cazapatos.models.User;

import java.util.List;

//fichero responsable de dibujar en la pantalla cada uno de los elementos de la lista ranking
public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    //diseño del fragmento
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    //encargado de incluir la informacion del elemento que estamos dibujando en la lista
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = position + 1;
        holder.tvPosition.setText(pos + "º");
        holder.tvAnimal.setText(String.valueOf(mValues.get(position).getAnimal()));
        holder.tvNick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //encargada de mapear una referencia a cada elemento del layout donde tenemos el ranking
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvPosition;
        public final TextView tvAnimal;
        public final TextView tvNick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPosition = view.findViewById(R.id.textViewPosition);
            tvAnimal = view.findViewById(R.id.textViewAnimal);
            tvNick = view.findViewById(R.id.textViewNick);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNick.getText() + "'";
        }
    }
}
