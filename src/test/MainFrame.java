package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * ������
 */
public class MainFrame extends JFrame {
	public static int graph[][] = null;     //ͼ�������ʾ
	public static boolean isStep = false; 	//�Ƿ񵥲�ִ��
	public static int first = 0;			//��������ִ�еı���
	public static JLabel label = null;		//��ʾ��ǩ

	public MainFrame() {
		//���ڳ�ʼ��
		this.setTitle("��С������");
		this.setSize(550, 700);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		//������ͼ���
		ShowPanel showPanel = new ShowPanel();
		VerticeListener l = new VerticeListener(showPanel);
		showPanel.addMouseListener(l);
		this.add(showPanel);
		showPanel.setBounds(0, 0, 550, 550);
		
		//�м���ƶ���������
		ArrayPanel apanel = new ArrayPanel();
		this.add(apanel);
		apanel.setBounds(0, 550, 550, 50);
		
		//��ť���
		JPanel buttonPanel = new JPanel();
		//��ӱ߰�ť
		JButton edgeButton = new JButton("��ӱ�");
		edgeButton.addActionListener(new AddEdgeListener(showPanel, l));
		buttonPanel.add(edgeButton);
		//ִ���㷨��ť
		JButton execute = new JButton("ִ���㷨");
		execute.addActionListener(new ExecutionListener(showPanel, apanel));
		buttonPanel.add(execute);
		//����ִ�а�ť
		JButton step = new JButton("����ִ��");
		step.addActionListener(new StepListener(showPanel, apanel));
		buttonPanel.add(step);
		//���¿�ʼ��ť
		JButton restart = new JButton("���¿�ʼ");
		restart.addActionListener(new RestartListener(showPanel));
		buttonPanel.add(restart);
		//������ʾ��ť
		JButton random = new JButton("������ʾ");
		random.addActionListener(new RandomListener(showPanel));
		buttonPanel.add(random);

		this.add(buttonPanel);
		buttonPanel.setBounds(0, 630, 550, 70);

		//�����ʾ��ǩ
		JPanel panel = new JPanel();
		MainFrame.label = new JLabel("������Ӷ���");
		label.setFont(new Font("����", Font.BOLD + Font.ITALIC, 17));
		panel.add(label);
		this.add(panel);
		panel.setBounds(0, 600, 550, 80);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
}

//��ӱ߰�ť��������
class AddEdgeListener implements ActionListener {
	private ShowPanel panel = null;
	private VerticeListener l = null;

	public AddEdgeListener(ShowPanel panel, VerticeListener l) {
		this.panel = panel;
		this.l = l;
	}

	public void actionPerformed(ActionEvent e) {
		MainFrame.graph = new int[ShowPanel.number][ShowPanel.number];
		
		//��ʼ��ͼ�����ʾ
		for (int i = 0; i < ShowPanel.number; i++) {
			for (int j = 0; j < ShowPanel.number; j++) {
				MainFrame.graph[i][j] = 0;
			}
		}
		//�ı������� ����ӱ�
		panel.removeMouseListener(l);
		panel.addMouseListener(new EdgeListener(panel));
		MainFrame.label.setText("����������ӱ�");
	}
}

//ִ���㷨��ť��������
class ExecutionListener implements ActionListener {
	private ShowPanel panel = null;
	private ArrayPanel p = null;

	public ExecutionListener(ShowPanel panel, ArrayPanel p) {
		this.panel = panel;
		this.p = p;
	}

	public void actionPerformed(ActionEvent e) {
		int time = 1000;
		try {
			time = Integer.parseInt((String) JOptionPane.showInputDialog(null,
					"ͣ��ʱ��(��λ:���� Ĭ��1000)", "ͣ��ʱ������", JOptionPane.PLAIN_MESSAGE,
					null, null, null));
		} catch (Exception ex) {

		}
		//����һ��Run�߳�ִ���㷨
		Thread thread = new Thread(new Run(panel, time, p));
		thread.start();
	}
}
//ʵ��ִ���㷨���߳�
class Run implements Runnable {
	private ShowPanel panel = null;
	private MainFrame frame = null;
	private int time;
	private ArrayPanel p = null;

	public Run(ShowPanel panel, int t, ArrayPanel p) {
		this.panel = panel;
		time = t;
		this.p = p;
	}

