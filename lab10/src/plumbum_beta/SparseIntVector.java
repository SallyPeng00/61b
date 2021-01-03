package plumbum_beta;

import java.util.List;
import java.util.ArrayList;

/**
 * Plumbum|Beta v0.0.0.1
 *
 * SparseIntVector: a class representing an ordered 1 dimensional vector of integers, optimized
 * for sparse vectors. Sparse vectors are vectors that contain very few non-zero elements.
 *
 * @author Eli Lipsitz
 */

public class SparseIntVector {
	private List<Entry> entries;
	private int size;

	public SparseIntVector(int... values) {
		// replace this comment with something?
		entries = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
//			if (values[i] != 0) {
				entries.add(new Entry(i, values[i]));
//			}
		}
		// replace this comment with something?

		size = values.length;

		// replace this comment with something?
	}

	public Entry getEntry(int index) {
		return entries.get(index);
	}

	/**
	 * Returns the size of the vector (i.e. the dimension of the vector). This includes zero entries.
	 * @return the dimension of the vector
	 */
	public int size() {
		return size;
	}

	/**
	 * Computes the dot product of two SparseIntVectors
	 * @param a the first vector
	 * @param b the second vector
	 * @return the dot product of the two vectors
	 * @throws IllegalArgumentException if the two vectors do not have the same size.
	 */
	public static int dot(SparseIntVector a, SparseIntVector b) {
		if (a.size() != b.size()) {
			throw new IllegalArgumentException("you cannot dot vectors of different sizes");
		}

		int value = 0;
		for (int ind = 0; ind < a.size(); ind++) {
			System.out.println(ind);
			Entry entryA = a.getEntry(ind);
			Entry entryB = b.getEntry(ind);
			value += entryA.value * entryB.value;
		}

		return value;
	}

	private class Entry implements Comparable<Entry> {
		int index;
		int value;

		Entry(int index, int value) {
			this.index = index;
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		@Override
		public int compareTo(Entry o) {
			return Integer.compare(index, o.index);
		}
	}
}
