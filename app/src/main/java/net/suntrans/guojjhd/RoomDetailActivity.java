package net.suntrans.guojjhd;

import android.databinding.DataBindingUtil;
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

import net.suntrans.guojjhd.bean.DeviceEntity;
import net.suntrans.guojjhd.databinding.ActivityRoomDetailBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class RoomDetailActivity extends BasedActivity {
    private List<DeviceEntity.DataBean.ListsBean > socketDatas;
    private List<DeviceEntity.DataBean.ListsBean > lightDatas;
    private ActivityRoomDetailBinding binding;
    private RoomDetailActivity.DeviceAdapter lightAdapter;
    private RoomDetailActivity.DeviceAdapter socketAdapter;
    private String id;

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


        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        System.out.println("id="+id);
        binding.title.setText(name);

        lightAdapter = new RoomDetailActivity.DeviceAdapter(R.layout.item_room_detail_devices, lightDatas);
        socketAdapter = new RoomDetailActivity.DeviceAdapter(R.layout.item_room_detail_devices, socketDatas);

        lightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        socketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        binding.lightRecyclerView.setAdapter(lightAdapter);
        binding.socketRecyclerView.setAdapter(socketAdapter);
    }


    private class DeviceAdapter extends BaseQuickAdapter<DeviceEntity.DataBean.ListsBean , BaseViewHolder> {

        public DeviceAdapter(int layoutResId, @Nullable List<DeviceEntity.DataBean.ListsBean > data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DeviceEntity.DataBean.ListsBean item) {
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(item.status.equals("1") ? R.drawable.ic_light_on : R.drawable.ic_light_off);
            helper.setText(R.id.name, item.name);
        }
    }

}
