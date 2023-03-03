package com.example.todolist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoListModel;
import com.example.todolist.R;
import com.example.todolist.Utils.DatabaseHelper;

import java.util.Calendar;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoListModel> todoList;
    private final MainActivity todoMainActivity;
    private final DatabaseHelper myDatabase;

    public ToDoAdapter(DatabaseHelper myDatabase, MainActivity mainActivity) {
        this.todoMainActivity = mainActivity;
        this.myDatabase = myDatabase;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoListModel item = todoList.get(position);

        Calendar calendar = Calendar.getInstance();
        int MONTH = calendar.get(Calendar.MONTH) + 1;
        int DAY = calendar.get(Calendar.DATE);
        int YEAR = calendar.get(Calendar.YEAR);

        String completeDate = String.format(MONTH + "/" + DAY + "/" + YEAR);
        item.setCompletedDate(completeDate);

        boolean status = toBoolean(item.getStatus());
        String completed = String.format("Completed: " + item.getCompletedDate());
        String due = !item.getDate().isBlank() ? String.format("Due: " + item.getDate()) : "";

        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(status);
        holder.dueDate.setText(item.getDate());
        holder.category.setText(item.getCategory());

        if (status) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setTextColor(Color.GRAY);
            holder.dueDate.setText(completed);
        } else {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setTextColor(Color.BLACK);
            holder.dueDate.setText(due);
        }
        holder.checkBox.setOnClickListener(v -> {
            boolean isChecked = holder.checkBox.isChecked();
            if (isChecked) {
                myDatabase.updateStatus(item.getId(), 1, completeDate);
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkBox.setTextColor(Color.GRAY);
                holder.dueDate.setText(completed);
            } else {
                myDatabase.updateStatus(item.getId(), 0, completeDate);
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkBox.setTextColor(Color.BLACK);
                holder.dueDate.setText(due);
            }
        });
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return todoMainActivity;
    }

    public void setTasks(List<ToDoListModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoListModel item = todoList.get(position);
        myDatabase.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoListModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("date", item.getDate());
        bundle.putString("category", item.getCategory());
        bundle.putInt("categoryIndex", item.getCategoryIndex());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(todoMainActivity.getSupportFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView dueDate;
        TextView category;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.taskCheckbox);
            dueDate = itemView.findViewById(R.id.dueDate);
            category = itemView.findViewById(R.id.category);
        }
    }
}
