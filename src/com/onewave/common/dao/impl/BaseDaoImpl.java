package com.onewave.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import net.zhilink.tools.InitManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.onewave.common.annotation.IBean;
import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IUtil;
import com.onewave.common.dao.IBaseDao;
import com.onewave.common.dao.Pagination;
import com.onewave.common.util.GlobalInformation;
import com.onewave.common.util.ZLUtil;

/**
 * 
 * @Class: BaseDaoImpl
 * @Description: 扩展HibernateDaoSupport的泛型基类
 *
 * @author Haidi Liu
 * @param <E> 实体类型
 * @param <P> 实体类型
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<E extends Serializable, PK extends Serializable> implements IBaseDao<E, PK> {

	protected static final Logger logger = Logger.getLogger(BaseDaoImpl.class);
	
	private final Class<E> entityClass;
	private final String HQL_FIND_ALL;
	private final String HQL_COUNT_ALL;
	
	private String idName;
	private int idType;
	private String tableName;
	
	public BaseDaoImpl() {
		entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		logger.info("************************************************");
		
		idName = IUtil.parserBeanForIdField(entityClass);
		tableName = IUtil.parserBeanForTableName(entityClass);
		idType = IUtil.parserBeanForIdType(entityClass);
		logger.info("*************idName:"+ idName);
		logger.info("*************tableName:"+ tableName);
		logger.info("*************idType:"+ idType);
		
		HQL_FIND_ALL = "select * from " + tableName;
		HQL_COUNT_ALL = "SELECT COUNT(*) FROM " + tableName;
		
	}
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Resource(name="dataSource")
	public void setJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public boolean save(E entity) {
		
		logger.info("saving instance");
		
		IBean ibean = IUtil.parserBean(entity);
		
		Map<String, Object> objMap = ibean.getBeanMap();
		Map<String, Integer> typesMap = ibean.getFieldTypes();
		List<String> notSaveField = ibean.getNotSaveField();
		for(String key : notSaveField) {
			objMap.remove(key);
			typesMap.remove(key);
		}
		
		int fieldSize = typesMap.size();
		Object[] params = new Object[fieldSize];
		int[] types = new int[fieldSize];
		
		String sql = "INSERT INTO "+ibean.getTableName();
		StringBuffer fieldStr = new StringBuffer("(");
		StringBuffer fieldValue = new StringBuffer("(");
		
		Iterator<String> keyIterator = typesMap.keySet().iterator();
		String key = "";
		int index = 0;
		while(keyIterator.hasNext()) {
			key = keyIterator.next();
			fieldStr.append(key+",");
			fieldValue.append("?,");
			types[index] = typesMap.get(key);
			
			Object objTemp = objMap.get(key);
			if(objTemp==null) {
				objTemp = "";
			}
			
			if(types[index]==Types.TIMESTAMP) {
				if(objTemp == null || "".equals(objTemp)){
					objTemp = null;
				}else{
					objTemp = new Timestamp(((Date) objTemp).getTime());
				}
			}
			params[index] = objTemp;
			index++;
		}
		
		sql += fieldStr.substring(0, fieldStr.length()-1) 
				+ ")" + " VALUES" 
				+ fieldValue.substring(0, fieldValue.length()-1) + ")";
		
		logger.info("insert sql: " + sql);
				
		
		GlobalInformation.printBeanParams(entity, "save");
		int result = jdbcTemplate.update(sql, params, types);
		logger.info("---------------result:"+result);
		
		logger.info("save successful");
		
		return result>0?true:false;
		
	}
	
	public String saveAndReturnId(E entity) {
		
		logger.info("saving instance");
		
		IBean ibean = IUtil.parserBean(entity);
		
		Map<String, Object> objMap = ibean.getBeanMap();
		Map<String, Integer> typesMap = ibean.getFieldTypes();
		List<String> notSaveField = ibean.getNotSaveField();
		for(String key : notSaveField) {
			objMap.remove(key);
			typesMap.remove(key);
		}
		
		int fieldSize = typesMap.size();
		Object[] params = new Object[fieldSize];
		int[] types = new int[fieldSize];
		
		String sql = "INSERT INTO "+ibean.getTableName();
		StringBuffer fieldStr = new StringBuffer("(");
		StringBuffer fieldValue = new StringBuffer("(");
		
		Iterator<String> keyIterator = typesMap.keySet().iterator();
		String key = "";
		int index = 0;
		while(keyIterator.hasNext()) {
			key = keyIterator.next();
			fieldStr.append(key+",");
			fieldValue.append("?,");
			types[index] = typesMap.get(key);
			
			Object objTemp = objMap.get(key);
			if(objTemp==null) {
				objTemp = "";
			}
			
			if(types[index]==Types.TIMESTAMP) {
				if(objTemp == null || "".equals(objTemp)){
					objTemp = null;
				}else{
					objTemp = new Timestamp(((Date) objTemp).getTime());
				}
			}
			params[index] = objTemp;
			index++;
		}
		
		sql += fieldStr.substring(0, fieldStr.length()-1) 
				+ ")" + " VALUES" 
				+ fieldValue.substring(0, fieldValue.length()-1) + ")";
		
		logger.info("insert sql: " + sql);
		
		final String sqlSave =sql;
		final List<Object> paramsSave = Arrays.asList(params);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {

				PreparedStatement ps = conn.prepareStatement(sqlSave,
						new String[] { "id" });
				for (int i = 0; i < paramsSave.size(); i++) {
					ps.setObject(i + 1, paramsSave.get(i));
				}
				return ps;
			}

		}, keyHolder);

		String returnId = "" + keyHolder.getKey().longValue();
		logger.info("save  successful, return id: " + returnId);
		
		return returnId;
		
	}
	
	public boolean update(E entity) {
		
		logger.info("updating instance");
		
		IBean ibean = IUtil.parserBean(entity);
		
		Map<String, Object> objMap = ibean.getBeanMap();
		Map<String, Integer> typesMap = ibean.getFieldTypes();
		List<String> notSaveField = ibean.getNotSaveField();
		for(String key : notSaveField) {
			objMap.remove(key);
			typesMap.remove(key);
		}
		List<String> notUpdateField = ibean.getNotUpdateField();
		logger.info("update index: " + typesMap.size());
		for(String key : notUpdateField) {
			objMap.remove(key);
			typesMap.remove(key);
		}
		
		int fieldSize = typesMap.size();
		Object[] params = new Object[fieldSize];
		int[] types = new int[fieldSize];
		
		Object idValue = objMap.get(idName);
		objMap.remove(idName);
		typesMap.remove(idName);
		
		StringBuffer setStr = new StringBuffer();
		
		Iterator<String> keyIterator = typesMap.keySet().iterator();
		String key = "";
		int index = 0;
		while(keyIterator.hasNext()) {
			key = keyIterator.next();
			setStr.append(key+"=?,");
			types[index] = typesMap.get(key);
			
			Object objTemp = objMap.get(key);
			if(objTemp==null) {
				objTemp = "";
			}
			
			if(types[index]==Types.TIMESTAMP) {
				if(objTemp == null || "".equals(objTemp)){
					objTemp = null;
				}else{
					objTemp = new Timestamp(((Date) objTemp).getTime());
				}
			}
			params[index] = objTemp;
			
			index++;
		}
		logger.info("update index: " + index);
		types[index] = idType;
		params[index] = idValue;
		
		
		String sql = "UPDATE " + tableName + " SET " 
				+ setStr.substring(0, setStr.length()-1) 
				+ " WHERE " + idName + "=?";
		
		logger.info("update sql: " + sql);
				
		
		GlobalInformation.printBeanParams(entity, "update");
		int result = jdbcTemplate.update(sql, params, types);
		logger.info("---------------result:"+result);
		
		logger.info("update successful");
		
		return result>0?true:false;
		
	}
	
	public boolean delete(E entity) {
		
		logger.info("deleting instance");
		
		GlobalInformation.printBeanParams(entity, "delete");
		
		IBean ibean = IUtil.parserBean(entity);
		Object idObj = ibean.getBeanMap().get(idName);
		
		logger.info("delete idName: " + idName);
		
		String sql = "delete from " + tableName + " where " + idName + "=?";
		
		logger.info("delete sql: " + sql);
		
		Object[] params = new Object[1];
		params[0] = idObj;
		int[] types = new int[1];
		types[0] = idType;
		int result = jdbcTemplate.update(sql, params, types);
		logger.info("---------------result:"+result);
		
		logger.info("delete successful");
		
		return result>0?true:false;
		
	}
	
	public boolean deleteById(PK id) {
		
		logger.info("deleting instance by Id ------ id:" + id);
		
		String sql = "delete from " + tableName + " where " + idName + "=?";
		
		logger.info("delete sql: " + sql);
		
		Object[] params = new Object[1];
		params[0] = id;
		int[] types = new int[1];
		types[0] = idType;
		int result = jdbcTemplate.update(sql, params, types);
		logger.info("---------------result:"+result);
		
		logger.info("delete successful");
		
		return result>0?true:false;
		
	}
	
	public E findById(PK id) {
		
		logger.info("getting instance by id: " + id);
		
		String sql = "SELECT * FROM " + tableName + " WHERE " + idName + "=?";
		
		logger.info("query sql: " + sql);
		
		Object[] params = new Object[1];
		params[0] = id;
		int[] types = new int[1];
		types[0] = idType;
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params, types);
		
		if(list.size() <= 0) {
			return null;
		}
		
		E entity = parserMapForE(list.get(0));
		
		GlobalInformation.printBeanParams(entity, "findById");
		
		logger.info("get successful");
		
		return entity;
		
	}
	
	public List<E> findAll() {
		return findAll(null, null);
	}
	
	public List<E> findAll(String where){
		return findAll(where ,null);
	}
	
	public List<E> findAll(String where, String order) {
		
		String sql = null;
		if(where==null || "".equals(where.trim())) {
			sql = HQL_FIND_ALL + (order == null ? " order by " + idName + " desc" : " "+order);
		} else {
			sql = HQL_FIND_ALL + " where " + where + (order == null ? " order by " + idName + " desc" : " "+order);
		}
    	
    	logger.info("find sql: " + sql);
    	
    	List list = jdbcTemplate.queryForList(sql);
    	
    	List<E> results = new ArrayList<E>();
    	
    	Iterator iterator = list.iterator();
    	E entity = null;
    	while (iterator.hasNext()) {
    		entity = parserMapForE((Map) iterator.next());
    		results.add(entity);
    	}
		
        return results;
	}
	
	public List<E> findAllByPage(int pageNum, int pageSize) {
		return findAll(ZLUtil.getPageFirstResult(pageNum, pageSize), pageSize);
	}
	
	public List<E> findAllByPage(int pageNum, int pageSize, String where) {
		return findAll(ZLUtil.getPageFirstResult(pageNum, pageSize), pageSize, where);
	}
	
	public List<E> findAll(int firstResult, int maxResults) {
		
		logger.info("getting instances");
		
		List<E> entityList = find(firstResult, maxResults, null,null);
		
		logger.info("get successful");
		
		return entityList;
	}
	
	public List<E> findAll(int firstResult, int maxResults, String where) {
		
		logger.info("getting instances");
		
		List<E> entityList = find(firstResult, maxResults, where,null);
		
		logger.info("get successful");
		
		return entityList;
	}
	
	public List<E> findAllBySql(String sql) {
		
		logger.info("findBySql instances");
		
		logger.info("find sql: " + sql);
    	
    	List list = jdbcTemplate.queryForList(sql);
    	
    	List<E> results = new ArrayList<E>();
    	
    	Iterator iterator = list.iterator();
    	E entity = null;
    	while (iterator.hasNext()) {
    		entity = parserMapForE((Map) iterator.next());
    		results.add(entity);
    	}
		
		logger.info("findBySql successful");
		
		return results;
	}
	
	public int countAll() {
		
		logger.info("aggregating instance");
		
		int total = jdbcTemplate.queryForInt(HQL_COUNT_ALL);
		
		logger.info("aggregate successful ------ total: " + total);
		
		return total;
	}
	
	public int countAll(String where) {
		
		logger.info("aggregating instance");
		
		String sql = HQL_COUNT_ALL;
				
		if(where!=null && !"".equals(where.trim())) {
			sql += " where " + where;
		}
		
		logger.info("countAll sql: " + sql);
		
		int total = jdbcTemplate.queryForInt(sql);
		
		logger.info("aggregate successful ------ total: " + total);
		
		return total;
	}
	
	public int countBySql(String sql) {
		logger.info("aggregating instance");
		
		logger.info("countAll sql: " + sql);
		
		int total = jdbcTemplate.queryForInt(sql);
		
		logger.info("aggregate successful ------ total: " + total);
		
		return total;
	}
    
    private List<E> find(final int firstResult, final int maxResults, String where, String order) {
    	
    	String sql = "";
    	
    	if(where == null) {
    		where = "";
    	} else {
    		where = where.trim();
    	}
    	
    	logger.info("-------------------firstResult:"+firstResult);
    	logger.info("-------------------maxResults:"+maxResults);
    	
    	if(firstResult <= InitManager.Defaut_Unselected_ID || maxResults <= InitManager.Defaut_Unselected_ID || maxResults < firstResult) {
    		if("".equals(where)) {
    			sql = HQL_FIND_ALL + " order by " + idName + " desc";
    		} else {
    			sql = HQL_FIND_ALL + " where " + where + " order by " + idName + " desc";
    		}
    	} else {
    		if("".equals(where)) {
    			sql = "select t.* from (select r.*, rownum linenum from ( " + HQL_FIND_ALL+" order by "+(StringUtils.isBlank(order)? "" : order+", ")+ idName+" desc ) r where rownum <= " + maxResults + ") t WHERE linenum > " + firstResult;
    		} else {
    			sql = "select t.* from (select r.*, rownum linenum from ( "+HQL_FIND_ALL +" where " + where +" order by "+(StringUtils.isBlank(order)? "" : order+", ")+ idName+" desc) r where " + where + " and rownum <= " + maxResults + ") t WHERE linenum > " + firstResult;
    		}
    	}
    	
    	logger.info("find sql: " + sql);
    	
    	List list = jdbcTemplate.queryForList(sql);
    	
    	List<E> results = new ArrayList<E>();
    	
    	Iterator iterator = list.iterator();
    	E entity = null;
    	while (iterator.hasNext()) {
    		entity = parserMapForE((Map) iterator.next());
    		results.add(entity);
    	}
		
        return results;
    	
    }
    public boolean updateBySql(String sql) {
    	boolean results = false;
		
		logger.info("updateBySql instances");
		
		logger.info("updateBySql sql: " + sql);
    	
		int flag = jdbcTemplate.update(sql);
		if(flag>0){
			results = true;
		}
		
		logger.info("updateBySql successful");
		
		return results;
	}
    
    public E parserMapForE(Map eMap) {
    	Object newObj = null;
		
		try {
			newObj = entityClass.newInstance();
			Class<?> clazzEnTity = newObj.getClass();
			Field[] fields = clazzEnTity.getDeclaredFields();
			
			for(Field field : fields) {
				field.setAccessible(true);
				String key = "", name = "";
				
				IField iField = field.getAnnotation(IField.class);
				if(iField != null) {
					key = iField.title();
				}
				
				name = field.getName();
				if("".equals(key)) {
					key = name;
				}
				
				Object eObj = eMap.get(key);
				if(eObj instanceof BigDecimal) {
					String temp = ((BigDecimal) eObj).toString();
					switch(ZLUtil.toDBType(field.getGenericType())) {
					case Types.INTEGER:
						field.set(newObj, new Integer(temp));
						break;
					case Types.BIGINT:
						field.set(newObj, new Long(temp));
						break;
					case Types.TINYINT:
						field.set(newObj, new Byte(temp));
						break;
					case Types.SMALLINT:
						field.set(newObj, new Short(temp));
						break;
					case Types.REAL:
						field.set(newObj, new Float(temp));
						break;
					case Types.DOUBLE:
						field.set(newObj, new Double(temp));
						break;
					case Types.BIT:
						field.set(newObj, new Boolean(temp));
						break;
					case Types.VARCHAR:
						field.set(newObj, temp);
						break;
					case Types.TIMESTAMP:
					case Types.DATE:
					case Types.TIME:
						field.set(newObj, (Date) eObj);
						break;
					default:
						field.set(newObj, eObj);
					}
				} else if(eObj != null) {
					switch(ZLUtil.toDBType(field.getGenericType())) {
					case Types.INTEGER:
						field.set(newObj, (Integer)eObj);
						break;
					case Types.BIGINT:
						field.set(newObj, (Long) eObj);
						break;
					case Types.TINYINT:
						field.set(newObj, (Byte) eObj);
						break;
					case Types.SMALLINT:
						field.set(newObj, (Short) eObj);
						break;
					case Types.REAL:
						field.set(newObj, (Float) eObj);
						break;
					case Types.DOUBLE:
						field.set(newObj, (Double) eObj);
						break;
					case Types.BIT:
						field.set(newObj, (Boolean) eObj);
						break;
					case Types.VARCHAR:
						field.set(newObj, (String) eObj);
						break;
					case Types.TIMESTAMP:
					case Types.DATE:
					case Types.TIME:
						field.set(newObj, (Date) eObj);
						break;
					default:
						field.set(newObj, eObj);
					}
				}
				
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if(newObj!=null) {
			return (E) newObj;
		}
		
		return null;
    }

	@Override
	public String getSplitFieldSql(String orgiSetSql) {
		if(StringUtils.isEmpty(orgiSetSql)) return null;
		StringBuffer sql= new StringBuffer();
		sql.append(" with temp as")
			.append(" (")
				.append(orgiSetSql)
			.append(")")
				.append(" select substr(text,instr(text,',',1,rn)+1,instr(text,',',1,rn+1)-instr(text,',',1,rn)-1) text from")
				.append("(")
				  .append(" select ','||t1.text||',' text,t2.rn from")
				   	.append("(select text,length(text)-length(replace(text,',',''))+1 rn from temp) t1,")
				   	.append("(select rownum rn from all_objects where rownum <= (select max(length(text)-length(replace(text,',',''))+1) rn from temp)) t2")
				  .append(" where t1.rn >= t2.rn order by text,rn")
				.append(")");
		return sql.toString();
	}
	
	@Override
	public Pagination<E> findPagination(int pageNum,int pageSize,String where,String order) {
		Pagination<E> page = new Pagination<E>();
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		long totalSize = countAll(where);
		int firstResult = ZLUtil.getPageFirstResult(pageNum, pageSize);
		int maxResults =  firstResult + pageSize;
		List<E> results = find(firstResult , maxResults, where, order);
		page.setTotalSize(totalSize);
		page.setList(results);
		return page;
	}
}
