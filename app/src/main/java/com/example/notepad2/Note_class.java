package com.example.notepad2;
public class Note_class {

    String title;
    int id;
    String type;

    public Note_class(String title ,int id )
    {
    this.title = title;
    this.id= id;

    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

}
