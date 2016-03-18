package com.onewave.backstage.dao.impl;

import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.RelaMenuAndContDao;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("relaMenuAndContDao")
public class RelaMenuAndContDaoImpl extends
		BaseDaoImpl<RelaMenuAndCont, String> implements RelaMenuAndContDao {

	public List<RelaMenuAndCont> findAllForMAR(String menuId) {
		String where = " where mc.c_id=c.id ";

		if (menuId != null && !"".equals(menuId)) {
			where += " and mc.menu_id='" + menuId + "' ";
		}

		//where += " order by mc.id desc";
		where += " order by mc.order_num asc";

		String sql = "select * from zl_rela_menu_cont mc, zl_cont c " + where;

		return findAllBySql(sql);
	}

	public List<RelaMenuAndCont> findAllForMAR(int firstResult, int maxResults, String menuId, int status, int status_type) {
		String where = " where mc.c_id=c.id";// and c.status >= 10 and sysdate between c.active_time and c.deactive_time ";
		switch (status_type) {
		case -1:
			where += " and (status<10 or sysdate < c.active_time or sysdate > c.deactive_time)";
			break;
		case 0:
			if(status != InitManager.Defaut_Unselected_ID) {
				where += " and status='" + status + "' ";
			}
			break;
		case 1:
			where += " and status>=10 and sysdate between c.active_time and c.deactive_time";
			break;
		}
		if (menuId != null && !"".equals(menuId)) {
			where += " and mc.menu_id='" + menuId + "' ";
		}
		
		String sql = "";
		
		where += " order by mc.order_num asc ";
		
		sql = "select t.* from (select r.*, rownum linenum from (select mc.* from zl_rela_menu_cont mc, zl_cont c "
				+ where
				+ " ) r where rownum <= "
				+ maxResults
				+ ") t WHERE linenum > " + firstResult;
		
		return findAllBySql(sql);
	}

	public int countAllForMAR(String menuId, int status, int status_type) {
		String where = " where mc.c_id=c.id ";
		switch (status_type) {
		case -1:
			where += " and (c.status<10 or sysdate < c.active_time or sysdate > c.deactive_time)";
			break;
		case 0:
			if(status != InitManager.Defaut_Unselected_ID) {
				where += " and status='" + status + "' ";
			}
			break;
		case 1:
			where += " and c.status>=10 and sysdate between c.active_time and c.deactive_time";
			break;
		}
		if (menuId != null && !"".equals(menuId)) {
			where += " and mc.menu_id='" + menuId + "' ";
		}

		String sql = "select COUNT(*) from zl_rela_menu_cont mc, zl_cont c "
				+ where;
		return countBySql(sql);
	}

	public int getMaxOrderNum(String menuId, int status, int status_type) {

		int maxOrder = 0;
		if (menuId != null && !"".equals(menuId)) {
			String sql = "select nvl(max(t.order_num),9999) from zl_rela_menu_cont t where t.menu_id = '"
					+ menuId + "' ";

			switch (status_type) {
			case -1:
				sql += " and c_id in (select id from zl_cont c where c.status<10 or sysdate < c.active_time or sysdate > c.deactive_time) ";
				break;
			case 0:
				if(status != InitManager.Defaut_Unselected_ID) {
					sql += " and c_id in (select id from zl_cont c where c.status='" + status + "') ";
				}
				break;
			case 1:
				sql += " and c_id in (select id from zl_cont c where c.status>=10 and sysdate between c.active_time and c.deactive_time) ";
				break;
			}

			maxOrder = this.getJdbcTemplate().queryForInt(sql);
		}

		return maxOrder;
	}
   public void deleteDuplicateOrderNum(String menuId) {

      if (menuId != null && !"".equals(menuId)) try{
    	  while (true){
    		  String sql = 
    				  "update zl_rela_menu_cont rela set order_num=order_num+1 where menu_id='@menu_id@' and order_num in ("
    						  +" select t2.order_num from("
    						  +"    select t1.order_num,count(1) tcount from zl_rela_menu_cont t1 where t1.menu_id='@menu_id@' group by t1.order_num"
    						  +" ) t2 where t2.tcount>1"
    						  +" ) and exists (select 1 from zl_rela_menu_cont tt where menu_id='@menu_id@' and tt.order_num=rela.order_num and tt.rowid>rela.rowid)";
              sql = sql.replace("@menu_id@", menuId);
    		  int updateCount = this.getJdbcTemplate().update(sql);
    		  if (updateCount <= 0){
    			  break;
    		  }
    	  }
      }
      catch(Exception E){
    	  
      }
   }
	public int getMinOrderNum(String menuId, int status, int status_type) {

		int maxOrder = 0;
		if (menuId != null && !"".equals(menuId)) {
			String sql = "select nvl(min(t.order_num),0) from zl_rela_menu_cont t where t.menu_id = '"
					+ menuId + "' ";

			switch (status_type) {
			case -1:
				sql += " and c_id in (select id from zl_cont c where c.status<10 or sysdate < c.active_time or sysdate > c.deactive_time) ";
				break;
			case 0:
				if(status != InitManager.Defaut_Unselected_ID) {
					sql += " and c_id in (select id from zl_cont c where c.status='" + status + "') ";
				}
				break;
			case 1:
				sql += " and c_id in (select id from zl_cont c where c.status>=10 and sysdate between c.active_time and c.deactive_time) ";
				break;
			}

			maxOrder = this.getJdbcTemplate().queryForInt(sql);
		}

		return maxOrder;
	}
	public int getPrevOrderNum(String menuId, String orderNum, int status, int status_type){
		int maxOrder = 0;
		if (menuId != null && !"".equals(menuId)) {
			String sql = "select nvl(max(t.order_num),"+orderNum+") from zl_rela_menu_cont t where t.menu_id = '"	+ menuId + "' and t.order_num<"+orderNum;
	 		
			switch (status_type) {
			case -1:
				sql += " and c_id in (select id from zl_cont c where c.status<10 or sysdate < c.active_time or sysdate > c.deactive_time) ";
				break;
			case 0:
				if(status != InitManager.Defaut_Unselected_ID) {
					sql += " and c_id in (select id from zl_cont c where c.status='" + status + "') ";
				}
				break;
			case 1:
				sql += " and c_id in (select id from zl_cont c where c.status>=10 and sysdate between c.active_time and c.deactive_time) ";
				break;
			}

			maxOrder = this.getJdbcTemplate().queryForInt(sql);
		}

		return maxOrder;
	}
	public int getNextOrderNum(String menuId, String orderNum, int status, int status_type){
		int maxOrder = 0;
		if (menuId != null && !"".equals(menuId)) {
			String sql = "select nvl(min(t.order_num),"+orderNum+") from zl_rela_menu_cont t where t.menu_id = '" + menuId + "' and t.order_num>"+orderNum;

			switch (status_type) {
			case -1:
				sql += " and c_id in (select id from zl_cont c where c.status<10 or sysdate < c.active_time or sysdate > c.deactive_time) ";
				break;
			case 0:
				if(status != InitManager.Defaut_Unselected_ID) {
					sql += " and c_id in (select id from zl_cont c where c.status='" + status + "') ";
				}
				break;
			case 1:
				sql += " and c_id in (select id from zl_cont c where c.status>=10 and sysdate between c.active_time and c.deactive_time) ";
				break;
			}

			maxOrder = this.getJdbcTemplate().queryForInt(sql);
		}

		return maxOrder;
	}
	public boolean deleteRelaMenuAndCont(String menuId, String ids) {

		if (menuId != null && !"".equals(menuId.trim())) {

			String sql = "delete from zl_rela_menu_cont mc where mc.menu_id='"+ menuId+"'"
			      + (StringUtils.isBlank(ids)?"":" and mc.c_id in (" + ids + ")");
			int result = this.getJdbcTemplate().update(sql);
			logger.info("---------------result:" + result);

			logger.info("delete successful");

			return result > 0 ? true : false;
		}

		return false;
	}
	public boolean lockRelaMenuAndCont(String menuId, String ids, String locked) {

		if (menuId != null && !"".equals(menuId.trim())) {

			String sql = "update zl_rela_menu_cont mc set locked='"+locked+"' where mc.menu_id='"+ menuId+"'"
			      + (StringUtils.isBlank(ids)?"":" and mc.c_id in (" + ids + ")");
			int result = this.getJdbcTemplate().update(sql);
			logger.info("---------------result:" + result);

			logger.info("update locked successful");

			return result > 0 ? true : false;
		}

		return false;
	}
//	public boolean deleteInvalidRelaMenuAndCont(String menuId){
//
//		if (menuId != null && !"".equals(menuId.trim())) {
//			String sql = "delete from zl_rela_menu_cont mc where mc.menu_id='"+menuId+"'"
//					+"   and mc.c_id in ("
//					+"   select c_id from zl_rela_menu_cont rela left outer join zl_cont c on (rela.c_id=c.id and c.status >= 10 and sysdate between c.active_time and c.deactive_time)"
//					+"   where rela.menu_id=mc.menu_id and c.id is null"
//					+")";
//			
//			int result = 1;//this.getJdbcTemplate().update(sql);
//			logger.info("---------------result:" + result);
//
//			logger.info("delete successful");
//
//			return result > 0 ? true : false;
//		}
//
//		return false;
//	}	
	public RelaMenuAndCont find(String menuId, String orderNum) {
		String where = " where t.menu_id='" + menuId + "' and t.order_num='" + orderNum + "' ";
		String sql = "select * from zl_rela_menu_cont t " + where;

		RelaMenuAndCont relaMenuAndCont = null;
		List<RelaMenuAndCont> relaMenuAndContList = findAllBySql(sql);
		if(relaMenuAndContList!=null && relaMenuAndContList.size() > 0) {
			relaMenuAndCont = relaMenuAndContList.get(0);
		}
		
		return relaMenuAndCont;
	}

	public RelaMenuAndCont findById(String id, String menuId) {
		String where = " where t.menu_id='" + menuId + "' and t.c_id='" + id + "' ";
		String sql = "select * from zl_rela_menu_cont t " + where;
		
		RelaMenuAndCont relaMenuAndCont = null;
		List<RelaMenuAndCont> relaMenuAndContList = findAllBySql(sql);
		if(relaMenuAndContList!=null && relaMenuAndContList.size() > 0) {
			relaMenuAndCont = relaMenuAndContList.get(0);
		}
		
		return relaMenuAndCont;
	}

	@Override
	public List<RelaMenuAndCont> findby_id(String id) {
		String where = "where t.c_id='" + id + "' ";
		String sql = "select * from zl_rela_menu_cont t " + where;
		List<RelaMenuAndCont> relaMenuAndContList = findAllBySql(sql);
		logger.info("------------sql:" + sql);
		return relaMenuAndContList;
	}

}
