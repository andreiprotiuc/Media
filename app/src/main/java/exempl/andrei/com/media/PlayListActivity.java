package exempl.andrei.com.media;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import exempl.andrei.com.media.data.DBManager;
import exempl.andrei.com.media.data.EditAdapter;
import exempl.andrei.com.media.data.SimpleAdapter;
import exempl.andrei.com.media.data.model.VideoModel;

/**
 * Created by andreiprotiuc on 8/2/17.
 */

public class PlayListActivity extends AppCompatActivity implements SimpleAdapter.PlayListListener {
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SimpleAdapter(DBManager.getInstance().getAppDataBase(this).playListDao().getAll(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPlayListSelected(int position) {
        Toast.makeText(this, "position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PlayerActivity.class);
        String playListUris = mAdapter.getmItems().get(position).playList;
        String[] uris = playListUris.split(";");
        List<VideoModel> videoModelList = new ArrayList<>();
        for(String uri: uris){
            VideoModel videoModel = new VideoModel();
            videoModel.setmUri(Uri.parse(uri));
            videoModelList.add(videoModel);
        }
        intent.putParcelableArrayListExtra(PlayerActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) videoModelList);
        startActivity(intent);
    }
}
