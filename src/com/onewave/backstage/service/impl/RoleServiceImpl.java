package com.onewave.backstage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ColumnDao;
import com.onewave.backstage.dao.ContProviderDao;
import com.onewave.backstage.dao.ModuleDao;
import com.onewave.backstage.dao.RoleDao;
import com.onewave.backstage.dao.SiteDao;
import com.onewave.backstage.dao.UserGroupDao;
import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Role;
import com.onewave.backstage.model.Site;
import com.onewave.backstage.model.UserGroup;
import com.onewave.backstage.service.OperatorService;
import com.onewave.backstage.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	@Qualifier("roleDao")
	private RoleDao roleDao;

	@Autowired
	@Qualifier("operatorService")
	private OperatorService operatorService;

   @Autowired
   @Qualifier("moduleDao")
   private ModuleDao moduleDao;

   @Autowired
   @Qualifier("siteDao")
   private SiteDao siteDao;

   @Autowired
   @Qualifier("contProviderDao")
   private ContProviderDao contProviderDao;

   @Autowired
   @Qualifier("columnDao")
   private ColumnDao columnDao;

   @Autowired
   @Qualifier("userGroupDao")
   private UserGroupDao userGroupDao;

	public List<Role> findAll() {
		return roleDao.findAll();
	}

	@Override
	public boolean save(Role role) {
		role.setCreate_time(new Date());
		role.setUpdate_time(new Date());
		return roleDao.save(role);
	}

	@Override
	public boolean update(Role role) {
		role.setUpdate_time(new Date());
		return roleDao.update(role);
	}

	@Override
	public boolean deleteById(String id) {
		if (!operatorService.roleIsUsed(id)) {
			return roleDao.deleteById(id);
		}

		return false;
	}

	@Override
	public int countAll() {
		return roleDao.countAll();
	}

	@Override
	public Role findById(String id) {
		return roleDao.findById(id);
	}

	@Override
	public List<Role> findAll(int firstResult, int maxResults) {
		return roleDao.findAll(firstResult, maxResults);
	}

	@Override
	public boolean isExist(Role role) {
		String where = " name ='" + role.getName() + "' ";
		if (!StringUtils.isEmpty(role.getId())) {
			where += " and id <> '" + role.getId() + "'";
		}
		int i = roleDao.countAll(where);
		
		return i >= 1 ? true : false;
	}


   @Override
   public String queryIdsWithAuth(Operator operator, String type) {
      String ids = "";
      if (operator != null && !StringUtils.isEmpty(operator.getRole_ids())) {
         List<Role> roles = roleDao.findAll(" id in ("
               + operator.getRole_ids() + ")", null);
         if ("provider".equals(type)){
            for (Role role : roles) {
               ids += role.getProvider_ids() + ",";
            }
         }
         else if ("site".equals(type)){
            for (Role role : roles) {
               ids += role.getSite_ids() + ",";
            }
         }
         else if ("menu".equals(type)){
            for (Role role : roles) {
               ids += role.getMenu_ids() + ",";
            }
         }
         else if ("usergroup".equals(type)){
            for (Role role : roles) {
               ids += role.getGroup_ids() + ",";
            }
         }
         else if ("module".equals(type)){
            for (Role role : roles) {
               ids += role.getModule_ids() + ",";
            }
         }
      }
      if (!StringUtils.isEmpty(ids)) {
         ids = ids.substring(0, ids.length() - 1);
      }
      return ids;
   }

   @Override
   public List<Module> queryModuleListWithAuth(Operator operator, boolean isAdmin) {
      List<Module> list = null;

      if (isAdmin){
         list = moduleDao.findAll(null, null);
      }
      else if (operator != null){
         String ids = queryIdsWithAuth(operator,"module");
         if (!StringUtils.isBlank(ids)){
            String where = "id in ("+ids+")";
            list = moduleDao.findAll(where, null);
         }
      }

      if (list == null)
         list = new ArrayList<Module>();
      return list;
   }

   @Override
   public List<Site> querySiteListWithAuth(Operator operator, boolean isAdmin) {
      List<Site> list = null;

      if (isAdmin){
         list = siteDao.findAll(null, null);
      }
      else if (operator != null){
         String ids = queryIdsWithAuth(operator,"site");
         if (!StringUtils.isBlank(ids)){
            String where = "id in ("+ids+")";
            list = siteDao.findAll(where, null);
         }
      }

      if (list == null)
         list = new ArrayList<Site>();
      return list;
   }

   @Override
   public List<ContProvider> queryProviderListWithAuth(Operator operator, boolean isAdmin) {
      List<ContProvider> list = null;

      if (isAdmin){
         list = contProviderDao.findAll(null, null);
      }
      else if (operator != null){
         String ids = queryIdsWithAuth(operator,"provider");
         if (!StringUtils.isBlank(ids)){
            String where = "id in ("+ids+")";
            list = contProviderDao.findAll(where, null);
         }
      }

      if (list == null)
         list = new ArrayList<ContProvider>();
      return list;
   }

   @Override
   public List<UserGroup> queryUserGroupListWithAuth(Operator operator, String type, boolean isAdmin) {
      List<UserGroup> list = null;

      if (isAdmin){
         if (StringUtils.isBlank(type))
            list = userGroupDao.findAll(null, null);
         else
            list = userGroupDao.findAll("type='"+type+"'", null);
      }
      else if (operator != null){
         String ids = queryIdsWithAuth(operator,"usergroup");
         if (!StringUtils.isBlank(ids)){
            String where = "type='"+type+"' and id in ("+ids+")";
            list = userGroupDao.findAll(where, null);
         }
      }
      if (list == null)
         list = new ArrayList<UserGroup>();
      return list;
   }

   @Override
   public List<Column> queryColumnListWithAuth(Operator operator, boolean isAdmin) {
      List<Column> list = null;
      if (isAdmin){
         list = columnDao.findAll(null, null);
      }
      else if (operator != null){
         String siteIds = queryIdsWithAuth(operator,"site");
         if ((","+siteIds+",").indexOf(",0,")>=0){
            list = columnDao.findAll(null, null);
         }
         else{
            String ids = queryIdsWithAuth(operator,"menu");
            if (!StringUtils.isBlank(ids)){
               String where = "id in ("+ids+")";
               list = columnDao.findAll(where, null);
            }
         }
      }
      if (list == null)
         list = new ArrayList<Column>();
      return list;
   }
}
