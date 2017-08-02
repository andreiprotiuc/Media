package exempl.andrei.com.media.data;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by andreiprotiuc on 8/2/17.
 */

public class DBManager {
    private static DBManager instance;
    AppDatabase mAppDatabase;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public AppDatabase getAppDataBase(Context context) {
        if (mAppDatabase == null) {
            mAppDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "media").allowMainThreadQueries().build();
        }
        return mAppDatabase;
    }


}
