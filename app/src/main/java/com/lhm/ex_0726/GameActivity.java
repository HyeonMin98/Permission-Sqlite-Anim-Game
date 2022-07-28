package com.lhm.ex_0726;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageView com[] = new ImageView[3];
    ImageView user[] = new ImageView[3];
    Button btnStart, btnExit;
    Button selectR, selectS, selectP;

    int count = 0; //그림을 움직이기 위한 변수
    int rot = 0; //그림을 움직이기 위한 변수
    int comRand = 0; //컴퓨터 난수발생.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        com[0] = (ImageView)findViewById(R.id.comR);
        com[1]= (ImageView)findViewById(R.id.comS);
        com[2] = (ImageView)findViewById(R.id.comP);

        user[0] = (ImageView)findViewById(R.id.uR);
        user[1] = (ImageView)findViewById(R.id.uS);
        user[2] = (ImageView)findViewById(R.id.uP);

        selectR = (Button) findViewById(R.id.btn_R);
        selectS = (Button) findViewById(R.id.btn_S);
        selectP = (Button) findViewById(R.id.btn_P);

        btnStart = (Button)findViewById(R.id.btn_Start);
        btnExit = (Button)findViewById(R.id.btn_Exit);

        //버튼 이벤트 감지자 등록
        btnStart.setOnClickListener(myBtn);
        btnExit.setOnClickListener(myBtn);

        //버튼 이벤트 감지자 등록
        selectR.setOnClickListener(selectButton);
        selectS.setOnClickListener(selectButton);
        selectP.setOnClickListener(selectButton);
    }


    View.OnClickListener myBtn = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_Start:
                    //핸들러 호출
                    mHandler.sendEmptyMessage(0);
                    break;

                case R.id.btn_Exit:
                    finish();
                    break;
            }
        }
    };

    Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            moving();
            mHandler.sendEmptyMessageDelayed(0, 50);
        }
    };

    //이미지를 움직이게 하는 함수
    void moving() {

        count++;
        rot = count % 3;

        visible(rot, rot);

        if(count == 3)
            count = 0;
    }

    //유저와 컴퓨터 이미지의 숨김처리를 하는 메서드
    void visible(int c, int u){

        com[c].setVisibility(View.VISIBLE);
        user[u].setVisibility(View.VISIBLE);

        for(int i = 0; i < com.length; i++){

            if(i != c)
                com[i].setVisibility(View.INVISIBLE);

            if(i != u)
                user[i].setVisibility(View.INVISIBLE);
        }
    }

    View.OnClickListener selectButton = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            comRand = new Random().nextInt(3);
            mHandler.removeMessages(0);

            //유저의 결과
            int uResult = 0;

            if(v == selectR)
                uResult = 0;


            else if(v == selectS)
                uResult = 1;


            else
                uResult = 2;

            visible(comRand, uResult);
        }
    };
}