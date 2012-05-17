package org.adougou.utils;

/**
 * Queue implementation (adopted from "Data Structures in Java")
 */
public class Queue {
	
	private Object array_[];
	private int front_;
	private int rear_;
	private int count_;
	private int capacity_;
	private int capacityIncrement_;

	public Queue() {
		capacity_ = 10;
		array_ = new Object[capacity_];
		front_ = 0;
		rear_ = 0;
		capacityIncrement_ = 5;
	}

	public boolean empty() {
		return (count_ == 0);
	}

	public void insert(Object item) {
		// if the array does not have enough capacity, grow it
		if (count_ == capacity_) {
			capacity_ += capacityIncrement_;
			Object[] tempArray = new Object[capacity_];
			if (front_ < rear_) {	// if items ordered [front:rear-1]
				for (int i=front_; i < rear_; i++) {
					tempArray[i] = array_[i];
				}
			} else {	// items ordered in two sections
				for (int i=0; i < rear_; i++) {		// from [0:rear-1]
					tempArray[i] = array_[i];
				}
				for (int i=front_; i < count_; i++) { // from [front:count-1]
					tempArray[i+capacityIncrement_] = array_[i];
				}
				front_ += capacityIncrement_;
			}
			array_ = tempArray;
		}

		// insert the new item at the rear
		array_[rear_] = item;
		rear_ = (rear_+1)%capacity_;	// circular index
										// jumps back to front if at 
										// the end of the array
		count_++;
	}
	
	public Object remove() {
		if (count_ == 0) {
			return null;
		} else {
			Object forYou = array_[front_];
			front_ = (front_+1)%capacity_;
			count_--;
			return forYou;
		}
	}

	public int getCount() {
		return count_;
	}
}
