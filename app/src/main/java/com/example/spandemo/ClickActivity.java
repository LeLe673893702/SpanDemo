package com.example.spandemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.spandemo.databinding.ActivityClickBinding;

public class ClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);
        findViewById(R.id.view1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TestDialogFragment().show(getSupportFragmentManager(), "TestDialogFragment");
                Log.d("ClickActivity", "view1");
            }
        });
        findViewById(R.id.view2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ClickActivity", "view2");
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(ClickActivity.this, MainActivity.class);
        startActivity(intent);
        Log.d("MainActivity", "finish");
    }


}