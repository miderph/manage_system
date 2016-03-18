package com.onewave.service.impl;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RoleService;
import com.onewave.backstage.service.impl.ContProviderServiceImpl;
import com.onewave.common.base.TestBase;

public class MenuServiceTest extends TestBase {
	
	@Autowired  @Qualifier("menuService")
	private MenuService menuService;
	private ContService contService;
	private ContProviderServiceImpl contProviderService;
	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

	@Test
    public void testCount() {
		Operator operator =  new Operator();
		operator.setRole_ids("1");
		//String contProviderIds = roleService.queryProviderIdsWithAuth(operator);
		String contProviderIds = roleService.queryIdsWithAuth(operator, "provider");
		long beforeCount = contService.countAllByRoleForMAR(contProviderIds);
		System.out.println(beforeCount);
        
    }
	
	
	
	@Test
    public void testSave() {
		long beforeCount = menuService.countAll();
        
		Menu menu = generateRandomModel();
		
		menuService.save(menu);
        
		long afterCount = menuService.countAll();
        
        assertEquals(beforeCount + 1, afterCount);
        
    }
	
	
	@Test
    public void testUpdate() {
        
		Menu menu = generateRandomModel();
		
		menuService.save(menu);
		
        String expectedDescription = "123234";
        
        menu.setTitle(expectedDescription);
        
        menuService.update(menu);
        
        String actualDescription = menuService.findById(menu.getId()).getTitle();
        
        assertEquals(expectedDescription, actualDescription);
        
    }
	
	@Test
    public void testDelete() {

        long beforeCount = menuService.countAll();
        
        Menu menu = generateRandomModel();
        
        menuService.save(menu);
        
        menuService.delete(menu);
        
        long afterCount = menuService.countAll();
        
        assertEquals(beforeCount, afterCount);
        
    }
	
	@Test
    public void testDeleteById() {

        long beforeCount = menuService.countAll();
        
        Menu menu = generateRandomModel();
        
        menuService.save(menu);
        
        menuService.delete(menu.getId());
        
        long afterCount = menuService.countAll();
        
        assertEquals(beforeCount, afterCount);
        
    }
	
	@Test
	public void testFindAll() {
		
		Menu menu = generateRandomModel();
		
		menuService.save(menu);
		menu = menuService.findById(menu.getId());
        
        List<Menu> modelList = menuService.findAll();
        List<String> idList = new ArrayList<String>();
        for(int i=0; i<modelList.size(); i++) {
        	String id = modelList.get(i).getId();
        	idList.add(id);
        }
        
        assertThat(idList, hasItem(menu.getId()));
        
	}
	
	@Test
	public void testFindAllByWhere() {
		
		Menu menu = generateRandomModel();
		menu.setStatus(10);
		menu.setSite_id("16");
		menu.setParent_id("99");
		menuService.save(menu);
		
		menu = menuService.findById(menu.getId());
        System.out.println("-----------------------------------------");
        List<Menu> modelList = menuService.findMenu("16", 99, 10);
        List<String> idList = new ArrayList<String>();
        for(int i=0; i<modelList.size(); i++) {
        	String id = modelList.get(i).getId();
        	idList.add(id);
        }
        
        assertThat(idList, hasItem(menu.getId()));
        
	}
	
	@Test
	public void testfindAllPaging() {
		Menu menu = generateRandomModel();
		
		long beforeCount = menuService.countAll();
		
		menuService.save(menu);
		
		long afterCount = menuService.countAll();
		
		List<Menu> modelList = menuService.findAll(0, 5);
		
		boolean flag = (modelList.size() >= 1) && (afterCount > beforeCount);
		
        assertEquals(true, flag);
	}
	
	@Test
	public void testFindMaxSerialBySite_idAndParent_id() {
		
		long maxSerial = menuService.findMaxSerialBySite_idAndParent_id("123", "0");
		System.out.println(maxSerial);
        
	}
	
	AtomicInteger counter = new AtomicInteger();
	
	public Menu generateRandomModel() {
		
		long key = System.nanoTime() + counter.addAndGet(1);
		
		Menu menu = new Menu();
		menu.setId(key+"");
		menu.setActive_time(new Date());
		menu.setDeactive_time(new Date());
		menu.setCreate_time(new Date());
		menu.setModify_time(new Date());
		
		return menu;
		
	}

}
