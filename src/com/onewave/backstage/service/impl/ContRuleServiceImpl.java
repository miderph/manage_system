package com.onewave.backstage.service.impl;

import java.util.Date;
import java.util.List;

import net.zhilink.tools.StringTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.ContRuleDao;
import com.onewave.backstage.model.Column;
import com.onewave.backstage.model.ContRule;
import com.onewave.backstage.service.ColumnService;
import com.onewave.backstage.service.ContRuleService;

@Service("contRuleService")
public class ContRuleServiceImpl implements ContRuleService {
	
	@Autowired
	@Qualifier("contRuleDao")
	private ContRuleDao contRuleDao;
	
	@Autowired
	@Qualifier("columnService")
	private ColumnService columnService;
	
	public ContRule findById(String id) {
		return contRuleDao.findById(id);
	}
	
	@Override
	public boolean save(ContRule ContRule) {
		ContRule.setCreate_time(new Date());
		ContRule.setModify_time(new Date());
		String id = contRuleDao.saveAndReturnId(ContRule);
		try {
			if(Integer.parseInt(id)>1){
				ContRule.setId(id);
				return true;
			}else{
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		
	}
	
	@Override
	public boolean saveAndRef(ContRule ContRule,Column column) {
		ContRule.setCreate_time(new Date());
		ContRule.setModify_time(new Date());
		boolean flag = false;
		String id = contRuleDao.saveAndReturnId(ContRule);
		try {
			if(Integer.parseInt(id)>1){
				ContRule.setId(id);
				column.setImport_rule_ids(StringTool.appendValue(column.getImport_rule_ids(), id));
				flag = columnService.update(column);
			}else{
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return flag;
		
	}
	
	@Override
	public boolean update(ContRule ContRule) {
		ContRule.setModify_time(new Date());
		return contRuleDao.update(ContRule);
	}
	
	@Override
	public boolean delete(String id) {
		return contRuleDao.deleteById(id);
	}
	
	@Override
	public boolean deleteAndRef(String id,String menu_id) {
		boolean flag = false;
		if(flag = delete(id)){
			Column column =  columnService.findById(menu_id);
			String ruleids = column.getImport_rule_ids();
			column.setImport_rule_ids(StringTool.removeValue(ruleids, id));
			flag = columnService.update(column);
		}
		return flag;
	}

	@Override
	public List<ContRule> findAllByMenuId(String menu_id) {
		return contRuleDao.findAllByMenuId(menu_id);
	}
	
}
