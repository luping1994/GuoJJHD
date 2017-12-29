package net.suntrans.bieshuhd;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pgyersdk.update.PgyUpdateManager;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.suntrans.bieshuhd.api.Api;
import net.suntrans.bieshuhd.api.RetrofitHelper;
import net.suntrans.bieshuhd.bean.DeviceEntity;
import net.suntrans.bieshuhd.bean.EnergyEntity;
import net.suntrans.bieshuhd.bean.EnvEntity;
import net.suntrans.bieshuhd.bean.HomeSceneBean;
import net.suntrans.bieshuhd.bean.NormalInfo;
import net.suntrans.bieshuhd.bean.RespondBody;
import net.suntrans.bieshuhd.bean.RoomEntity;
import net.suntrans.bieshuhd.databinding.ActivityMainBinding;
import net.suntrans.bieshuhd.rx.BaseSubscriber;
import net.suntrans.bieshuhd.util.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
    private List<HomeSceneBean.DataBean.ListsBean> sceneLists;
    private long envRefreshTime;
    private long energyRefreshTime;
    private long lightRefreshTime;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        api = RetrofitHelper.getApi();
        envRefreshTime = App.getSharedPreferences().getLong("envRefreshTime", 10 * 60 * 1000);
        energyRefreshTime = App.getSharedPreferences().getLong("energyRefreshTime", 10 * 60 * 1000);
        lightRefreshTime = App.getSharedPreferences().getLong("lightRefreshTime", 10 * 60 * 1000);

        String familyname = App.getSharedPreferences().getString("familyname", "我的家");
        binding.familyname.setText(familyname);

        DisplayMetrics metric = new DisplayMetrics();
        DisplayMetrics metric1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        getWindowManager().getDefaultDisplay().getMetrics(metric1);
        float density = metric.density;
        widthPixels = metric.widthPixels;
        heightPixels = metric.heightPixels;
        int densityDpi = metric.densityDpi;
//        System.out.println("density=" + density);
//        System.out.println("xdensity=" + metric.xdpi);
//        System.out.println("ydensity=" + metric.ydpi);
//        System.out.println("设备真实宽度=" + widthPixels);
//        System.out.println("设备宽度2=" + metric1.widthPixels);
//        System.out.println("设备高度2=" + metric1.heightPixels);
//        System.out.println("设备真实高度=" + heightPixels);
//        System.out.println("设备密度dpi=" + densityDpi);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.CHINESE);

        String format1 = sdf.format(new Date());

        binding.time.setText(format1);



        setUpFullScreen();

        setUpRecyclerView();


        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLight();
                getNormal();
                getEnergyData();
                getHomeScene();
                getRoom();
            }
        });

        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("是否注销登录?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getSharedPreferences().edit()
                                .putString("password", "")
                                .commit();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }).create().show();

            }
        });

        binding.goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("是否执行该场景?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (sceneLists != null && sceneLists.size() == 2) {

                                    conScene(sceneLists.get(0).id + "");
                                    System.out.println(sceneLists.get(0).id);
                                } else {
                                    UiUtils.showToast(getApplicationContext(), "无法获取场景");
                                }
                            }
                        }).create().show();
            }
        });
        binding.leaveHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("是否执行该场景?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (sceneLists != null && sceneLists.size() == 2) {

                                    conScene(sceneLists.get(1).id + "");
                                } else {
                                    UiUtils.showToast(getApplicationContext(), "无法获取场景");
                                }
                            }
                        }).create().show();
            }
        });
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
                if (devicesDatas == null || devicesDatas.size() == 0) {
                    UiUtils.showToast(getApplicationContext(), "请稍后");
                    return;
                }
                if (position == -1) {
                    UiUtils.showToast(getApplicationContext(), "请稍后");
                    return;
                }
                sendCmd(devicesDatas.get(position).id, devicesDatas.get(position).status.equals("1") ? "0" : "1");

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

//        handler.postDelayed(energyRunable, 500);
        PgyUpdateManager.register(this,"net.suntrans.bieshuhd.fileProvider");

        getRoom();
        getNormal();
        getHomeScene();

        handler.post(envRunable);
        handler.post(energyRunable);
        handler.post(lightRunable);
