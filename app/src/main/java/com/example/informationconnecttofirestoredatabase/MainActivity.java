package com.example.informationconnecttofirestoredatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;

    EditText NameEditText, AgeEditText;
    Spinner genderSpinner;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NameEditText = findViewById(R.id.nameEditText);
        AgeEditText = findViewById(R.id.ageEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        submitButton = findViewById(R.id.submitButton);

        firestore = FirebaseFirestore.getInstance();

        // Set click listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDataToFirestore();
            }
        });

        //Gender Function
        getSpinnerGender();
    }

    private void saveUserDataToFirestore() {
        String name = NameEditText.getText().toString();
        String age = AgeEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("age", age);
        user.put("gender", gender);

        firestore.collection("users").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "User data added successfully", Toast.LENGTH_SHORT).show();
                        // Clear input fields after successful submission
                        NameEditText.setText("");
                        AgeEditText.setText("");
                        genderSpinner.setSelection(0); // Reset spinner to default selection
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getSpinnerGender(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
    }
}