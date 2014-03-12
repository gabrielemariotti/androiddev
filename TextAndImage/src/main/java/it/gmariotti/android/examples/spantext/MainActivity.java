/*
 * ******************************************************************************
 *  * Copyright (c) 2014 Gabriele Mariotti.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package it.gmariotti.android.examples.spantext;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

    /**
     * TextView
     */
    TextView mTextView;

    /**
     * ImageView
     */
    ImageView mImageView;

    int finalHeight, finalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        mImageView = (ImageView) findViewById(R.id.icon);


        final ViewTreeObserver vto = mImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                finalHeight = mImageView.getMeasuredHeight();
                finalWidth = mImageView.getMeasuredWidth();
                makeSpan();
            }
        });
    }

    /**
     * This method builds the text layout
     */
    private void makeSpan() {

        /**
         * Get the text
         */
        String plainText=getResources().getString(R.string.text_sample);
        Spanned htmlText = Html.fromHtml(plainText);
        SpannableString mSpannableString= new SpannableString(htmlText);

        int allTextStart = 0;
        int allTextEnd = htmlText.length() - 1;

        /**
         * Calculate the lines number = image height.
         * You can improve it... it is just an example
         */
        int lines;
        Rect bounds = new Rect();
        mTextView.getPaint().getTextBounds(plainText.substring(0,10), 0, 1, bounds);

        //float textLineHeight = mTextView.getPaint().getTextSize();
        float fontSpacing=mTextView.getPaint().getFontSpacing();
        lines = (int) (finalHeight/(fontSpacing));

        /**
         * Build the layout with LeadingMarginSpan2
         */
        MyLeadingMarginSpan2 span = new MyLeadingMarginSpan2(lines, finalWidth +10 );
        mSpannableString.setSpan(span, allTextStart, allTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        mTextView.setText(mSpannableString);

    }

    /**
     *
     */
    class MyLeadingMarginSpan2 implements LeadingMarginSpan.LeadingMarginSpan2 {

        private int margin;
        private int lines;

        MyLeadingMarginSpan2(int lines, int margin) {
            this.margin = margin;
            this.lines = lines;
        }

        /**
         * Apply the margin
         *
         * @param first
         * @return
         */
        @Override
        public int getLeadingMargin(boolean first) {
            if (first) {
                return margin;
            } else {
                return 0;
            }
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                      int top, int baseline, int bottom, CharSequence text,
                                      int start, int end, boolean first, Layout layout) {}


        @Override
        public int getLeadingMarginLineCount() {
            return lines;
        }
    };


}
