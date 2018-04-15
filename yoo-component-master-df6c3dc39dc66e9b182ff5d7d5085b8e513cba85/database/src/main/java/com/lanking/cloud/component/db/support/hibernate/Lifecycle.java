package com.lanking.cloud.component.db.support.hibernate;

public interface Lifecycle<E> {

	void onLoad();

	void onSave();
}
