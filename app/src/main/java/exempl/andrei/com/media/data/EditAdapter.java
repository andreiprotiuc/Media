package exempl.andrei.com.media.data;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import exempl.andrei.com.media.R;
import exempl.andrei.com.media.data.model.VideoModel;

/**
 * Created by andreiprotiuc on 8/1/17.
 */

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.ViewHolder> {
    private List<VideoModel> mItems;

    public EditAdapter(List<VideoModel> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
        return new EditAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        VideoModel model = mItems.get(position);
        holder.mTitle.setText(model.getmTitle());
        holder.mAppCompatCheckedBox.setChecked(model.ismChecked());
        holder.mAppCompatCheckedBox.setVisibility(View.VISIBLE);
        holder.mAppCompatCheckedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mItems.get(position).setmChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<VideoModel> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public String getSelectedItems() {
        StringBuilder builder = new StringBuilder();
        for (VideoModel model : mItems) {
            if (model.ismChecked()) {
                builder.append(model.getmUri());
                builder.append(";");
            }
        }
        return builder.toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mThumb;
        private TextView mTitle;
        private AppCompatCheckBox mAppCompatCheckedBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mThumb = (ImageView) itemView.findViewById(R.id.video_image_view);
            mTitle = (TextView) itemView.findViewById(R.id.video_title);
            mAppCompatCheckedBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