//        timer1.schedule(task1,envRefreshTime);
//        timer2.schedule(task2,energyRefreshTime);
//        timer3.schedule(task3,lightRefreshTime);
    }

    private Timer timer1 = new Timer(true);
    private Timer timer2 = new Timer(true);
    private Timer timer3 = new Timer(true);
    private TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            getEnvData();
        }
    };
    private TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            getEnergyData();
        }
    };
    private TimerTask task3 = new TimerTask() {
        @Override
        public void run() {
            getLight();
        }
    };


    private class RoomAdapter extends BaseQuickAdapter<RoomEntity.DataBean.ListsBean, BaseViewHolder> {

        private int width;

        public RoomAdapter(int layoutResId, @Nullable List<RoomEntity.DataBean.ListsBean> data) {
            super(layoutResId, data);
            width = getResources().getDimensionPixelSize(R.dimen.item_room_text_size);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomEntity.DataBean.ListsBean item) {

            helper.setText(R.id.name,item.name)
                    .addOnClickListener(R.id.image);
            ImageView imageView = helper.getView(R.id.image);
            TextView textview = helper.getView(R.id.name);
            Glide.with(MainActivity.this)
                    .load(item.img_small)
                    .placeholder(R.drawable.ic_room)
                    .transform(new GlideRoundTransform(MainActivity.this, radioSize))
                    .into(imageView);

        }

        private void reSizeTextView(TextView textView, String text, float maxWidth) {
            Paint paint = textView.getPaint();
            float textWidth = paint.measureText(text);
            int textSizeInDp = 35;

            if (textWidth > maxWidth) {
                for (; textSizeInDp > 0; textSizeInDp--) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSizeInDp);
                    paint = textView.getPaint();
                    textWidth = paint.measureText(text);
                    if (textWidth <= maxWidth) {
                        break;
                    }
                }
            }
            textView.invalidate();
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.CHINESE);

            String format1 = sdf.format(new Date());

            binding.time.setText(format1);
            handler.postDelayed(this, envRefreshTime);

        }
    };
    private Runnable energyRunable = new Runnable() {
        @Override
        public void run() {
            getEnergyData();
            handler.postDelayed(this, energyRefreshTime);
        }
    };
    private Runnable lightRunable = new Runnable() {
        @Override
        public void run() {
            getLight();
            handler.postDelayed(this, lightRefreshTime);
        }
    };

    private Handler handler = new Handler();

    private void getEnvData() {
        api
                .getEnvData()
                .compose(this.<EnvEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<EnvEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(EnvEntity envEntity) {
                        super.onNext(envEntity);


                        binding.wendu.setText("" + envEntity.data.wendu.value + envEntity.data.wendu.unit);
                        binding.shidu.setText("" + envEntity.data.shidu.value + envEntity.data.shidu.unit);
                        binding.jiaquan.setText("" + envEntity.data.jiaquan.value + envEntity.data.jiaquan.unit);
                        binding.pm25.setText("" + envEntity.data.pm25.value + envEntity.data.pm25.unit);

//                        binding.wenduText.setText(envEntity.data.wendu.text);
//                        binding.wenduText.setTextColor(Color.parseColor(envEntity.data.wendu.color));

                        binding.shiduText.setText(envEntity.data.shidu.text);
                        binding.shiduText.setTextColor(Color.parseColor(envEntity.data.shidu.color));

                        binding.jiaquanText.setText(envEntity.data.jiaquan.text);
                        binding.jiaquanText.setTextColor(Color.parseColor(envEntity.data.jiaquan.color));

                        binding.pmText.setText(envEntity.data.pm25.text);
                        binding.pmText.setTextColor(Color.parseColor(envEntity.data.pm25.color));

                    }
                });
    }

    private void getEnergyData() {
        api
                .getEnergyData()
                .compose(this.<EnergyEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<EnergyEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onNext(EnergyEntity envEntity) {
                        super.onNext(envEntity);
                        binding.benyueyongdian.setText(envEntity.data.lists.get(0).month + "kW·h");
                        binding.jinriyongdian.setText(envEntity.data.lists.get(0).today + "kW·h");
                        binding.zuoriyongdian.setText(envEntity.data.lists.get(0).yesterday + "kW·h");
                        binding.fuzai.setText(envEntity.data.lists.get(0).Power + "kW");

                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    private void getRoom() {
        api
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
        api
                .getLight()
                .compose(this.<DeviceEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<DeviceEntity>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onNext(DeviceEntity envEntity) {
                        super.onNext(envEntity);
                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                        devicesDatas.clear();
                        devicesDatas.addAll(envEntity.data.lists);
                        deviceAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void getNormal() {
        api.getNormal()
                .compose(this.<NormalInfo>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<NormalInfo>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(NormalInfo envEntity) {
                        super.onNext(envEntity);
                        List<String> device = envEntity.data.device;
                        if (device.size() == 3) {
                            binding.device1.setText(device.get(0));
                            binding.device2.setText(device.get(1));
                            binding.device3.setText(device.get(2));
                            boolean equals = envEntity.data.status.equals("1");
                            if (equals) {

                                binding.device2Status.setBackgroundResource(R.drawable.green);
                            } else {
                                binding.device2Status.setBackgroundResource(R.drawable.red);

                            }
                        }

                    }
                });
    }

    private void getHomeScene() {
        api
                .getHomeScene()
                .compose(this.<HomeSceneBean>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<HomeSceneBean>(getApplicationContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }

                    @Override
                    public void onNext(HomeSceneBean envEntity) {
                        super.onNext(envEntity);
                        sceneLists = envEntity.data.lists;
                        if (sceneLists != null) {
                            if (sceneLists.size() == 2) {
                                binding.goHome.setText(sceneLists.get(0).name);
                                binding.leaveHome.setText(sceneLists.get(1).name);
                            }
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    boolean sending = false;

    private void sendCmd(String channel_id, String cmd) {
        if (sending) {
            UiUtils.showToast(getApplicationContext(), "请稍后");
            return;
        }
        sending = true;
        api.sendCmd(channel_id, cmd)
                .compose(this.<RespondBody>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<RespondBody>(this) {
                    @Override
                    public void onNext(RespondBody respondBody) {
                        super.onNext(respondBody);
                        sending = false;
                        if (respondBody.code != 200)
                            UiUtils.showToast(getApplicationContext(), respondBody.msg);
                        getLight();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        sending = false;
                        getLight();

                    }
                });
    }

    private void conScene(String sceneid) {

        api.conScene(sceneid)
                .compose(this.<RespondBody>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<RespondBody>(this) {
                    @Override
                    public void onNext(RespondBody respondBody) {
                        super.onNext(respondBody);
                        UiUtils.showToast(getApplicationContext(), respondBody.msg);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getLight();
                            }
                        }, 500);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }


}
