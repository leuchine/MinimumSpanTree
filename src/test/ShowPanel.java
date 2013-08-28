package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * 
 * 展示图和算法的面板
 *
 */
public class ShowPanel extends JPanel {
	private ArrayList<Vertice> arrayV = new ArrayList<Vertice>(20); //保存要画的顶点
	private ArrayList<Edge> arrayE = new ArrayList<Edge>(20);       //保存要画的边
	public static int number = 0;									//顶点的个数

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		//画顶点
		for (int i = 0; i < arrayV.size(); i++) {
			g2.setColor(arrayV.get(i).getColor());
			Point2D.Double point = arrayV.get(i).getPoint();
			g2.fillOval((int) point.x - 15, (int) point.y - 15, 30, 30);
			g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 19));
			g2.setColor(Color.WHITE);
			g2.drawString(Integer.toString(arrayV.get(i).getNumber()),
					(int) point.x - 7, (int) point.y + 7);
		}
		Stroke stroke = new BasicStroke(1.7f);
		g2.setStroke(stroke);
		//画边
		for (int i = 0; i < arrayE.size(); i++) {
			g2.setColor(arrayE.get(i).getColor());
			g2.drawLine((int) arrayE.get(i).getLine().x1, (int) arrayE.get(i)
					.getLine().y1, (int) arrayE.get(i).getLine().x2,
					(int) arrayE.get(i).getLine().y2);
			int x = ((int) arrayE.get(i).getLine().x1 + (int) arrayE.get(i)
					.getLine().x2) / 2;
			int y = ((int) arrayE.get(i).getLine().y1 + (int) arrayE.get(i)
					.getLine().y2) / 2;
			g2.setColor(Color.BLUE);
			g2.drawString(Integer.toString(arrayE.get(i).getWeight()), x, y);
		}
	}

	public ArrayList<Edge> getArrayE() {
		return arrayE;
	}

	public void addVertice(Vertice v) {
		if (v != null) {
			arrayV.add(v);
		}
		this.repaint();
	}

	public void addEdge(Edge e) {
		arrayE.add(e);
		this.repaint();
	}

	public ArrayList<Vertice> getArrayV() {
		return arrayV;
	}

	public void setArrayV(ArrayList<Vertice> arrayV) {
		this.arrayV = arrayV;
	}

	public void setArrayE(ArrayList<Edge> arrayE) {
		this.arrayE = arrayE;
	}
	
	
}
//点击面板时添加顶点的侦听器
class VerticeListener extends MouseAdapter {
	private ShowPanel panel = null;

	public VerticeListener(ShowPanel panel) {
		this.panel = panel;
	}

	public void mouseClicked(MouseEvent e) {
		Vertice v = new Vertice((double) e.getX(), (double) e.getY(),
				ShowPanel.number, Color.CYAN);
		ShowPanel.number++;
		panel.addVertice(v);
	}
}
//点击面板时添加边的侦听器
class EdgeListener extends MouseAdapter {
	private ShowPanel panel = null;

	public EdgeListener(ShowPanel panel) {
		this.panel = panel;
	}

	public void mouseClicked(MouseEvent e) {
		int contain = -1;
		int differColor = -1;
		ArrayList<Vertice> arrayV = panel.getArrayV();
		int x = e.getX();
		int y = e.getY();
		for (int i = 0; i < arrayV.size(); i++) {
			Point2D.Double point = arrayV.get(i).getPoint();
			Ellipse2D.Double ellipse = new Ellipse2D.Double(point.x - 15,
					point.y - 15, 30, 30);
			if (ellipse.contains(x, y))
				contain = i;
			if (!arrayV.get(i).getColor().equals(Color.CYAN))
				differColor = i;
		}
		
		//点击点没有在任何一个顶点的范围内
		if (contain == -1)
			return;
		//两个顶点已配对 画出一条边来
		if (differColor != -1) {
			int weight = 0;
			while (true) {
				//输入权值
				String input = (String) JOptionPane.showInputDialog(panel,
						"请输入权值", "权值", JOptionPane.PLAIN_MESSAGE, null, null,
						null);
				if (input != null) {
					try {
						weight = Integer.parseInt(input);
						if (weight > 0)
							break;
					} catch (Exception exception) {

					}
				}
			}
			//设置这条边的数据 然后加入ArrayList
			arrayV.get(differColor).setColor(Color.CYAN);
			Edge edge = new Edge(arrayV.get(differColor).getPoint().x, arrayV
					.get(differColor).getPoint().y, arrayV.get(contain)
					.getPoint().x, arrayV.get(contain).getPoint().y, weight,
					Color.red);
			panel.addEdge(edge);
			MainFrame.graph[arrayV.get(differColor).getNumber()][arrayV.get(
					contain).getNumber()] = weight;
			MainFrame.graph[arrayV.get(contain).getNumber()][arrayV.get(
					differColor).getNumber()] = weight;
		} 
		//已选中一个顶点
		else {
			arrayV.get(contain).setColor(Color.blue);
			panel.repaint();
		}

	}
}