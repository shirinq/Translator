package com.example.translator.Model;

import java.util.UUID;

public class Word {

    private UUID id;
    private String Title;
    private String Translation;

    public Word() {
        id = UUID.randomUUID();
    }
    public Word(UUID id){
        this.id = id;
    }

    public UUID getId(){return id;}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTranslation() {
        return Translation;
    }

    public void setTranslation(String translation) {
        Translation = translation;
    }

}
