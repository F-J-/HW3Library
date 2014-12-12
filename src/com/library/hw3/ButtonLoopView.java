package com.library.hw3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hw3library.R;

public class ButtonLoopView extends View{
	
	    public static final int MIN_VALUE = 0;
	    public static final int MAX_VALUE = 100;

	    private PointF center = new PointF();
	    private RectF circleRect = new RectF();
	    private RectF circleRect1 = new RectF();
	    private Paint strokePaint = new Paint();
	    private Paint textPaint = new Paint();
	    
	    private Path segment = new Path();  
	    private Paint circlePaint = new Paint();
	    private Paint fillPaint = new Paint();

	    
	    private MyEventHandler[] handlers;
 	    private int arcColor;
	    private int count;
	    private int thickness;
	    private CharSequence[] names;
	    private CharSequence[] angles;
	    private int[] intAngles;
	    
	    private int fillColor;
	    private int circleColor;
	    private float value;
	    private int circleWidth;
	    
	    private float fontSize;
	    private int fontColor;
	    
	    private int radius;
	    private int radius2;
	    private int radius3;
	    
	    public ButtonLoopView(Context context)
	    {
	        this(context, null);
	    }

	    public ButtonLoopView(Context context, AttributeSet attrs)
	    {
	        super(context, attrs);
	        
	        
	        TypedArray a = context.getTheme().obtainStyledAttributes(
	            attrs,
	            R.styleable.ButtonLoopView,
	            0, 0);

	        try
	        {
	        	
	        	
	        	fontSize = a.getFloat(R.styleable.ButtonLoopView_fontSize, 25);
	        	fontColor = a.getColor(R.styleable.ButtonLoopView_fontColor, Color.BLACK);
	        	
	            arcColor = a.getColor(R.styleable.ButtonLoopView_arcColor, Color.BLACK);
	            count = a.getInteger(R.styleable.ButtonLoopView_count, 1);
	            thickness = a.getInteger(R.styleable.ButtonLoopView_thickness, 40);
	            names = a.getTextArray(R.styleable.ButtonLoopView_names);
	            angles = a.getTextArray(R.styleable.ButtonLoopView_angles);
	            
	            circleColor = a.getColor(R.styleable.ButtonLoopView_circleColor, Color.BLUE);
	            fillColor = a.getColor(R.styleable.ButtonLoopView_fullColor, Color.CYAN);
	            circleWidth = a.getInt(R.styleable.ButtonLoopView_circleWidth, 1);
	            value = a.getFloat(R.styleable.ButtonLoopView_value, 0);
	            adjustValue(value);
	        }
	        finally
	        {
	            a.recycle();
	        }   
	        
	        setStrokeColor(arcColor);
	        setFillColor(fillColor);
	        setCircleWidth(circleWidth, circleColor);
	        setIntAngles();
	        setTextPaint(fontColor, fontSize);
	        
	        strokePaint.setStyle(Paint.Style.STROKE);
	        circlePaint.setStyle(Paint.Style.STROKE);
	        textPaint.setTextAlign(Align.CENTER);
	    }

	    public void setFillColor(int fillColor)
	    {
	        this.fillColor = fillColor;
	        fillPaint.setColor(fillColor);
	        invalidate();
	    }
	    
	    public void setTextPaint(int fontColor, float fontSize) {
	    	this.fontColor = fontColor;
	    	this.fontSize = fontSize;
	    	textPaint.setColor(fontColor);
	    	textPaint.setTextSize(fontSize);
	    }

	    public int getFillColor()
	    {
	        return fillColor;
	    }

	    
	    public void setStrokeColor(int arcColor)
	    {
	        this.arcColor = arcColor;
	        strokePaint.setColor(arcColor);
	        strokePaint.setStrokeWidth(thickness);
	        
	        invalidate();
	    }

	    public int getStrokeColor()
	    {
	        return arcColor;
	    }
	    
	    public void setCircleWidth(int circleWidth, int circleColor) {
	    	this.circleWidth = circleWidth;
	    	this.circleColor = circleColor;
	    	circlePaint.setStrokeWidth(circleWidth);
	    	circlePaint.setColor(circleColor);
	    	
	    	invalidate();
	    }
	    
	    public void setValue(float value)
	    {
	        adjustValue(value);
	        setPaths();
	        invalidate();
	    }

	    public float getValue()
	    {
	        return value;
	    }

	    private void adjustValue(float value)
	    {
	        this.value = Math.min(MAX_VALUE, Math.max(MIN_VALUE, value));
	    }

	    
	    
	    public void setHandler(MyEventHandler[] handlers) {
	    	this.handlers = handlers;
	    }
	    
