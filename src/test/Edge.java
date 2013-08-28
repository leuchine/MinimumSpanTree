package test;

import java.awt.Color;
import java.awt.geom.Line2D;
/**
 * 
 * ShowPanel中边的数据结构
 *
 */
public class Edge {
	private Line2D.Double line = null; //边的线表示
	private int weight = -1;		    //边的权值
	private Color color = null;			//边的颜色
	//构造器
	public Edge(double x1, double y1, double x2, double y2, int w, Color c) {
		line = new Line2D.Double(x1, y1, x2, y2);
		this.weight = w;
		color = c;
	}
	//Getters和Setters
	public Line2D.Double getLine() {
		return line;
	}

	public void setLine(double x1, double y1, double x2, double y2) {
		this.line.setLine(x1, y1, x2, y2);
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
