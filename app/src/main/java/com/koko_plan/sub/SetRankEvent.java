package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;

import com.koko_plan.R;

import java.util.HashMap;

import static com.koko_plan.main.MainActivity.tveventeffect;
import static com.koko_plan.main.MainActivity.tvrankereffect;

public class SetRankEvent {

    public static boolean fulladview;
    public static boolean adview1;
    public static boolean adview2;
    public static boolean adview3;
    public static boolean adview4;
    public static boolean adview5;
    public static boolean adview6;
    public static boolean adview7;
    public static boolean blurview1;
    public static boolean blurview2;
    public static boolean blurview3;
    public static boolean blurview4;

    @SuppressLint("SetTextI18n")
    public static void set(float rankscore) {
        new Thread(() -> {

            fulladview = true;
            adview1 = true;
            adview2 = true;
            adview3 = true;
            adview4 = true;
            adview5 = true;
            adview6 = true;
            adview7 = true;
            blurview1 = true;
            blurview2 = true;
            blurview3 = true;

            if(99.94 < rankscore && rankscore <= 100) {
                tvrankereffect.setText("Iron IV 보상 없슴");
//                tveventeffect.setText(" - 해당 보상 내용 없슴");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거" +
                        "\n - [습관 리스트] 상단 배너 광고 제거"+
                        "\n - [전체습관 리스트] 하단 배너 광고 제거"+
                        "\n - [습관 달성 순위] 하단 배너 광고 제거");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                adview5 = true;
                adview6 = true;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if(99.64 < rankscore && rankscore <= 99.94) {
                tvrankereffect.setText("Iron III 보상 발동 중");
                tveventeffect.setText(" - [선물 받은 명언] 보이기"+
                        " - [상세 습관] 보이기");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = true;
                adview5 = true;
                adview6 = true;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = false;
                blurview4 = false;
            } else if (98.94 < rankscore && rankscore <= 99.64) {
                tvrankereffect.setText("Iron II 보상 발동 중");
                tveventeffect.setText(" - [선물 받은 명언] 보이기" +
                        "\"- [명언 리스트] 상단 배너 광고 제거");
                fulladview = true;
                adview1 = true;
                adview2 = true;
                adview3 = true;
                adview4 = false;
                adview5 = true;
                adview6 = true;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = false;
            } else if (97.93 < rankscore && rankscore <= 98.94) {
                tvrankereffect.setText("Iron I 보상 발동 중");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거");
                fulladview = true;
                adview1 = true;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = true;
                adview6 = true;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (95.53 < rankscore && rankscore <= 97.93) {
                tvrankereffect.setText("Bronze IV 보상 발동 중");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거" +
                        "\n - [습관 리스트] 상단 배너 광고 제거");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = true;
                adview6 = true;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (92.78 < rankscore && rankscore <= 95.53) {
                tvrankereffect.setText("Bronze III 보상 발동 중");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거" +
                        "\n - [습관 리스트] 상단 배너 광고 제거"+
                        "\n - [전체습관 리스트] 하단 배너 광고 제거");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = true;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (88.73 < rankscore && rankscore <= 92.78) {
                tvrankereffect.setText("Bronze II 보상 발동 중");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거" +
                        "\n - [습관 리스트] 상단 배너 광고 제거"+
                        "\n - [전체습관 리스트] 하단 배너 광고 제거"+
                        "\n - [상세 습관 보기] 하단 배너 광고 제거");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                adview7 = true;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (82.76 < rankscore && rankscore <= 88.73) {
                tvrankereffect.setText("Bronze I 보상 발동 중");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거" +
                        "\n - [습관 리스트] 상단 배너 광고 제거"+
                        "\n - [전체습관 리스트] 하단 배너 광고 제거"+
                        "\n - [상세 습관 보기] 하단 배너 광고 제거"+
                        "\n - [습관 달성 순위] 하단 배너 광고 제거");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                adview7 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
            } else if (73.61 < rankscore && rankscore <= 82.76) {
                tvrankereffect.setText("Silver IV 보상 발동 중");
                tveventeffect.setText(" - [명언 리스트] 상단 배너 광고 제거" +
                        "\n - [월간 성취율] 상단 배너 광고 제거" +
                        "\n - [습관 비중 추이] 상단 배너 광고 제거" +
                        "\n - [습관 리스트] 상단 배너 광고 제거"+
                        "\n - [전체습관 리스트] 하단 배너 광고 제거"+
                        "\n - [상세 습관 보기] 하단 배너 광고 제거"+
                        "\n - [습관 달성 순위] 하단 배너 광고 제거"+
                        "\n - [습관 달성 순위] 하단 배너 광고 제거");
                fulladview = true;
                adview1 = false;
                adview2 = false;
                adview3 = false;
                adview4 = false;
                adview5 = false;
                adview6 = false;
                adview7 = false;
                blurview1 = true;
                blurview2 = true;
                blurview3 = true;
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
