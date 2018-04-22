package vn.manroid.opencv.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import vn.manroid.opencv.utils.CharDetectOCR;
import vn.manroid.opencv.utils.CommonUtils;
import vn.manroid.opencv.utils.Config;

import static vn.manroid.opencv.utils.CommonUtils.info;

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
    private ProgressDialog dialog;
    private String lastFileName = "";
    private static int REQUEST_IMAGE_CAPTURE = 1;
    private static ProcessImage processImg = new ProcessImage();
    private boolean isRecognized = false;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        language = Config.LANGUAGE;
        ProcessImage.language = language;
        ProcessImage.thresholdMin =  Config.THRESH_OLD_MIN;

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
//                    startActivity(RecognizeTextActivity.class,null, AnimationType.ANIM_RIGHT_TO_LEFT);
                    takePicture();
                }
            }
        });

        //init data from folder asset
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            new StartActivity.InitTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new StartActivity.InitTask().execute();
        }

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
//                    startActivity(RecognizeTextActivity.class,null, AnimationType.ANIM_LEFT_TO_RIGHT);
                    takePicture();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        super.startActivity(intent);
        overridePendingTransition(anim[0], anim[1]);
    }


    private class InitTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(StartActivity.this);
            dialog.setTitle("Card Scanner");
            dialog.setMessage("Loading data ... !!!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                CharDetectOCR.init(getAssets());
                return "";
            } catch (Exception e) {
                Log.e("COMPA", "Error init data OCR. Message: " + e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
        }

    }


    private void takePicture() {
        Intent takePicIntent = new Intent(StartActivity.this, AndroidCamera.class);
        lastFileName = CommonUtils.APP_PATH + "capture" + System.currentTimeMillis() + ".jpg";
        takePicIntent.putExtra("output", lastFileName);
        info(lastFileName);
        startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap imageBitmap = BitmapFactory.decodeFile(lastFileName, options);

            if (imageBitmap == null) {
                // Try again
                isRecognized = false;
//                image.setImageBitmap(imageBitmap);
//                hideProcessBar();
//                dialogBox("Can not recognize sheet. Please try again", "Retry", "Exist", true);
                return;
            }
            final Bitmap finalImageBitmap = imageBitmap.getWidth() > imageBitmap.getHeight()
                    ? rotateBitmap(imageBitmap, 90) : imageBitmap;

            int top = data.getIntExtra("top", 0);
            int bot = data.getIntExtra("bot", 0);
            int right = data.getIntExtra("right", 0);
            int left = data.getIntExtra("left", 0);

//            image.setImageBitmap(finalImageBitmap);
            displayResult(finalImageBitmap, top, bot, right, left);

        }
    }

    public void displayResult(Bitmap imageBitmap, int top, int bot, int right, int left) {
        info("Origin size: " + imageBitmap.getWidth() + ":" + imageBitmap.getHeight());
        // Parser
//        recognizeResult.setText("");
        if (processImg.parseBitmap(imageBitmap, top, bot, right, left)) {
            // TODO: set result
//            recognizeResult.setText(processImg.recognizeResult);
            info(processImg.recognizeResult);
            Toast.makeText(this, processImg.recognizeResult, Toast.LENGTH_SHORT).show();
            // TODO: write result to image
            // image.setImageBitmap(toBitmap(processImg.drawAnswered(numberAnswer)));
            isRecognized = true;
//            hideProcessBar();
        } else {
            // Try again
            isRecognized = false;
//            image.setImageBitmap(imageBitmap);
//            hideProcessBar();
//            dialogBox("Can not recognize sheet. Please try again", "Retry", "Exist", true);
        }
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
