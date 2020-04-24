package com.example.movie;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.bean.Casts;
import com.example.movie.bean.Directors;
import com.example.movie.bean.Subjects;
import java.util.List;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemSubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Subjects> ss = null;
    private Context c;
    private LayoutInflater ll;

    public ItemSubjectAdapter(List<Subjects> ss, Context c) {
        this.c = c;
        this.ss = ss;
        this.ll = LayoutInflater.from(c);
    }
    public int upDown=0;
    public void loadMoreData(List<Subjects> ss){
        if(upDown==0){
            ss.addAll(this.ss);
            this.ss = ss;
        }else{
            this.ss.addAll(ss);
        }
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0) {
            View v = ll.inflate(R.layout.item_simple_subject_layout, parent, false);
            return new ItemViewHolder(v);
        }else {
            View v = ll.inflate(R.layout.foot_load_tips, parent, false);
            fvh = new FootViewHolder(v);
            return fvh;
        }
    }
    FootViewHolder fvh;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==0) {
            ((ItemViewHolder) holder).update();
        }else {
            ((FootViewHolder) holder).update();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position<ss.size()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return ss.size()+1;
    }

    public void fail(){
        fvh.fail();
    }
    public int totalSize=0;
    class FootViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar pbViewLoadTip;
        private TextView tvViewLoadTip;
        public FootViewHolder(View itemView) {
            super(itemView);
            pbViewLoadTip = (ProgressBar) itemView.findViewById(R.id.pb_view_load_tip);
            tvViewLoadTip = (TextView) itemView.findViewById(R.id.tv_view_load_tip);

        }
        public void update(){
            if(ss.size()>=totalSize){
                pbViewLoadTip.setVisibility(View.GONE);
                tvViewLoadTip.setText("");
            }else{
                pbViewLoadTip.setVisibility(View.VISIBLE);
                tvViewLoadTip.setText("加载更多..");
            }
        }
        public void fail(){
            pbViewLoadTip.setVisibility(View.GONE);
            tvViewLoadTip.setText("加载失败");
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemSimpleSubjectImage;
        private LinearLayout llItemSimpleSubjectRating;
        private RatingBar rbItemSimpleSubjectRating;
        private TextView tvItemSimpleSubjectRating;
        private TextView tvItemSimpleSubjectCount;
        private TextView tvItemSimpleSubjectTitle;
        private TextView tvItemSimpleSubjectOriginalTitle;
        private TextView tvItemSimpleSubjectGenres;
        private TextView tvItemSimpleSubjectDirector;
        private TextView tvItemSimpleSubjectCast;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivItemSimpleSubjectImage = (ImageView) itemView.findViewById(R.id.iv_item_simple_subject_image);
            llItemSimpleSubjectRating = (LinearLayout) itemView.findViewById(R.id.ll_item_simple_subject_rating);
            rbItemSimpleSubjectRating = (RatingBar) itemView.findViewById(R.id.rb_item_simple_subject_rating);
            tvItemSimpleSubjectRating = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_rating);
            tvItemSimpleSubjectCount = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_count);
            tvItemSimpleSubjectTitle = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_title);
            tvItemSimpleSubjectOriginalTitle = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_original_title);
            tvItemSimpleSubjectGenres = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_genres);
            tvItemSimpleSubjectDirector = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_director);
            tvItemSimpleSubjectCast = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_cast);
        }

        public void update() {
            int position = this.getLayoutPosition();
            Subjects subject = ss.get(position);
            tvItemSimpleSubjectTitle.setText(subject.getTitle());
            rbItemSimpleSubjectRating.setRating((float) subject.getRating().getAverage()/2);
            tvItemSimpleSubjectRating.setText(""+subject.getRating().getAverage());
            tvItemSimpleSubjectCount.setText("("+subject.getCollect_count()+")人评价");
            tvItemSimpleSubjectOriginalTitle.setText(subject.getOriginal_title());
            StringBuffer genres=new StringBuffer();
            for (String g:subject.getGenres()) {
                genres.append(g+" ");
            }
            tvItemSimpleSubjectGenres.setText(genres);
            StringBuffer directors=new StringBuffer();
            for (Directors d:subject.getDirectors()) {
                directors.append(  d.getName()+" ");
            }
            tvItemSimpleSubjectDirector.setText(getSpannableString("导演:" , Color.GRAY));
            tvItemSimpleSubjectDirector.append(directors.toString());
            StringBuffer casts=new StringBuffer();
            for (Casts d:subject.getCasts()) {
                casts.append( d.getName()+" ");
            }
            tvItemSimpleSubjectCast.setText(getSpannableString("演员:",Color.GRAY));
            tvItemSimpleSubjectCast.append(casts.toString());
            ImageLoader.getInstance().displayImage(subject.getImages().getLarge(),ivItemSimpleSubjectImage,MyApplication.getLoaderOptions());
        }
        public SpannableString getSpannableString(String str, int color) {
            SpannableString span = new SpannableString(str);
            span.setSpan(new ForegroundColorSpan(
                    color), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }
    }
}

