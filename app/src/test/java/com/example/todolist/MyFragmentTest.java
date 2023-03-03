package com.example.todolist;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyFragmentTest {
    private AddNewTask addNewTask;
    @Mock
    private View mockView;
    @Mock
    private EditText mockEditText;
    @Mock
    private TextView mockTextView;
    @Mock
    private Button mockButton;

    @Before
    public void setUp() {
        addNewTask = new AddNewTask();
    }

    @Test
    public void onViewCreated_withNoBundle_shouldSetButtonEnabledToFalse() {
        // Arrange
        when(mockView.findViewById(R.id.setTaskName)).thenReturn(mockEditText);
        when(mockView.findViewById(R.id.setDueDate)).thenReturn(mockTextView);
        when(mockView.findViewById(R.id.saveButton)).thenReturn(mockButton);
        when(mockEditText.length()).thenReturn(0);

        // Act
        addNewTask.onViewCreated(mockView, null);

        // Assert
        verify(mockButton).setEnabled(false);
    }

    @Test
    public void onViewCreated_withNonEmptyTask_shouldSetButtonEnabledToTrue() {
        // Arrange
        when(mockView.findViewById(R.id.setTaskName)).thenReturn(mockEditText);
        when(mockView.findViewById(R.id.setDueDate)).thenReturn(mockTextView);
        when(mockView.findViewById(R.id.saveButton)).thenReturn(mockButton);
        when(mockEditText.length()).thenReturn(5);

        // Act
        addNewTask.onViewCreated(mockView, null);

        // Assert
        verify(mockButton).setEnabled(true);
    }
}