package com.koko_plan;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version = 1, exportSchema = false)
public abstract class MemoDatabase extends RoomDatabase {

    public abstract MemoDao memoDao();

    private static volatile MemoDatabase INSTANCE;

    //싱글톤
    public static MemoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MemoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MemoDatabase.class, "memo_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //DB 객체 제거
    public static void destroyInstance() {
        INSTANCE = null;
    }


}