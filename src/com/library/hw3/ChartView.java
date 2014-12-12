package com.library.hw3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ChartView extends View {
	
	private static final int DEFAULT_LINE_COLOR = Color.GREEN;
	private static final int DEFAULT_LINE_WIDTH = 3;
	private static final int DEFAULT_COLUMNS_COLOR = Color.RED;
	private static final int DEFAULT_LABELS_COLOR = Color.BLACK;
	private static final int DEFAULT_VALUES_COLOR = Color.BLACK;
	private static final int DEFAULT_VALUES_SIZE = 11;
	private static final int DEFAULT_LABELS_SIZE = 15;
	private static final int DEFAULT_TOP_MARGIN = 50;
	private static final int DEFAULT_BOTTOM_MARGIN = 50;
	private static final int DEFAULT_LEFT_MARGIN = 50;
	private static final int DEFAULT_RIGHT_MARGIN = 50;
	private static final int DEFAULT_VALUE_TOP_MARGIN = 10;
	private static final int DEFAULT_VALUE_BOTTOM_MARGIN = 10;
	private static final int DEFAULT_LINE_TOP_MARGIN = 10;
	private static final int DEFAULT_LINE_BOTTOM_MARGIN = 10;
	private static final int DEFAULT_LINE_LEFT_MARGIN = -10;
	private static final int DEFAULT_LINE_RIGHT_MARGIN = -10;
	private static final int DEFAULT_LABEL_BOTTOM_MARGIN = 10;
	private static final int DEFAULT_COLUMN_PADDING = 10;
	
	private int topMargin = DEFAULT_TOP_MARGIN;
	private int bottomMargin = DEFAULT_BOTTOM_MARGIN;
	private int leftMargin = DEFAULT_LEFT_MARGIN;
	private int rightMargin = DEFAULT_RIGHT_MARGIN;
	private int valueTopMargin = DEFAULT_VALUE_TOP_MARGIN;
	private int valueBottomMargin = DEFAULT_VALUE_BOTTOM_MARGIN;
	private int lineTopMargin = DEFAULT_LINE_TOP_MARGIN;
	private int lineBottomMargin = DEFAULT_LINE_BOTTOM_MARGIN;
	private int lineLeftMargin = DEFAULT_LINE_LEFT_MARGIN;
	private int lineRightMargin = DEFAULT_LINE_RIGHT_MARGIN;
	private int labelBottomMargin = DEFAULT_LABEL_BOTTOM_MARGIN;
	private int columnPadding = DEFAULT_COLUMN_PADDING;
	
    float scaleFactor;
	
	private int lineColor = DEFAULT_LINE_COLOR;
	private int lineWidth = DEFAULT_LINE_WIDTH;
	private Paint linePaint;
	
	private int categoriesNumber;
	private int categoriesColors[];
	private int categoriesValuesColors[];
	private int columnsNumber;
	private String labels[];
	private float values[][];
	private int labelsColor = DEFAULT_LABELS_COLOR;
	private int labelsSize = DEFAULT_LABELS_SIZE;
	private int valuesSize = DEFAULT_VALUES_SIZE;
	
    public ChartView(Context context) {
        super(context);
        initialize();
    }
    
    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    
    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }
    
    public void initialize() {
    	
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        scaleFactor = metrics.density;

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        
        /*for (int i = 0; i < 20; i++)
        {
        	for (int j = 0; j < 10; j++)
        	{
        		columnsPaints[i][j] = new Paint();
        		valuesPaints[i][j] = new Paint();
        		valuesPaints[i][j].setTextSize(valuesSize * scaleFactor);
        		if (categoriesColors[j] == 0)
        		{
        			columnsPaints[i][j].setColor(DEFAULT_COLUMNS_COLOR);
        			valuesPaints[i][j].setColor(DEFAULT_VALUES_COLOR);
        		}
        		else
        		{
        			columnsPaints[i][j].setColor(categoriesColors[j]);
        			valuesPaints[i][j].setColor(categoriesValuesColors[j]);
        		}
        	}
        	labelsPaints[i] = new Paint();
        	labelsPaints[i].setColor(labelsColor);
        	labelsPaints[i].setTextSize(labelsSize * scaleFactor);
        }*/
    }

    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float viewWidth = getWidth() / scaleFactor;
            float viewHeight = getHeight() / scaleFactor;
            float valueSpace = valueTopMargin + valueBottomMargin + valuesSize;
            float lineSpace = lineTopMargin + lineBottomMargin + lineWidth;
            float labelSpace = labelBottomMargin + labelsSize;
            float maxColumnHeight = viewHeight - topMargin - valueSpace - lineSpace - labelSpace - bottomMargin;
            
            float maxValue = values[0][0];
            for (int i = 0; i < columnsNumber; i++)
            	for (int j = 0; j < categoriesNumber; j++)
            		if (maxValue < values[i][j])
            			maxValue = values[i][j];

            float x1 = (leftMargin + lineLeftMargin) * scaleFactor;
            float y1 = (topMargin + valueSpace + maxColumnHeight + lineTopMargin) * scaleFactor;
            float x2 = (viewWidth - rightMargin - lineRightMargin) * scaleFactor;
            float y2 = y1;
            canvas.drawLine(x1, y1, x2, y2, linePaint);
            
            float columnSpace = ((viewWidth - leftMargin - rightMargin) - (columnsNumber - 1) * columnPadding) / columnsNumber;
            Paint paint;
            for (int i = 0; i < columnsNumber; i++)
            {
            	for (int j = 0; j < categoriesNumber; j++)
            	{
            		x1 = (leftMargin + i * (columnSpace + columnPadding) + j * columnSpace / categoriesNumber) * scaleFactor;
            		y2 = (viewHeight - bottomMargin - labelSpace - lineSpace) * scaleFactor;
            		x2 = x1 + (columnSpace / categoriesNumber) * scaleFactor;
            		y1 = y2 - scaleFactor * values[i][j] * maxColumnHeight / maxValue;
            		paint = new Paint();
            		if (categoriesColors == null)
            			paint.setColor(DEFAULT_COLUMNS_COLOR);
            		else
            			paint.setColor(categoriesColors[j]);
            		canvas.drawRect(x1, y1, x2, y2, paint);
            		
            		y1 = y1 - (valueBottomMargin * scaleFactor);
            		paint = new Paint();
            		paint.setTextSize(valuesSize * scaleFactor);
            		if (categoriesValuesColors == null)
            		{
            			if (categoriesColors == null)
            				paint.setColor(DEFAULT_VALUES_COLOR);
            			else
            				paint.setColor(categoriesColors[j]);
            		}
            		else
            			paint.setColor(categoriesValuesColors[j]);
            		canvas.drawText(Float.toString(values[i][j]), x1, y1, paint);
            	}
            	paint = new Paint();
            	paint.setColor(labelsColor);
            	paint.setTextSize(labelsSize * scaleFactor);
            	x1 = x1 + columnSpace / categoriesNumber - columnSpace / 2 - columnSpace / categoriesNumber;
            	y2 = y2 + (lineSpace + labelSpace) * scaleFactor;
            	canvas.drawText(labels[i], x1, y2, paint);
            }
        }

