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

    private StringBuilder composing = new StringBuilder();

    private InputMethodManager inputMethodManager;

    private KeyboardLayerView inputView;

    private KeyboardLayer mainKeyboard;
    private KeyboardLayer currentKeyboard;

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

    public void onKey(int kc, int[] kcs) {
        composing.append(String.valueOf((char)kc));
        commit(getCurrentInputConnection());
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        ic.beginBatchEdit();
        commit(ic);
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }

    private void commit(InputConnection ic) {
        if (composing.length() > 0) {
            ic.commitText(composing, composing.length());
            composing.setLength(0);
        }
    }

    public void swipeUp() {}
    public void swipeDown() {}
    public void swipeLeft() {}
    public void swipeRight() {}

    public void onPress(int kc) {}
    public void onRelease(int kc) {}

}
