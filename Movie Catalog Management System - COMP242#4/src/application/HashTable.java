package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HashTable<T extends Comparable<T>> implements Iterable<T> {
	private AVLTree<T>[] table;
	private int size;
	private int count;

	@SuppressWarnings("unchecked")
	public HashTable() {
		setSize(5);
		table = (AVLTree<T>[]) new AVLTree[5];
		for (int i = 0; i < table.length; i++) {
			table[i] = new AVLTree<>();
		}
	}

	@SuppressWarnings("unchecked")
	public HashTable(int size) {
		setSize(size);
		table = (AVLTree<T>[]) new AVLTree[this.size];
		for (int i = 0; i < table.length; i++) {
			table[i] = new AVLTree<>();
		}
	}

	@SuppressWarnings("unchecked")
	private void rehash() {
		reSize(); // Update size
		AVLTree<T>[] oldTable = table;
		table = (AVLTree<T>[]) new AVLTree[size];
		for (int i = 0; i < size; i++) {
			table[i] = new AVLTree<>();
		}
		for (AVLTree<T> avlTree : oldTable) {
			List<T> elements = avlTree.inOrderTraversal();
			for (T data : elements) {
				int index = hash(data.hashCode());
				table[index].insert(data);
			}
		}
	}

	private void reSize() {
		this.size *= 2;
		while (!isPrime(this.size)) {
			this.size++;
		}
	}

	private int getAvgHeight() {
		int sum = 0;
		int count = 0;
		for (AVLTree<T> avlTree : table) {
			if (!avlTree.isEmpty()) {
				sum += avlTree.getHeight();
				count++;
			}
		}
		return count == 0 ? 0 : sum / count;// To avoid division by zero
	}

	private boolean isPrime(int n) {
		if (n < 2)
			return false;
		int sqrt = (int) Math.sqrt(n);
		for (int i = 2; i <= sqrt; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	private int hash(int key) {
		return Math.floorMod(key, table.length);
	}

	public boolean insert(T data) {
		if ((double) count / size > 5 || getAvgHeight() > 3) {
			rehash();
		}
		int index = hash(data.hashCode());
		if (table[index].search(data) == null) {
			table[index].insert(data);
			count++;
			return true; // True for insert and false for overwrite
		} else {
			table[index].insert(data); // Overwrite if exists
			return false;
		}
	}

	public T search(T data) {
		int index = hash(data.hashCode());
		return table[index].search(data);
	}

	public boolean contains(T data) {
		int index = hash(data.hashCode());
		return table[index].search(data) != null;
	}

	public TNode<T> delete(T data) {
		int index = hash(data.hashCode());
		if (table[index].search(data) != null) {
			count--;
		}
		return table[index].delete(data);
	}

	public void clear() {
		for (AVLTree<T> avlTree : table) {
			avlTree.clear();
		}
		count = 0;
	}

	public int getSize() {
		return size;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setSize(int size) {
		while (!isPrime(size)) {
			size++;
		}
		this.size = size;
	}

	public AVLTree<T> getCell(int index) {
		if (index > size)
			throw new IndexOutOfBoundsException();
		return table[index];
	}

	@Override
	public Iterator<T> iterator() {
		List<T> allElements = new ArrayList<>();
		for (AVLTree<T> avlTree : table) {
			allElements.addAll(avlTree.inOrderTraversal());
		}
		return allElements.iterator();
	}
}
