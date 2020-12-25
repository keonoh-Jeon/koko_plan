package com.koko_plan.sub;

import android.util.Log;

public class RandomGoodText {

    private static final String TAG = "RandomGoodText" ;

    private static String[] text = {
            "내면의 작은 가능성의 불꽃을 부채질해 성취의 불기둥을 만듦으로써 자기 자신을 최대한 활용하라.",
            "겉치레 말을 하는 사람이 친구라면, 늑대는 개다",
            "좋은 친구, 좋은 책, 그리고 나른한 의식. 이것이 이상적 삶이다",
            "세상은 온통 문이고, 온통 기회이고, 울려주길 기다리는 팽팽한 줄이다.",
            "몸을 건강하게 지키는 것은 의무다. 그렇지 않으면 우리의 정신을 강인하고 맑게 지킬수 없다.",
            "친구의 집에 자주 가라, 길을 걷지 않는다면 잡초가 그 맥을 끊어버릴 것이니.",
            "누군가 다른 사람이 내 삶을 보다 윤택하게, 보다 만족스럽게 해주길 바란다면 내 손과발은 줄곧 묶여있는 꼴이다.",
            "기회는 모든 사람들의 문을 한 번쯤 노크한다. 어떤 사람의 집에서는 망치로 두들기듯 문을 부셔서라도 집에 들어간다.",
            "바르게, 아름답게, 정의롭게 사는 것, 이것은 모두 하나이다.",
            "행복은 사랑을 받는 것보다는 사랑을 주는데서 오는 것이다.",
            "사랑하고 자주 상처를 입는 것, 그리고 또다시 사랑하는 것. 용감한 삶.",
            "친절은 사회를 함께 묶어주는 황금 사슬이다.",
            "인간은 인연으로 엮어 만든 하나의 매듭, 망, 그물이다.",
            "불신하는 자는 불신을 당한다.",
            "그 누구도 당신의 동의없이 당신을 열등하다고 느끼게 만들수 없다.",
            "처음으로 진정 자기 자신을 마음껏 비웃어본 날, 당신은 성장한다.",
            "자신을 있는 그대로 받아들이고 자주적으로 생각하라.",
            "혼자 살다보면 모든 성가신 습성들이 사라져버린다.",
            ""

    };

    public static String make(){

        int randomNum = (int)(Math.random() * text.length);
        Log.e(TAG, "make: " + text.length);
        return text[randomNum];
    }

    public static int getTextnum(){

        int randomNum = (int)(Math.random() * text.length);
        return randomNum;

    }
}
