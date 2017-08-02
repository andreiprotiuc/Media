package exempl.andrei.com.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import exempl.andrei.com.media.data.DBManager;
import exempl.andrei.com.media.data.EditAdapter;
import exempl.andrei.com.media.data.model.PlayList;
import exempl.andrei.com.media.data.model.VideoModel;

/**
 * Created by andreiprotiuc on 8/1/17.
 */

public class CreatePlayListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TASK_LOADER_ID = 0;
    private static final String TAG = CreatePlayListActivity.class.getName();
    public static final String EXTRA_PLAYLIST = "extraPlayList";

    private EditAdapter mAdapter;
    private EditText mPlayListNameEditText;
    private PlayList mPlayList;

    public static void startActivityForResult(Activity activity, PlayList playList, int code) {
        Intent intent = new Intent(activity, CreatePlayListActivity.class);
        intent.putExtra(EXTRA_PLAYLIST, playList);
        activity.startActivityForResult(intent, code);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CreatePlayListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_play_list_activity);
        mPlayListNameEditText = (EditText) findViewById(R.id.play_list_name);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new EditAdapter(new ArrayList<VideoModel>());
        mRecyclerView.setAdapter(mAdapter);
        mPlayList = getIntent().getParcelableExtra(EXTRA_PLAYLIST);
        if (mPlayList != null) {
            mPlayListNameEditText.setText(mPlayList.name);
        }
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (mPlayList == null) {
                    PlayList playList = new PlayList();
                    playList.name = mPlayListNameEditText.getText().toString();
                    if (!TextUtils.isEmpty(mAdapter.getSelectedItems())) {
                        playList.playList = mAdapter.getSelectedItems();
                        DBManager.getInstance().getAppDataBase(this).playListDao().insert(playList);
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    mPlayList.name = mPlayListNameEditText.getText().toString();
                    if (!TextUtils.isEmpty(mAdapter.getSelectedItems())) {
                        mPlayList.playList = mAdapter.getSelectedItems();
                        DBManager.getInstance().getAppDataBase(this).playListDao().updatePlayList(mPlayList);
                        setResult(RESULT_OK);
                        finish();
                    }
                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // If we have data the deliver it
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DISPLAY_NAME, MediaStore.Video.VideoColumns._ID};
                    return getContentResolver().query(uri, projection, null, null, null);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<VideoModel> items = new ArrayList<>();
        while (data.moveToNext()) {
            VideoModel videoModel = new VideoModel(data);
            videoModel.setmChecked(isChecked(videoModel.getmUri().toString()));
            items.add(videoModel);
        }
        mAdapter.updateItems(items);
    }

    private boolean isChecked(String uri) {
        if (mPlayList != null) {
            String[] data = mPlayList.playList.split(";");
            for (String currentUri : data) {
                if (currentUri.equals(uri)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.updateItems(new ArrayList<VideoModel>());
    }
}
