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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.equals(""))
        {
            int length=words.size();
            Random random_num=new Random(length);
            String s=words.get(random_num.nextInt());
            return(s);
        }
        else{
            String Final_word=binarysearch(prefix);
            if(Final_word!=null) {
                return(Final_word);
            }
            else
                return null;
        }
    }

    public String binarysearch(String prefix)
    {
        int start=0;
        int length=words.size();
        int end=length;
        int plength=prefix.length();
        int mid=(start+length)/2;
        Log.e("bs", Integer.toString(length));
        Log.e("bs", Integer.toString(plength));
        Log.e("bs", Integer.toString(mid));
        while(start<end) {
            mid=(start+end)/2;
            Log.e("bs", Integer.toString(mid));
            String sub_word=words.get(mid);
            if(sub_word.length()<plength)
                start=mid+1;
            else {
                sub_word = words.get(mid).substring(0, plength);
                if (sub_word.equals(prefix)) {
                    return (words.get(mid));
                } else if (sub_word.charAt(0) < prefix.charAt(0)) {
                    end = mid - 1;
                    Log.e("bs", Integer.toString(end));
                } else if (sub_word.charAt(0) > prefix.charAt(0)) {
                    start = mid + 1;
                    Log.e("bs", Integer.toString(start));

                }
            }
        }
        return (null);
        }
    /*@Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }*/
}

