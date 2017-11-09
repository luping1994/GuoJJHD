package net.suntrans.guojjhd;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.suntrans.guojjhd.api.RetrofitHelper;
import net.suntrans.guojjhd.bean.DeviceEntity;
import net.suntrans.guojjhd.bean.EnergyEntity;
import net.suntrans.guojjhd.bean.EnvEntity;
import net.suntrans.guojjhd.bean.RoomEntity;
import net.suntrans.guojjhd.databinding.ActivityMainBinding;
import net.suntrans.guojjhd.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    private int widthPixels;
    private int heightPixels;
    private ActivityMainBinding binding;

    private List<RoomEntity.DataBean.ListsBean> roomDatas;
    private List<DeviceEntity.DataBean.ListsBean> devicesDatas;
    private DeviceAdapter deviceAdapter;
    private RoomAdapter roomAdapter;
    private int radioSize;

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
        radioSize = getResources().getDimensionPixelSize(R.dimen.item_room_img_radio_size);


        deviceAdapter = new DeviceAdapter(R.layout.item_devices, devicesDatas);
        roomAdapter = new RoomAdapter(R.layout.item_room, roomDatas);
        deviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        roomAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, RoomDetailActivity.class);
                intent.putExtra("id", roomDatas.get(position).id);
                intent.putExtra("name", roomDatas.get(position).name);
                startActivity(intent);
            }
        });
        binding.roomRecyclerView.setAdapter(roomAdapter);
        binding.deviceRecyclerView.setAdapter(deviceAdapter);
        handler.post(envRunable);
        handler.postDelayed(energyRunable, 500);
    }

    private class RoomAdapter extends BaseQuickAdapter<RoomEntity.DataBean.ListsBean, BaseViewHolder> {

        public RoomAdapter(int layoutResId, @Nullable List<RoomEntity.DataBean.ListsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomEntity.DataBean.ListsBean item) {
            helper.setText(R.id.name, item.name)
                    .addOnClickListener(R.id.image);
            ImageView imageView = helper.getView(R.id.image);


            Glide.with(MainActivity.this)
                    .load(item.img_url)
                    .placeholder(R.drawable.ic_room)
                    .transform(new GlideRoundTransform(MainActivity.this, radioSize))
                    .into(imageView);

        }
    }

    private class DeviceAdapter extends BaseQuickAdapter<DeviceEntity.DataBean.ListsBean, BaseViewHolder> {

        public DeviceAdapter(int layoutResId, @Nullable List<DeviceEntity.DataBean.ListsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceEntity.DataBean.ListsBean item) {
            ImageView imageView = helper.getView(R.id.image);
            imageView.setBackgroundResource(item.status.equals("1") ? R.drawable.ic_light_on : R.drawable.ic_light_off);
            helper.setText(R.id.name, item.name);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private Runnable envRunable = new Runnable() {
        @Override
        public void run() {
            getEnvData();
            handler.postDelayed(this, 10000);
        }
    };
    private Runnable energyRunable = new Runnable() {
        @Override
        public void run() {
            getEnergyData();
            handler.postDelayed(this, 30 * 60 * 1000);
        }
    };

    private Handler handler = new Handler();

    private void getEnvData() {
        RetrofitHelper.getApi()
                .getEnvData()
                .compose(this.<EnvEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<EnvEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(EnvEntity envEntity) {
                        super.onNext(envEntity);
                        binding.wendu.setText("" + envEntity.data.wendu + "℃");
                        binding.shidu.setText("湿度：" + envEntity.data.shidu + "%");
                        binding.jiaquan.setText("甲醛：" + envEntity.data.jiaquan + "ppm");
                        binding.pm25.setText("PM2.5：" + envEntity.data.pm25 + "");
                    }
                });
    }

    private void getEnergyData() {
        RetrofitHelper.getApi()
                .getEnergyData()
                .compose(this.<EnergyEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<EnergyEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(EnergyEntity envEntity) {
                        super.onNext(envEntity);
                        binding.benyueyongdian.setText(envEntity.data.month + "");
                        binding.jinriyongdian.setText(envEntity.data.today + "kW·h");
                        binding.zuoriyongdian.setText(envEntity.data.yesterday + "kW·h");
                        binding.fuzai.setText(envEntity.data.power + "kW·h");

                    }
                });
    }

    private void getRoom() {
        RetrofitHelper.getApi()
                .getRoom()
                .compose(this.<RoomEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<RoomEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(RoomEntity envEntity) {
                        super.onNext(envEntity);
                        roomDatas.clear();
                        roomDatas.addAll(envEntity.data.lists);
                        roomAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void getLight() {
        RetrofitHelper.getApi()
                .getLight()
                .compose(this.<DeviceEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<DeviceEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(DeviceEntity envEntity) {
                        super.onNext(envEntity);
                        devicesDatas.clear();
                        devicesDatas.addAll(envEntity.data.lists);
                        deviceAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRoom();
        getLight();
    }
}
