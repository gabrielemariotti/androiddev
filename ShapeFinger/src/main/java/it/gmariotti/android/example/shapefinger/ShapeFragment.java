/*
 * Copyright 2014 Gabriele Mariotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.gmariotti.android.example.shapefinger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ShapeFragment extends Fragment {

    protected DrawingView mDrawingView;

    public ShapeFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mDrawingView = (DrawingView) rootView.findViewById(R.id.drawingview);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_line:
                mDrawingView.mCurrentShape = DrawingView.LINE;
                mDrawingView.reset();
                break;
            case R.id.action_smoothline:
                mDrawingView.mCurrentShape = DrawingView.SMOOTHLINE;
                mDrawingView.reset();
                break;
            case R.id.action_rectangle:
                mDrawingView.mCurrentShape = DrawingView.RECTANGLE;
                mDrawingView.reset();
                break;
            case R.id.action_square:
                mDrawingView.mCurrentShape = DrawingView.SQUARE;
                mDrawingView.reset();
                break;
            case R.id.action_circle:
                mDrawingView.mCurrentShape = DrawingView.CIRCLE;
                mDrawingView.reset();
                break;
            case R.id.action_triangle:
                mDrawingView.mCurrentShape = DrawingView.TRIANGLE;
                mDrawingView.reset();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
