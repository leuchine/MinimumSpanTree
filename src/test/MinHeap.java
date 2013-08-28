package test;
/**
 * 
 * 一个最小堆
 *
 */
public class MinHeap {
	public MinHeap(int MaxSize) {
		array = new SimEdge[MaxSize + 1];
		CurrentSize = 0;
		this.MaxSize = MaxSize;
	}

	public void add(SimEdge simEdge) {
		CurrentSize++;
		int i = CurrentSize;
		while (i != 1 && simEdge.weight < array[i / 2].weight) {
			array[i] = array[i / 2];
			i = i / 2;
		}
		array[i] = simEdge;
	}

	public SimEdge deleteMin() {
		SimEdge simEdge = array[1];
		SimEdge insert = array[CurrentSize];
		CurrentSize--;
		int i = 1;
		int ci = 2;
		while (ci <= CurrentSize) {
			if (ci < CurrentSize && array[ci].weight > array[ci + 1].weight)
				ci++;
			if (insert.weight <= array[ci].weight)
				break;
			array[i] = array[ci];
			i = ci;
			ci = ci * 2;
		}
		array[i] = insert;
		return simEdge;
	}

	public SimEdge[] getArray() {
		return array;
	}

	public void output() {
		for (int i = 1; i <= CurrentSize; i++) {
			System.out.println(array[i].weight);
		}
	}

	SimEdge array[] = null;
	int MaxSize;
	int CurrentSize;
}
//堆保存的数据结构
class SimEdge {
	int begin;
	int end;
	int weight;

	public SimEdge(int begin, int end, int weight) {
		this.begin = begin;
		this.end = end;
		this.weight = weight;
	}
}