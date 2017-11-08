package net.suntrans.guojjhd;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.guojjhd.databinding.ActivityRoomDetailBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class RoomDetailActivity extends AppCompatActivity {
    private List<DeviceEntity> socketDatas;
    private List<DeviceEntity> lightDatas;
    private ActivityRoomDetailBinding binding;
    private RoomDetailActivity.DeviceAdapter lightAdapter;
    private RoomDetailActivity.DeviceAdapter socketAdapter;

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
        lightDatas = new ArrayList<>();
        socketDatas = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            DeviceEntity entity = new DeviceEntity();
            entity.name = "照明灯" + i;
            entity.status = i % 2 == 0 ? false : true;
            lightDatas.add(entity);
        }
        for (int i = 0; i < 6; i++) {
            DeviceEntity entity = new DeviceEntity();
            entity.name = "插座" + i;
            entity.status = i % 2 == 0 ? false : true;
            socketDatas.add(entity);
        }
        lightAdapter = new RoomDetailActivity.DeviceAdapter(R.layout.item_room_detail_devices, lightDatas);
        socketAdapter = new RoomDetailActivity.DeviceAdapter(R.layout.item_room_detail_devices, socketDatas);

        lightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                lightDatas.get(position).status = !lightDatas.get(position).status;
                lightAdapter.notifyDataSetChanged();
            }
        });
        socketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                socketDatas.get(position).status = !socketDatas.get(position).status;
                socketAdapter.notifyDataSetChanged();
            }
        });

        binding.lightRecyclerView.setAdapter(lightAdapter);
        binding.socketRecyclerView.setAdapter(socketAdapter);
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
