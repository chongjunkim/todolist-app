package com.example.todolist.Model;

import android.content.ContentValues;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryModel {
    private static final ArrayList<CategoryModel> categoryArrayList = new ArrayList<>();
    private int id;
    private String name;

    public CategoryModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static void initCategory() {
        CategoryModel category0 = new CategoryModel(0, "Select Category");
        CategoryModel category1 = new CategoryModel(1, "Personal");
        CategoryModel category2 = new CategoryModel(2, "Learning");
        CategoryModel category3 = new CategoryModel(3, "Hobbies");
        CategoryModel category4 = new CategoryModel(4, "Grocery");
        CategoryModel category5 = new CategoryModel(5, "Urgent");
        CategoryModel category6 = new CategoryModel(6, "Later");

        categoryArrayList.add(category0);
        categoryArrayList.add(category1);
        categoryArrayList.add(category2);
        categoryArrayList.add(category3);
        categoryArrayList.add(category4);
        categoryArrayList.add(category5);
        categoryArrayList.add(category6);
    }

    public int getImage() {
        switch (getId()) {
            case 0:
                return id;
            case 1:
                return R.drawable.baseline_sentiment_satisfied_alt_24;
            case 2:
                return R.drawable.baseline_school_24;
            case 3:
                return R.drawable.baseline_sports_basketball_24;
            case 4:
                return R.drawable.baseline_shopping_cart_24;
            case 5:
                return R.drawable.baseline_error_outline_24;
            case 6:
                return R.drawable.baseline_snooze_24;

        }
        return id;
    }

    public static ArrayList<CategoryModel> getCategoryArrayList() {
        return categoryArrayList;
    }
}
