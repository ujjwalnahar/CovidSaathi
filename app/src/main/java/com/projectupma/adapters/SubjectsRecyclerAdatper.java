package com.projectupma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectupma.DataClasses.Subject;
import com.projectupma.R;

import java.util.List;

public class SubjectsRecyclerAdatper extends RecyclerView.Adapter<SubjectsRecyclerAdatper.SubjectRecyclerViewHolder> {
    private Context context;
    private List<Subject> subjectList;

    public SubjectsRecyclerAdatper(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectsRecyclerAdatper.SubjectRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_subjects, parent, false);
        return new SubjectRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsRecyclerAdatper.SubjectRecyclerViewHolder holder, int position) {
Subject subject=subjectList.get(position);
    holder.txtSubject.setText(subject.getSubjectName());
    holder.txtSubjectSymbol.setText(subject.getSubjectName().substring(0,1));
    holder.txtSubjectCode.setText(subject.getSubjectCode());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }
    class  SubjectRecyclerViewHolder extends RecyclerView.ViewHolder {

       TextView txtSubject,txtSubjectCode,txtSubjectSymbol;

        SubjectRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject=itemView.findViewById(R.id.txt_subject_name);
            txtSubjectCode=itemView.findViewById(R.id.txt_subject_code);
            txtSubjectSymbol=itemView.findViewById(R.id.txt_symbol_subject);


        }
    }
}
