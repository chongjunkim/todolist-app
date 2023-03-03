package com.example.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.Adapter.CategoryAdapter;
import com.example.todolist.Model.CategoryModel;
import com.example.todolist.Model.ToDoListModel;
import com.example.todolist.Utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";
    private EditText editTaskName;
    private Button saveButton;
    private DatabaseHelper myDatabase;
    private Context context;
    private TextView setDueDate;
    private Spinner setCategory;
    private String categoryName = "";
    private int categoryIndex;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTaskName = view.findViewById(R.id.setTaskName);
        setDueDate = view.findViewById(R.id.setDueDate);
        saveButton = view.findViewById(R.id.saveButton);
        setCategory = view.findViewById(R.id.categorySpinner);

        myDatabase = new DatabaseHelper(getActivity());

        CategoryModel.initCategory();
        CategoryAdapter customAdapter = new CategoryAdapter(view.getContext(), R.layout.category_spinner, CategoryModel.getCategoryArrayList());
        setCategory.setAdapter(customAdapter);

        if (editTaskName.length() > 0) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;

            // display data as field values
            String task = bundle.getString("task");
            String date = !Objects.equals(bundle.getString("date"), "") ?
                    bundle.getString("date") :
                    "Set Due Date"; // show "Set Due Date" if there is no value
            int categoryIndex = bundle.getInt("categoryIndex");

            editTaskName.setText(task);
            setDueDate.setText(date);
            setCategory.setSelection(categoryIndex);

            if (task.length() > 0) {
                saveButton.setEnabled(true);
            } else {
                saveButton.setEnabled(false);
            }

        }

        editTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    saveButton.setEnabled(false);
                } else {
                    saveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int MONTH;
                int YEAR;
                int DAY;

                String existingDate = setDueDate.getText().toString();
                String[] existingDateSplit = existingDate.split("/");

                if (!existingDate.equals("Set Due Date")) {
                    MONTH = Integer.parseInt(existingDateSplit[0]) - 1;
                    DAY = Integer.parseInt(existingDateSplit[1]);
                    YEAR = Integer.parseInt(existingDateSplit[2]);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    MONTH = calendar.get(Calendar.MONTH);
                    DAY = calendar.get(Calendar.DATE);
                    YEAR = calendar.get(Calendar.YEAR);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = String.format(month + "/" + day + "/" + year);
                        setDueDate.setText(date);
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });

        setCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryModel selectedItem = (CategoryModel) (parent.getItemAtPosition(position));
                categoryName = selectedItem.getName();
                categoryIndex = selectedItem.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final boolean finalIsUpdate = isUpdate;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set data that is passed when saving
                String taskName = editTaskName.getText().toString();
                String dueDate = !setDueDate.getText().toString().equals("Set Due Date") ?
                        setDueDate.getText().toString() :
                        ""; // save blank if there is no due date
                String category = !categoryName.equals("Select Category") ?
                        categoryName :
                        "";
                int index = categoryIndex;

                if (finalIsUpdate) {
                    myDatabase.updateTask(bundle.getInt("id"), taskName, dueDate, category, index);
                    Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show();
                } else {
                    ToDoListModel item = new ToDoListModel();
                    item.setTask(taskName);
                    item.setStatus(0);
                    item.setDate(dueDate);
                    item.setCategory(category);
                    item.setCategoryIndex(index);
                    myDatabase.insertTask(item);
                    Toast.makeText(context, "Task Added!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
        CategoryModel.getCategoryArrayList().clear(); // prevent duplicate spinner items
    }
}