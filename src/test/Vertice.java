package test;

import java.awt.Color;
import java.awt.geom.Point2D;
/**
 * 
 * ShowPanel�ж�������ݽṹ
 *
 */
public class Vertice {
	private Point2D.Double point = null;  //�����Բ��λ��
	private int number = -1;			//����ı��
	private Color color = null;			//�������ɫ

	public Vertice(double x, double y, int n, Color c) {
		point = new Point2D.Double(x, y);
		number = n;
		color = c;
	}

	public Vertice(Point2D.Double p, int n, Color c) {
		point = p;
		number = n;
		color = c;
	}

	public Point2D.Double getPoint() {
		return point;
	}

	public void setPoint(double x, double y) {
		this.point.setLocation(x, y);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
