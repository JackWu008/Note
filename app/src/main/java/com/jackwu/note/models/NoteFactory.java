package com.jackwu.note.models;

import android.content.Context;
import android.content.Intent;

import com.jackwu.note.constants.DbConstants;
import com.jackwu.note.utils.AppUtils;

import net.lzzy.sqllib.SqlRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Created by JackWu on 2017/9/3.
 */

public class NoteFactory {
    private List<Note> notes;
    private static NoteFactory instance;
    private SqlRepository<Note> repository;

    private NoteFactory() {
        repository = new SqlRepository<>(AppUtils.getContext(), Note.class, DbConstants.dbPackage);
        try {
            notes = repository.get();
        } catch (Exception e) {
            notes = new ArrayList<>();
        }

    }

    public static NoteFactory getInstance() {
        if (instance == null) {
            synchronized (NoteFactory.class) {
                if (instance == null)
                    instance = new NoteFactory();
            }
        }
        return instance;
    }

    public List<Note> getNotes() {
        return notes;
    }
    public List<Note> getNotes(String kw) {
        try {
            return repository.getByKeyword(kw, new String[]{DbConstants.NOTE_CONTENT}, false);
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    public void addNote(Note note) {
        repository.insert(note);
        notes.add(0, note);
    }

    public void deleteNote(Note note) {
        repository.delete(note);
        notes.remove(note);
    }

    public Note getNoteById(UUID uuid) {
        for (Note n : notes) {
            if (n.getId().equals(uuid))
                return n;
        }
        return null;
    }

    public void updateNote(Note note) {
        repository.update(note);
        sort(notes);
    }

    public void shareText(Context context, String text) {
        if (context == null || text == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "分享便签内容"));
    }

    public void sort(List<Note> notes) {
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                long lTime = lhs.getTime().getTime();
                long rTime = rhs.getTime().getTime();
                if (lTime > rTime)
                    return -1;
                if (rTime > lTime)
                    return 1;
                if (rTime == lTime) {
                    if (lhs.getContent().length() > rhs.getContent().length())
                        return 1;
                    else
                        return -1;
                }
                return 0;
            }
        });
    }
}
