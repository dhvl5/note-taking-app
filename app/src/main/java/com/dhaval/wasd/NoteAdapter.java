package com.dhaval.wasd;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>
{
    private ArrayList<Note> noteList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void OnItemClick(int position, View view, View v, MyViewHolder holder);
    }

    void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    private OnItemLongClickListener mLongListener;

    public interface OnItemLongClickListener
    {
        void OnLongClick(int position, MyViewHolder holder);
    }

    void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        mLongListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        MaterialTextView noteText;
        MaterialTextView noteTitle;
        MaterialCardView noteCard;

        MyViewHolder(final View view)
        {
            super(view);
            noteTitle = view.findViewById(R.id.note_title);
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
                            mListener.OnItemClick(position, noteText, noteCard, MyViewHolder.this);
                        }
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mLongListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            mLongListener.OnLongClick(position, MyViewHolder.this);
                        }
                    }
                    return true;
                }
            });
        }

        void bind(final Note note)
        {
            noteTitle.setText(note.getNoteTitle());
            noteText.setText(note.getNote());

            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    note.setChecked(!note.isChecked());
                    if(note.isChecked())
                        noteCard.setCardBackgroundColor(Color.BLACK);
                    else
                        noteCard.setCardBackgroundColor(Color.BLUE);
                    Log.e("Long Pressed", "Long Pressed!!!");
                    return true;
                }
            });*/
        }
    }

    public ArrayList<Note> getAll()
    {
        return noteList;
    }

    ArrayList<Note> getSelected()
    {
        ArrayList<Note> selected = new ArrayList<>();
        for(int i = 0; i < noteList.size(); i++)
        {
            if(noteList.get(i).isChecked())
                selected.add(noteList.get(i));
        }
        return selected;
    }

    NoteAdapter(Context context, ArrayList<Note> noteList)
    {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        /*Note note = noteList.get(position);
        holder.noteText.setText(note.getNote());
        holder.noteTitle.setText(note.getNoteTitle());*/
        holder.bind(noteList.get(position));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
