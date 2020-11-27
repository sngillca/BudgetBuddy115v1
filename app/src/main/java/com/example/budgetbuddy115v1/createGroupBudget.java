package com.example.budgetbuddy115v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createGroupBudget extends AppCompatActivity {
    EditText budgetName, tAmount, sAmount, tExpenses;
    Button creating;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    String userEmail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_budget);

        budgetName = findViewById(R.id.budgName);
        tAmount = findViewById(R.id.totalAmount);
        sAmount = findViewById(R.id.startingAmount);
        tExpenses = findViewById(R.id.expenses);
        creating = findViewById(R.id.create);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        creating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String budgetname = budgetName.getText().toString().trim();
                String dummytotal = tAmount.getText().toString().trim();
                String dummysaved = sAmount.getText().toString().trim();
                String dummyexpenses = tExpenses.getText().toString().trim();
                userEmail = fAuth.getCurrentUser().getEmail();

                int total = Integer.parseInt(dummytotal);
                int saved = Integer.parseInt(dummysaved);
                int expense = Integer.parseInt(dummyexpenses);

                Map<String, Object> docData = new HashMap<>();
                docData.put("budgetName", budgetname);
                docData.put("Total", total);
                docData.put("SavedsoFar", saved);
                docData.put("currentExpenses", expense);

                Map<String, Object> participants = new HashMap<>();
                participants.put("1", userEmail);
                participants.put("2", "christian");
                participants.put("3", "Natalia");
                participants.put("4", "Elijah");
                participants.put("5", "Victor");

                docData.put("participants", participants);

                DocumentReference documentReference = fstore.collection("groupBudget").document(budgetname);
                documentReference.set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "group budget created");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "group budget failed");
                    }
                });


                //Toast.makeText(createGroupBudget.this, "All Credentials: " + budgetName + " " + tAmount + " " + sAmount + " " + creating, Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), groupBudget.class));
                Intent intent = new Intent(createGroupBudget.this, groupBudget.class);
                intent.putExtra("name", budgetname);
                intent.putExtra("total", total);
                intent.putExtra("savings", saved);
                intent.putExtra("expenses", expense);
                startActivity(intent);

            }
        });

    }

}