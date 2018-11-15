package org.totalit.sbms.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.totalit.sbms.R;
import org.totalit.sbms.domain.Notes;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>  {
    List<Notes> notes;

    public NotesAdapter(List<Notes> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.noteId.setText(String.valueOf(notes.get(position).getId()));
         holder.datecreated.setText(notes.get(position).getDateCreated());
         holder.noteText.setText(notes.get(position).getNote());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView noteId;
        public TextView datecreated, noteText;


        public ViewHolder(final View itemView) {

            super(itemView);


            itemView.setOnClickListener(this);
            noteId =  itemView.findViewById(R.id.note_id);
            datecreated = itemView.findViewById(R.id.date_created);
            noteText = itemView.findViewById(R.id.note_text);



        }

        @Override
        public void onClick(View v) {

        }
    }
}
