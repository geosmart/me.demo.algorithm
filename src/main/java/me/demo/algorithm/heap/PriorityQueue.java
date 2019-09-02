package me.demo.algorithm.heap;

import sun.misc.SharedSecrets;

import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * >* An `unbounded priority queue` based on a `priority heap`.
 * >* The elements of the priority queue are ordered according to their `natural ordering`, or by a `Comparator provided` at queue >construction time, depending on which constructor is used.
 * >* A priority queue does `not permit null elements`.
 * >* A priority queue relying on natural ordering also does `not permit insertion of non-comparable objects` (doing so may result in >ClassCastException).
 * >* The `head of this queue` is the `least element` with respect to the `specified ordering`. If multiple elements are tied for >least value, the head is one of those elements -- ties are broken arbitrarily.
 * >* The queue retrieval operations `poll`, `remove`, `peek`, and element access the element at the head of the queue.
 * >* `A priority queue is unbounded`, but has an `internal capacity` governing the size of an `array` used to store the elements on >the queue. It is always at least as large as the queue size. As elements are added to a priority queue, its `capacity grows >automatically`. The details of the growth policy are not specified.
 * >* This class and its iterator implement all of the optional methods of the `Collection and Iterator interfaces`.
 * >* The Iterator provided in method iterator() is `not guaranteed` to traverse the elements of the priority queue in any particular >`order`. If you need ordered traversal, consider using `Arrays.sort(pq.toArray())`.
 * >* Note that this implementation is `not synchronized`. Multiple threads should not access a PriorityQueue instance concurrently if >any of the threads modifies the queue. Instead, use the `thread-safe PriorityBlockingQueue` class.
 * >* Implementation note: this implementation provides
 * >    * `O(log(n)) time` for the `enqueuing` and `dequeuing` methods (`offer, poll, remove() and add`);
 * >    * `linear time` for the `remove(Object) and contains(Object)` methods;
 * >    * `constant time` for the retrieval methods (`peek, element, and size`).
 * >This class is a member of the `Java Collections Framework`.
 *
 * @param <E> the type of elements held in this collection
 *
 * @author Josh Bloch, Doug Lea
 * @since 1.5
 */
