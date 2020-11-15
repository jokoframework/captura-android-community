package py.com.sodep.mobileforms.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.core.CameraUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by jmpr on 27/02/15.
 */
public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private static final String LOG_TAG = SimpleScannerActivity.class.getSimpleName();

    public static final String RESULT_TEXT = "text";

    public static final String EXTRA_ID = "id";

    public static final String RESULT_BARCODE_FORMAT = "barcode_format";

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            Camera camera = CameraUtils.getCameraInstance();
            if(camera!=null) {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
            }
        }*/
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v(LOG_TAG, rawResult.getText());
        Log.v(LOG_TAG, rawResult.getBarcodeFormat().toString()); //para que podemos querer el formato?
        Intent result = new Intent();
        result.putExtra(RESULT_TEXT, rawResult.getText());
        result.putExtra(RESULT_BARCODE_FORMAT, rawResult.getBarcodeFormat().toString());
        Long id = getIntent().getLongExtra(EXTRA_ID, -1);
        result.putExtra(EXTRA_ID, id);
        setResult(RESULT_OK, result);
        finish();
    }
}
