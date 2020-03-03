package com.dhaval.wasd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>
{
    private Context context;
    private List<Note> noteList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void OnItemClick(int position, View view, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MaterialTextView noteText;
        public MaterialCardView noteCard;

        public MyViewHolder(final View view)
        {
            super(view);
            noteText = view.findViewById(R.id.note_txt);
            noteCard = view.findViewById(R.id.cv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            mListener.OnItemClick(position, noteText, noteCard);
                        }
                    }
                }
            });
        }
    }

    public NoteAdapter(Context context, List<Note> noteList)
    {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteText.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
