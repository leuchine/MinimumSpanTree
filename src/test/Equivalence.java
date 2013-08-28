package test;
/**
 * 
 * 一个等价类
 *
 */
public class Equivalence {
	private int array[] = null;

	public Equivalence(int n) {
		array = new int[n];
		for (int i = 0; i < n; i++) {
			array[i] = i;
		}
	}

	public int find(int i) {
		return array[i];
	}

	public void union(int x, int y) {
		for (int k = 0; k < array.length; k++) {
			if (array[k] == x)
				array[k] = y;
		}
	}
}
