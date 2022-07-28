package com.lhm.ex_0726;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SqliteActivity extends AppCompatActivity {

    //안드로이드에서 기본적으로 제공을 해주는 클래스
    SQLiteDatabase mDatabase;
    SharedPreferences pref;

    EditText et_name, et_phone, et_age;
    Button btn_all, btn_delete, btn_insert, btn_search;
    TextView result_txt;

    //처음 한번 복사를 하면 다음번에 또 켤때 복사할일이 없도록 만드는 변수
    boolean isFirst = true;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        pref = PreferenceManager.getDefaultSharedPreferences(SqliteActivity.this);

        load();
        //앱 최초 실행시 assets폴더의 DB를 휴대폰 내부장소에 저장
        copyAssets();
        save();

        //위에서 copyAssets()을 통해 복사된 DB를 읽기 (mDatabase가 읽어와야한다.)
        mDatabase = openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/database/myDB.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_age = findViewById(R.id.et_age);

        result_txt = findViewById(R.id.result_txt);

        btn_all = findViewById(R.id.btn_all);
        btn_delete = findViewById(R.id.btn_delete);
        btn_insert = findViewById(R.id.btn_insert);
        btn_search = findViewById(R.id.btn_search);


        btn_all.setOnClickListener(myClick);
        btn_search.setOnClickListener(myClick);
        btn_insert.setOnClickListener(myClick);
        btn_delete.setOnClickListener(myClick);


    }//onCreate();

    View.OnClickListener myClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){

                case R.id.btn_all:  //전체조회
                    searchQuery("select * from friend");
                    break;
                case R.id.btn_search:   //상세조회
                    String name = et_name.getText().toString().trim();
                    if (name.length() == 0){
                        Toast.makeText(SqliteActivity.this, "검색할 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    }else{
                        searchQuery(String.format("select * from friend where name ='%s' ",name));
                    }
                    break;
                case R.id.btn_insert:   //정보 추가
                    name = et_name.getText().toString().trim();
                    String phone = et_phone.getText().toString().trim();
                    String age = et_age.getText().toString().trim();
                    if (name.length() == 0){
                        Toast.makeText(SqliteActivity.this, "추가할 이름을 입력", Toast.LENGTH_SHORT).show();
                    }else if(phone.length() ==0 ){
                        Toast.makeText(SqliteActivity.this, "추가할 번호를 입력", Toast.LENGTH_SHORT).show();
                    }else if(age.length() ==0 ){
                        Toast.makeText(SqliteActivity.this, "추가할 나이를 입력", Toast.LENGTH_SHORT).show();
                    }else{
                        int m_age = Integer.parseInt(age);
                        searchQuery(String.format("insert into friend values('%s','%s',%d)",name,phone,m_age));

                        et_name.setText("");
                        et_age.setText("");
                        et_phone.setText("");
                    }
                    //데이터 추가 후 전체를 로드
                    searchQuery("select * from friend");
                    break;

                case R.id.btn_delete:   //정보 삭제
                    name = et_name.getText().toString();
                    if(name.length() == 0){
                        Toast.makeText(SqliteActivity.this, "삭제할 이름을 입력", Toast.LENGTH_SHORT).show();
                    }else {
                        searchQuery(String.format("delete from friend where name='%s'",name));
                    }
                    searchQuery("select * from friend");



                    break;


            }
        }
    };

    //쿼리문 수행 메서드
    public void searchQuery(String query){  //내가 요청하고 싶은 쿼리문을 파라미터로 정한다.
        Cursor c = mDatabase.rawQuery(query, null);


        //컬럼의 수만큼 배열의 공간을 확보해라.
        String[] col = new String[c.getColumnCount()];
        col = c.getColumnNames();

        String[] str = new String[c.getColumnCount()];
        String result = ""; //최종 결과를 저장해둘 변수

//        Log.i("MY", col[0]+ "/" + col[1] + "/" + col[2]);

        //행 단위로 한줄씩 커서가 이동한다.
        while(c.moveToNext()){
            for(int i = 0 ; i < col.length; i++){
                str[i]="";
                str[i]+=c.getString(i);


                //result = 컬럼명 : 값 (name : hong ~~~)
                result += col[i] + ":" + str[i] + "\n";
            }//for
        }//while
        result_txt.setText(result);

   }


    //assets폴더의 DB를 휴대폰 내부에 저장하기 위한 메서드 작성
    public void copyAssets() {

        //InputStream으로 읽어서 outputStream으로 휴대폰 내부에 저장할것이다.

        AssetManager assetManager = getAssets();

        String[] files = null;
        String mkdir = "";

        try{
            files = assetManager.list("");

            InputStream in = null;
            OutputStream out = null;

            //files[0]의 값인 "myDB.db"와 같은 이름의 파일을 찾아서
            //inputStream으로 읽어온다.
            in = assetManager.open(files[1]);

            //내부 저장소에 폴더 생성
            //휴대폰 내부(기본)저장소의 root(최상위) 경로로 접근

            String str = "" + Environment.getExternalStorageDirectory();
            mkdir = str + "/database"; //database라고 하는 이름의 폴더를 생성할 예정

            File mpath = new File(mkdir);

            if(!mpath.exists()){
                isFirst = true;
            }
            if(isFirst){    //isFirst가 true라는것은 database라는 폴더를 못찾았다는 뜻이다.
                mpath.mkdirs(); //database폴더를 실질적으로 생성한다.

                //database이름의 폴더까지 접근해서 myDB.db라는 이름으로 output할 준비를 한다.
                //root/database/myDB.db
                out = new FileOutputStream(mkdir + "/" + files[1]);

                byte[] buffer = new byte[2048];
                int read = 0;

                while((read = in.read(buffer))!= -1){
                    out.write(buffer, 0, read);
                }
                //stream을 역순으로 닫는다.
                out.close();
                in.close();

                isFirst = false;
            }



        }catch (Exception e){

        }
    }//copyAssets()

    public void save(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("save", isFirst);
    }

    public void load(){
        //save라는 키값으로 저장 되어 있는 값을 불러오고, 만약 값이 없다면 기본값은 ture로 한다.
        isFirst = pref.getBoolean("save",true);
    }



}