package com.covidsaathi.adapters;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.fragments.ResourceElementFragment;
import com.covidsaathi.models.SubjectModel;
import com.covidsaathi.utils.AnimationClass;
import com.covidsaathi.utils.AppHelper;
import com.covidsaathi.utils.RandomCatcherClass;

import java.util.List;

public class SubjectsRecyclerAdatper extends RecyclerView.Adapter<SubjectsRecyclerAdatper.ViewHolder> {
    private FragmentActivity context;

    private List<SubjectModel> subjectList;
    private String branch;
    private String semester;

    public SubjectsRecyclerAdatper(FragmentActivity context, List<SubjectModel> subjectList, String branch, String sem) {
        this.context = context;
        this.subjectList = subjectList;
        this.branch = branch;
        this.semester = sem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectModel subject = subjectList.get(position);
        holder.methods(subject, position);

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

        private void methods(SubjectModel subject, int position) {
            txtSubject.setText(subject.getSubjectName());
            txtSubjectSymbol.setText(subject.getSubjectName().substring(0, 1));
            txtSubjectCode.setText(subject.getSubjectCode());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubjectModel subject1 = subjectList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(Db.SUBJECT_CODE, subject1.getSubjectCode());
                    bundle.putString(Db.CONST_BRANCH, branch);
                    bundle.putString(Db.CONST_SEMESTER, semester);
                    AppHelper.replaceFragments(ResourceElementFragment.class, context,bundle);
                }
            });
        }
    }
}
