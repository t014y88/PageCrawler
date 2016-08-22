package com.pagecrawler.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

public class SqlStorage implements Storage {
	private static final Log LOGGER = LogFactoryImpl.getLog(SqlStorage.class);
	private Connection connection;

	public SqlStorage(Connection connection) {
		this.connection = connection;
	}

	@Override
	public boolean save(String url, Map<String, Integer> wordCounts) {
		try {
			String query = "INSERT INTO wordCounts (url, word, count) VALUES (?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(query);
			for (Entry<String, Integer> wordCount : wordCounts.entrySet()) {
				ps.setString(1, url);
				ps.setString(2, wordCount.getKey());
				ps.setInt(3, wordCount.getValue());
				ps.addBatch();
			}
			ps.executeBatch();
			return true;
		} catch (SQLException e) {
			LOGGER.warn("There was an error writing to SQL", e);
			return false;
		}
	}

}
