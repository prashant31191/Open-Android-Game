/* GraphicsView.java - Class for main drawing surface and listeners.
 * 
 * Copyright © 2011 Kyle Paulsen
 * Please see the file COPYING in this
 * distribution for license terms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */

package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 
 * @author Kyle
 * This class is the primary graphics class that is 
 * responsible for all drawing routines. It takes the MainActivity
 * as its context.
 * 
 * I guess it will eventually listen for input.
 */
public class GraphicsView extends SurfaceView implements Callback {

	//Create a buffer bitmap so that static things can be redrawn fast and easy.
	private Bitmap cBuffer;
	//Create a current frame canvas for animated changes in the graphics.
	private Canvas cFrame;
	//A reference to the surface stuff.
	private SurfaceHolder graphicsHolder;
	//Game loop and thread.
	private GameProgram prog;
	

	
	//temp
	private int x = 0;
	
	public GraphicsView(Context context) {
		super(context);
		prog = new GameProgram(this, context);
		
		graphicsHolder = this.getHolder();
		graphicsHolder.addCallback(this);

		cFrame = new Canvas();
		
		// initialize a player --XC July2
		Player player = new Player(Color.GREEN);
		
		//keeps the screen on while playing the game.
		this.setKeepScreenOn(true);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		prog.setRunning(true);
		prog.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		prog.setRunning(false);
		while(prog.isAlive()){
			try{
				prog.join();
				
			}catch(InterruptedException e){}
		}
		this.setKeepScreenOn(false);
	}
	
	//Here is the main drawing method. 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.WHITE);
		

		
		if(cBuffer == null){
			//if the buffer is new, set it up and set it to draw with the cFrame.
			//ARGB_8888 means each pixel uses 4 bytes.
			cBuffer = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
	        cFrame.setBitmap(cBuffer);
	        cFrame.drawRect(0, 0, canvas.getWidth(),canvas.getHeight(), p);
		}
		
		
		//draw the current frame buffer.
		//not sure what or how I'm going to use a buffer for yet. Maybe backgrounds?
		canvas.drawBitmap(cBuffer, 0, 0, null);	
		
		//Clear canvas. Remember that surfaceviews do not clean
		//the canvas on each call to this method.
		//canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), p);
		
		//draw to frame and buffer.
		//cFrame.drawRect(x, 0, x+50, 50, p);
		
		//draw directly to canvas
		p.setColor(Color.RED);
		canvas.drawRect(x, 0, x+50, 50, p);
		x++;	
		

	}
	
	//buffer getter
	public Canvas getBuffer(){
		return this.cFrame;
	}
	
	//buffer setter.
	public void setBuffer(Canvas b){
		this.cFrame = b;
	}

	
	
}
