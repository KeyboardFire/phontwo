/*
 * phontwo - an IPA keyboard for Android
 * Copyright (C) 2017  Keyboard Fire <andy@keyboardfire.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.keyboardfire.phontwo;

import android.view.*;
import android.view.inputmethod.*;
import android.inputmethodservice.*;

import android.util.Log;

public class Phontwo extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private final static String[][] keyData = {
        {"i", "y"}, {"ɨ", "ʉ"}, {"ɯ", "u"}, {"b", "p"}, {".", "."}, {"d", "t"}, {"ɖ", "ʈ"}, {"ɟ", "c"}, {"ɡ", "k"}, {"ɢ", "q"}, {"ð", "θ"}, {".", "."},
        {"ɪ", "ʏ"}, {".", "."}, {".", "ʊ"}, {"m", "."}, {"ɱ", "."}, {"n", "."}, {"ɳ", "."}, {"ɲ", "."}, {"ŋ", "."}, {"ɴ", "."}, {"ɮ", "ɬ"}, {".", "."},
        {"e", "ø"}, {"ɘ", "ɵ"}, {"ɤ", "o"}, {"ʙ", "."}, {"ⱱ", "."}, {"r", "."}, {"ɽ", "."}, {"ɾ", "."}, {".", "."}, {"ʀ", "."}, {"ʒ", "ʃ"}, {".", "."},
        {"ɛ", "œ"}, {"ɜ", "ɞ"}, {"ʌ", "ɔ"}, {"β", "ɸ"}, {"v", "f"}, {"z", "s"}, {"ʐ", "ʂ"}, {"ʝ", "ç"}, {"ɣ", "x"}, {"ʁ", "χ"}, {"ʑ", "ɕ"}, {".", "."},
        {"æ", "."}, {"ɐ", "."}, {".", "."}, {"ə", "."}, {"ʋ", "."}, {"ɹ", "."}, {"ɻ", "."}, {"j", "."}, {"j", "."}, {"ʕ", "ħ"}, {"w", "ʍ"}, {"⌫", "."},
        {"a", "ɶ"}, {".", "."}, {"ɑ", "ɒ"}, {" ", " "}, {" ", " "}, {"l", "."}, {"ɭ", "."}, {"ʎ", "."}, {"ʟ", "."}, {"ɦ", "h"}, {"ʔ", "ʡ"}, {"⏎", "."}
    };

    private StringBuilder composing = new StringBuilder();

    private InputMethodManager inputMethodManager;

    private KeyboardLayerView inputView;

    private KeyboardLayer mainKeyboard;
    private KeyboardLayer currentKeyboard;

    private long pressTime;

    @Override public void onCreate() {
        super.onCreate();
        inputMethodManager = (InputMethodManager)
            getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override public void onInitializeInterface() {
        mainKeyboard = new KeyboardLayer(this, R.xml.main_layer);
    }

    @Override public View onCreateInputView() {
        inputView = (KeyboardLayerView) getLayoutInflater()
            .inflate(R.layout.input, null);
        inputView.setOnKeyboardActionListener(this);
        setKeyboardLayer(mainKeyboard);
        return inputView;
    }

    private void setKeyboardLayer(KeyboardLayer layer) {
        inputView.setKeyboard(layer);
    }

    @Override public void onStartInput(EditorInfo attr, boolean restarting) {
        super.onStartInput(attr, restarting);
        currentKeyboard = mainKeyboard;
        // currentKeyboard.setImeOptions(getResources(), attr.imeOptions);
    }

    @Override public void onPress(int kc) {
        pressTime = System.currentTimeMillis();
    }
    @Override public void onRelease(int kc) {
        switch (kc) {
            case 59:
                if (composing.length() > 0) {
                    composing.setLength(0);
                    getCurrentInputConnection().commitText("", 0);
                } else {
                    sendRawKey(KeyEvent.KEYCODE_DEL);
                }
                break;
            case 63:
                commit();
                sendRawKey(KeyEvent.KEYCODE_SPACE);
                break;
            case 71:
                commit();
                sendRawKey(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                final long deltaTime = System.currentTimeMillis() - pressTime;
                commit();
                composing.append(keyData[kc][deltaTime > 200 ? 1 : 0]);
                getCurrentInputConnection().setComposingText(composing, 1);
        }
    }

    @Override public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        ic.beginBatchEdit();
        commit();
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }

    private void sendRawKey(int kc) {
        InputConnection ic = getCurrentInputConnection();
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, kc));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, kc));
    }

    private void commit() {
        if (composing.length() > 0) {
            getCurrentInputConnection().commitText(composing, composing.length());
            composing.setLength(0);
        }
    }

    @Override public void swipeUp() {}
    @Override public void swipeDown() {}
    @Override public void swipeLeft() {}
    @Override public void swipeRight() {}

    @Override public void onKey(int kc, int[] kcs) {}

}
