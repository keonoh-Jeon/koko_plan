package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.pref;

public class AlarmReceiver extends BroadcastReceiver {

  /*  //메니페스트 등록
    <receiver android:name=".sub.AlarmReceiver" />*/

    @SuppressLint({"WakelockTimeout", "InvalidWakeLockTag"})
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //연결되는 액티비티(돌아갈 액티비티)
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //헤더 클릭시 액티비티 이동시 필요
        PendingIntent pendingI = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //노티 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            //헤더의 사용 아이콘
            builder.setSmallIcon(R.drawable.habiticon); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남

            String channelName ="매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            //노티의 알람 강도를 설정
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            //노티의 다양한 설정 값 세팅(진동...)
            //채널은 초기값 세팅으로 되어 있어 수정을 하고 적용을 시키려면 어플리케이션을 제거하고 다시 설치하여 확인을 해야 한다고 한다.
            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);
            channel.enableVibration(true); // 진동 무음
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        } else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        //noti 세부 설정
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("습관 할당 시간 100% 도달!")
                .setContentText("'"+ pref.getString("alarmtitle", null) + "목표 시간을 달성하였습니다.")
                .setContentInfo("INFO")
                .setOngoing(true) // 사용자가 직접 못지우게 계속 실행하기.
                .setContentIntent(pendingI) //눌렀을때 액티비티 이동
                .setAutoCancel(true);

        if (notificationManager != null) {

            // 노티피케이션 동작시킴
            notificationManager.notify(1234, builder.build());
            /*Calendar nextNotifyTime = Calendar.getInstance();

            // 내일 같은 시간으로 알람시간 결정
            nextNotifyTime.add(Calendar.DATE, 1);

            //  Preference에 설정한 값 저장
            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
            editor.apply();

            Date currentDateTime = nextNotifyTime.getTime();
            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
            Toast.makeText(context.getApplicationContext(),"다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();*/
        }
    }



}
