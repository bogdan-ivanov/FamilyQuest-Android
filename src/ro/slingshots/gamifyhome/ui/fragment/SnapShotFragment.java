package ro.slingshots.gamifyhome.ui.fragment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import ro.slingshots.gamifyhome.R;
import ro.slingshots.gamifyhome.Utils;
import ro.slingshots.gamifyhome.http.request.AsyncCallback;
import ro.slingshots.gamifyhome.http.request.AsyncRequest;
import ro.slingshots.gamifyhome.http.request.RequestCloseChore;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SnapShotFragment extends BaseFragment{
	private static final String TAG = "SnapShotFragment";
	private SurfaceView mPreview = null;
	private Button cancelButton;
	private Button mSnapshotButton;
	private SurfaceHolder previewHolder = null;
	private Camera mCamera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	protected boolean mAutoFocusDone;
	private boolean mShouldTakePicture;
	private ProgressBar mProgressSmall;
	private int mOrientation;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View v = inflater.inflate(R.layout.snapshot, container,
				false);
		
		mPreview = (SurfaceView)v.findViewById(R.id.preview);
		mPreview.setVisibility(View.VISIBLE);
		
		mSnapshotButton  = (Button)v.findViewById(R.id.button_snapshot);
		mSnapshotButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN: {
					// start on focus
					onStartFocus();
					break;
				}
				case MotionEvent.ACTION_UP: {
					// start take picture right after on focus finish
					onTakePicture();

					break;
				}
				}
				return false;
			}
		});
		initCameraPreview();
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();

		mCamera = Camera.open();
		startPreview();
	}
	
	@Override
	public void onPause() {
		if (inPreview) {
			mCamera.stopPreview();
		}

		mCamera.setPreviewCallback(null);
		mCamera.release();
		mCamera = null;
		inPreview = false;

		super.onPause();
	}
	public void initCameraPreview() {

		previewHolder = mPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			// no-op -- wait until surfaceChanged()
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			initPreview(width, height);
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};
	private void initPreview(int width, int height) {
		if (mCamera != null && previewHolder.getSurface() != null) {
			try {
				mCamera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
				Toast.makeText(SnapShotFragment.this.getActivity(),
						t.getMessage(), Toast.LENGTH_LONG).show();
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = mCamera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				// initZoom(parameters);

				if (size != null) {

					Display display = ((WindowManager) getActivity()
							.getSystemService(Activity.WINDOW_SERVICE))
							.getDefaultDisplay();

					if (display.getRotation() == Surface.ROTATION_0) {
						parameters.setPreviewSize(size.height, size.width);
						mCamera.setDisplayOrientation(90);
						mOrientation = 90;
					}

					if (display.getRotation() == Surface.ROTATION_90) {
						parameters.setPreviewSize(size.width, size.height);
					}

					if (display.getRotation() == Surface.ROTATION_180) {
						parameters.setPreviewSize(size.height, size.width);
					}

					if (display.getRotation() == Surface.ROTATION_270) {
						parameters.setPreviewSize(size.width, size.height);
						mCamera.setDisplayOrientation(180);
						mOrientation = 180;
					}

					parameters.setPreviewSize(size.width, size.height);
					mCamera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}
	protected boolean onStartFocus() {
		if (mCamera == null)
			return false;
		try {
			mCamera.autoFocus(new AutoFocusCallback() {

				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					mAutoFocusDone = true;
					doTakePicture();
				}
			});
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	protected void onTakePicture() {
		if (mCamera != null) {
			Log.i(TAG, "onTakePicture");
			mShouldTakePicture = true;
			if(mAutoFocusDone){
				doTakePicture();
			}else{
				boolean autoFocusFailed = onStartFocus();

				// try again to take photo ;)
				if (autoFocusFailed) {
					try{
						mCamera.cancelAutoFocus();
					}catch(Exception e){};
						doTakePicture();
				}
			}
			
		}
	}

	protected void onAutoFocusFinished(boolean success) {
		Log.i(TAG, "onTakePicture onAutoFocus " + success);
		doTakePicture();
	}

	protected synchronized void doTakePicture() {
		if(!mShouldTakePicture)
			return;
		
		mShouldTakePicture=false;
		mAutoFocusDone = false;
		Log.i(TAG, "doTakePicture");
		mCamera.takePicture(null, null, mPictureCallback);
	}
	private void startPreview() {
		if (cameraConfigured && mCamera != null) {

			mCamera.setPreviewCallback(previewCb);
			mCamera.startPreview();
			// camera.autoFocus(autoFocusCB);

			inPreview = true;
		}
	}
	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

		}
	};
	PictureCallback mPictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = Utils.getFileByName(Utils.getNewFileName()); 

			// Log.i(TAG,"savingPicture "+pictureFile.getAbsolutePath());
			try {
				// save image locally
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				switch(mOrientation){
				
				case 90:{
					setExifRotation(pictureFile.getAbsolutePath(),String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
					break;
				}
				case 180:{
					setExifRotation(pictureFile.getAbsolutePath(),String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
					break;
				}
				}
				
			} catch (FileNotFoundException e) {
				Log.e(TAG, "" + e.getMessage());
				return;
			} catch (IOException e) {
				Log.e(TAG, "" + e.getMessage());
				return;
			}
			Log.i(TAG, "savingPicture DONE " + pictureFile.getAbsolutePath());

			onPictureSavedLocally(pictureFile);
		}

	};
	public static boolean setExifRotation(String imgPath,String rotation){
		
		try {
			ExifInterface newExif = new ExifInterface(imgPath);

			newExif.setAttribute(ExifInterface.TAG_ORIENTATION, rotation);
			newExif.saveAttributes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Called after camera image saved on SD card shows options to upload or
	 * retry
	 */
	protected void onPictureSavedLocally(File pictureFile) {
		
		new AsyncRequest(new AsyncCallback<String>() {
			
			@Override
			public void onFinish(String response, Exception e) {
				if(e==null){
					Log.i(TAG,"onFinish upload "+response);
					
				}else{
					Toast.makeText(SnapShotFragment.this.getActivity(), "error uploading "+e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
			//TODO fix null json input -> add id etc !!!!!
		}).execute(new RequestCloseChore(pictureFile.getAbsolutePath(), null));
	}
}
