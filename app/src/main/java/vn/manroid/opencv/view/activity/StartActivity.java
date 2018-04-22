package vn.manroid.opencv.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.manroid.opencv.R;
import vn.manroid.opencv.utils.AnimationType;
import vn.manroid.opencv.utils.CommonUtils;

public class StartActivity extends Activity {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_des)
    TextView txtDes;
    @BindView(R.id.txt_policy)
    TextView txt_policy;
    @BindView(R.id.btn_start)
    Button btnStart;
    private final int MULTIPLE_PERMISSIONS = 10;
    private String[] PERMISSIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};


        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        txtTitle.startAnimation(anim1);
        txtTitle.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                txtDes.startAnimation(anim2);
                txtDes.setVisibility(View.VISIBLE);
            }
        }, 1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                txt_policy.startAnimation(anim3);
                txt_policy.setVisibility(View.VISIBLE);

            }
        }, 2000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                btnStart.startAnimation(anim4);
                btnStart.setVisibility(View.VISIBLE);
            }
        }, 3000);



        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions(StartActivity.this, PERMISSIONS, MULTIPLE_PERMISSIONS);
                } else {
                    CommonUtils.cleanFolder();
                    startActivity(LoginActivity.class,null, AnimationType.ANIM_RIGHT_TO_LEFT);
                }
            }
        });

    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    startActivity(LoginActivity.class,null, AnimationType.ANIM_LEFT_TO_RIGHT);
                } else {
                    Toast.makeText(this, "Please grant permission for application !!!", Toast.LENGTH_SHORT).show();
//                    this.finish();
                }
                return;
            }
        }
    }


    public void startActivity(Class<?> cls, Bundle bundle, int[] anim) {
        Intent intent = new Intent(this, cls);
        super.startActivity(intent);
        overridePendingTransition(anim[0], anim[1]);
    }

}