	    @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh)
	    {
	        super.onSizeChanged(w, h, oldw, oldh);

	        center.x = getWidth() / 2;
	        center.y = getHeight() / 2;
	        radius = Math.min(getWidth(), getHeight()) / 2;
	        System.out.println("x:" + center.x + " y: " + center.y + " r: " + radius + "wid:" + getWidth() + "h:" + getHeight() );
	        radius2 = radius - thickness;
	        radius3 = radius2 - thickness / 2 - 4;
	        circleRect1.set(center.x - radius3, center.y - radius3, center.x + radius3, center.y + radius3);
	        circleRect.set(center.x - radius2, center.y - radius2, center.x + radius2, center.y + radius2);
	        
	        setPaths();
	    }
	    
	    private void setPaths()
	    {
	        float y = center.y + radius3 - (2 * radius3 * value / 100 - 1);
	        float x = center.x - (float) Math.sqrt(Math.pow(radius3, 2) - Math.pow(y - center.y, 2));

	        float angle = (float) Math.toDegrees(Math.atan((center.y - y) / (x - center.x)));
	        float startAngle = 180 - angle;
	        float sweepAngle = 2 * angle - 180;

	        segment.rewind();
	        segment.addArc(circleRect1, startAngle, sweepAngle);
	        segment.close();
	    }

	    
	    private void setIntAngles() {
	    	int size = count;
	    	intAngles = new int[size];
	    	int i = 0, temp, sum = 0;
	    	for(i = 0; i < size - 1; i++) {
	    		if(angles != null) {
	    			temp = Integer.parseInt(angles[i].toString());
	    			temp = temp % 360;
	    		}
	    		else {
	    			temp = 360 / count;
	    		}
	    		sum += temp;
	    		intAngles[i] = temp;
	    	} 
	    	
	    	intAngles[i] = 360 - sum;
	    	
	    }
	    

	    @Override
	    protected void onDraw(Canvas canvas)
	    {
	        super.onDraw(canvas);
	   
	        int startAngle;
	        int sweepAngle;
	        int sum = 0;
	        
	        	
	        
	        for(int i = 0; i < count; i++) {
	        	sweepAngle = intAngles[i]; 
	        	if(i == 0) {
	        		startAngle = 0;
	        	}
	        	else {
	        		startAngle = sum;
	        	}
	        	sum += sweepAngle;
	        	canvas.drawArc(circleRect, startAngle, sweepAngle - 2, false, strokePaint);
	        	
	 
	        	if(names != null) {
	        		Path textPath = new Path();
	        		textPath.addArc(circleRect, startAngle, sweepAngle);
	        		canvas.drawTextOnPath(names[i].toString(), textPath, 0, 0, textPaint);
	        	}
	        }

	        canvas.drawPath(segment, fillPaint);
	        canvas.drawCircle(center.x, center.y, radius3, circlePaint);
	        canvas.drawText(value + "%", center.x, center.y, textPaint);
	        
	    }
	    
	    private double getAngle(double x, double y) {
	    	double angle = Math.atan((double) (y / x));
	    	angle = Math.toDegrees(angle);
	    	if(y > 0 && x < 0) 
	    		angle += 180;
	    	if(y < 0 && x < 0)
	    		angle += 180;
	    	if(y < 0 && x > 0)
	    		angle += 360;
	    	return angle;
	    }
	    
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	System.out.println("I am in onTouch");
	    	PointF touchEv = new PointF(event.getX(), event.getY()); 
	    	PointF circleCenter = center; 
	    	

	    	

	    	//calculate the distance of the touch point from the center of your circle
	    	double dist = Math.pow(touchEv.x-circleCenter.x,2) + Math.pow(touchEv.y-  circleCenter.y,2);
	    	dist = Math.sqrt(dist);
	    	double angle = getAngle(touchEv.x - circleCenter.x, touchEv.y - circleCenter.y);

	    	if (dist <= radius2 + thickness / 2 + 5 && dist > radius2 - thickness / 2 - 5) {
	    		
	    		System.out.println("click on the loop" + angle);
	    		int startAngle;
		        int endAngle;
		        int sum = 0;
		        for(int i = 0; i < count; i++) {
		        	
		        	if(i == 0) {
		        		startAngle = 0;
		        	}
		        	else {
		        		startAngle = sum;
		        	}
		        	endAngle = startAngle + intAngles[i]; 
		        	System.out.println("start: " + startAngle + " end: " + endAngle);
		        	sum += intAngles[i];
		        	
		        	System.err.println("handler" + handlers);
		        	if(angle >= startAngle - 2 && angle <= endAngle + 2 && handlers != null) {
		        		handlers[i].handleEvent();
		        		System.err.println("if ejra shoood");
		        		return true;
		        	}
		        }
	    	}
	    	return true;
	    	}
	    	
	    	return false;
	    }
	    
}

