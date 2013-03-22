package com.igrs.tivic.phone.Listener;

import android.util.SparseArray;
import android.util.SparseIntArray;

public interface EPGCollectionListener {
	public void notifyEPGCollection(SparseArray<SparseIntArray> result);
}
