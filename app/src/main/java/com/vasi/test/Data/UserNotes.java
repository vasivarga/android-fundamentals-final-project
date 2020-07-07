package com.vasi.test.Data;

import java.io.Serializable;

public class UserNotes implements Serializable {

    private String noteTitle = "", noteDesc="", noteDate="", noteId="";

    public UserNotes(String noteTitle, String noteDesc, String noteDate, String noteId) {
        this.noteTitle = noteTitle;
        this.noteDesc = noteDesc;
        this.noteDate = noteDate;
        this.noteId = noteId;
    }

    public UserNotes(){

    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
