package application;

public class TNode<T extends Comparable<T>> {
	private T data;
	private TNode<T> left;
	private TNode<T> right;

	public TNode(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public TNode<T> getLeft() {
		return left;
	}

	public void setLeft(TNode<T> left) {
		this.left = left;
	}

	public TNode<T> getRight() {
		return right;
	}

	public void setRight(TNode<T> right) {
		this.right = right;
	}

	public boolean hasRight() {
		return this.right != null;
	}

	public boolean hasLeft() {
		return this.left != null;
	}

	public boolean isLeaf() {
		return !hasRight() && !hasLeft();
	}

	@Override
	public String toString() {
		return "[" + this.data + "]";
	}

}
