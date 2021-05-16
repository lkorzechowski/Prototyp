package com.orzechowski.prototyp.poradnikrecycler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;;
import com.orzechowski.prototyp.R;
import com.orzechowski.prototyp.poradnikrecycler.objects.Instrukcja;

import java.util.LinkedList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.InstrukcjeViewHolder> {
    private final List<Instrukcja> instrukcje;
    private final LayoutInflater mPompka;
    private final Activity mActivity;
    private final List<InstrukcjeViewHolder> viewHolders;
    private Thread currentPlayer;

    public ListAdapter(Activity kontekst, List<Instrukcja> listaInstrukcji) {
        this.mPompka = kontekst.getLayoutInflater();
        this.instrukcje = listaInstrukcji;
        this.mActivity = kontekst;
        this.viewHolders = new LinkedList<>();
    }

    @NonNull
    @Override
    public InstrukcjeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View wiersz = mPompka.inflate(R.layout.wiersz_instrukcje, null);
        return new InstrukcjeViewHolder(wiersz);
    }

    @Override
    public void onBindViewHolder(@NonNull InstrukcjeViewHolder instrukcjeholder, int numerWiersza) {
        TextView tytul = instrukcjeholder.tytul;
        TextView instrukcja = instrukcjeholder.instrukcja;
        tytul.setVisibility(View.VISIBLE);
        instrukcja.setVisibility(View.GONE);
        tytul.setText(instrukcje.get(numerWiersza).getTytul());
        instrukcja.setText(instrukcje.get(numerWiersza).getInstrukcja());
    }

    @Override
    public int getItemCount() {
        return instrukcje.size();
    }

    public class InstrukcjeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tytul, instrukcja;

        public InstrukcjeViewHolder(@NonNull View glownyElementWiersza){
            super(glownyElementWiersza);
            this.tytul = glownyElementWiersza.findViewById(R.id.tytul);
            this.instrukcja = glownyElementWiersza.findViewById(R.id.instrukcja);
            glownyElementWiersza.setOnClickListener(this);
            viewHolders.add(this);
        }

        @Override
        public void onClick(View v) {
            play(getAdapterPosition());
        }

    }

    public class Player extends Thread {
        private int time, line;
        private TextView tytul, instrukcja;
        private Boolean aktywneAutoplay = true;

        Player(int position){
            this.line = position;
            this.time = instrukcje.get(line).getCzas();
            InstrukcjeViewHolder currentView = viewHolders.get(line);
            this.tytul = currentView.tytul;
            this.instrukcja = currentView.instrukcja;
        }

        @Override
        public void run(){
            mActivity.runOnUiThread(() -> {
                tytul.setVisibility(View.INVISIBLE);
                instrukcja.setVisibility(View.VISIBLE);
            });
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mActivity.runOnUiThread(() -> {
                tytul.setVisibility(View.VISIBLE);
                instrukcja.setVisibility(View.GONE);
            });
        }
        //if(numerWiersza==viewHolders.size()) aktywneAutoplay = false;
    }

    public void play(int position){
        if(currentPlayer!=null){
            currentPlayer.interrupt();
        }
        currentPlayer = new Player(position);
        currentPlayer.start();
    }
}