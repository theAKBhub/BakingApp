package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import java.util.List;

/**
 * {@link StepsAdapter} creates a list of recipe steps to a {@link android.support.v7.widget.RecyclerView}
 * Created by aditibhattacharya on 31/01/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private StepsOnClickHandler mOnClickHandler;
    private Context mContext;
    private List<Step> mSteps;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    /**
     * Interface to receive onClick messages
     */
    public interface StepsOnClickHandler {
        void onClick(Step step);
    }


    /**
     * OnClick handler for the adapter that handles situation when a single item is clicked
     * @param onClickHandler
     */
    public StepsAdapter(Context context, StepsOnClickHandler onClickHandler) {
        mContext = context;
        mOnClickHandler = onClickHandler;
        mSharedPreferences = mContext.getSharedPreferences(Config.PREFERENCE_KEY_RECIPE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.rlayout_step) RelativeLayout rlayoutStep;
        @BindView(R.id.textview_step) TextView textViewStep;
        @BindColor(R.color.colorLatte) int colorDefaultBackground;
        @BindColor(R.color.colorListItemSelected) int colorSelectedBackground;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * This method gets called when child view is clicked
         * @param view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mOnClickHandler.onClick(step);
        }
    }


    /**
     * Method called when a new ViewHolder gets created in the event of RecyclerView being laid out.
     * This creates enough ViewHolders to fill up the screen and allow scrolling
     * @param parent
     * @param viewType
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int listItemLayoutId = R.layout.list_item_step;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(listItemLayoutId, parent, false);
        return new StepViewHolder(view);
    }


    /**
     * Method used by RecyclerView to list the steps
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(StepsAdapter.StepViewHolder holder, int position) {
        if (position < getItemCount()) {
            Step step = mSteps.get(position);
            String stepShortDescription = String.format(mContext.getString(R.string.display_step_id),
                    step.getStepId(), step.getStepShortDescription());

            if (!Utils.isEmptyString(stepShortDescription)) {
                holder.textViewStep.setText(stepShortDescription);
            }

            if (mSteps.get(position).getIsSelected()) {
                holder.rlayoutStep.setBackgroundColor(holder.colorSelectedBackground);
            } else {
                holder.rlayoutStep.setBackgroundColor(holder.colorDefaultBackground);
            }
        }
    }


    @Override
    public int getItemCount() {
        return (mSteps == null) ? 0 : mSteps.size();
    }


    /**
     * Method used to refresh the list once the adapter is already created, to avoid creating a new one
     * @param steps
     */
    public void setStepsData(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }


    /**
     * Method to set step item currently selected, and also save the selected step in SharedPreferences.
     * This is used to toggle the list item background color.
     * @param pos
     */
    public void setSelected(int pos) {
        int oldPos = mSharedPreferences.getInt(Config.PREFERENCE_KEY_STEP_SELECTOR, -1);
        if ((oldPos > -1) && (oldPos < getItemCount())) {
            mSteps.get(oldPos).setIsSelected(false);
        }

        mEditor.putInt(Config.PREFERENCE_KEY_STEP_SELECTOR, pos);
        mEditor.commit();
        mSteps.get(pos).setIsSelected(true);
        notifyDataSetChanged();
    }

}
