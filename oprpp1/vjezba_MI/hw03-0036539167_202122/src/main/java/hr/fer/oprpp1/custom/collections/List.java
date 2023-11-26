package hr.fer.oprpp1.custom.collections;

public interface List<T> extends Collection<T>{
    /**
     * Returns the value of the node at the given index.
     * @param index the index of the node whose value is to be returned.
     * @return the value of the element at the given index.
     * @throws IndexOutOfBoundsException if the index is bigger than or equal to
     * the size of the list or negative.
     */
    T get(int index);

    /**
     * Inserts the specified value into the specified position in the list.
     * @param value the value to be inserted.
     * @param position the position to insert the value.
     * @throws IndexOutOfBoundsException if the specified position is less than zero
     * or bigger than the size of the list.
     * @throws NullPointerException if the specified value is null.
     */
    void insert(T value, int position);

    /**
     * Returns the index of the first occurrence of the specified value in the list.
     * If the value is not found in the list, returns -1.
     * @param value the value to search index for.
     * @return the index of the first occurrence of the specified value or
     * -1 if the value is not found in the list.
     * @throws NullPointerException if the value is null.
     */
    int indexOf(Object value);

    /**
     * Removes the node at the specified index from the list.
     * @param index the index of the node to be removed.
     * @throws IndexOutOfBoundsException if the index is bigger than or equal to
     * the size of the list or negative.
     */
    void remove(int index);

}
