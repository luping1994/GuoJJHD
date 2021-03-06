package net.suntrans.guojjhd;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.guojjhd.api.Api;
import net.suntrans.guojjhd.api.RetrofitHelper;
import net.suntrans.guojjhd.bean.DeviceEntity;
import net.suntrans.guojjhd.bean.RespondBody;
import net.suntrans.guojjhd.bean.RoomDetailEntity;
import net.suntrans.guojjhd.databinding.ActivityRoomDetailBinding;
import net.suntrans.guojjhd.rx.BaseSubscriber;
import net.suntrans.guojjhd.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/9.
 */

public class RoomDetailActivity extends BasedActivity {
    private List<DeviceEntity.DataBean.ListsBean> socketDatas;
    private List<DeviceEntity.DataBean.ListsBean> lightDatas;
    private ActivityRoomDetailBinding binding;
    private RoomDetailActivity.DeviceAdapter lightAdapter;
    private RoomDetailActivity.DeviceAdapter socketAdapter;
    private String id;
    private DisplayMetrics metric1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_detail);
        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        System.out.println("id=" + id);
        binding.title.setText(name);
        setUpFullScreen();
        setUpRecyclerView();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(id);
            }
        });
        metric1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metric1);
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
        lightDatas = new ArrayList<>();
        socketDatas = new ArrayList<>();


        lightAdapter = new RoomDetailActivity.DeviceAdapter(R.layout.item_room_detail_devices, lightDatas,"light");
        socketAdapter = new RoomDetailActivity.DeviceAdapter(R.layout.item_room_detail_devices, socketDatas,"socket");

        lightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (lightDatas == null || lightDatas.size() == 0)
                    return;
                if (position == -1)
                    return;
                sendCmd(lightDatas.get(position).id, lightDatas.get(position).status.equals("1") ? "0" : "1");
            }
        });
        socketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (socketDatas == null || socketDatas.size() == 0)
                    return;
                if (position == -1)
                    return;
                sendCmd(socketDatas.get(position).id, socketDatas.get(position).status.equals("1") ? "0" : "1");
            }
        });

        binding.lightRecyclerView.setAdapter(lightAdapter);
        binding.socketRecyclerView.setAdapter(socketAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(id);

    }

    private class DeviceAdapter extends BaseQuickAdapter<DeviceEntity.DataBean.ListsBean, BaseViewHolder> {

        private String type;
        public DeviceAdapter(int layoutResId, @Nullable List<DeviceEntity.DataBean.ListsBean> data,String type) {
            super(layoutResId, data);
            this.type = type;
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceEntity.DataBean.ListsBean item) {
            ImageView imageView = helper.getView(R.id.image);
            if (type.equals("socket")){
                imageView.setImageResource(item.status.equals("1") ? R.drawable.ic_socket_on : R.drawable.ic_socket_off);
            }else {

            imageView.setImageResource(item.status.equals("1") ? R.drawable.ic_light_on : R.drawable.ic_light_off);
            }
            helper.setText(R.id.name, item.name);
        }
    }

    private void getData(String id) {
        addSubscription(RetrofitHelper.getApi().getRoomDevices(id), new BaseSubscriber<RoomDetailEntity>(this) {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                binding.refreshLayout.setRefreshing(false);
                e.printStackTrace();
            }

            @Override
            public void onNext(RoomDetailEntity roomDetailEntity) {
                super.onNext(roomDetailEntity);
                lightDatas.clear();
                if (roomDetailEntity.data.light != null)
                    lightDatas.addAll(roomDetailEntity.data.light.lists);
                lightAdapter.notifyDataSetChanged();
                socketDatas.clear();
                if (roomDetailEntity.data.socket != null) {
                    List<DeviceEntity.DataBean.ListsBean> lists = roomDetailEntity.data.socket.lists;
                    socketDatas.addAll(lists);
                }

                Glide.with(RoomDetailActivity.this)
                        .load(roomDetailEntity.data.house.img_big)
                        .override(metric1.widthPixels,metric1.heightPixels)
                        .into(binding.bg);

                socketAdapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);

            }

        });
    }

    boolean sending = false;

    private void sendCmd(String channel_id, String cmd) {
        if (sending) {
            UiUtils.showToast(getApplicationContext(), "请稍后");
            return;
        }
        sending = true;
        RetrofitHelper.getApi().sendCmd(channel_id, cmd)
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
                        getData(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        sending = false;
                        getData(id);

                    }
                });
    }
}
