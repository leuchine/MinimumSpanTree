package test;

import java.awt.Color;
import java.awt.geom.Line2D;
/**
 * 
 * ShowPanel�бߵ����ݽṹ
 *
 */
public class Edge {
	private Line2D.Double line = null; //�ߵ��߱�ʾ
	private int weight = -1;		    //�ߵ�Ȩֵ
	private Color color = null;			//�ߵ���ɫ
	//������
	public Edge(double x1, double y1, double x2, double y2, int w, Color c) {
		line = new Line2D.Double(x1, y1, x2, y2);
		this.weight = w;
		color = c;
	}
	//Getters��Setters
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
