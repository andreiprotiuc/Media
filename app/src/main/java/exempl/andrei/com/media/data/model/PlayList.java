package exempl.andrei.com.media.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by andreiprotiuc on 8/2/17.
 */

@Entity
public class PlayList {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "play_list")
    public String playList;
}
