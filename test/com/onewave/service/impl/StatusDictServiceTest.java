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

import com.onewave.backstage.model.StatusDict;
import com.onewave.backstage.service.StatusDictService;
import com.onewave.common.base.TestBase;

public class StatusDictServiceTest extends TestBase {
	
	@Autowired  @Qualifier("statusDictService")
	private StatusDictService statusDictService;
	
	
	@Test
    public void testSave() {
		long beforeCount = statusDictService.countAll();
        
		StatusDict statusDict = generateRandomModel();
		
		statusDictService.save(statusDict);
        
		long afterCount = statusDictService.countAll();
        
        assertEquals(beforeCount + 1, afterCount);
        
    }
	
	
	@Test
    public void testUpdate() {
        
		StatusDict statusDict = generateRandomModel();
		
		statusDictService.save(statusDict);
		
        String expectedDescription = "123234";
        
        statusDict.setDescription(expectedDescription);
        
        statusDictService.update(statusDict);
        
        String actualDescription = statusDictService.findById(statusDict.getId()).getDescription();
        
        assertEquals(expectedDescription, actualDescription);
        
    }
	
	@Test
    public void testDelete() {

        long beforeCount = statusDictService.countAll();
        
        StatusDict statusDict = generateRandomModel();
        
        statusDictService.save(statusDict);
        
        statusDictService.delete(statusDict);
        
        long afterCount = statusDictService.countAll();
        
        assertEquals(beforeCount, afterCount);
        
    }
	
	@Test
    public void testDeleteById() {

        long beforeCount = statusDictService.countAll();
        
        StatusDict statusDict = generateRandomModel();
        
        statusDictService.save(statusDict);
        
        statusDictService.delete(statusDict.getId());
        
        long afterCount = statusDictService.countAll();
        
        assertEquals(beforeCount, afterCount);
        
    }
	
	@Test
	public void testFindAll() {
		
		StatusDict statusDict = generateRandomModel();
		
		statusDictService.save(statusDict);
		statusDict = statusDictService.findById(statusDict.getId());
        
        List<StatusDict> modelList = statusDictService.findAll();
        List<String> idList = new ArrayList<String>();
        for(int i=0; i<modelList.size(); i++) {
        	String id = modelList.get(i).getId();
        	idList.add(id);
        }
        
        assertThat(idList, hasItem(statusDict.getId()));
        
	}
	
	@Test
	public void testfindAllPaging() {
		StatusDict statusDict = generateRandomModel();
		
		long beforeCount = statusDictService.countAll();
		
		statusDictService.save(statusDict);
		
		long afterCount = statusDictService.countAll();
		
		List<StatusDict> modelList = statusDictService.findAll(0, 5);
		
		boolean flag = (modelList.size() >= 1) && (afterCount > beforeCount);
		
        assertEquals(true, flag);
	}
	
	@Test
	public void testQueryStatusDict() {
		StatusDict statusDict = generateRandomModel();
		statusDict.setTable_name("TABLE_1");
		statusDict.setField_name("FIELD_1");
		statusDictService.save(statusDict);
		
		statusDict = statusDictService.findById(statusDict.getId());
        System.out.println("--------------------------------------");
        List<StatusDict> modelList = statusDictService.queryStatusDict("TABLE_1", "FIELD_1");
        List<String> idList = new ArrayList<String>();
        for(int i=0; i<modelList.size(); i++) {
        	String id = modelList.get(i).getId();
        	idList.add(id);
        }
        
        assertThat(idList, hasItem(statusDict.getId()));
	}
	
	AtomicInteger counter = new AtomicInteger();
	
	public StatusDict generateRandomModel() {
		
		long key = System.nanoTime() + counter.addAndGet(1);
		
		StatusDict statusDict = new StatusDict();
		statusDict.setId(key+"");
		statusDict.setCreate_time(new Date());
		statusDict.setModify_time(new Date());
		
		return statusDict;
		
	}

}
