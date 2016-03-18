package com.onewave.backstage.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.zhilink.tools.InitManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.onewave.backstage.dao.ContDao;
import com.onewave.backstage.model.Cont;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contDao")
public class ContDaoImpl extends BaseDaoImpl<Cont, String> implements ContDao {
	
	public List<Cont> findAllInIds(String ids) {
		
		List<Cont> contList = new ArrayList<Cont>();
		if(ids!=null && ids.trim().length()>0) {
			String sql = "select * from zl_cont c where c.id in (" + ids +")";
			logger.info("------------sql:" + sql);
			contList = findAllBySql(sql);
		}
		
		return contList;
	}

	public int countAllForApp(String providerIds, String type, String status, String name) {
		String where = " 1=1 ";
		if (InitManager.isValidValue(providerIds.trim())) {
	         where += " and cv.provider_id in (" + providerIds.trim() + ") ";
	     }
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and r.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and r.status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
			String contId = null;
			try {
				contId = ""+Long.parseLong(name);
			} catch (Exception e1) {
				//igonre e1.printStackTrace();
			}

			where += "and (r.name like '%" + name + "%' or ca.package_name like '%" + name + "%'"
		               +(!StringUtils.isBlank(contId)?" or r.id='" + contId + "' ":"")
		               +")";
		}

		String sql = "select COUNT(*) from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where "
				+ where + " and r.id=cv.c_id and r.id=ca.c_id";

