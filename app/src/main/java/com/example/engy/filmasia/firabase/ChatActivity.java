package com.example.engy.filmasia.firabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.engy.filmasia.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Engy on 2/27/2018.
 */

public class ChatActivity extends AppCompatActivity {

    EditText typeMsg;
    Button send;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference msgDbRef;
    ChildEventListener childEventListener;
    final static String MSG="messages";

    ChatMessageAdapter chatAdapter;
    List<ChatMessage> msgList;
    ListView chatListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ///
        initialize();
    }

    private void initialize(){
        firebaseDatabase=FirebaseDatabase.getInstance();
        msgDbRef=firebaseDatabase.getReference().child(MSG);

        chatListView=(ListView) findViewById(R.id.chat_ListView);
        msgList=new ArrayList<>();
        chatAdapter=new ChatMessageAdapter(this,R.layout.chat_item,msgList);
        chatListView.setAdapter(chatAdapter);

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage=dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.add(chatMessage);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        msgDbRef.addChildEventListener(childEventListener);

        typeMsg =(EditText) findViewById(R.id.chat_edit_text);
        send=(Button) findViewById(R.id.chat_send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=typeMsg.getText().toString();
                ChatMessage message=new ChatMessage("Engy",msg,null);
                msgDbRef.push().setValue(message);

                typeMsg.setText("");
            }
        });
    }
}
