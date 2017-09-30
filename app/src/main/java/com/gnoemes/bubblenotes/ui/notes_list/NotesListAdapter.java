package com.gnoemes.bubblenotes.ui.notes_list;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnoemes.bubblenotes.R;
import com.gnoemes.bubblenotes.repo.model.Note;


import java.util.List;

/**
 * Created by kenji1947 on 25.09.2017.
 */


public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteHolder> {

    private NotesListAdapter.ItemClickListener clickListener;
    private List<Note> adapterData;

    public interface ItemClickListener {
        void onClick(Long id);
    }

    public NotesListAdapter(@Nullable List<Note> adapterData,
                            NotesListAdapter.ItemClickListener clickListener) {
        this.clickListener = clickListener;
        this.adapterData = adapterData;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);
        return new NotesListAdapter.NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        Note note = getItem(position);
        holder.id.setText(note.getId() + "");
        holder.name.setText(note.getName());
        holder.priority.setText(note.getUnixTime() + "");
    }


    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    public Note getItem(int index) {
        return adapterData.get(index);
    }

    public void updateData(@Nullable List<Note> data) {
        adapterData = data;
        notifyDataSetChanged();
    }

    //TODO make Holder class static
    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id;
        TextView name;
        TextView priority;

        public NoteHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.idTextView);
            name = (TextView) itemView.findViewById(R.id.nameTextView);
            priority = (TextView) itemView.findViewById(R.id.priorityTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getItem(getAdapterPosition()).getId());
        }
    }
}
