package com.gnoemes.bubblenotes.repo.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by kenji1947 on 29.09.2017.
 */
@Entity
public class Description {
    @Id
    long id;
    private String name;
    int priority;

    ToOne<Note> noteToOne;

    public ToOne<Note> getNoteToOne() {
        return noteToOne;
    }

    public void setNoteToOne(ToOne<Note> noteToOne) {
        this.noteToOne = noteToOne;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
