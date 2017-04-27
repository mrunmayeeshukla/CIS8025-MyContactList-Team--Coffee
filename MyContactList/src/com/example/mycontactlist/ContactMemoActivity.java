package com.example.mycontactlist;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactMemoActivity extends Activity {

    private Contact currentContact;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_memo);
        initTextChangedEvents();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        id = Integer.parseInt(extras.getString("id"));

            initMemo(id);
        setForEditing(true);


    }

    private void initTextChangedEvents(){
        final EditText memo = (EditText) findViewById(R.id.editMemo);
        memo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentContact.setMemo(memo.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
    }

    private void setForEditing(boolean enabled) {
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        Button buttonSave = (Button) findViewById(R.id.buttonSave1);

        editMemo.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave1);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                ContactDataSource ds = new ContactDataSource(ContactMemoActivity.this);
                ds.open();
                ds.updateContact(currentContact);
                ds.close();
                setForEditing(false);
            }

            });
        }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editMemo= (EditText) findViewById(R.id.editMemo);
        imm.hideSoftInputFromWindow(editMemo.getWindowToken(), 0);
    }

    private void initMemo(int id) {

        ContactDataSource ds = new ContactDataSource(ContactMemoActivity.this );
        ds.open();
        currentContact = ds.getSpecificContact(id);
        ds.close();
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        editMemo.setText(currentContact.getMemo());
        TextView name = (TextView) findViewById(R.id.textContact);
        name.setText("Name: " + currentContact.getContactName());

    }
}