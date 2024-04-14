package pack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicArray {
    private Object[] array;
    private int size;
    private static final int DefaultCapacity = 10;

    public DynamicArray() {
        this(DefaultCapacity);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Invalid initial capacity: " + initialCapacity);
        }
        array = new Object[initialCapacity];
    }

    public void add(Object element) {
        ensureCapacity(size + 1);
        array[size++] = element;
    }

    public void addAll(Object[] elements) {
        ensureCapacity(size + elements.length);
        System.arraycopy(elements, 0, array, size, elements.length);
        size += elements.length;
    }

    public void add(int index, Object element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;
    }

    public Object get(int index) {
        checkIndex(index);
        return array[index];
    }

    public Object set(int index, Object element) {
        checkIndex(index);
        Object oldValue = array[index];
        array[index] = element;
        return oldValue;
    }

    public void remove(int index) {
        checkIndex(index);
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        array[--size] = null;
    }

    public boolean remove(Object element) {
        int index = indexOf(element);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    public boolean removeAll(Object[] elements) {
        boolean modified = false;
        for (Object element : elements) {
            while (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean contains(Object element) {
        return indexOf(element) != -1;
    }

    public int indexOf(Object element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object element) {
        for (int i = size - 1; i >= 0; i--) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        Arrays.fill(array, 0, size, null);
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public Iterator iterator() {
        return new DynamicArrayIterator();
    }

    public Iterator reverseIterator() {
        return new ReverseDynamicArrayIterator();
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > array.length) {
            int newCapacity = Math.max(minCapacity, array.length * 2);
            array = Arrays.copyOf(array, newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    private class DynamicArrayIterator implements Iterator {
        private int cursor;
        private int lastRet = -1;

        public DynamicArrayIterator() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return array[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            DynamicArray.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    private class ReverseDynamicArrayIterator implements Iterator {
        private int cursor;
        private int lastRet = -1;

        public ReverseDynamicArrayIterator() {
            cursor = size - 1;
        }

        @Override
        public boolean hasNext() {
            return cursor >= 0;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return array[cursor--];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            DynamicArray.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            sb.append(array[i]).append(", ");
        }
        sb.append(array[size - 1]).append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        // Yeni bir DynamicArray oluşturalım
        DynamicArray dynamicArray = new DynamicArray();

        // Eleman ekleme işlemleri
        dynamicArray.add(1);
        dynamicArray.add(2);
        dynamicArray.add(3);
        System.out.println("After adding elements: " + dynamicArray);

        // Index'e göre eleman ekleme işlemi
        dynamicArray.add(1, 10);
        System.out.println("After adding element at index 1: " + dynamicArray);

        // Elemanları dizi olarak ekleme işlemi
        Object[] elementsToAdd = {4, 5, 6};
        dynamicArray.addAll(elementsToAdd);
        System.out.println("After adding array elements: " + dynamicArray);

        // Eleman çıkarma işlemleri
        dynamicArray.remove(1);
        System.out.println("After removing element at index 1: " + dynamicArray);
        dynamicArray.remove((Object) 6);
        System.out.println("After removing element 6: " + dynamicArray);

        // İçeriyor mu kontrolü
        System.out.println("Contains 3? " + dynamicArray.contains(3));
        System.out.println("Contains 6? " + dynamicArray.contains(6));

        // İndex bulma işlemleri
        System.out.println("Index of 2: " + dynamicArray.indexOf(2));
        System.out.println("Last index of 2: " + dynamicArray.lastIndexOf(2));

        // Diziyi temizleme işlemi
        dynamicArray.clear();
        System.out.println("After clearing the array: " + dynamicArray);

        // Boş mu kontrolü
        System.out.println("Is empty? " + dynamicArray.isEmpty());

        // Boyut kontrolü
        System.out.println("Size: " + dynamicArray.size());

        // Iterator kullanımı
        dynamicArray.add(1);
        dynamicArray.add(2);
        dynamicArray.add(3);
        System.out.println("Using iterator:");
        Iterator iterator = dynamicArray.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        // Ters iterator kullanımı
        System.out.println("Using reverse iterator:");
        Iterator reverseIterator = dynamicArray.reverseIterator();
        while (reverseIterator.hasNext()) {
            System.out.println(reverseIterator.next());
        }
    }

}
