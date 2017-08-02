package exempl.andrei.com.media.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import exempl.andrei.com.media.R;
import exempl.andrei.com.media.data.model.PlayList;

/**
 * Created by andreiprotiuc on 8/2/17.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    public interface PlayListListener {
        void onPlayListSelected(int position);
    }

    private List<PlayList> mItems;
    private PlayListListener mListener;

    public SimpleAdapter(List<PlayList> items, PlayListListener listener) {
        this.mItems = items;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(mItems.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<PlayList> getmItems() {
        return mItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.play_list_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onPlayListSelected(getAdapterPosition());
                    }
                }
            });
        }
    }
}
