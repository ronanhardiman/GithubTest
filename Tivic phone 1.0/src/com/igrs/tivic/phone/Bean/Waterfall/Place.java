package com.igrs.tivic.phone.Bean.Waterfall;

public class Place {
	private float point_x;
	private float point_y;
	private float display_width;
	private float display_height;
	public float getPoint_x() {
		return point_x;
	}
	public void setPoint_x(float point_x) {
		this.point_x = point_x;
	}
	public float getPoint_y() {
		return point_y;
	}
	public void setPoint_y(float point_y) {
		this.point_y = point_y;
	}
	public float getDisplay_width() {
		return display_width;
	}
	public void setDisplay_width(float display_width) {
		this.display_width = display_width;
	}
	public float getDisplay_height() {
		return display_height;
	}
	public void setDisplay_height(float display_height) {
		this.display_height = display_height;
	}
	@Override
	public String toString() {
		return "Place [point_x=" + point_x +", point_y=" + point_y +",display_width=" + display_width +
		",display_height=" + display_height +"]";
	}
}
