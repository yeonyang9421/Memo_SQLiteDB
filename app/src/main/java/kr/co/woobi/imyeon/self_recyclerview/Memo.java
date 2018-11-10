package kr.co.woobi.imyeon.self_recyclerview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class Memo extends AppCompatActivity {
    private EditText mTitleEditText;
    private EditText mContentsEditText;
    private long mMemoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        mTitleEditText = (EditText) findViewById(R.id.title_edit);
        mContentsEditText = (EditText) findViewById(R.id.content_edit);

        Intent intent = getIntent();
        if (intent != null) {
            mMemoId = intent.getLongExtra("id", -1);
            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");
            mTitleEditText.setText(title);
            mContentsEditText.setText(contents);
        }
    }

    @Override
    public void onBackPressed() {
        String title = mTitleEditText.getText().toString();
        String contents = mContentsEditText.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemoContract.memoEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(MemoContract.memoEntry.COLUMN_NAME_CONTENTS, contents);
        SQLiteDatabase db = MemoDbHelper.getInstance(this).getWritableDatabase();

        if(mMemoId==-1){
            long newRowId = db.insert(MemoContract.memoEntry.TABLE_NAME, null, contentValues);
            if (newRowId == -1) {
                Toast.makeText(this, "저장에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }else{
            int count = db.update(MemoContract.memoEntry.TABLE_NAME,contentValues,
                    MemoContract.memoEntry._ID+"=" +mMemoId, null);
            if(count==0){
                Toast.makeText(this, "수정에 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "메모가 수정되었습니다.",Toast.LENGTH_SHORT).show();;
                setResult(RESULT_OK);
            }
        }



        super.onBackPressed();
    }
}
