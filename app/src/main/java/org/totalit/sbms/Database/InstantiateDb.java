package org.totalit.sbms.Database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class InstantiateDb {
    public static final AbstractDb sbmsDb(Context context){
        return Room.databaseBuilder(context, AbstractDb.class,"sbms").fallbackToDestructiveMigration()
                .build();
    }
}