	public void run() {
		//�ȼ���
		Equivalence equivalence = new Equivalence(ShowPanel.number);
		//��С��
		MinHeap heap = new MinHeap(panel.getArrayE().size());
		//��С�ѳ�ʼ��
		for (int i = 0; i < ShowPanel.number; i++) {
			for (int j = 0; j < i; j++) {
				if (MainFrame.graph[i][j] != 0) {
					SimEdge edge = new SimEdge(i, j, MainFrame.graph[i][j]);
					heap.add(edge);
				}
			}
		}
		//���� ����������
		p.setCurrentSize(heap.CurrentSize);
		SimEdge array[] = new SimEdge[heap.CurrentSize+1];
		for (int i = 1; i <= heap.CurrentSize; i++) {
			SimEdge a = new SimEdge(0, 0, heap.array[i].weight);
			array[i] = a;
		}
		p.setArray(array);
		p.repaint();
		
		//ִ���㷨
		int k = 0;
		while (k < ShowPanel.number - 1) {
			SimEdge edge = null;
			try {
				//ɾ��Ȩֵ��С�ı�
				edge = heap.deleteMin();
				//�ػ� ���������
				p.setCurrentSize(heap.CurrentSize);
				array = new SimEdge[heap.CurrentSize+1];
				for (int i = 1; i <= heap.CurrentSize; i++) {
					SimEdge a = new SimEdge(0, 0, heap.array[i].weight);
					array[i] = a;
				}
				p.setArray(array);
				p.repaint();
				
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(null, "������ͨͼ,���¿�ʼ", "����",
						JOptionPane.PLAIN_MESSAGE, null);

				return;
			}
			
			int i = equivalence.find(edge.begin);
			int j = equivalence.find(edge.end);
			//�ж����������Ƿ���ͨ
			if (i != j) {
				//����ͨ ��������������
				ArrayList<Vertice> arrayV = panel.getArrayV();
				ArrayList<Edge> arrayE = panel.getArrayE();
				Point2D.Double p1 = arrayV.get(edge.begin).getPoint();
				Point2D.Double p2 = arrayV.get(edge.end).getPoint();
				for (int y = 0; y < arrayE.size(); y++) {
					Line2D.Double line = arrayE.get(y).getLine();
					if ((p1.x == line.x1 && p1.y == line.y1 && p2.x == line.x2
							&& p2.y == line.y2 || p1.x == line.x2
							&& p1.y == line.y2 && p2.x == line.x1
							&& p2.y == line.y1)) {
						Edge ed = arrayE.get(y);
						ed.setColor(Color.GREEN);
						int x3 = (int) ((2 * p1.x + p2.x) / (3));
						int y3 = (int) ((2 * p1.y + p2.y) / (3));
						ed.setLine(p1.x, p1.y, x3, y3);
						panel.repaint();
						if (edge.begin < edge.end) {
							MainFrame.label.setText("����" + edge.begin + "�Ͷ���"
									+ edge.end + "֮��ı߼���������");
						} else {
							MainFrame.label.setText("����" + edge.end + "�Ͷ���"
									+ edge.begin + "֮��ı߼���������");
						}
						try {
							Thread.sleep(time);
						} catch (InterruptedException e1) {

						}
						x3 = (int) ((p1.x + p2.x) / (2));
						y3 = (int) ((p1.y + p2.y) / (2));
						ed.setLine(p1.x, p1.y, x3, y3);
						panel.repaint();
						try {
							Thread.sleep(time);
						} catch (InterruptedException e1) {

						}
						ed.setLine(p1.x, p1.y, p2.x, p2.y);
						panel.repaint();
						try {
							Thread.sleep(time);
						} catch (InterruptedException e1) {

						}
					}
				}
				equivalence.union(i, j);
				k++;
				//�Ƿ񵥲�ִ��
				if (MainFrame.isStep == true) {
					try {
						Thread.sleep(400000);
					} catch (InterruptedException e) {
					}
				}
			} else {
				if (edge.begin < edge.end) {
					MainFrame.label.setText("����" + edge.begin + "�Ͷ���"
							+ edge.end + "֮��ı߻������·��������");
				} else {
					MainFrame.label.setText("����" + edge.end + "�Ͷ���"
							+ edge.begin + "֮��ı߻������·��������");
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
			}
		}
		JOptionPane.showMessageDialog(null, "��С���������", "���",
				JOptionPane.PLAIN_MESSAGE, null);

	}
}
//����ִ�а�ť��������
class StepListener implements ActionListener {
	private ShowPanel panel = null;
	private Thread thread;
	private ArrayPanel p;

