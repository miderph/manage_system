package com.onewave.backstage.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ImgDao;
import com.onewave.backstage.model.Img;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("imgDao")
public class ImgDaoImpl extends BaseDaoImpl<Img, String> implements ImgDao {
	
	public List<Img> findAll(String targetId, String useType) {
		
		String where = " target_id='" + targetId + "' ";
		
		if(useType!=null && !"".equals(useType.trim())) {
			where += " and use_type='" + useType + "' ";
		}
		
		return super.findAll(where, null);
	}
	
	public String saveAndReturnId(final Img img) {
		
      final String sqlUpdate = "update zl_img set url=?, url_little=?, intro=?, platgroup_id=?, provider_id=?," +
            " create_time=?, modify_time=?, active_time=?, deactive_time=?, is_url_used=?  where target_id=? and use_type="+img.getUse_type();
		final String sqlInsert = "insert into zl_img(use_type, target_id, url, url_little, intro, platgroup_id, provider_id," +
                " create_time, modify_time, active_time, deactive_time,is_url_used,url_icon,url_4_squares,url_6_squares,locked) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      String returnId = "";
      try {
         int iRow = 0;
         if (!"2".equals(img.getUse_type()))
         try {
            logger.info("update sqlUpdate: " + sqlUpdate);
            iRow = this.getJdbcTemplate().update(new PreparedStatementCreator() {

               public PreparedStatement createPreparedStatement(Connection conn)
                     throws SQLException {
                  
                  int i = 0;
                  
                  PreparedStatement ps = conn.prepareStatement(sqlUpdate);

                  ps.setString(++i, img.getUrl());
                  ps.setString(++i, img.getUrl_little());
                  ps.setString(++i, img.getIntro());
                  ps.setString(++i, img.getPlatgroup_id());
                  ps.setString(++i, img.getProvider_id());
                  ps.setDate(++i, new Date(img.getCreate_time().getTime()));
                  ps.setDate(++i, new Date(img.getModify_time().getTime()));
                  ps.setDate(++i, new Date(img.getActive_time().getTime()));
                  ps.setDate(++i, new Date(img.getDeactive_time().getTime()));
                  ps.setString(++i, img.getIs_url_used());
                  //ps.setString(++i, img.getLocked());
                  //ps.setString(++i, img.getUse_type());
                  ps.setString(++i, img.getTarget_id());
                  
                  
                  return ps;
               }
            });
         } catch (InvalidDataAccessApiUsageException e) {
            e.printStackTrace();
         }
         if (iRow == 0)
         {
            logger.info("sqlInsert sql: " + sqlInsert);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            this.getJdbcTemplate().update(new PreparedStatementCreator() {
               
               public PreparedStatement createPreparedStatement(Connection conn)
                     throws SQLException {
                  
                  int i = 0;
                  
                  PreparedStatement ps = conn.prepareStatement(sqlInsert, new String[] {"id"});
                  ps.setString(++i, img.getUse_type());
                  ps.setString(++i, img.getTarget_id());
                  ps.setString(++i, img.getUrl());
                  ps.setString(++i, img.getUrl_little());
                  ps.setString(++i, img.getIntro());
                  ps.setString(++i, img.getPlatgroup_id());
                  ps.setString(++i, img.getProvider_id());
                  ps.setDate(++i, new Date(img.getCreate_time().getTime()));
                  ps.setDate(++i, new Date(img.getModify_time().getTime()));
                  ps.setDate(++i, new Date(img.getActive_time().getTime()));
                  ps.setDate(++i, new Date(img.getDeactive_time().getTime()));
                  ps.setString(++i, img.getIs_url_used());
                  ps.setString(++i, img.getUrl_icon());
                  ps.setString(++i, img.getUrl_4_squares());
                  ps.setString(++i, img.getUrl_6_squares());
                  ps.setString(++i, img.getLocked());

                  return ps;
               }
               
            }, keyHolder);
            
            returnId = "" + keyHolder.getKey().longValue();
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

		logger.info("img return id: " + returnId);
		
		return returnId;
	}

	@Override
	public Img findByTargetId(String targetId) {
		String sql = "select * from ZL_IMG c where target_id ='"+ targetId +" and  use_type in ('0','1')";
		if(StringUtils.isNotBlank(targetId)){
			List<Img> imgList =findAllBySql(sql);
			if(imgList!=null&&!imgList.isEmpty()){
				return imgList.get(0);
			}	
		}		
		return null;
		
	}

	@Override
	public List<Img> findByIds(String ids) {
		List<Img> imgList = new ArrayList<Img>();
		if(StringUtils.isNotBlank(ids)) {
			String sql = "select * from ZL_IMG c where target_id in (" + ids +") and use_type in ('0','1')";
			logger.info("------------sql:" + sql);
			imgList = findAllBySql(sql);
		}
		
		return imgList;
	}

	@Override
	public boolean deleteAll(String targetId, String useType) {
		String sql = "delete from zl_img i where i.target_id= "+targetId;
		if (StringUtils.isNotBlank(useType))
		   sql += " and use_type = '" + useType + "'" ;
		return updateBySql(sql);
	}

	@Override
	public boolean updatelocked(String targetId, String useType,String locked) {
		String sql = "update zl_img i  set locked = "+locked+ " where i.target_id= "+targetId +" and use_type = '" + useType + "'" ;
		return updateBySql(sql);
	}
	
}
