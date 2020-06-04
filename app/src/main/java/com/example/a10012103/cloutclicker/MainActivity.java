package com.example.a10012103.cloutclicker;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    AtomicInteger clout= new AtomicInteger(0);
    int bInt= 0;
    int gInt= 0;
    int bCount= 0;
    int gCount= 0;
    int bCost= 20;
    int gCost= 100;
    int bIncrement= 5;
    int gIncrement= 25;
    TextView tClout, tCPS, tBCount, tGCount, tBCost, tGCost;
    ImageView iSupreme, iBelt, iGlasses;
    Button bReset;
    ConstraintLayout layout;
    ArrayList<ImageView> powerUps= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ScaleAnimation scaleAnimation= new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);

        tClout= findViewById(R.id.id_tClout);
        tCPS= findViewById(R.id.id_tCPS);
        tBCount= findViewById(R.id.id_tBCount);
        tGCount= findViewById(R.id.id_tGCount);
        tBCost= findViewById(R.id.id_tBCost);
        tGCost= findViewById(R.id.id_tGCost);
        iSupreme= findViewById(R.id.id_iSupreme);
        iBelt= findViewById(R.id.id_iBelt);
        iGlasses= findViewById(R.id.id_iGlasses);
        bReset= findViewById(R.id.id_bReset);
        layout= findViewById(R.id.id_layout);

        tBCost.setText("Cost: "+bCost+" Clout");
        tGCost.setText("Cost: "+gCost+" Clout");


        Thread autoAddThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    clout.getAndAdd(bInt+gInt);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                }
            }
        };

        autoAddThread.start();

        iSupreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);
                clout.getAndAdd(1);
                plusOne();
                refresh();
            }
        });
        iBelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clout.intValue()>=bCost) {
                    clout.getAndAdd(bCost*-1);
                    bInt+= bIncrement;
                    bCount++;
                    newBelt();
                }
                refresh();
            }
        });
        iGlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clout.intValue()>=gCost) {
                    clout.getAndAdd(gCost*-1);
                    gInt+= gIncrement;
                    gCount++;
                    newGlasses();
                }
                refresh();
            }
        });
        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bInt= 0;
                gInt= 0;
                bCount= 0;
                gCount= 0;
                clout.set(0);
                for (ImageView i: powerUps)
                    i.setVisibility(View.INVISIBLE);
                powerUps.clear();
                refresh();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clout", clout.intValue());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        clout= new AtomicInteger(savedInstanceState.getInt("clout"));
    }

    public void refresh() {

        final ScaleAnimation scaleAnimation= new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);

        if (clout.get()<20) {
            iBelt.setVisibility(View.INVISIBLE);
            tBCost.setVisibility(View.INVISIBLE);
            iGlasses.setVisibility(View.INVISIBLE);
            tGCost.setVisibility(View.INVISIBLE);
        }
        else if (clout.get()>=20 && clout.get()<100) {
            if(iBelt.getVisibility()==View.INVISIBLE)
                iBelt.startAnimation(scaleAnimation);
            iBelt.setVisibility(View.VISIBLE);
            tBCost.setVisibility(View.VISIBLE);
            iGlasses.setVisibility(View.INVISIBLE);
            tGCost.setVisibility(View.INVISIBLE);
        }
        else {
            if (iBelt.getVisibility()==View.INVISIBLE)
                iBelt.startAnimation(scaleAnimation);
            iBelt.setVisibility(View.VISIBLE);
            tBCost.setVisibility(View.VISIBLE);
            if (iGlasses.getVisibility()==View.INVISIBLE)
                iGlasses.startAnimation(scaleAnimation);
            iGlasses.setVisibility(View.VISIBLE);
            tGCost.setVisibility(View.VISIBLE);
        }

        tClout.setText("Clout Level: "+clout);
        tCPS.setText("Clout per Second: "+(bInt+gInt));
        tBCount.setText("Gucci Belts: "+bCount);
        tGCount.setText("Clout Glasses: "+gCount);
    }

    public void plusOne() {
        TextView tPlusOne= new TextView(this);
        tPlusOne.setId(View.generateViewId());
        tPlusOne.setText("+1");
        tPlusOne.setTextColor(Color.WHITE);
        tPlusOne.bringToFront();

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        tPlusOne.setLayoutParams(params);

        layout.addView(tPlusOne);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(tPlusOne.getId(), ConstraintSet.TOP, iSupreme.getId(), ConstraintSet.TOP);
        set.connect(tPlusOne.getId(), ConstraintSet.BOTTOM, iSupreme.getId(), ConstraintSet.BOTTOM);
        set.connect(tPlusOne.getId(), ConstraintSet.LEFT, iSupreme.getId(), ConstraintSet.LEFT);
        set.connect(tPlusOne.getId(), ConstraintSet.RIGHT, iSupreme.getId(), ConstraintSet.RIGHT);

        set.setVerticalBias(tPlusOne.getId(),(float)Math.random());
        set.setHorizontalBias(tPlusOne.getId(),(float)Math.random());

        set.applyTo(layout);

        float delta = -100;
        float finalY = tPlusOne.getY()+delta;
        final TranslateAnimation translateAnimation= new TranslateAnimation(tPlusOne.getX(), tPlusOne.getX(), tPlusOne.getY(), finalY);

        translateAnimation.setDuration(200);
        tPlusOne.startAnimation(translateAnimation);
        tPlusOne.setVisibility(View.INVISIBLE);
    }

    public void newBelt(){
        ImageView belt = new ImageView(this);
        belt.setId(View.generateViewId());
        belt.setImageResource(R.drawable.belt);
        powerUps.add(belt);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100,100);
        belt.setLayoutParams(params);

        layout.addView(belt);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(belt.getId(), ConstraintSet.TOP, tBCount.getId(), ConstraintSet.BOTTOM);
        set.connect(belt.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        set.connect(belt.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        set.connect(belt.getId(), ConstraintSet.RIGHT, tBCount.getId(), ConstraintSet.RIGHT);

        set.setVerticalBias(belt.getId(), (float) (Math.random()*0.5+0.25));
        set.setHorizontalBias(belt.getId(), (float) Math.random());

        set.applyTo(layout);

        final ScaleAnimation scaleAnimation= new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        belt.setAnimation(scaleAnimation);
    }

    public void newGlasses(){
        ImageView glasses = new ImageView(this);
        glasses.setId(View.generateViewId());
        glasses.setImageResource(R.drawable.glasses);
        powerUps.add(glasses);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100,100);
        glasses.setLayoutParams(params);

        layout.addView(glasses);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(glasses.getId(), ConstraintSet.TOP, tGCount.getId(), ConstraintSet.BOTTOM);
        set.connect(glasses.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        set.connect(glasses.getId(), ConstraintSet.LEFT, tGCount.getId(), ConstraintSet.LEFT);
        set.connect(glasses.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

        set.setVerticalBias(glasses.getId(), (float) (Math.random()*0.5+0.5));
        set.setHorizontalBias(glasses.getId(), (float) Math.random());

        set.applyTo(layout);

        final ScaleAnimation scaleAnimation= new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        glasses.setAnimation(scaleAnimation);
    }



}

