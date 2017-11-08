package net.suntrans.guojjhd;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.guojjhd.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int widthPixels;
    private int heightPixels;
    private ActivityMainBinding binding;

    private List<RoomEntity> roomDatas;
    private List<DeviceEntity> devicesDatas;
    private DeviceAdapter deviceAdapter;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        DisplayMetrics metric = new DisplayMetrics();
        DisplayMetrics metric1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        getWindowManager().getDefaultDisplay().getMetrics(metric1);
        float density = metric.density;
        widthPixels = metric.widthPixels;
        heightPixels = metric.heightPixels;
        int densityDpi = metric.densityDpi;

        System.out.println("density=" + density);
        System.out.println("xdensity=" + metric.xdpi);
        System.out.println("ydensity=" + metric.ydpi);
        System.out.println("设备宽度=" + widthPixels);
        System.out.println("设备宽度2=" + metric1.widthPixels);
        System.out.println("设备高度2=" + metric1.heightPixels);
        System.out.println("设备高度=" + heightPixels);
        System.out.println("设备密度dpi=" + densityDpi);

        setUpFullScreen();

        setUpRecyclerView();

    }


    private void setUpFullScreen() {
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setUpRecyclerView() {
        roomDatas = new ArrayList<>();
        devicesDatas = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            DeviceEntity entity = new DeviceEntity();
            entity.name = "照明灯" + i;
            entity.status = i % 2 == 0 ? false : true;
            devicesDatas.add(entity);
        }
        for (int i = 0; i < 8; i++) {
            RoomEntity entity = new RoomEntity();
            entity.name = "房间" + i;
            roomDatas.add(entity);
        }
        deviceAdapter = new DeviceAdapter(R.layout.item_devices, devicesDatas);
        roomAdapter = new RoomAdapter(R.layout.item_room, roomDatas);
        deviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                devicesDatas.get(position).status = !devicesDatas.get(position).status;
                deviceAdapter.notifyDataSetChanged();
            }
        });
        roomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(MainActivity.this,RoomDetailActivity.class));
            }
        });
        binding.roomRecyclerView.setAdapter(roomAdapter);
        binding.deviceRecyclerView.setAdapter(deviceAdapter);
    }

    private class RoomAdapter extends BaseQuickAdapter<RoomEntity, BaseViewHolder> {

        public RoomAdapter(int layoutResId, @Nullable List<RoomEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomEntity item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);


            Glide.with(MainActivity.this)
                    .load(R.drawable.ic_room)
                    .transform(new GlideRoundTransform(MainActivity.this, UiUtils.dip2px(MainActivity.this, 10)))
                    .into(imageView);

        }
    }

    private class DeviceAdapter extends BaseQuickAdapter<DeviceEntity, BaseViewHolder> {

        public DeviceAdapter(int layoutResId, @Nullable List<DeviceEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceEntity item) {
            ImageView imageView = helper.getView(R.id.image);
            imageView.setBackgroundResource(item.status ? R.drawable.bg_on : R.drawable.bg_off);
            helper.setText(R.id.name, item.name);
        }
    }

}
