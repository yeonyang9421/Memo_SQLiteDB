package kr.co.woobi.imyeon.self_recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INSERT = 1;
    private MemoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, Memo.class), REQUEST_CODE_INSERT);
            }
        });
        ListView listView = findViewById(R.id.memo_list);
        final Cursor cursor = getMemoCursor();
        mAdapter = new MemoAdapter(this, cursor);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Memo.class);
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String title = cursor.getString(cursor.getColumnIndexOrThrow((MemoContract.memoEntry.COLUMN_NAME_TITLE)));
                String contents = cursor.getString((cursor.getColumnIndexOrThrow(MemoContract.memoEntry.COLUMN_NAME_CONTENTS)));

                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);
                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long deleteId=id;
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("메모삭제");
                builder.setMessage("메모를 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db=MemoDbHelper.getInstance(MainActivity.this).getWritableDatabase();
                        int deleterCount = db.delete(MemoContract.memoEntry.TABLE_NAME,MemoContract.memoEntry._ID + " = " + deleteId, null);
                        if(deleteId==0){
                            Toast.makeText(MainActivity.this,"삭제에 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show();
                        }else {
                            mAdapter.swapCursor(getMemoCursor());
                            Toast.makeText(MainActivity.this, "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                builder.setNegativeButton("취소", null);
                builder.show();
                return true;
            }
        });
    }
    private Cursor getMemoCursor() {
        MemoDbHelper dbHelper=MemoDbHelper.getInstance(this);
        return dbHelper.getReadableDatabase().
                query(MemoContract.memoEntry.TABLE_NAME,null,null,null,null,null, MemoContract.memoEntry._ID+" DESC");
    }

    private  static  class MemoAdapter extends CursorAdapter{
        public MemoAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleText=view.findViewById(android.R.id.text1);
            titleText.setText(cursor.getString(cursor.getColumnIndex(MemoContract.memoEntry.COLUMN_NAME_TITLE)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_INSERT && resultCode == RESULT_OK){
            mAdapter.swapCursor(getMemoCursor());

        }
    }
}
