package exempl.andrei.com.media;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import exempl.andrei.com.media.data.DBManager;
import exempl.andrei.com.media.data.SimpleAdapter;
import exempl.andrei.com.media.data.model.VideoModel;

/**
 * Created by andreiprotiuc on 8/2/17.
 */

public class PlayListActivity extends AppCompatActivity implements SimpleAdapter.PlayListListener {
    private SimpleAdapter mAdapter;
    private static final int REQUEST_UPDATE_CODE = RESULT_FIRST_USER + 1;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            DBManager.getInstance().getAppDataBase(PlayListActivity.this).playListDao().delete(mAdapter.getmItems().get(position));
            mAdapter.getmItems().remove(position);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SimpleAdapter(DBManager.getInstance().getAppDataBase(this).playListDao().getAll(), this);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onPlayListSelected(int position) {
        Toast.makeText(this, "position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PlayerActivity.class);
        String playListUris = mAdapter.getmItems().get(position).playList;
        String[] uris = playListUris.split(";");
        List<VideoModel> videoModelList = new ArrayList<>();
        for (String uri : uris) {
            VideoModel videoModel = new VideoModel();
            videoModel.setmUri(Uri.parse(uri));
            videoModelList.add(videoModel);
        }
        intent.putParcelableArrayListExtra(PlayerActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) videoModelList);
        startActivity(intent);
    }

    @Override
    public void onEditPlayListSelected(int position) {
        CreatePlayListActivity.startActivityForResult(this, mAdapter.getmItems().get(position), REQUEST_UPDATE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_CODE && resultCode == RESULT_OK) {
            mAdapter.updateItems(DBManager.getInstance().getAppDataBase(this).playListDao().getAll());
        }
    }
}
