package com.onewave.backstage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zhilink.tools.InitManager;
import net.zhilink.tools.XMLSender;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.RoleDao;
import com.onewave.backstage.dao.SiteDao;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Role;
import com.onewave.backstage.model.Site;
import com.onewave.backstage.service.SiteService;

@Service("siteService")
public class SiteServiceImpl implements SiteService {
	
	@Autowired
	@Qualifier("siteDao")
	private SiteDao siteDao;
	
	@Autowired
	@Qualifier("roleDao")
	private RoleDao roleDao;
	
	@Override
	public boolean save(Site site) {
		site.setCreate_time(new Date());
		site.setModify_time(new Date());
		return siteDao.save(site);
	}
	
	@Override
	public boolean update(Site site) {
		site.setModify_time(new Date());
		return siteDao.update(site);
	}
	
	@Override
	public boolean delete(String id) {
		return siteDao.deleteById(id);
	}
	
	@Override
	public int countAll() {
		
		return siteDao.countAll();
	}
	
	public Site findById(String id) {
		return siteDao.findById(id);
	}
	
	public List<Site> findAll() {
		return siteDao.findAll();
	}
	
	@Override
	public List<Site> findAll(int firstResult, int maxResults) {
		return siteDao.findAll(firstResult, maxResults);
	}

	public String findNames(String ids) {
		String names ="";
		if(!StringUtils.isEmpty(ids)){
			List<Site> sites = siteDao.findAll(" id in ("+ids+")", " order by name asc");
			if(sites!=null && sites.size() > 0){
				for(Site site : sites){
					names += site.getName()+",";
				}
				names = names.substring(0,names.length()-1);
			}
		}
		return names;
	}
	
	public List<Site> findWithAuth(Operator operator) {	
		return siteDao.findWithAuth(operator);
	}

	public List<Site> queryWithAuth(Operator operator) {
		List<Site> siteList = new ArrayList<Site>();
		String siteIds =queryIdsWithAuth(operator);
			if(!StringUtils.isEmpty(siteIds)){
				siteList = siteDao.findAll(" id in ("+siteIds+")", " order by name asc");
			}
		return siteList;
	}

	public String queryIdsWithAuth(Operator operator) {
		String siteIds = "";
		if(operator!=null && !StringUtils.isEmpty(operator.getRole_ids())){
			List<Role> roles = roleDao.findAll(" id in ("+operator.getRole_ids()+")",null);
			for(Role role : roles){
				siteIds += role.getSite_ids()+",";
			}
			if(!StringUtils.isEmpty(siteIds)){
				siteIds = siteIds.substring(0,siteIds.length()-1);
			}
		}
		return siteIds;
	}

	@Override
	/*
	 * 生成索引接口  http://192.168.1.142:8081/qdghSearch/data!index.action
	       消息 {msg : "成功"; succ: true}
	 */
	public String updateIndex() {
		//TODO init.xml中配置updateIndexUrl
		String url = InitManager.getUpdateIndexUrl();
		try {
			String str = XMLSender.get(url,1800000);
			return str;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
}
