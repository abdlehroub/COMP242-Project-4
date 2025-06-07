package application;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<T extends Comparable<T>> {
	TNode<T> root;

	public void insert(T data) {
		root = insert(data, root);
	}

	private TNode<T> insert(T data, TNode<T> node) {
		if (node == null)
			return new TNode<>(data);

		if (data.compareTo(node.getData()) < 0)
			node.setLeft(insert(data, node.getLeft()));
		else if (data.compareTo(node.getData()) > 0)
			node.setRight(insert(data, node.getRight()));
		else // Overwrite
			node.setData(data);

		return rebalance(node);
	}

	public TNode<T> delete(T data) {
		root = delete(root, data);
		return root;
	}

	private TNode<T> delete(TNode<T> node, T data) {
		if (node == null)
			return null;

		if (data.compareTo(node.getData()) < 0) {
			node.setLeft(delete(node.getLeft(), data));
		} else if (data.compareTo(node.getData()) > 0) {
			node.setRight(delete(node.getRight(), data));
		} else {
			if (!node.hasLeft() && !node.hasRight()) {
				return null;
			} else if (node.hasLeft() && !node.hasRight()) {
				return node.getLeft();
			} else if (!node.hasLeft() && node.hasRight()) {
				return node.getRight();
			} else {
				TNode<T> successor = getMin(node.getRight());
				node.setData(successor.getData());
				node.setRight(delete(node.getRight(), successor.getData()));
			}
		}
		return rebalance(node);
	}

	private TNode<T> getMin(TNode<T> node) {
		while (node.hasLeft())
			node = node.getLeft();
		return node;
	}

	private TNode<T> rebalance(TNode<T> node) {
		int balance = balanceFactor(node);
		if (balance > 1) {
			if (balanceFactor(node.getLeft()) >= 0)
				node = rotateRight(node);
			else
				node = rotateLeftRight(node);
		} else if (balance < -1) {
			if (balanceFactor(node.getRight()) <= 0)
				node = rotateLeft(node);
			else
				node = rotateRightLeft(node);
		}
		return node;
	}

	private TNode<T> rotateRight(TNode<T> node) {
		TNode<T> temp = node.getLeft();
		node.setLeft(temp.getRight());
		temp.setRight(node);
		return temp;
	}

	private TNode<T> rotateLeft(TNode<T> node) {
		TNode<T> temp = node.getRight();
		node.setRight(temp.getLeft());
		temp.setLeft(node);
		return temp;
	}

	private TNode<T> rotateRightLeft(TNode<T> node) {
		node.setRight(rotateRight(node.getRight()));
		return rotateLeft(node);
	}

	private TNode<T> rotateLeftRight(TNode<T> node) {
		node.setLeft(rotateLeft(node.getLeft()));
		return rotateRight(node);
	}

	public int balanceFactor(TNode<T> node) {
		return getHeight(node.getLeft()) - getHeight(node.getRight());
	}

	public int getHeight() {
		return getHeight(root);
	}

	private int getHeight(TNode<T> node) {
		if (node == null)
			return 0;
		return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
	}

	public T search(T data) {
		TNode<T> curr = root;
		while (curr != null) {
			int cmp = data.compareTo(curr.getData());
			if (cmp == 0)
				return curr.getData();
			else if (cmp < 0)
				curr = curr.getLeft();
			else
				curr = curr.getRight();
		}
		return null;
	}

	public List<T> inOrderTraversal() {
		return inOrderTraversal(root);
	}

	private List<T> inOrderTraversal(TNode<T> node) {
		List<T> list = new ArrayList<>();
		if (node != null) {
			list.addAll(inOrderTraversal(node.getLeft()));
			list.add(node.getData());
			list.addAll(inOrderTraversal(node.getRight()));
		}
		return list;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void clear() {
		root = null;
	}
}
