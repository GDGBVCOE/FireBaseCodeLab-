package com.example.vaibhavchellani.firebaseioextended;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private ListView messageListView;
    private listviewAdapter mlistviewAdapter;
    private String mUsername="ANONYMOUS";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //linking xml and java
        messageEditText=(EditText) findViewById(R.id.messageEditText);
        sendButton=(Button) findViewById(R.id.sendButton);
        messageListView=(ListView)findViewById(R.id.messageListView);
        final List<Message> messages=new ArrayList<Message>();
        Message newMessage=new Message();
        mlistviewAdapter=new listviewAdapter(this,R.layout.row_layout,messages);
        messageListView.setAdapter(mlistviewAdapter);

        sendButton.setEnabled(false);


        //getting reference to database
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();


        // if edit text is empty this disables the send button .
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Send button listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message addNewMessage=new Message();
                addNewMessage.setUserName(mUsername+" : ");
                addNewMessage.setUserMessage(messageEditText.getText().toString());

                //the below line replaces existing data in the database , but we have to append to the list not replace
                //mDatabaseReference.child("messages").setValue(addNewMessage);

                mDatabaseReference.child("messages").push().setValue(addNewMessage);

                messageEditText.setText("");
            }
        });
    }
}
