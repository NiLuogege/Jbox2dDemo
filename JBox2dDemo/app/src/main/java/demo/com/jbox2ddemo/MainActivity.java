package demo.com.jbox2ddemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private JboxLayout mJl;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    float f;

    private int mipmaps[] = {R.mipmap.share_fb,
            R.mipmap.share_kongjian,
            R.mipmap.share_pyq,
            R.mipmap.share_qq,
            R.mipmap.share_tw,
            R.mipmap.share_wechat,
            R.mipmap.share_weibo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        Log.e("tag", "1/60=" + 1 / 60);
        Log.e("tag", "1/60f=" + 1 / 60f);
        Log.e("tag", "1f/60f=" + 1f / 60f);

        mJl = (JboxLayout) findViewById(R.id.jl);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        addView();

    }

    private void addView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < mipmaps.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(mipmaps[i]);
            imageView.setTag(R.id.jbox_cycle_view, true);
            mJl.addView(imageView, layoutParams);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            mJl.onSensorChanged(-x * 10, y * 10);
//            if (BuildConfig.DEBUG) Log.e("MainActivity", "x:" + x + "y:" + y + "z:" + z);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