		return countBySql(sql);
	}

	public List<Cont> findAllForApp(int firstResult, int maxResults,
			String type, String status, String name,String providerIds) {

		String where = " 1=1 ";
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and r.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and r.status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
	         String contId = null;
	         try {
	            contId = ""+Long.parseLong(name);
	         } catch (Exception e1) {
	            //igonre e1.printStackTrace();
	         }

			where += "and (r.name like '%" + name + "%' or ca.package_name like '%" + name + "%'"
		               +(!StringUtils.isBlank(contId)?" or r.id='" + contId + "' ":"")
		               +")";
		}
		if(!StringUtils.isEmpty(providerIds) && !(""+InitManager.Defaut_Unselected_ID).equals(providerIds.trim())){
			where += "and r.provider_id in (" + providerIds + ") ";
		}
		String sql = "";

		if (firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = "select * from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where "
					+ where
					+ " and r.id=cv.c_id and r.id=ca.c_id order by r.modify_time desc";
		} else {
			sql = "select t.* from (select k.*,rownum linenum from ( select r.* from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where "
					+ where
					+ " and r.id=cv.c_id and r.id=ca.c_id order by r.modify_time desc,r.create_time desc "
					+" ) k where rownum <= "+ maxResults 
				+") t WHERE linenum > " + firstResult;
		}

		return findAllBySql(sql);
	}
	public int countAllForSales(String providerIds, String type, String status, String name, String price_from, String price_to) {
		String where = " 1=1 ";
		if (InitManager.isValidValue(providerIds.trim())) {
	         where += " and cv.provider_id in (" + providerIds.trim() + ") ";
	     }
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and cont.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and cont.status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
			where += "and cont.name like '%" + name + "%' ";
			
			String contId = null;
			try {
				contId = ""+Long.parseLong(name);
			} catch (Exception e1) {
				//igonre e1.printStackTrace();
			}

			where += "and (cont.name like '%" + name + "%' or ca.sales_no like '%" + name + "%'"
					+(!StringUtils.isBlank(contId)?" or cont.id='" + contId + "' ":"")
					+")";
		}
        if (StringUtils.isNotBlank(price_from)){
		   where += "and ca.SALE_PRICE >= '" + price_from + "' ";
		}
        if (StringUtils.isNotBlank(price_to)){
 		   where += "and ca.SALE_PRICE <= '" + price_to + "' ";
        }
		String sql = "select COUNT(*) from zl_cont cont, zl_cont_video cv, zl_cont_sales ca where "
				+ where + " and cont.id=cv.c_id and cont.id=ca.c_id(+)";

		return countBySql(sql);
	}

	public List<Cont> findAllForSales(int firstResult, int maxResults,
			String type, String status, String name,String providerIds, String price_from, String price_to) {

		String where = " 1=1 ";
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and cont.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and cont.status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
			String contId = null;
			try {
				contId = ""+Long.parseLong(name);
			} catch (Exception e1) {
				//igonre e1.printStackTrace();
			}

			where += "and (cont.name like '%" + name + "%' or ca.sales_no like '%" + name + "%'  or ca.key_words like '%" + name + "%'"
					+(!StringUtils.isBlank(contId)?" or cont.id='" + contId + "' ":"")
					+")";
		}
		if(!StringUtils.isEmpty(providerIds) && !(""+InitManager.Defaut_Unselected_ID).equals(providerIds.trim())){
			where += "and cont.provider_id in (" + providerIds + ") ";
		}
        if (StringUtils.isNotBlank(price_from)){
		   where += "and ca.SALE_PRICE >= '" + price_from + "' ";
		}
        if (StringUtils.isNotBlank(price_to)){
 		   where += "and ca.SALE_PRICE <= '" + price_to + "' ";
        }

		String sql = "";

		if (firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = "select * from zl_cont cont, zl_cont_video cv, zl_cont_sales ca where "
					+ where
					+ " and cont.id=cv.c_id and cont.id=ca.c_id(+) order by cont.modify_time desc";
		} else {
			sql = "select t.* from (select k.*,rownum linenum from ( select cont.* from zl_cont cont, zl_cont_video cv, zl_cont_sales ca where "
					+ where
					+ " and cont.id=cv.c_id and cont.id=ca.c_id(+) order by cont.modify_time desc,cont.create_time desc "
					+" ) k where rownum <= "+ maxResults 
				+") t WHERE linenum > " + firstResult;
		}

		return findAllBySql(sql);
	}
	/**
	 * 排除app
	 */
	public int countAll(String providerIds, String type, String status, String name) {
		String where = " 1=1 ";
      if (providerIds != null && !"".equals(providerIds) && !(""+InitManager.Defaut_Unselected_ID).equals(providerIds.trim())) {
         where += " and cv.provider_id in (" + providerIds + ") ";
      }
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and r.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and r.status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
	      String contId = null;
	      try {
	         contId = ""+Long.parseLong(name);
	      } catch (Exception e1) {
	         //igonre e1.printStackTrace();
	      }
			where += "and (r.name like '%" + name + "%' "
	               +(!StringUtils.isBlank(contId)?" or r.id='" + contId + "' ":"")
	               +")";
		}
      //不查询app type=7和sales type=8
      where += " and r.type != 7 and r.type != 8 and r.type != 17";
		
		String sql = "select COUNT(*) from zl_cont r, zl_cont_video cv where "
				+ where + " and r.id=cv.c_id";

		return countBySql(sql);
	}

	public List<Cont> findAllByPage(int pageNum, int pageSize, String type,
			String status, String name) {

		String where = " 1=1 ";
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and r.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
			where += "and name like '%" + name + "%' ";
		}

		return findAllByPage(pageNum, pageSize, where);
	}

	/**
	 * 排除app
	 */
	public List<Cont> findAll(int firstResult, int maxResults, String providerIds, String type,
			String status, String name) {

		String where = " 1=1 ";
      if (providerIds != null && !"".equals(providerIds) && !(""+InitManager.Defaut_Unselected_ID).equals(providerIds.trim())) {
         where += " and r.provider_id in (" + providerIds + ") ";
      }
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and r.type='" + type + "' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and r.status=" + status + " ";
		}

		if (name != null && !"".equals(name.trim())) {
         String contId = null;
         try {
            contId = ""+Long.parseLong(name);
         } catch (Exception e1) {
            //igonre e1.printStackTrace();
         }
         where += "and (r.name like '%" + name + "%' "
                  +(!StringUtils.isBlank(contId)?" or r.id='" + contId + "' ":"")
                  +")";
		}
		//不查询app type=7和sales type=8
		where += " and r.type != 7 and r.type != 8 and r.type != 17";
		
		String sql = "";

		if (firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = "select * from zl_cont r, zl_cont_video cv where " + where
					+ " and r.id=cv.c_id order by r.modify_time desc";
		} else {
			sql = "select t.* from ( select k.* ,rownum linenum from ( select r.* from zl_cont r, zl_cont_video cv where "
					+ where
					+ " and r.id=cv.c_id order by nvl(r.modify_time,r.create_time) desc, r.create_time desc "+ ") k where rownum <= "+maxResults
					+" ) t WHERE linenum > " + firstResult;
		}

		return findAllBySql(sql);
	}

	public List<Cont> findAllForMAR(int firstResult, int maxResults,
			String type, String provider, String keyword, String status, String startTime,
			String endTime, String menuId) {

		String sql = "";// /"select COUNT(*) from zl_cont c, zl_cont_video cv ";
		String where = " where c.type!=17 and rela.menu_id is null";// and c.status>=10 ";
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and c.type='" + type + "' ";
		}

		if (provider != null && !"".equals(provider) && !(""+InitManager.Defaut_Unselected_ID).equals(provider.trim())) {
			where += " and c.provider_id in (" + provider + ") ";
		}

		if (keyword != null && !"".equals(keyword)) {
			where += " and c.name like '%" + keyword + "%' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += "and c.status=" + status + " ";
		}
		else
			where += " and c.status>=10";
		if (startTime != null && startTime.length() > 0) {
			startTime = startTime.substring(0, 10);
			where += " and c.create_time >= to_date('" + startTime
					+ "','yyyy-mm-dd') ";
		} else {
			where += " and c.create_time >= sysdate-1 ";
		}

		if (endTime != null && endTime.length() > 0) {
			endTime = endTime.substring(0, 10);
			where += " and c.create_time <= to_date('" + endTime
					+ "','yyyy-mm-dd')+1 ";
		}

		where += " order by c.id desc ";

		if(firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = "select c.* from zl_cont c " + where;
		} else if ("8".equals(type)){//购物资产
			where += " ca."
			
		}else {
			sql = "select t.* from (select r.*, rownum linenum from (select c.* from zl_cont c left outer join zl_rela_menu_cont rela on c.id=rela.c_id and rela.menu_id="+menuId
					+ where
					+ ") r where rownum <= "
					+ maxResults
					+ ") t WHERE linenum > " + firstResult;
		}

		return findAllBySql(sql);
	}

	public int countAllForMAR(String type, String provider, String keyword,
			String status, String startTime, String endTime, String menuId) {

		String sql = "select COUNT(*) from zl_cont c left outer join zl_rela_menu_cont rela on c.id=rela.c_id and rela.menu_id="+menuId;
		String where = " where c.type!=17 and rela.menu_id is null";
		if (type != null && !"".equals(type) && !(""+InitManager.Defaut_Unselected_ID).equals(type.trim())) {
			where += " and c.type='" + type + "' ";
		}

		if (provider != null && !"".equals(provider) && !(""+InitManager.Defaut_Unselected_ID).equals(provider.trim())) {
			where += " and c.provider_id in (" + provider + ") ";
		}

		if (keyword != null && !"".equals(keyword)) {
			where += " and c.name like '%" + keyword + "%' ";
		}
		if (status != null && !"".equals(status.trim()) && !(""+InitManager.Defaut_Unselected_ID).equals(status.trim())) {
			where += " and c.status=" + status + " ";
		}
		else
			where += " and c.status>=10";
		if (startTime != null && startTime.length() > 0) {
			startTime = startTime.substring(0, 10);
			where += " and c.create_time >= to_date('" + startTime
					+ "','yyyy-mm-dd') ";
		} else {
			where += " and c.create_time >= sysdate-1 ";
		}

		if (endTime != null && endTime.length() > 0) {
			endTime = endTime.substring(0, 10);
			where += " and c.create_time <= to_date('" + endTime
					+ "','yyyy-mm-dd')+1 ";
		}

		return countBySql(sql + where);
	}

	public String saveAndReturnId(final Cont cont) {

		final String sql = "insert into zl_cont(type, status, name, description, provider_id, "
				+ "active_time, deactive_time, ad_type, pinyin) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		logger.info("insert sql: " + sql);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {

				int i = 0;

				PreparedStatement ps = conn.prepareStatement(sql,
						new String[] { "id" });
				ps.setInt(++i, cont.getType());
				ps.setInt(++i, cont.getStatus());
				ps.setString(++i, cont.getName());
				ps.setString(++i, cont.getDescription());
				ps.setString(++i, cont.getProvider_id());
				//ps.setDate(++i, new Date(cont.getCreate_time().getTime()));
				//ps.setDate(++i, new Date(cont.getModify_time().getTime()));
				ps.setDate(++i, new Date(cont.getActive_time().getTime()));
				ps.setDate(++i, new Date(cont.getDeactive_time().getTime()));
				ps.setInt(++i, cont.getAd_type());
				ps.setString(++i, cont.getPinyin());

				return ps;
			}

		}, keyHolder);

		String returnId = "" + keyHolder.getKey().longValue();
		cont.setId(returnId);

		logger.info("cont return id: " + returnId);
		return returnId;
	}

	@Override
	public int countAllByRoleForMAR(String providers) {
		String sql = "select COUNT(*) from zl_cont c, zl_cont_video cv ";
		String where = " where c.id=cv.c_id and type = 13 and ad_type= 80 and c.provider_id in (" + providers + ") order by c.id desc ";
		return countBySql(sql + where);
	}

	@Override
	public List<Cont> findAllByRoleForMAR(int firstResult, int maxResults,
			String providers) {
		String sql = "";
		String where = " where c.id=cv.c_id and type = 13 and ad_type= 80 and c.provider_id in (" + providers + ") order by c.id desc ";

		if(firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
			sql = "select c.* from zl_cont c, zl_cont_video cv " + where;
		} else {
			sql = "select t.* from (select r.*, rownum linenum from (select c.* from zl_cont c, zl_cont_video cv "
					+ where
					+ ") r where rownum <= "
					+ maxResults
					+ ") t WHERE linenum > " + firstResult;
		}

		return findAllBySql(sql);
	}

	@Override
	public List<Cont> findAllSuperscripts() {
		String sql = "";
		String where = " where c.id=cv.c_id and type = 13 and ad_type= 80  order by c.id desc ";
		sql = "select c.* from zl_cont c, zl_cont_video cv " + where;
		return findAllBySql(sql);
	}

	@Override
	public List<Cont> findAllAppByPackageName(String packageName,
			String providerId) {
		String sql = "";
		String where = "";
		if (!StringUtils.isBlank(packageName)) {
			where += " ca.package_name = '" + packageName + "' ";
		}
		if(!StringUtils.isBlank(providerId) && !(""+InitManager.Defaut_Unselected_ID).equals(providerId.trim())){
			where += "and r.provider_id in (" + providerId + ") ";
		}
		sql = "select * from zl_cont r, zl_cont_video cv, zl_cont_appstore ca where "
			+ where
			+ " and  r.id=cv.c_id and r.id=ca.c_id  and r.type = 7 order by id desc";
		return findAllBySql(sql);
	}
}
