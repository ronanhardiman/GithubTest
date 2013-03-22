package com.igrs.tivic.phone.Bean;

public class Point {
	public int xPos = 0;
	public int yPos = 0;
	Point(int xpos, int ypos)
	{
		xPos = xpos;
		yPos = ypos;
	}
	Point()
	{
		
	}
	public void setPoint(int xpos, int ypos)
	{
		xPos = xpos;
		yPos = ypos;
	}
	
	public Point newPoint(int xpos, int ypos)
	{
		return new Point(xpos, ypos);
	}
	
	public int getX()
	{
		return xPos;
	}
	
	public int getY()
	{
		return yPos;
	}
}