//////////////////////////////////////////////////////////////////////setters

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	public void setValueTopMargin(int valueTopMargin) {
		this.valueTopMargin = valueTopMargin;
	}

	public void setValueBottomMargin(int valueBottomMargin) {
		this.valueBottomMargin = valueBottomMargin;
	}

	public void setLineTopMargin(int lineTopMargin) {
		this.lineTopMargin = lineTopMargin;
	}

	public void setLineBottomMargin(int lineBottomMargin) {
		this.lineBottomMargin = lineBottomMargin;
	}

	public void setLineLeftMargin(int lineLeftMargin) {
		this.lineLeftMargin = lineLeftMargin;
	}

	public void setLineRightMargin(int lineRightMargin) {
		this.lineRightMargin = lineRightMargin;
	}

	public void setLabelBottomMargin(int labelBottomMargin) {
		this.labelBottomMargin = labelBottomMargin;
	}

	public void setColumnPadding(int columnPadding) {
		this.columnPadding = columnPadding;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setCategoriesNumber(int categoriesNumber) {
		this.categoriesNumber = categoriesNumber;
	}

	public void setCategoriesColors(int[] categoriesColors) {
		this.categoriesColors = categoriesColors;
	}

	public void setCategoriesLabelsColors(int[] categoriesLabelsColors) {
		this.categoriesValuesColors = categoriesLabelsColors;
	}

	public void setColumnsNumber(int columnsNumber) {
		this.columnsNumber = columnsNumber;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public void setValues(float[][] values) {
		this.values = values;
	}

	public void setLabelsColor(int labelsColor) {
		this.labelsColor = labelsColor;
	}

	public void setLabelsSize(int labelsSize) {
		this.labelsSize = labelsSize;
	}

	public void setValuesSize(int valuesSize) {
		this.valuesSize = valuesSize;
	}
	
}
