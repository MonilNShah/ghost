/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    //private SimpleDictionary simpleDictionary;
    private FastDictionary simpleDictionary;
    TextView label;
    TextView text;
    Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        restart=(Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(" ");
                onStart(view);
            }
        });
        try {
            simpleDictionary = new FastDictionary(assetManager.open("words.txt"));
        }
        catch (IOException e) {

        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String frag=text.getText().toString();
        if((frag.length()>=4) &&  (simpleDictionary.isWord(frag))) {
            Toast.makeText(getApplicationContext(),"Computer Wins",Toast.LENGTH_SHORT).show();
        }
        else{
            String words;
            words=simpleDictionary.getAnyWordStartingWith(frag);
            if(words==null) {
                Toast.makeText(getApplicationContext(),"Computer Wins",Toast.LENGTH_SHORT).show();

            }
            else{
                int length=frag.length();
                char c=words.charAt(length);
                text.setText(text.getText().toString()+c);
            }


        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     *  @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        char c=(char)event.getUnicodeChar();
        if(Character.isLetter(c)) {
            addTextTogame(c);
            label.setText(COMPUTER_TURN);
            userTurn=false;
            computerTurn();
            //text.setText(text.getText().toString()+c);

        }
        else {
            Toast.makeText(getApplicationContext(),"Not a character",Toast.LENGTH_SHORT).show();
            return super.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode,event);
    }

    private void addTextTogame(char c)
         {
        text.setText(text.getText().toString()+c);
    }

    public void onChallenge(View view)
    {
        String currentWord=text.getText().toString();
        if(currentWord.length()>=4 && simpleDictionary.isWord(currentWord))
            Toast.makeText(this,"User Won ",Toast.LENGTH_SHORT).show();
        else if(simpleDictionary.getAnyWordStartingWith(currentWord)!=null){
            Toast.makeText(this,"Computer Won",Toast.LENGTH_SHORT).show();
            text.setText(simpleDictionary.getAnyWordStartingWith(currentWord));
        }
        else
            Toast.makeText(this,"User Won",Toast.LENGTH_SHORT).show();

    }
}
