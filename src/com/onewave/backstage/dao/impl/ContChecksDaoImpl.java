package com.onewave.backstage.dao.impl;

import java.util.List;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ContChecksDao;
import com.onewave.backstage.model.ContCheck;
import com.onewave.common.dao.Pagination;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("contChecksDao")
public class ContChecksDaoImpl extends BaseDaoImpl<ContCheck, String> implements ContChecksDao {

	@Override
	public ContCheck findByItemUrl(String linkUrl) {
		
		List<ContCheck> list = findAll("item_url = '"+linkUrl+"'");
		if(null != list && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public Pagination<ContCheck> findPagination(int pageNum, int pageSize,
			String c_status, String c_name, String price_from, String price_to,
			String start_time, String end_time) {
		String where = " 1=1 ";
		if(!StringUtils.isBlank(c_status) && !(""+InitManager.Defaut_Unselected_ID).equals(c_status)){
			where +=" and status = '"+c_status+"'";
		}
		if(!StringUtils.isBlank(c_name)){
			long c_id =-1;
			try{
				c_id =  Long.parseLong(c_name);
			}catch(Exception e){}
			where +=" and (name like '%"+c_name+"%' or classify like '%"+c_name+"%' or code='"+c_name+"'" +(c_id >0 ?  " or id="+c_name: "" )+ " ) ";
		}
		if(!StringUtils.isBlank(price_from)){
			double price = -1;
			try{ 
				price= Double.parseDouble(price_from); 
			}catch(Exception e){};
			if(price>0){
				where +=" and to_number(REGEXP_REPLACE(price,'[a-zA-Z/:]','')) >="+price +"";
			}
		}
		if(!StringUtils.isBlank(price_to)){
			double price = -1;
			try{ 
				price= Double.parseDouble(price_to); 
			}catch(Exception e){};
			if(price>0){
				where +=" and to_number(REGEXP_REPLACE(price,'[a-zA-Z/:]','')) <="+ price +"";
			}
		}
		if(!StringUtils.isBlank(start_time)){
			where +=" and update_time >= to_date('"+start_time+"','yyyy-mm-dd hh24:mi:ss')";
		}
		if(!StringUtils.isBlank(end_time)){
			where +=" and update_time <= to_date('"+end_time+"','yyyy-mm-dd hh24:mi:ss')";
		}
		
		return findPagination(pageNum, pageSize, where, "update_time desc");
	}
	
	
	
}
