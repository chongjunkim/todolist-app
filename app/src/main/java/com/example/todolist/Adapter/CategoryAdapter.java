package com.example.todolist.Adapter;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.Model.CategoryModel;
import com.example.todolist.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryModel> {

    LayoutInflater layoutInflater;

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<CategoryModel> categories) {
        super(context, resource, categories);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.category_spinner, null, true);
        CategoryModel categoryModel = getItem(position);
        TextView textView = (TextView)rowView.findViewById(R.id.nameTextView);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageIcon);
        textView.setText(categoryModel.getName());
        imageView.setImageResource(categoryModel.getImage());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.category_spinner, parent, false);

        CategoryModel categoryModel = getItem(position);
        TextView textView = (TextView)convertView.findViewById(R.id.nameTextView);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageIcon);
        textView.setText(categoryModel.getName());
        imageView.setImageResource(categoryModel.getImage());
        return convertView;
    }
}
