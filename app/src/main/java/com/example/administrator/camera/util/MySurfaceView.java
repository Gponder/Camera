package com.example.administrator.camera.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    SurfaceHolder surfaceHolder;
    Camera camera;
    MediaRecorder mediaRecorder;
    private File videoFile;
    private boolean isPreview;

    public MySurfaceView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null){
            camera = Camera.open();
        }
        try {
            camera.setPreviewDisplay(holder);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.startPreview();
        isPreview = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        isPreview = false;
        camera.release();
        camera = null;
    }

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback raw = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                File file = new File("/sdcard/camera.raw");
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            savePic(data);
        }
    };

    private void savePic(byte[] data) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            File file = new File("/sdcard/camera.jpg");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tackVideo(){
        if (isPreview) {
            camera.stopPreview();
            isPreview = false;
            camera.release();
            camera = null;
        }
        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }else{
            mediaRecorder.reset();
        }
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoSize(320,240);
        mediaRecorder.setVideoFrameRate(30);
        try {
            File mRecVedioPath = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/hfdatabase/video/temp/");
            if (!mRecVedioPath.exists()) {
                mRecVedioPath.mkdirs();
            }
            videoFile = File.createTempFile("video",".3gp",mRecVedioPath);
        }catch (Exception e){

        }
        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
//            Timer
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tackVideoStop(){
        if (mediaRecorder==null){
            return;
        }
        mediaRecorder.stop();
//        Timer
        mediaRecorder.release();
        mediaRecorder=null;
        videoRename();
    }

    private void videoRename() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/hfdatabase/video/"
                + String.valueOf("id+who") + "/";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".3gp";
        File out = new File(path);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(path, fileName);
        if (videoFile.exists())
            videoFile.renameTo(out);
    }

    public void tackPicture(){
        boolean canAutoFocus = getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
        if (canAutoFocus){
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.takePicture(shutterCallback,raw,jpeg);
                }
            });
        }else{
            camera.takePicture(shutterCallback,raw,jpeg);
        }
    }

    public void voerTack(){
        camera.startPreview();
        isPreview = true;
    }
}
