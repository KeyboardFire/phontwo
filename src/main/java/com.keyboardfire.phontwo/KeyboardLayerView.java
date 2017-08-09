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

import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.inputmethodservice.*;
import android.graphics.*;

public class KeyboardLayerView extends KeyboardView {

    public KeyboardLayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreviewEnabled(false);
    }

    @Override public void onDraw(Canvas canvas) {
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(12);
            paint.setColor(Color.WHITE);

            final String label = key.label.toString();

            Rect bounds = new Rect();
            paint.getTextBounds(label, 0, label.length(), bounds);

            canvas.drawText(label, key.x + (key.width / 2),
                    key.y + key.height / 2 + (key.height - bounds.bottom) / 4,
                    paint);
        }
    }

}
