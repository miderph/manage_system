package com.onewave.backstage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.SoftwareDao;
import com.onewave.backstage.model.SoftwareVersion;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("softwareDao")
public class SoftwareDaoImpl extends BaseDaoImpl<SoftwareVersion, String> implements SoftwareDao {

	private static final Logger logger = Logger
			.getLogger(SoftwareDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcInsert insertActor;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertActor = new SimpleJdbcInsert(dataSource).withTableName(
				"zl_software_version").usingGeneratedKeyColumns("ID");
	}

	public List<SoftwareVersion> query(int firstResult, int maxResults, String version_number,
			String software_info, String plat, String enforce_flag,
			String usergroup_id, String file_type, String update_url,
			String description, String url_type, String status) {
		String sqlWhere = combileWhereClause(version_number, software_info,
				plat, enforce_flag, usergroup_id, file_type, update_url,
				description, url_type, status);
		String sql = "select t.* from (select r.*, rownum row_num from (select * from zl_software_version "
				+ (sqlWhere == null ? "" : " where " + sqlWhere)
				+ "order by PUBLISH_TIME desc, id desc) r) t "
				+ " where row_num>" + firstResult + " and row_num<=" + maxResults;

		logger.info("sql=" + sql);
		List<SoftwareVersion> temp = null;
		try {
			temp = this.findAllBySql(sql);
		} catch (Exception E) {
			E.printStackTrace();
		}

		logger.info("query software end!");

		return temp;
	}

	public long getLength(String version_number, String software_info,
			String plat, String enforce_flag, String usergroup_id,
			String file_type, String update_url, String description,
			String url_type, String status) {
		String sqlWhere = combileWhereClause(version_number, software_info,
				plat, enforce_flag, usergroup_id, file_type, update_url,
				description, url_type, status);

		String sql = "select count(*) from zl_software_version"
				+ (sqlWhere == null ? "" : " where " + sqlWhere);
		long i = this.jdbcTemplate.queryForLong(sql);
		return i;
	}

	public long countByVersionNum(String version_num, String plat) {

		String sql = "select count(*) from zl_software_version where version_num='"
				+ version_num + "' and plat='" + plat + "'";

		logger.info("sql=" + sql);
		long i = this.jdbcTemplate.queryForLong(sql);
		return i;
	}

	private static String combileWhereClause(String version_number,
			String software_info, String plat, String enforce_flag,
			String usergroup_id, String file_type, String update_url,
			String description, String url_type, String status) {
		String where = "";
		where += (version_number == null) ? "" : " and version_num like '%"
				+ version_number + "%'";
		where += (software_info == null) ? "" : " and software_info like '%"
				+ software_info + "%'";
		where += (plat == null) ? "" : " and plat like '%" + plat + "%'";
		where += (enforce_flag == null) ? "" : " and enforce_flag='"
				+ enforce_flag + "'";
		where += (usergroup_id == null) ? "" : " and usergroup_id='"
				+ usergroup_id + "'";
		where += (file_type == null) ? "" : " and file_type='" + file_type
				+ "'";
		where += (update_url == null) ? "" : " and update_url like '%"
				+ update_url + "%'";
		where += (description == null) ? "" : " and description like '%"
				+ description + "%'";
		where += (url_type == null) ? "" : " and url_type='" + url_type + "'";
		where += (status == null) ? "" : " and status='" + status + "'";

		if (where != null && !"".equals(where))
			where = where.substring(" and ".length());
		else
			where = null;

		return where;
	}

	public boolean isExistRecord(SoftwareVersion test) {
		String sql = "select count(*) from zl_software_version where "
		      + " version_num='" + test.getVersion_num()+ "'"
				//+ " and software_info='" + test.getSoftware_info()+"'"
				+ " and plat='" + test.getPlat() + "'"
				+(StringUtils.isBlank(test.getID())?"":" and id !='"+test.getID()+"'");
		int i = this.jdbcTemplate.queryForInt(sql);
		if (i == 1) {
			return true;
		} else {
			return false;
		}

	}

}
