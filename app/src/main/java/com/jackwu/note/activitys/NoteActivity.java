package com.jackwu.note.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jackwu.note.R;
import com.jackwu.note.models.Note;
import com.jackwu.note.models.NoteFactory;
import com.jackwu.note.utils.AppUtils;
import com.jackwu.note.utils.KeyboardUtils;
import com.jackwu.note.utils.ToastUtils;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NoteActivity extends AppCompatActivity {
    @BindView(R.id.activity_note_edt)
    EditText text;
    @BindView(R.id.activity_channel_toolbar)
    Toolbar toolbar;
    private Note note;
    private String id;
    private NoteFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        ((AppUtils) getApplication()).addActivity(this);
        factory = NoteFactory.getInstance();

        id = getIntent().getStringExtra(NotesActivity.NOTE_ID);
        if (id != null) {
            note = factory.getNoteById(UUID.fromString(id));
            text.setText(note.getContent());
            text.requestFocus();
            text.setSelection(text.getText().length());//让光标后置
        } else {
            KeyboardUtils.toggleSoftInput();
            note = new Note();
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == null)
                    KeyboardUtils.hideSoftInput(NoteActivity.this);
                if (text.getText().toString().trim().length() > 0)
                    addOrUpdateNote();
                else {
                    if (id != null)
                        factory.deleteNote(note);
                }
                finish();
            }
        });
        text.addTextChangedListener(new TextWatcher() {//文本变化Listener
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.setContent(s.toString());
                note.setTime(new Date());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void addOrUpdateNote() {
        if (id != null) {
            factory.updateNote(note);
        } else
            factory.addNote(note);
    }

    @Override
    public void onBackPressed() {
        if (text.getText().toString().trim().length() > 0) {
            addOrUpdateNote();
        } else {
            if (id != null)
                factory.deleteNote(note);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_actionbar_note_delete:
                if (id == null && text.getText().toString().trim().length() == 0) {
                    KeyboardUtils.hideSoftInput(NoteActivity.this);
                    finish();
                } else {
                    new AlertDialog.Builder(NoteActivity.this)
                            .setTitle("提示")
                            .setMessage("是否删除此条便签？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (id != null) {
                                        factory.deleteNote(note);
                                        dialog.dismiss();
                                        finish();
                                    } else {

                                        dialog.dismiss();
                                        finish();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            case R.id.menu_actionbar_note_share:
                if (text.getText().toString().trim().length() > 0) {
                    KeyboardUtils.hideSoftInput(NoteActivity.this);
                    factory.shareText(NoteActivity.this, note.getContent());
                } else
                    ToastUtils.showShortToastSafe("分享内容不能为空！");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
