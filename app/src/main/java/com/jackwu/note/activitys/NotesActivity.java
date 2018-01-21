package com.jackwu.note.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jackwu.note.R;
import com.jackwu.note.adapter.GenericAdapter;
import com.jackwu.note.adapter.ViewHolder;
import com.jackwu.note.models.Note;
import com.jackwu.note.models.NoteFactory;
import com.jackwu.note.utils.AppUtils;
import com.jackwu.note.utils.TimeUtils;
import com.jackwu.note.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotesActivity extends AppCompatActivity implements NavigationView.
        OnNavigationItemSelectedListener {
    public static final String NOTE_ID = "note_id";
    @BindView(R.id.activity_notes_btn_add)
    FloatingActionButton btn_add;
    @BindView(R.id.activity_notes_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_notes_nv)
    NavigationView nv;
    @BindView(R.id.activity_notes_lv)
    ListView lv;
    List<Note> notes;
    GenericAdapter<Note> adapter;
    @BindView(R.id.activity_notes_drawer)
    DrawerLayout drawer;
    private long exitTime = 0;
    private boolean allSelected;
    private SearchView searchView;
    private NoteFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ButterKnife.bind(this);
        ((AppUtils) getApplication()).addActivity(this);
        factory = NoteFactory.getInstance();
        notes = factory.getNotes();
        factory.sort(notes);
        adapter = new GenericAdapter<Note>(NotesActivity.this, R.layout.item_note, notes) {
            @Override
            public void populate(ViewHolder holder, Note note) {
                holder.setTextView(R.id.item_note_tv_title, note.getTitle())
                        .setTextView(R.id.item_note_tv_time, TimeUtils.getSimpleDateFormat(note.getTime()));
            }
        };
        lv.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        setSupportActionBar(toolbar);


        //设置Toolbar和DrawerLayout实现动画和联动
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nv.setNavigationItemSelectedListener(this);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return false;
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotesActivity.this, NoteActivity.class);
                intent.putExtra(NOTE_ID, notes.get(position).getId().toString());
                startActivity(intent);
            }
        });

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(multiChoiceModeListener);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                ToastUtils.showShortToastSafe("关于");
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_action_search).getActionView();
        searchView.setQueryHint("输入关键字搜索");
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                btn_add.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                btn_add.setVisibility(View.VISIBLE);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notes.clear();
                notes.addAll(NoteFactory.getInstance().getNotes(newText));
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @OnClick(R.id.activity_notes_btn_add)
    public void addNote() {
        Intent intent = new Intent(NotesActivity.this, NoteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(nv)) {
            drawer.closeDrawers();
            return;
        }
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showShortToastSafe("再按一次退出应用");
            exitTime = System.currentTimeMillis();
        } else {
            ((AppUtils) getApplication()).allFinishActivity();
        }

    }

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            mode.setTitle("已选中" + lv.getCheckedItemCount() + "项");

            if (lv.getCheckedItemCount() == notes.size()) {
                mode.getMenu().getItem(1).setTitle("取消");
                allSelected = true;
            } else {
                mode.getMenu().getItem(1).setTitle("全选");
                allSelected = false;
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            btn_add.setVisibility(View.GONE);
            mode.getMenuInflater().inflate(R.menu.menu_action_mode_main, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_main_delete:
                    if (lv.getCheckedItemCount() > 0) {
                        new AlertDialog.Builder(NotesActivity.this)
                                .setTitle("提示")
                                .setMessage("是否删除这" + lv.getCheckedItemCount() + "条便签？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                            if (lv.isItemChecked(i)) {
                                                factory.deleteNote(adapter.getItem(i));
                                            }
                                        }
                                        mode.finish();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    } else
                        ToastUtils.showShortToastSafe("没有选中哦");
                    break;
                case R.id.menu_main_choose:
                    if (allSelected) {
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            lv.setItemChecked(i, false);
                        }
                    } else {
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            lv.setItemChecked(i, true);
                        }
                    }
                    break;
                case R.id.menu_main_reverse:
                    for (int i = 0; i < adapter.getCount(); i++)
                        lv.setItemChecked(i, !lv.isItemChecked(i));
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            btn_add.setVisibility(View.VISIBLE);
        }
    };


}