public class PriorityQueue<E> extends AbstractQueue<E>
        implements java.io.Serializable {

    private static final long serialVersionUID = -7720805057305804111L;

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    /**
     * Priority queue represented as a balanced binary heap: the two
     * children of queue[n] are queue[2*n+1] and queue[2*(n+1)].  The
     * priority queue is ordered by comparator, or by the elements'
     * natural ordering, if comparator is null: For each node n in the
     * heap and each descendant d of n, n <= d.  The element with the
     * lowest value is in queue[0], assuming the queue is nonempty.
     */
    transient Object[] queue; // non-private to simplify nested class access

    /**
     * The number of elements in the priority queue.
     */
    private int size = 0;

    /**
     * The comparator, or null if priority queue uses elements'
     * natural ordering.
     */
    private final Comparator<? super E> comparator;

    /**
     * The number of times this priority queue has been
     * <i>structurally modified</i>.  See AbstractList for gory details.
     */
    transient int modCount = 0; // non-private to simplify nested class access

    /**
     * Creates a {@code PriorityQueue} with the default initial
     * capacity (11) that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     */
    public PriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Creates a {@code PriorityQueue} with the specified initial
     * capacity that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     *
     * @param initialCapacity the initial capacity for this priority queue
     *
     * @throws IllegalArgumentException if {@code initialCapacity} is less
     *                                  than 1
     */
    public PriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Creates a {@code PriorityQueue} with the default initial capacity and
     * whose elements are ordered according to the specified comparator.
     *
     * @param comparator the comparator that will be used to order this
     *                   priority queue.  If {@code null}, the {@linkplain Comparable
     *                   natural ordering} of the elements will be used.
     *
     * @since 1.8
     */
    public PriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    /**
     * Creates a {@code PriorityQueue} with the specified initial capacity
     * that orders its elements according to the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator      the comparator that will be used to order this
     *                        priority queue.  If {@code null}, the {@linkplain Comparable
     *                        natural ordering} of the elements will be used.
     *
     * @throws IllegalArgumentException if {@code initialCapacity} is
     *                                  less than 1
     */
    public PriorityQueue(int initialCapacity,
                         Comparator<? super E> comparator) {
        // Note: This restriction of at least one is not actually needed,
        // but continues for 1.5 compatibility
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        this.queue = new Object[initialCapacity];
        this.comparator = comparator;
    }

    /**
     * Creates a {@code PriorityQueue} containing the elements in the
     * specified collection.  If the specified collection is an instance of
     * a {@link SortedSet} or is another {@code PriorityQueue}, this
     * priority queue will be ordered according to the same ordering.
     * Otherwise, this priority queue will be ordered according to the
     * {@linkplain Comparable natural ordering} of its elements.
     *
     * @param c the collection whose elements are to be placed
     *          into this priority queue
     *
     * @throws ClassCastException   if elements of the specified collection
     *                              cannot be compared to one another according to the priority
     *                              queue's ordering
     * @throws NullPointerException if the specified collection or any
     *                              of its elements are null
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(Collection<? extends E> c) {
        if (c instanceof SortedSet<?>) {
            SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
            this.comparator = (Comparator<? super E>) ss.comparator();
            initElementsFromCollection(ss);
        } else if (c instanceof PriorityQueue<?>) {
            PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
            this.comparator = (Comparator<? super E>) pq.comparator();
            initFromPriorityQueue(pq);
        } else {
            this.comparator = null;
            initFromCollection(c);
        }
    }

    /**
     * Creates a {@code PriorityQueue} containing the elements in the
     * specified priority queue.  This priority queue will be
     * ordered according to the same ordering as the given priority
     * queue.
     *
     * @param c the priority queue whose elements are to be placed
     *          into this priority queue
     *
     * @throws ClassCastException   if elements of {@code c} cannot be
     *                              compared to one another according to {@code c}'s
     *                              ordering
     * @throws NullPointerException if the specified priority queue or any
     *                              of its elements are null
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(PriorityQueue<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initFromPriorityQueue(c);
    }

    /**
     * Creates a {@code PriorityQueue} containing the elements in the
     * specified sorted set.   This priority queue will be ordered
     * according to the same ordering as the given sorted set.
     *
     * @param c the sorted set whose elements are to be placed
     *          into this priority queue
     *
     * @throws ClassCastException   if elements of the specified sorted
     *                              set cannot be compared to one another according to the
     *                              sorted set's ordering
     * @throws NullPointerException if the specified sorted set or any
     *                              of its elements are null
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(SortedSet<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initElementsFromCollection(c);
    }

    private void initFromPriorityQueue(PriorityQueue<? extends E> c) {
        if (c.getClass() == PriorityQueue.class) {
            this.queue = c.toArray();
            this.size = c.size();
        } else {
            initFromCollection(c);
        }
    }

    private void initElementsFromCollection(Collection<? extends E> c) {
        Object[] a = c.toArray();
        // If c.toArray incorrectly doesn't return Object[], copy it.
        if (a.getClass() != Object[].class) {
            a = Arrays.copyOf(a, a.length, Object[].class);
        }
        int len = a.length;
        if (len == 1 || this.comparator != null) {
            for (Object o : a) {
                if (o == null) {
                    throw new NullPointerException();
                }
            }
        }
        this.queue = a;
        this.size = a.length;
    }

    /**
     * Initializes queue array with elements from the given Collection.
     *
     * @param c the collection
     */
    private void initFromCollection(Collection<? extends E> c) {
        initElementsFromCollection(c);
        heapify();
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Increases the capacity of the array.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        int oldCapacity = queue.length;
        // Double size if size<64; else grow by 50%
        int newCapacity = oldCapacity + (
                (oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1)
        );
        // overflow-conscious code 防越界
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        //将queue中数据复制到扩容后的queue
        queue = Arrays.copyOf(queue, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        // overflow
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @return {@code true} (as specified by {@link Collection#add})
     *
     * @throws ClassCastException   if the specified element cannot be
     *                              compared with elements currently in this priority queue
     *                              according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @return {@code true} (as specified by {Queue#offer})
     *
     * @throws ClassCastException   if the specified element cannot be
     *                              compared with elements currently in this priority queue
     *                              according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        //修改次数+1
        modCount++;
        int i = size;
        if (i >= queue.length) {
            //队列已满时，动态扩容
            grow(i + 1);
        }
        size = i + 1;
        if (i == 0) {
            //队列为空时
            queue[0] = e;
        } else {
            //上浮调整堆顺序
            siftUp(i, e);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        //返回堆顶元素
        return (size == 0) ? null : (E) queue[0];
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element {@code e} such
     * that {@code o.equals(e)}, if this queue contains one or more such
     * elements.  Returns {@code true} if and only if this queue contained
     * the specified element (or equivalently, if this queue changed as a
     * result of the call).
     *
     * @param o element to be removed from this queue, if present
     *
     * @return {@code true} if this queue changed as a result of the call
     */
    @Override
    public boolean remove(Object o) {
        //是否存在元素
        int i = indexOf(o);
        if (i == -1) {
            return false;
        } else {
            //移除制定index的元素
            removeAt(i);
            return true;
        }
    }

    /**
     * Version of remove using reference equality, not equals.
     * Needed by iterator.remove.
     *
     * @param o element to be removed from this queue, if present
     *
     * @return {@code true} if removed
     */
    boolean removeEq(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == queue[i]) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     *
     * @return {@code true} if this queue contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Returns an array containing all of the elements in this queue.
     * The elements are in no particular order.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this queue.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this queue
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(queue, size);
    }

    /**
     * Returns an array containing all of the elements in this queue; the
     * runtime type of the returned array is that of the specified array.
     * The returned array elements are in no particular order.
     * If the queue fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this queue.
     *
     * <p>If the queue fits in the specified array with room to spare
     * (i.e., the array has more elements than the queue), the element in
     * the array immediately following the end of the collection is set to
     * {@code null}.
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose {@code x} is a queue known to contain only strings.
     * The following code can be used to dump the queue into a newly
     * allocated array of {@code String}:
     *
     * <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
     * <p>
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * @param a the array into which the elements of the queue are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     *
     * @return an array containing all of the elements in this queue
     *
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in
     *                              this queue
     * @throws NullPointerException if the specified array is null
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        final int size = this.size;
        if (a.length < size) {
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(queue, size, a.getClass());
        }
        System.arraycopy(queue, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private final class Itr implements Iterator<E> {
        /**
         * Index (into queue array) of element to be returned by
         * subsequent call to next.
         */
        private int cursor = 0;

        /**
         * Index of element returned by most recent call to next,
         * unless that element came from the forgetMeNot list.
         * Set to -1 if element is deleted by a call to remove.
         */
        private int lastRet = -1;

        /**
         * A queue of elements that were moved from the unvisited portion of
         * the heap into the visited portion as a result of "unlucky" element
         * removals during the iteration.  (Unlucky element removals are those
         * that require a siftup instead of a siftdown.)  We must visit all of
         * the elements in this list to complete the iteration.  We do this
         * after we've completed the "normal" iteration.
         * <p>
         * We expect that most iterations, even those involving removals,
         * will not need to store elements in this field.
         */
        private ArrayDeque<E> forgetMeNot = null;

        /**
         * Element returned by the most recent call to next iff that
         * element was drawn from the forgetMeNot list.
         */
        private E lastRetElt = null;

        /**
         * The modCount value that the iterator believes that the backing
         * Queue should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor < size ||
                    (forgetMeNot != null && !forgetMeNot.isEmpty());
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor < size) {
                return (E) queue[lastRet = cursor++];
            }
            if (forgetMeNot != null) {
                lastRet = -1;
                lastRetElt = forgetMeNot.poll();
                if (lastRetElt != null) {
                    return lastRetElt;
                }
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastRet != -1) {
                E moved = PriorityQueue.this.removeAt(lastRet);
                lastRet = -1;
                if (moved == null) {
                    cursor--;
                } else {
                    if (forgetMeNot == null) {
                        forgetMeNot = new ArrayDeque<>();
                    }
                    forgetMeNot.add(moved);
                }
            } else if (lastRetElt != null) {
                PriorityQueue.this.removeEq(lastRetElt);
                lastRetElt = null;
            } else {
                throw new IllegalStateException();
            }
            expectedModCount = modCount;
        }
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Removes all of the elements from this priority queue.
     * The queue will be empty after this call returns.
     */
    @Override
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        //queue修改次数+1
        modCount++;
        //堆顶元素
        E result = (E) queue[0];

        //堆尾索引
        int s = --size;
        //堆尾元素
        E x = (E) queue[s];
        //置空堆尾元素
        queue[s] = null;
        if (s != 0) {
            //堆尾元素拿出作为堆顶值后，从堆顶执行下沉
            siftDown(0, x);
        }
        //返回堆顶元素
        return result;
    }

    /**
     * Removes the ith element from queue.
     * <p>
     * Normally this method leaves the elements at up to i-1,
     * inclusive, untouched.  Under these circumstances, it returns null.
     * Occasionally, in order to maintain the heap invariant,
     * it must swap a later element of the list with one earlier thani.
     * Under these circumstances,
     * this method returns the element that was previously at the end of the list and is now at some position before i.
     * This fact is used by iterator.remove so as to avoid missing traversing elements.
     */
    @SuppressWarnings("unchecked")
    private E removeAt(int i) {
        assert i >= 0 && i < size;
        // 修改次数+1
        modCount++;
        // 堆尾元素Index
        int s = --size;
        if (s == i) {
            //如果删除的是堆尾元素，不需要进行siftUp
            queue[i] = null;
        } else {
            //拿出堆尾元素
            E moved = (E) queue[s];
            queue[s] = null;
            //将堆尾元素放到要删除的元素的位置，并执行siftDown
            siftDown(i, moved);
            //siftDown后，若元素没有改变，可能是因为要删除的结点和堆尾结点是跨子树，或者要删除的结点是叶子结点
            if (queue[i] == moved) {
                //如果删除的元素和堆尾元素不在一个子树，需要siftUp操作
                siftUp(i, moved);
                if (queue[i] != moved) {
                    return moved;
                }
            }
        }
        return null;
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by
     * promoting x up the tree until it is greater than or equal to
     * its parent, or is the root.
     * <p>
     * To simplify and speed up coercions and comparisons. the
     * Comparable and Comparator versions are separated into different
     * methods that are otherwise identical. (Similarly for siftDown.)
     *
     * @param k the position to fill
     * @param x the item to insert
     */
    private void siftUp(int k, E x) {
        if (comparator != null) {
            siftUpUsingComparator(k, x);
        } else {
            siftUpComparable(k, x);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        //从当前节点循环上浮到堆顶节点
        while (k > 0) {
            //k节点的父节点索引
            int parent = (k - 1) >>> 1;
            //k节点的父节点值
            Object e = queue[parent];
            //比较k节点与父节点的值大小，父节点值较小时，终止遍历
            if (key.compareTo((E) e) >= 0) {
                break;
            }
            //父节点值较大时，交换k节点与父节点值
            queue[k] = e;
            //当前节点移到父节点，继续向上遍历
            k = parent;
        }
        //将当前节点值赋给交换后的父节点
        queue[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0) {
                break;
            }
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by demoting x down the tree repeatedly
     * until it is less than or equal to its children or is a leaf.
     *
     * @param k the position to fill
     * @param x the item to insert
     */
    private void siftDown(int k, E x) {
        if (comparator != null) {
            //按自定义顺序swap下沉
            siftDownUsingComparator(k, x);
        } else {
            //按字典顺序swap下沉
            siftDownComparable(k, x);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int parent, E x) {
        Comparable<? super E> parentVal = (Comparable<? super E>) x;
        int half = size >>> 1;
        //二叉树结构，下标大于size/2都是叶子节点，其他的节点都有子节点。
        //循环直到k没有子节点：loop while a non-leaf
        while (parent < half) {
            //假设left节点为child中的最小值节点
            int left = (parent << 1) + 1;
            int right = left + 1;
            Object minVal = queue[left];
            //存在right，且right<left，则最小为right
            if (right < size && ((Comparable<? super E>) minVal).compareTo((E) queue[right]) > 0) {
                left = right;
                minVal = queue[right];
            }
            //如果parent节点<min(left,right),则不需要swap
            if (parentVal.compareTo((E) minVal) <= 0) {
                break;
            }
            //否则swap parent节点和min(left,right)的节点
            queue[parent] = minVal;
            //当前父节点取最小值的index继续loop
            parent = left;
        }
        //1.当前节点没有子节点，则k是叶子节点的下标，没有比它更小的了，直接赋值即可
        //2.当前节点下沉n轮后，将节点的值放到最终不需要再交换的位置（没有比它更小的或者到达叶子节点）
        queue[parent] = parentVal;
    }

    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size && comparator.compare((E) c, (E) queue[right]) > 0) {
                c = queue[child = right];
            }
            if (comparator.compare(x, (E) c) <= 0) {
                break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     */
    @SuppressWarnings("unchecked")
    private void heapify() {
        //从最后一个非叶子节点（父亲节点）开始遍历所有父节点，直到堆顶
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            //下沉（将3 or 2者中较大元素下沉）
            siftDown(i, (E) queue[i]);
        }
    }

    /**
     * Returns the comparator used to order the elements in this
     * queue, or {@code null} if this queue is sorted according to
     * the {@linkplain Comparable natural ordering} of its elements.
     *
     * @return the comparator used to order this queue, or
     * {@code null} if this queue is sorted according to the
     * natural ordering of its elements
     */
    public Comparator<? super E> comparator() {
        return comparator;
    }

    /**
     * Saves this queue to a stream (that is, serializes it).
     *
     * @param s the stream
     *
     * @serialData The length of the array backing the instance is
     * emitted (int), followed by all of its elements
     * (each an {@code Object}) in the proper order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out element count, and any hidden stuff
        s.defaultWriteObject();

        // Write out array length, for compatibility with 1.5 version
        s.writeInt(Math.max(2, size + 1));

        // Write out all elements in the "proper order".
        for (int i = 0; i < size; i++) {
            s.writeObject(queue[i]);
        }
    }

    /**
     * Reconstitutes the {@code PriorityQueue} instance from a stream
     * (that is, deserializes it).
     *
     * @param s the stream
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in (and discard) array length
        s.readInt();

        SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, size);
        queue = new Object[size];

        // Read in all elements.
        for (int i = 0; i < size; i++) {
            queue[i] = s.readObject();
        }

        // Elements are guaranteed to be in "proper order", but the
        // spec has never explained what that might be.
        heapify();
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * queue.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, and {@link Spliterator#NONNULL}.
     * Overriding implementations should document the reporting of additional
     * characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this queue
     *
     * @since 1.8
     */
    @Override
    public final Spliterator<E> spliterator() {
        return new PriorityQueueSpliterator<E>(this, 0, -1, 0);
    }

    static final class PriorityQueueSpliterator<E> implements Spliterator<E> {
        /*
         * This is very similar to ArrayList Spliterator, except for
         * extra null checks.
         */
        private final PriorityQueue<E> pq;
        private int index;            // current index, modified on advance/split
        private int fence;            // -1 until first use
        private int expectedModCount; // initialized when fence set

        /**
         * Creates new spliterator covering the given range
         */
        PriorityQueueSpliterator(PriorityQueue<E> pq, int origin, int fence,
                                 int expectedModCount) {
            this.pq = pq;
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize fence to size on first use
            int hi;
            if ((hi = fence) < 0) {
                expectedModCount = pq.modCount;
                hi = fence = pq.size;
            }
            return hi;
        }

        @Override
        public PriorityQueueSpliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null :
                    new PriorityQueueSpliterator<E>(pq, lo, index = mid,
                            expectedModCount);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc; // hoist accesses and checks from loop
            PriorityQueue<E> q;
            Object[] a;
            if (action == null) {
                throw new NullPointerException();
            }
            if ((q = pq) != null && (a = q.queue) != null) {
                if ((hi = fence) < 0) {
                    mc = q.modCount;
                    hi = q.size;
                } else {
                    mc = expectedModCount;
                }
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (E e; ; ++i) {
                        if (i < hi) {
                            if ((e = (E) a[i]) == null) { // must be CME
                                break;
                            }
                            action.accept(e);
                        } else if (q.modCount != mc) {
                            break;
                        } else {
                            return;
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            int hi = getFence(), lo = index;
            if (lo >= 0 && lo < hi) {
                index = lo + 1;
                @SuppressWarnings("unchecked") E e = (E) pq.queue[lo];
                if (e == null) {
                    throw new ConcurrentModificationException();
                }
                action.accept(e);
                if (pq.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return (long) (getFence() - index);
        }

        @Override
        public int characteristics() {
            return Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL;
        }
    }
}
