package moe.koibito.findiction;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Astoration on 2016. 9. 2..
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback{
    private Camera mCamera;
    private SurfaceHolder mHolder;
    public CameraView(Context context,Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            if(mCamera==null)
                Log.e("camera","null");
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        }catch (IOException e){

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
