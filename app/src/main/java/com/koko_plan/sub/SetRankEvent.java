package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;

import com.koko_plan.R;

import java.util.HashMap;

import static com.koko_plan.main.MainActivity.tvrankereffect;

public class SetRankEvent {

    public static boolean fulladview = true;
    public static boolean adview1 = true;
    public static boolean adview2 = true;
    public static boolean adview3 = true;
    public static boolean adview4 = true;
    public static boolean adview5 = true;
    public static boolean adview6 = true;
    public static boolean blurview1 = true;
    public static boolean blurview2 = true;
    public static boolean blurview3 = true;

    @SuppressLint("SetTextI18n")
    public static void set(float rankscore) {
        new Thread(() -> {
            if(99.94 < rankscore && rankscore <= 100) {
                tvrankereffect.setText("Iron IV 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                adview5 = true;
                adview6 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if(99.64 < rankscore && rankscore <= 99.94) {
                tvrankereffect.setText("Iron III 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                adview5 = true;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (98.94 < rankscore && rankscore <= 99.64) {
                tvrankereffect.setText("Iron II 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (97.93 < rankscore && rankscore <= 98.94) {
                tvrankereffect.setText("Iron I 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (95.53 < rankscore && rankscore <= 97.93) {
                tvrankereffect.setText("Bronze IV 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (92.78 < rankscore && rankscore <= 95.53) {
                tvrankereffect.setText("Bronze III 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (88.73 < rankscore && rankscore <= 92.78) {
                tvrankereffect.setText("Bronze II 보상 발동 중");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (82.76 < rankscore && rankscore <= 88.73) {
                tvrankereffect.setText("Bronze I 보상 발동 중");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = false;
            } else if (73.61 < rankscore && rankscore <= 82.76) {
                tvrankereffect.setText("Silver IV 보상 발동 중");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = true;
                blurview2 = false;
                blurview3 = false;
            } else if (66.31 < rankscore && rankscore <= 73.61) {
                tvrankereffect.setText("Silver III 보상 발동 중");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = false;
                blurview2 = false;
                blurview3 = false;
            } else if (57.53 < rankscore && rankscore <= 66.31) {
                tvrankereffect.setText("Silver II 보상 발동 중");
                fulladview = false;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = false;
                blurview2 = false;
                blurview3 = false;
            } else if (50.21 < rankscore && rankscore <= 57.53) {
                tvrankereffect.setText("Silver I 보상 발동 중");
                fulladview = false;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = false;
                blurview2 = false;
                blurview3 = false;
            } else if (36.76 < rankscore && rankscore <= 50.21) {
                tvrankereffect.setText("Gold IV 보상 발동 중");
                fulladview = false;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = false;
                blurview2 = false;
                blurview3 = false;
            } else if (29.14 < rankscore && rankscore <= 36.76) {
                tvrankereffect.setText("Gold III 보상 발동 중");
                fulladview = false;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = false;
                blurview2 = false;
                blurview3 = false;
            } else if (22.53 < rankscore && rankscore <= 29.14) {
                tvrankereffect.setText("Gold II 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (18.36 < rankscore && rankscore <= 22.53) {
                tvrankereffect.setText("Gold I 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (10.58 < rankscore && rankscore <= 18.36) {
                tvrankereffect.setText("Platinum IV 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (7.58 < rankscore && rankscore <= 10.58) {
                tvrankereffect.setText("Platinum III 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (5.59 < rankscore && rankscore <= 7.58) {
                tvrankereffect.setText("Platinum II 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (3.67 < rankscore && rankscore <= 5.59) {
                tvrankereffect.setText("Platinum I 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (1.45 < rankscore && rankscore <= 3.67) {
                tvrankereffect.setText("Diamond IV 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (0.68 < rankscore && rankscore <= 1.45) {
                tvrankereffect.setText("Diamond III 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (0.31 < rankscore && rankscore <= 0.68) {
                tvrankereffect.setText("Diamond II 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (0.11 < rankscore && rankscore <= 0.31) {
                tvrankereffect.setText("Diamond I 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (0.06 < rankscore && rankscore <= 0.11) {
                tvrankereffect.setText("Master 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (0.02 < rankscore && rankscore <= 0.06) {
                tvrankereffect.setText("G_Master 보상 발동 중");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (0 < rankscore && rankscore <= 0.02) {
                tvrankereffect.setText("Challenger 보상 발동 중");
                fulladview = false;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                blurview1 = false;
                blurview2 = false;
                blurview3 = false;
            }
        }).start();
    }
}
