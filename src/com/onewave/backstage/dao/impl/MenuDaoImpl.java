package com.onewave.backstage.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.MenuDao;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.MenuStructType;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("menuDao")
public class MenuDaoImpl extends BaseDaoImpl<Menu, String> implements MenuDao {
	
	public List<Menu> findMenu(String site_id, long parent_id, int status) {
		String where = " site_id='" + site_id + "' ";
		
		if(status != InitManager.Defaut_Unselected_ID) {
			where += " and status='" + status + "' ";
		}
		
		if(parent_id != -1) {
			where += " and parent_id='" + parent_id + "' ";
		}
		
		return this.findAll(where," order by order_num");
	}
	
	public List<Menu> findMenu(String site_ids, int parent_id, String name) {
		return findMenu(site_ids, parent_id, InitManager.Defaut_Unselected_ID, name, null);
	}
	public List<Menu> findMenu(String site_ids, int parent_id, int status, String name,String ignore_id) {
		String where = "";
		String andTag = " and ";
		if(!"-1".equals(site_ids)) {
			where += andTag + "site_id in (" + site_ids + ") ";
		}
		
		if(parent_id != -1) {
			where += andTag + "parent_id='" + parent_id + "' ";
		}
		if(status != InitManager.Defaut_Unselected_ID) {
			where += andTag + "status='" + status + "' ";
		}
		if(name!=null && !"".equals(name)) {
			where += andTag + "title like '%" + name + "%' ";
		}
		if(!StringUtils.isBlank(ignore_id)){
			String ingnoreSql ="id  not in ( select  m.id  from zl_menu m start with m.id ="+ignore_id+" connect by prior m.id = m.parent_id ) ";
			where += andTag + ingnoreSql;
		}
        if (!"".equals(where)){
        	where = where.substring(andTag.length());
        }
		return this.findAll(where," order by order_num");
	}

	public List<MenuStructType> querySiteStatus() {
		String sql = "select t.id, t.status, t.description from zl_status_dict t where table_name='ZL_MENU' and field_name='STRUCT_TYPE'";
		logger.info("siteStatus sql:"+sql);
		List<MenuStructType> menuStructTypes = super.getJdbcTemplate().query(sql, new SiteStatusMapper());
		
		return menuStructTypes;
	}
	
	class SiteStatusMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			MenuStructType menuStructType = new MenuStructType();
			menuStructType.setId(rs.getString("id"));
			menuStructType.setStatus(rs.getString("status"));
			menuStructType.setDescription(rs.getString("description"));
			
			return menuStructType;
		}
	}

	public List<ContProvider> queryContProviderBySiteId(String siteId) {
		String sql = "select t.id, t.name, t.description, t.site_id from zl_cont_provider t"; // t where t.site_id=" + siteId;
		sql += " order by id";
		logger.info("ContProvider sql:"+sql);
		List<ContProvider> contProviderList = super.getJdbcTemplate().query(sql, new ContProviderMapper());
		
		return contProviderList;
	}
	
	class ContProviderMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ContProvider contProvider = new ContProvider();
			contProvider.setId(rs.getString("id"));
			contProvider.setName(rs.getString("name"));
			contProvider.setDescription(rs.getString("description"));
			contProvider.setSite_id(rs.getString("site_id"));
			
			return contProvider;
		}
	}
	
	public long findMaxSerialBySite_idAndParent_id(String site_id, String parent_id) {
		logger.info("Getting catalogNum is start!");
		
		String sql = "select max(order_num) from zl_menu where site_id='" + site_id + "' and parent_id='" + parent_id + "'";
		
		logger.info(sql);
		
		long catalogNum = this.getJdbcTemplate().queryForLong(sql);
		
		return catalogNum;
	}
	
	public long modifyBetweenSerialByParentId(String site_id, String parentId, long smallSerial, long largeSerial, String sign) {
		logger.info("Getting catalogNum is start!");
		
		String sql = "update zl_menu ";
		
		if(sign.equalsIgnoreCase("add")) {
			sql += "set order_num=order_num+1 ";
		} else if(sign.equalsIgnoreCase("sub")) {
			sql += "set order_num=order_num-1 ";
		} else {
			logger.info("参数sign必须为字符串\"add\"或字符串\"sub\"");
			return 0;
		}
		
		sql += "where site_id='" + site_id + "' and parent_id='" + parentId + "'";
		
		if(smallSerial != -1) {
			sql += " and order_num>'" + smallSerial + "'";
		}
		
		if(largeSerial != -1) {
			sql += " and order_num<'" + largeSerial + "'";
		}
		
		logger.info(sql);
		
		return this.getJdbcTemplate().update(sql);
	}

	@Override
	public List<Menu> findByShortCutId(String id) {
		List<Menu> menuList = new ArrayList<Menu>();
		String sql = "";
		if(id!=null && id.trim().length()>0) {
			sql = "select * from ZL_MENU t  where shortcut_contid ='" + id + "'";
			menuList = findAllBySql(sql);
			logger.info("------------sql:" + sql);
		}
		return menuList;
	}
	
	@Override
	public boolean isChild(String sourceMenuId, String targetMenuId) {
		if(!StringUtils.isBlank(sourceMenuId) && !StringUtils.isBlank(targetMenuId)) {
			String sql = "select count(1) from ( select  id  from zl_menu m start with m.id ="+sourceMenuId+" connect by prior m.id = m.parent_id ) s where s.id = "+ targetMenuId;
			logger.info("------------sql:" + sql);
			int total  = countBySql(sql);
			return total > 0? true :false;
		}
		return false;
	}
	@Override
   public List<Menu> querySubMenuForMar(long id){
	   List<Menu> menuList = new ArrayList<Menu>();
      String sql = "select * from ZL_MENU t where id in (select menu.id from zl_menu menu,zl_rela_menu_cont rela where menu.parent_id="+id+" and menu.id=rela.c_id(+) and rela.c_id is null)";
      menuList = findAllBySql(sql);
		return menuList;
	}	
}
