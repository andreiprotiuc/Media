package exempl.andrei.com.media.data;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import exempl.andrei.com.media.R;
import exempl.andrei.com.media.data.model.VideoModel;

/**
 * Created by andreiprotiuc on 8/1/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    @Nullable
    private Cursor mCursor;
    @NonNull
    private List<VideoModel> modelList;
    @Nullable
    private VideoListener mListener;

    public interface VideoListener {
        void onVideoSelected(int position);
    }

    public VideoAdapter(@Nullable Cursor cursor, @Nullable VideoListener listener) {
        this.mCursor = cursor;
        this.modelList = new ArrayList<>();
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            holder.mTitle.setText(mCursor.getString(1));
            //holder.mThumb.setImageURI(Uri.parse(thumbPath));
        }

    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mThumb;
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onVideoSelected(getAdapterPosition());
                    }
                }
            });
            mThumb = (ImageView) itemView.findViewById(R.id.video_image_view);
            mTitle = (TextView) itemView.findViewById(R.id.video_title);
        }
    }

    public Cursor getmCursor() {
        return mCursor;
    }

    public void swapCursor(Cursor cursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;
        modelList.clear();
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                VideoModel videoModel = new VideoModel();
                videoModel.setmTitle(cursor.getString(1));
                videoModel.setmUri(Uri.parse(cursor.getString(0)));
                modelList.add(videoModel);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    public List<VideoModel> getModelList() {
        return modelList;
    }
}
