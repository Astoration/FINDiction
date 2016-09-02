package moe.koibito.findiction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    private Camera mCamera;
    private CameraView mCameraView;
    private final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkCameraHardware(this);
        setContentView(R.layout.activity_main);
        getPermission();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null)
            mCamera = getCameraInstance();
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    public void getPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

            }
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    RelativeLayout camearaLayout = (RelativeLayout) findViewById(R.id.camera_layout);
                    mCamera = getCameraInstance();
                    mCameraView = new CameraView(this,mCamera);
                    camearaLayout.addView(mCameraView);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.e("permission","permission desined");
                    getPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }
}
