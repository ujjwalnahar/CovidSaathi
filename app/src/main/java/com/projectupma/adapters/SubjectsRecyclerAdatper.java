package com.projectupma.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.projectupma.models.SubjectModel;
import com.projectupma.R;
import com.projectupma.utils.AnimationClass;
import com.projectupma.utils.RandomCatcherClass;

import java.util.List;

public class SubjectsRecyclerAdatper extends RecyclerView.Adapter<SubjectsRecyclerAdatper.ViewHolder> {
    private Context context;
    private List<SubjectModel> subjectList;

    public SubjectsRecyclerAdatper(Context context, List<SubjectModel> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects_recycler_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectModel subject = subjectList.get(position);
        holder.methods(subject);


    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSubject, txtSubjectCode, txtSubjectSymbol;
        MaterialCardView rotatingCardView_Subjects;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
            animation();
            changeRotatingColors();
        }
        public void changeRotatingColors() {
            TypedArray ta = RandomCatcherClass.getColorList(context);
            rotatingCardView_Subjects.setCardBackgroundColor(ta.getColor((int) (Math.random() * ta.length()), 0));
            rotatingCardView_Subjects.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeRotatingColors();
                }
            });
        }

        private void initUI(View itemView) {

            txtSubject = itemView.findViewById(R.id.txt_subject_name);
            txtSubjectCode = itemView.findViewById(R.id.txt_subject_code);
            txtSubjectSymbol = itemView.findViewById(R.id.txt_symbol_subject);
            rotatingCardView_Subjects = itemView.findViewById(R.id.rotatingCardView_Subjects);
        }

        private void animation() {
            AnimationClass.setAnimationLTR(itemView);
            AnimationClass.setAnimationLTR(txtSubject);
            AnimationClass.setAnimationLTR(txtSubjectCode);
        }

        private void methods(SubjectModel subject) {
            txtSubject.setText(subject.getSubjectName());
            txtSubjectSymbol.setText(subject.getSubjectName().substring(0, 1));
            txtSubjectCode.setText(subject.getSubjectCode());
        }
    }
}
