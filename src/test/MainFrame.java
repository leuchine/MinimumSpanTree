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
 * 主界面
 */
public class MainFrame extends JFrame {
	public static int graph[][] = null;     //图的数组表示
	public static boolean isStep = false; 	//是否单步执行
	public static int first = 0;			//辅助单步执行的变量
	public static JLabel label = null;		//提示标签

	public MainFrame() {
		//窗口初始化
		this.setTitle("最小生成树");
		this.setSize(550, 700);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		//顶部绘图面板
		ShowPanel showPanel = new ShowPanel();
		VerticeListener l = new VerticeListener(showPanel);
		showPanel.addMouseListener(l);
		this.add(showPanel);
		showPanel.setBounds(0, 0, 550, 550);
		
		//中间绘制堆数组的面板
		ArrayPanel apanel = new ArrayPanel();
		this.add(apanel);
		apanel.setBounds(0, 550, 550, 50);
		
		//按钮面板
		JPanel buttonPanel = new JPanel();
		//添加边按钮
		JButton edgeButton = new JButton("添加边");
		edgeButton.addActionListener(new AddEdgeListener(showPanel, l));
		buttonPanel.add(edgeButton);
		//执行算法按钮
		JButton execute = new JButton("执行算法");
		execute.addActionListener(new ExecutionListener(showPanel, apanel));
		buttonPanel.add(execute);
		//单步执行按钮
		JButton step = new JButton("单步执行");
		step.addActionListener(new StepListener(showPanel, apanel));
		buttonPanel.add(step);
		//重新开始按钮
		JButton restart = new JButton("重新开始");
		restart.addActionListener(new RestartListener(showPanel));
		buttonPanel.add(restart);
		//案例演示按钮
		JButton random = new JButton("案例演示");
		random.addActionListener(new RandomListener(showPanel));
		buttonPanel.add(random);

		this.add(buttonPanel);
		buttonPanel.setBounds(0, 630, 550, 70);

		//添加提示标签
		JPanel panel = new JPanel();
		MainFrame.label = new JLabel("单击添加顶点");
		label.setFont(new Font("黑体", Font.BOLD + Font.ITALIC, 17));
		panel.add(label);
		this.add(panel);
		panel.setBounds(0, 600, 550, 80);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
}

//添加边按钮的侦听器
class AddEdgeListener implements ActionListener {
	private ShowPanel panel = null;
	private VerticeListener l = null;

	public AddEdgeListener(ShowPanel panel, VerticeListener l) {
		this.panel = panel;
		this.l = l;
	}

	public void actionPerformed(ActionEvent e) {
		MainFrame.graph = new int[ShowPanel.number][ShowPanel.number];
		
		//初始化图数组表示
		for (int i = 0; i < ShowPanel.number; i++) {
			for (int j = 0; j < ShowPanel.number; j++) {
				MainFrame.graph[i][j] = 0;
			}
		}
		//改变侦听器 以添加边
		panel.removeMouseListener(l);
		panel.addMouseListener(new EdgeListener(panel));
		MainFrame.label.setText("单击顶点添加边");
	}
}

//执行算法按钮的侦听器
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
					"停顿时间(单位:毫秒 默认1000)", "停顿时间设置", JOptionPane.PLAIN_MESSAGE,
					null, null, null));
		} catch (Exception ex) {

		}
		//开启一个Run线程执行算法
		Thread thread = new Thread(new Run(panel, time, p));
		thread.start();
	}
}
//实际执行算法的线程
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
		//等价类
		Equivalence equivalence = new Equivalence(ShowPanel.number);
		//最小堆
		MinHeap heap = new MinHeap(panel.getArrayE().size());
		//最小堆初始化
		for (int i = 0; i < ShowPanel.number; i++) {
			for (int j = 0; j < i; j++) {
				if (MainFrame.graph[i][j] != 0) {
					SimEdge edge = new SimEdge(i, j, MainFrame.graph[i][j]);
					heap.add(edge);
				}
			}
		}
		//绘制 堆数组的面板
		p.setCurrentSize(heap.CurrentSize);
		SimEdge array[] = new SimEdge[heap.CurrentSize+1];
		for (int i = 1; i <= heap.CurrentSize; i++) {
			SimEdge a = new SimEdge(0, 0, heap.array[i].weight);
			array[i] = a;
		}
		p.setArray(array);
		p.repaint();
		
		//执行算法
		int k = 0;
		while (k < ShowPanel.number - 1) {
			SimEdge edge = null;
			try {
				//删除权值最小的边
				edge = heap.deleteMin();
				//重画 堆数组面板
				p.setCurrentSize(heap.CurrentSize);
				array = new SimEdge[heap.CurrentSize+1];
				for (int i = 1; i <= heap.CurrentSize; i++) {
					SimEdge a = new SimEdge(0, 0, heap.array[i].weight);
					array[i] = a;
				}
				p.setArray(array);
				p.repaint();
				
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(null, "不是连通图,重新开始", "错误",
						JOptionPane.PLAIN_MESSAGE, null);

				return;
			}
			
			int i = equivalence.find(edge.begin);
			int j = equivalence.find(edge.end);
			//判断两个顶点是否连通
			if (i != j) {
				//不连通 动画画出这条边
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
							MainFrame.label.setText("顶点" + edge.begin + "和顶点"
									+ edge.end + "之间的边加入生成树");
						} else {
							MainFrame.label.setText("顶点" + edge.end + "和顶点"
									+ edge.begin + "之间的边加入生成树");
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
				//是否单步执行
				if (MainFrame.isStep == true) {
					try {
						Thread.sleep(400000);
					} catch (InterruptedException e) {
					}
				}
			} else {
				if (edge.begin < edge.end) {
					MainFrame.label.setText("顶点" + edge.begin + "和顶点"
							+ edge.end + "之间的边会产生回路，不加入");
				} else {
					MainFrame.label.setText("顶点" + edge.end + "和顶点"
							+ edge.begin + "之间的边会产生回路，不加入");
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
			}
		}
		JOptionPane.showMessageDialog(null, "最小生成树完成", "完成",
				JOptionPane.PLAIN_MESSAGE, null);

	}
}
//单步执行按钮的侦听器
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
		//是否单步执行
		if (MainFrame.first == 0) {
			MainFrame.first++;
			thread = new Thread(new Run(panel, 1200, p));
			thread.start();
		} else {
			thread.interrupt();
		}
	}
}
//重新开始按钮的侦听器
class RestartListener implements ActionListener {
	private ShowPanel panel = null;

	public RestartListener(ShowPanel panel) {
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e) {
		//重画每一条边
		ArrayList<Edge> arrayE = panel.getArrayE();
		for (int i = 0; i < arrayE.size(); i++) {
			arrayE.get(i).setColor(Color.RED);
		}
		panel.repaint();
		MainFrame.isStep = false;
		MainFrame.first = 0;
	}
}
//案例展示的侦听器
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
