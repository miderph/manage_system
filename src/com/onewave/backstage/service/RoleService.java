package com.onewave.backstage.service;

import java.util.List;

import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.Module;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Role;
import com.onewave.backstage.model.Site;
import com.onewave.backstage.model.UserGroup;


/**
 * The site and database interaction of various interfaces.
 * Dao connect to the database by the corresponding
 * @author liuhaidi
 * @category Service
 * 
 */
public interface RoleService {
	
	public List<Role> findAll();
	
	public boolean save(Role role);
	
	public boolean update(Role role);
	
	public boolean deleteById(String id);
	
	public int countAll();
	
	public Role findById(String id);
	
	public List<Role> findAll(int firstResult, int maxResults);
	
	public boolean isExist(Role role);

   public String queryIdsWithAuth(Operator operator, String type);
   public List<Module> queryModuleListWithAuth(Operator operator, boolean isAdmin);
   public List<Site> querySiteListWithAuth(Operator operator, boolean isAdmin);
   public List<Column> queryColumnListWithAuth(Operator operator, boolean isAdmin);
   public List<ContProvider> queryProviderListWithAuth(Operator operator, boolean isAdmin);
   public List<UserGroup> queryUserGroupListWithAuth(Operator operator, String type, boolean isAdmin);

}
