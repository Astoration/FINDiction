package moe.koibito.findiction;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    private Camera mCamera;
    private CameraView mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkCameraHardware(this);
        setContentView(R.layout.activity_main);
        RelativeLayout camearaLayout = (RelativeLayout) findViewById(R.id.camera_layout);
        mCamera = getCameraInstance();
        mCameraView = new CameraView(this,mCamera);
        camearaLayout.addView(mCameraView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null)
            mCamera = getCameraInstance();
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
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
        if(c==null)
            Log.e("asdf","asdf");
        return c; // returns null if camera is unavailable
    }
}
