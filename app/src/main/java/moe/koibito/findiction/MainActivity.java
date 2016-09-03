package moe.koibito.findiction;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomButton;

import java.security.Policy;

public class MainActivity extends Activity {
    private Camera mCamera;
    private CameraView mCameraView;
    private final int CAMERA_REQUEST_CODE = 1;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mNaviDrawer;
    private ImageView mNaviButton;
    private ImageView mFlashButton;
    private ImageView mChangeButton;
    private boolean isFront = false;
    private boolean isStop = false;
    private ImageView mStopButton;
    private RelativeLayout mMainLayout;
    private LinearLayout mCanvasContainer;
    private ListView mNavilistView;
    private NaviListAdapter mNaviMenuAdapter;
    private NaviMenuItem[] MenuList = new NaviMenuItem[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkCameraHardware(this);
        setContentView(R.layout.activity_main);
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNaviDrawer = (RelativeLayout) findViewById(R.id.left_drawer);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_content);
        mNaviButton = (ImageView) findViewById(R.id.ic_navi);
        mNaviButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDrawerLayout.isDrawerOpen(mNaviDrawer))
                    mDrawerLayout.closeDrawer(mNaviDrawer);
                else
                    mDrawerLayout.openDrawer(mNaviDrawer);
            }
        });
        mFlashButton = (ImageView) findViewById(R.id.ic_flash);
        mFlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFront) return;
                if(mCamera.getParameters().getFlashMode().equals("off")){
                    mFlashButton.setImageResource(R.drawable.ic_flash_on);
                    Camera.Parameters pm = mCamera.getParameters();
                    pm.setFlashMode("torch");
                    mCamera.setParameters(pm);
                }else {
                    mFlashButton.setImageResource(R.drawable.ic_flash_off);
                    Camera.Parameters pm = mCamera.getParameters();
                    pm.setFlashMode("off");
                    mCamera.setParameters(pm);
                }
            }
        });
        mChangeButton = (ImageView) findViewById(R.id.ic_change);
        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Camera.getNumberOfCameras()>=2){
                    if(isFront){
                        mCamera.release();
                        isFront=false;
                        mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                        mCameraView.setCamera(mCamera);
                    }else{
                        mCamera.release();
                        isFront=true;
                        mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                        mCameraView.setCamera(mCamera);
                    }
                }
            }
        });
        mStopButton = (ImageView) findViewById(R.id.ic_stop);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStop) {
                    isStop=false;
                    mMainLayout.setBackground(null);
                    mCamera.startPreview();
                }else {
                    isStop=true;
                    mMainLayout.setBackground(getDrawable(R.drawable.stop_line));
                    mCamera.stopPreview();
                }
            }
        });
        mCanvasContainer = (LinearLayout) findViewById(R.id.canvas_container);
        mNavilistView = (ListView) findViewById(R.id.navi_listview);
        MenuList[0]=new NaviMenuItem(getDrawable(R.drawable.ic_search),"내가 찾은 친구들");
        MenuList[1]=new NaviMenuItem(getDrawable(R.drawable.ic_location),"친구를 찾은 장소");
        MenuList[2]=new NaviMenuItem(getDrawable(R.drawable.ic_pen),"나만의 펜 바꾸기");
        mNaviMenuAdapter = new NaviListAdapter(MenuList);
        mNavilistView.setAdapter(mNaviMenuAdapter);
        mNavilistView.deferNotifyDataSetChanged();
        getPermission();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null)
            mCamera = getCameraInstance();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Camera.Parameters pmDown = mCamera.getParameters();
                int zoomDown = pmDown.getZoom();
                zoomDown-=10;
                if(zoomDown<0)
                    zoomDown=0;
                pmDown.setZoom(zoomDown);
                mCamera.setParameters(pmDown);
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Camera.Parameters pmUp = mCamera.getParameters();
                int zoomUp = pmUp.getZoom();
                zoomUp+=10;
                if(pmUp.getMaxZoom()<zoomUp)
                    zoomUp=pmUp.getMaxZoom();
                pmUp.setZoom(zoomUp);
                mCamera.setParameters(pmUp);
                break;
        }
        return true;
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
        }else{
            RelativeLayout camearaLayout = (RelativeLayout) findViewById(R.id.camera_layout);
            mCamera = getCameraInstance();
            mCameraView = new CameraView(this,mCamera);
            camearaLayout.addView(mCameraView);
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(0);
        }
        catch (Exception e){
        }
        return c;
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

                } else {
                    getPermission();
                }
                return;
        }
    }
}
