package com.lanking.cloud.sdk.util;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public final class KetamaLocator {

	private TreeMap<Long, String> ketamaNodes;
	private HashAlgorithm hashAlg;
	private int numReps = 160;

	public KetamaLocator(List<String> nodes, HashAlgorithm alg, int nodeCopies) {
		hashAlg = alg;
		ketamaNodes = new TreeMap<Long, String>();
		numReps = nodeCopies;
		for (String node : nodes) {
			for (int i = 0; i < numReps / 4; i++) {
				byte[] digest = hashAlg.computeMd5(node + i);
				for (int h = 0; h < 4; h++) {
					long m = hashAlg.hash(digest, h);
					ketamaNodes.put(m, node);
				}
			}
		}
	}

	String getNodeForKey(long hash) {
		final String rv;
		Long key = hash;
		if (!ketamaNodes.containsKey(key)) {
			SortedMap<Long, String> tailMap = ketamaNodes.tailMap(key);
			if (tailMap.isEmpty()) {
				key = ketamaNodes.firstKey();
			} else {
				key = tailMap.firstKey();
			}
		}
		rv = ketamaNodes.get(key);
		return rv;
	}

	public String getPrimary(final String k) {
		byte[] digest = hashAlg.computeMd5(k);
		String rv = getNodeForKey(hashAlg.hash(digest, 0));
		return rv;
	}

}
