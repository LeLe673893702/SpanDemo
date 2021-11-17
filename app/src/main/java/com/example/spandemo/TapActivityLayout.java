package com.example.spandemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TapActivityLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_layout);
        HotKeysFlowLayout tapFlowLayoutV2 = findViewById(R.id.tbFlow);

        List<HotKeysWrapper.HotKey> hotKeys = new ArrayList<>();
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "道具道具道具"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "道具"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "道具剧"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "道具怎么用"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "怎么去"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "好吧好吧好"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "不行不行不是"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈会"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈s哈"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈sfa"));
        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈哈哈"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa哈哈哈ddfsafa哈哈哈ddfsafa哈哈哈ddfsafa哈哈哈ddfsafa哈哈哈ddfsafa"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa111111111111111111111111111111111111111111"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈12322"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa11111111111111111"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈ddfsafa"));
//        hotKeys.add(new HotKeysWrapper.HotKey(1l, "哈哈哈", "哈哈哈111111111111111111333333333333333"));
        tapFlowLayoutV2.setTagAdapter(new HotKeysFlowAdapter(this, hotKeys));

    }
}