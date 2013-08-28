package test;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

/**
 * 
 * ������ չʾ���
 * 
 */
public class ArrayPanel extends JPanel {

	private SimEdge array[]; // ����ѵ�����
	private int CurrentSize; // ����Ԫ�ظ���

	public ArrayPanel() {
		CurrentSize = 0;
		array = null;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);

		Stroke stroke = new BasicStroke(1.9f);
		g2.setStroke(stroke);

		boolean flag = true;
		int x = 45;
		int y = 12;
		// ����ÿ��Ԫ��
		for (int i = 1; i <= CurrentSize; i++) {
			if (flag) {
				g2.drawString("������", 10, 25);
				flag = false;
			}
			Integer weight = array[i].weight;
			g2.drawRect(x, y, 25, 25);
			g2.drawString(weight.toString(), x + 10, y + 15);

			x = x + 25;
		}

	}

	// Getters��Setters
	public void setCurrentSize(int currentSize) {
		CurrentSize = currentSize;
	}

	public void setArray(SimEdge[] array) {
		this.array = array;
	}
}