	public StepListener(ShowPanel panel, ArrayPanel p) {
		this.panel = panel;
		this.p = p;
	}

	public void actionPerformed(ActionEvent e) {
		MainFrame.isStep = true;
		//�Ƿ񵥲�ִ��
		if (MainFrame.first == 0) {
			MainFrame.first++;
			thread = new Thread(new Run(panel, 1200, p));
			thread.start();
		} else {
			thread.interrupt();
		}
	}
}
//���¿�ʼ��ť��������
class RestartListener implements ActionListener {
	private ShowPanel panel = null;

	public RestartListener(ShowPanel panel) {
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e) {
		//�ػ�ÿһ����
		ArrayList<Edge> arrayE = panel.getArrayE();
		for (int i = 0; i < arrayE.size(); i++) {
			arrayE.get(i).setColor(Color.RED);
		}
		panel.repaint();
		MainFrame.isStep = false;
		MainFrame.first = 0;
	}
}
//����չʾ��������
class RandomListener implements ActionListener {

	private ShowPanel panel = null;

	public RandomListener(ShowPanel panel) {
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e) {
		Thread thread = new Thread(new RandomRun(panel));
		thread.start();
	}

	class RandomRun implements Runnable {
		private ShowPanel panel = null;

		public RandomRun(ShowPanel panel) {
			this.panel = panel;
		}

		public void run() {
			ArrayList<Vertice> arrayV = new ArrayList<Vertice>();
			Vertice v1 = new Vertice(264.0, 121.0, 0, Color.CYAN);
			Vertice v2 = new Vertice(95.0, 218.0, 1, Color.CYAN);
			Vertice v3 = new Vertice(455.0, 217.0, 2, Color.CYAN);
			Vertice v4 = new Vertice(92.0, 421.0, 3, Color.CYAN);
			Vertice v5 = new Vertice(459.0, 427.0, 4, Color.CYAN);
			Vertice v6 = new Vertice(270.0, 320.0, 5, Color.CYAN);

			arrayV.add(v1);
			arrayV.add(v2);
			arrayV.add(v3);
			arrayV.add(v4);
			arrayV.add(v5);
			arrayV.add(v6);

			ArrayList<Edge> arrayE = new ArrayList<Edge>();
			Edge e1 = new Edge(264.0, 121.0, 95.0, 218.0, 6, Color.red);
			Edge e2 = new Edge(264.0, 121.0, 455.0, 217.0, 5, Color.red);
			Edge e3 = new Edge(95.0, 218.0, 92.0, 421.0, 3, Color.red);
			Edge e4 = new Edge(95.0, 218.0, 270.0, 320.0, 5, Color.red);
			Edge e5 = new Edge(264.0, 121.0, 270.0, 320.0, 1, Color.red);
			Edge e6 = new Edge(92.0, 421.0, 270.0, 320.0, 5, Color.red);
			Edge e7 = new Edge(270.0, 320.0, 459.0, 427.0, 4, Color.red);
			Edge e8 = new Edge(92.0, 421.0, 459.0, 427.0, 6, Color.red);
			Edge e9 = new Edge(455.0, 217.0, 459.0, 427.0, 2, Color.red);
			Edge e10 = new Edge(270.0, 320.0, 455.0, 217.0, 7, Color.red);

			arrayE.add(e1);
			arrayE.add(e2);
			arrayE.add(e3);
			arrayE.add(e4);
			arrayE.add(e5);
			arrayE.add(e6);
			arrayE.add(e7);
			arrayE.add(e8);
			arrayE.add(e9);
			arrayE.add(e10);

			this.panel.setArrayE(arrayE);
			this.panel.setArrayV(arrayV);

			MainFrame.graph = new int[][] { { 0, 6, 5, 0, 0, 1 },
					{ 6, 0, 0, 3, 0, 5 }, { 5, 0, 0, 0, 2, 7 },
					{ 0, 3, 0, 0, 6, 5 }, { 0, 0, 2, 6, 0, 4 },
					{ 1, 5, 7, 5, 4, 0 } };
			ShowPanel.number = 6;
			this.panel.repaint();
		}
	}

}
