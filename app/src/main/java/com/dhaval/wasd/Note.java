package com.dhaval.wasd;

import java.io.Serializable;

public class Note implements Serializable
{
    private String noteTitle, note;
    private boolean isChecked = false;

    Note(String noteTitle, String note)
    {
        this.noteTitle = noteTitle;
        this.note = note;
    }

    String getNoteTitle()
    {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle)
    {
        this.noteTitle = noteTitle;
    }

    String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    boolean isChecked()
    {
        return isChecked;
    }

    void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }
}
