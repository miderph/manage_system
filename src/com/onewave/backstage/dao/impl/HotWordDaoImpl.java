package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.HotWordDao;
import com.onewave.backstage.model.HotWord;
import com.onewave.common.dao.impl.BaseDaoImpl;

/**
 * 热词管理
 * 
 * @author liuhaidi
 * 
 */

@Repository("hotWordDao")
public class HotWordDaoImpl extends BaseDaoImpl<HotWord, String> implements HotWordDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HotWord> findAll(String site_id, String hotword, int firstResult, int maxResult) {
		String where = "";
		if (!site_id.equals(""+InitManager.Defaut_Unselected_ID)){
		   where += "and hw.site_id='"+site_id+"' ";
		}

		if(StringUtils.isNotBlank(hotword)) {
			where += "and hw.hotword like '%"+hotword+"%' ";
		}
		where += "and rownum <= " + maxResult + " ";
		
		if (!StringUtils.isBlank(where)){
		   where = " where " + where.substring("and ".length());
		}
		String sql = "select hw.*,rownum linenum from zl_hotwords hw" + where;
		sql = "select * from ("+sql+") where linenum > " + firstResult;
		
		sql += " order by site_id,hotword";
		List<HotWord> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(HotWord.class));
		
		if(list == null) {
			list = new ArrayList<HotWord>();
		}

		return list;
	}
	
	@Override
	public int countAll(String site_id, String hotword) {
		
      String where = "";
      if (!site_id.equals(""+InitManager.Defaut_Unselected_ID)){
         where += "and hw.site_id='"+site_id+"' ";
      }

      if(StringUtils.isNotBlank(hotword)) {
         where += "and hw.hotword like '%"+hotword+"%' ";
      }

		String sql = "select COUNT(*) from zl_hotwords hw" + (StringUtils.isBlank(where)?"":" where "+where.substring("and ".length()));
		
		return countBySql(sql);
	}
	@Override
   public int countAllById(String id) {
		String sql = "select COUNT(*) from zl_hotwords hw where id!='"+id+"' and hw.hid='"+id+"' ";
		return countBySql(sql);
	}
	@Override
   public boolean updateAllRela(HotWord hotWord) {
		String sql = "update zl_hotwords set hotword='"+hotWord.getHotword()+"' where site_id!=0 and hid=" + hotWord.getId();
		getJdbcTemplate().update(sql);
		return true;
	}
	@Override
   public boolean addRelaHotword(String site_id, String ids) {
		String sql = "insert into zl_hotwords(hid,hotword,site_id)"
		          +" select t.id hid,t.hotword,'"+site_id+"' site_id from zl_hotwords t where site_id=0 and id in ("+ids+") and id not in (select hid from zl_hotwords where site_id='"+site_id+"')";
		getJdbcTemplate().update(sql);
		return true;
	}
	@Override
   public boolean deleteRelaHotword(String ids) {
		String sql = "delete from zl_hotwords where site_id!=0 and id in ("+ids+")";
		getJdbcTemplate().update(sql);
		return true;
	}
}
