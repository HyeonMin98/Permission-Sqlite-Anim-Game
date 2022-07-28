package com.lhm.ex_0726;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        //현재 수락이 필요한 권한들에 대한 상태를 확인
        //전화걸기, 기록하기, 주소록 접근에 대한 권한이 비활성화 되어있는지 확인

        if (ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            //전화 걸기 권한을 수락해줘야한다.
            setPermission();
            return;
        }

        if (ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //저장 권한을 수락해줘야한다.
            setPermission();
            return;
        }

        if (ActivityCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //주소록 접근 권한을 수락해줘야한다.
            setPermission();
            return;
        }



    }//onCreate()

    //앱 권한설정에 관한 감시자
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //모든 권한의 수락이 완료된 경우

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //한가지라도 허용이 되지 않은 권한이 있다면 호출

            //모든 권한이 수락되지 않았다면 강제로 종료
            Toast.makeText(PermissionActivity.this, "모든권한을 수락해야 합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    //권한 수락에 관한 여부를 묻는 메서드
    public void setPermission(){
        TedPermission.create().setPermissionListener(permissionListener).setDeniedMessage(
                "이 앱에서 요구하는 권한이 있습니다 \n [설정] > [권한]에서 해당 권한을 활성화 해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS).check();
        //수락해야하는 3개의 권한 명시, 자동으로 해줄지 물어보는 역할
    }
}