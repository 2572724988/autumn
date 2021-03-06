package com.autumn.mybatis.provider.sqlite.builder;

import com.autumn.mybatis.provider.DbProvider;
import com.autumn.mybatis.provider.builder.AbstractXmlBuilder;

/**
 * Sqlite xml生成器
 * 
 * @author 老码农 2019-06-09 22:01:40
 */
public class SqliteXmlBuilder extends AbstractXmlBuilder {

	/**
	 * 
	 * @param dbProvider
	 */
	public SqliteXmlBuilder(DbProvider dbProvider) {
		super(dbProvider);
	}

	@Override
	protected String createCriteriaWhenByLike(String criteriaName) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("<when test=\"%1$s.op == 'LIKE'\">", criteriaName));
		String content = "LIKE '%'|| #{" + criteriaName + ".value} || '%'";
		sql.append(createCriteriaContentItem(criteriaName, content));
		sql.append("</when>");
		return sql.toString();
	}

	@Override
	protected String createCriteriaWhenByLeftLike(String criteriaName) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("<when test=\"%1$s.op == 'LEFT_LIKE'\">", criteriaName));
		String content = "LIKE #{" + criteriaName + ".value} || '%'";
		sql.append(createCriteriaContentItem(criteriaName, content));
		sql.append("</when>");
		return sql.toString();
	}

	@Override
	protected String createCriteriaWhenByRightLike(String criteriaName) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("<when test=\"%1$s.op == 'RIGHT_LIKE'\">", criteriaName));
		String content = "LIKE '%'|| #{" + criteriaName + ".value}";
		sql.append(createCriteriaContentItem(criteriaName, content));
		sql.append("</when>");
		return sql.toString();
	}

}
