Ext.define('app.view.oldview.customerPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.customerPanel',
    //layout:'column',
	frame: true,
    columnLines: true,
	//iconCls: 'icon-grid',
	autoHeight: false,
	autoScroll: true,
    initComponent: function() {
    	
	var eids = '';
	var widthSize = document.body.clientWidth-150;
	var heightSize = document.body.clientHeight-53;
	
	var pgSize = 10;  //分页数
    var tmpStartTime = "2010-01-01"; //默认开始时间
	var menuId_temp = "-1";
	var menuName_temp = "";
  	var contStatus = "-10000";
  	var contMarFilterStatusType = "1";//1 有效，-1 无效，0 其他状态
  	var contMarFilterStatus = "-10000";
  	var selectedId = "0";

//用户信息begin**************************************************************************************
  	Ext.define("user_address_grid_store_model", {
  	    extend: "Ext.data.Model",
  		idProperty: "c_id",
  	    fields: [
 		        {name: 'user_id', type: 'string'},
				{name: 'user_name', type: 'string'},
				{name: 'gender', type: 'string'},
				{name: 'birthday', type: 'string'},
				{name: 'level', type: 'string'},
				{name: 'identify_no', type: 'string'},
				{name: 'mobile_no', type: 'string'},
	  			{name: 'contact_addr', type: 'string'},
	  			{name: 'contact_no', type: 'string'},
	  			{name: 'contact_post_code', type: 'string'},
	  			{name: 'isdefault', type: 'string'}
  	    ]
  	});
  	var user_address_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
		//fields: ['mar_s_contId','mar_s_contName','mar_s_status','mar_s_contType','mar_s_contProvider','mar_s_contIntro','cont_is_url_used','cont_superscript'],
  		model: 'user_address_grid_store_model',
		proxy: {
			type: 'ajax',
			url: 'contvideo/query_cont_for_mar.do?auth=1',
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'mar_s_contId'
			},
			actionMethods:{
				read: 'POST'
			}
		}
  	});
  	
  	user_address_grid_store.proxy.extraParams={
    	contType: "",
    	contProvider: "",
    	keyWord: "",
    	contStatus: contStatus,
    	startTime: tmpStartTime,
    	endTime: "",
    	menuId:menuId_temp
    }
    function onModify(tree, row, col, item, e, record) {
  		console.log("modify record id="+record.raw.mar_s_contId);
    }
  	function onDelete(tree, row, col, item, e, record) {
  		user_order_grid_store.removeAt(row);
  		console.log("delete record id="+record.get("mar_s_contId"));
  	}
  	function onAddBtn(btn) {
  		console.log("add btn="+btn);
  	}
  	function onAddToOrder(tree, row, col, item, e, record) {
  		console.log("add record id="+record.get("c_id") +"," +record.get("c_name"));
//  		var model = user_order_grid_store.add({
//  			'mar_s_contId':record.raw.c_id,
//  			'mar_s_contName':record.raw.c_name,
//  			'sales_no':record.raw.cs_sales_no,
//  			'cont_price':record.raw.cs_sale_price,
//  			'cont_amount':1,
//  			'mar_s_contProvider':record.raw.provider_id
//  		});
  		var model = user_order_grid_store.insert(0,{
  			'mar_s_contId':record.raw.c_id,
  			'mar_s_contName':record.raw.c_name,
  			'sales_no':record.raw.cs_sales_no,
  			'cont_price':record.raw.cs_sale_price,
  			'cont_amount':1,
  			'mar_s_contProvider':record.raw.provider_id
  		});
//  		user_order_grid_store.add(record);
  		model.commit();
  		user_order_grid_store.removeAt(-1);
  	}
  	var user_address_grid = new Ext.grid.GridPanel({
		//title:'用户信息',
	    store: user_address_grid_store,
	    region: 'west',
	    //width:560,
	    height:150,
	    //autoHeight: false,
		autoScroll: true,
	    frame:true,
		//collapsible : true,
		animate : true,
		border : false,
		titleCollapse : true,
		loadMask:{
			msg:'数据加载中...'
		},
	    //iconCls:'icon-grid',
		bodyStyle:'padding:0 10 1 0',
	    selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
        viewConfig: {
        	stripeRows: true,
        	enableTextSelection:true
        },
	    columns: [//用户ID	用户姓名	电话号码	性别	出生年月日	会员级别	身份证号	收货地址	联系人	收货电话	是否默认地址	消费金额	ClientID	MACID
			  { header: "用户ID",width: 80, sortable: true, dataIndex: 'mar_s_contId',locked: true},
			  { header: '用户姓名', width: 100, sortable: true, dataIndex: 'mar_s_contName',locked: true },
			  { header: '电话号码', width: 80, sortable: true, dataIndex: 'mar_s_contName',locked: true },
			  { header: '性别', width: 80, sortable: true, dataIndex: 'mar_s_status' },
			  { header: "出生年月日", width: 80, sortable: true, dataIndex: 'mar_s_contType' },
			  { header: "会员级别", width: 80, sortable: true, dataIndex: 'mar_s_contProvider' },
			  { header: "身份证号", width: 80, sortable: true, dataIndex: 'mar_s_contIntro' },
			  { header: "收货地址", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "联系人", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "收货电话", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "是否默认地址", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "消费金额", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "ClientID", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "MACID", width: 80, dataIndex: 'cont_superscript' }, {
					header: '',
			        xtype: 'actioncolumn',
			        locked: true,
			        width: 100,
			        sortable: false,
			        menuDisabled: true,
			        align: 'center',
			        items: [' ', {
			        	iconCls: 'edit',
			        	tooltip: '编辑条目',
			            handler: onModify
			        }, ' ', {
			        	iconCls: 'delete',
			        	tooltip: '删除条目',
			            handler: onDelete
			        }]
			    }
		],
      	bbar: new Ext.PagingToolbar({
          	pageSize: pgSize,
          	store: user_address_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条  共 {2}条',
			//autoScroll: true,
          	emptyMsg: '没有记录' 
      	})
	});
//用户信息end************************************************************************************** 
//订单信息begin**************************************************************************************
  	Ext.define("user_order_grid_store_model", {
  	    extend: "Ext.data.Model",
  		idProperty: "c_id",
  	    fields: [
		        {name: 'c_id', type: 'string'},
				{name: 'c_name', type: 'string'},
				{name: 'c_status', type: 'string'},
				{name: 'is_locked', type: 'string'},
				{name: 'provider_id', type: 'string'},
				{name: 'cs_sale_price', type: 'int'},
				{name: 'cs_sales_no', type: 'string'},
				
		        {name: 'order_id', type: 'string'},//订单号
		        {name: 'channel_order_id', type: 'string'},//渠道
		        {name: 'order_pay_time', type: 'string'},//订单支付时间
		        {name: 'order_cont_amount', type: 'string'},//商品数量
		        {name: 'channel_name', type: 'string'},//渠道名称
		        {name: 'rewards', type: 'string'},//积分
		        {name: 'tiket_no', type: 'string'},//优惠券编码

  	    ]
  	});
  	var user_order_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
  		model: 'user_order_grid_store_model',
//		fields: ['mar_s_contId','mar_s_contName','mar_s_status','mar_s_contType','mar_s_contProvider','mar_s_contIntro','cont_is_url_used','cont_superscript'
//		         ,'contact_name','contact_addr','contact_no','contact_post_code'],
		proxy: {
			type: 'ajax',
			url: 'orders/query.do?auth=1',
	    	extraParams: {
	    		ids: '216323168'
	    	},
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'c_id'
			},
			actionMethods:{
				read: 'POST'
			}
		}
  	});

  	user_order_grid_store.proxy.extraParams={
    	contType: "",
    	contProvider: "",
    	keyWord: "",
    	contStatus: contStatus,
    	startTime: tmpStartTime,
    	endTime: "",
    	menuId:menuId_temp
    }
  	var search_name = new Ext.form.TextField({
    	xtype: 'textfield',
    	name: 'search_name',
		fieldLabel: '资产名称',
		emptyText : '商品编号(模糊)、ID(精确)或名称(模糊)',
    	tooltip: '商品编号(模糊)、ID(精确)或名称(模糊)',
		hideLabel : true,
		labelWidth: 70,
		maxWidth: 250,
		flex: 1,
		labelAlign: 'right'
	});
  	var contSalesToolBar = new Ext.toolbar.Toolbar({
  		items: [{
  			xtype : 'panel',
  			baseCls : 'x-plain'
  		}, search_name, {
  			xtype: 'button',
  			text: '搜索',
  			glyph: 0xf002,
  			width: 60,
  			margin: '0, 0, 0, 5',
  			handler: function(){
  				var store = user_order_grid_store;

  				store.proxy.extraParams.c_name = search_name.getValue();
  				store.load();
  			}
  		}, {
  			xtype: 'button',
  			text: '重置',
  			width: 60,
  			margin: '0, 0, 0, 5',
  			handler: function(){
  				search_name.setValue("");
  			}
  		}]
  	});
	var mar_site_combo = new Ext.form.ComboBox({
		baseCls:'x-plain',
		xtype:'combo',
		maxHeight:120,
		width:120,
		hideLabel:true,
		displayField:'s_name',
		valueField:'s_id',
		//readOnly:true,
		editable: false,
		queryMode:'local',
		triggerAction:'all',
		emptyText:'请选择站点',
		store:new Ext.data.JsonStore({
			fields:['s_id', 's_name'],
			autoLoad: true,
			proxy: {
				type: 'ajax',
				url:"site/query_all.do",
				reader: {
					root:'data'
				}
			}
		})
	});
	var mar_code_combo = new Ext.form.ComboBox({
		baseCls:'x-plain',
		xtype:'combo',
		maxHeight:120,
		width:120,
		hideLabel:true,
		displayField:'s_name',
		valueField:'s_id',
		//readOnly:true,
		editable: false,
		queryMode:'local',
		triggerAction:'all',
		emptyText:'请选择站点',
		store:new Ext.data.JsonStore({
			fields:['s_id', 's_name'],
			autoLoad: true,
			proxy: {
				type: 'ajax',
				url:"site/query_all.do",
				reader: {
					root:'data'
				}
			}
		})
	});
	var user_order_grid = new Ext.grid.GridPanel({
		//title:'订单商品列表',
	    store: user_order_grid_store,
	    //region: 'west',
	    //width:560,
	    height:250,
	    //autoHeight: false,
		//autoScroll: true,
	    frame:true,
		//collapsible : true,
		//animate : true,
		//border : true,
		//titleCollapse : true,
		loadMask:{
			msg:'数据加载中...'
		},
	    //iconCls:'icon-grid',
		//bodyStyle:'padding:0 10 1 0',
	    //selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
        viewConfig: {
        	stripeRows: true,
        	enableTextSelection:true
        },
	    columns: [//支付时间	商品编号	商品名称	收货人	收货地址	优惠编码	单价	扣除礼金	实际价	渠道ID	渠道名称	收货人电话	邮编	获得积分
			  { header: "订单号",width: 80, sortable: true, dataIndex: 'order_id' },
			  { header: '支付时间', width: 100, sortable: true, dataIndex: 'order_pay_time',locked: true },
			  { header: '商品编号', width: 60, sortable: true, dataIndex: 'cs_sales_no',locked: true },
			  { header: '商品id', width: 60, sortable: true, dataIndex: 'c_id',locked: true },
			  { header: '商品名称', width: 100, sortable: true, dataIndex: 'c_name',locked: true },
//			  { header: "选择已有收货人", width: 150, sortable: true, dataIndex: 'mar_s_status', editor:mar_site_combo },
//			  { header: "收货人", width: 150, sortable: true, dataIndex: 'contact_name', editor:{
//	                xtype: 'textfield',
//	                allowBlank: false
//	            }},
//			  { header: "收货地址", width: 80, sortable: true, dataIndex: 'contact_addr', editor:{
//	                xtype: 'textfield',
//	                allowBlank: false
//	            } },
//			  { header: "收货人电话", width: 80, dataIndex: 'contact_no', editor:{
//	                xtype: 'textfield',
//	                allowBlank: false
//	            } },
//			  { header: "邮编", width: 80, dataIndex: 'contact_post_code', editor: 'textfield'},
			  { header: "优惠编码", width: 80, sortable: true, dataIndex: 'tiket_no', editor:mar_code_combo},
			  { header: "单价", width: 80, dataIndex: 'cs_sale_price' },
			  { header: "数量", width: 80, dataIndex: 'order_cont_amount' },
			  { header: "扣除礼金", width: 80, dataIndex: 'cont_reward' },
			  { header: "实际价", width: 80, dataIndex: 'order_price' },
			  { header: "渠道ID", width: 80, dataIndex: 'cs_channel_id' },
			  { header: "渠道名称", width: 80, dataIndex: 'cs_channel_name' },
			  { header: "渠道订单号",width: 80, sortable: true, dataIndex: 'channel_order_id' },
			  { header: "获得积分", width: 80, dataIndex: 'rewards' }, {
					header: '',
			        xtype: 'actioncolumn',
			        locked: true,
			        width: 100,
			        sortable: false,
			        menuDisabled: true,
			        align: 'center',
			        items: [' ', {
			        	iconCls: 'edit',
			        	tooltip: '编辑条目',
			            handler: onModify
			        }, ' ', {
			        	iconCls: 'delete',
			        	tooltip: '删除条目',
			            handler: onDelete
			        }]
			    }
		],
        plugins:[
                 Ext.create('Ext.grid.plugin.CellEditing',{  
                     clicksToEdit:1 //设置单击单元格编辑  
                 })
        ],
        dockedItems:[contSalesToolBar],
//	    tbar: [{
//	    	text: '添加',
//	    	iconCls: 'add',
//	    	handler: onAddBtn
//	    }],
      	bbar: new Ext.PagingToolbar({
          	pageSize: pgSize,
          	store: user_order_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条  共 {2}条',
			//autoScroll: true,
          	emptyMsg: '没有记录' 
      	})
	});
	user_order_grid.on('edit', function(editor, e) {
	    // commit the changes right after editing finished
		console.log('--:field='+e.field);
		console.log('--:field='+e.record.get(e.field));
	
//		if (e.field == 'mar_s_status'){
//			e.record.set('contact_name','收货人aaaa');
//			e.record.set('contact_addr','地址1111');
//			e.record.set('contact_no','电话13333');
//			e.record.set('contact_post_code','101010');
//		}
//		else 
		if (e.field == 'mar_s_contIntro'){
			e.record.set('cont_price', '132');
			e.record.set('cont_reward', '31');
			e.record.set('order_price', '101');
		}
		e.record.commit();
	});
//订单信息end************************************************************************************** 

//通话信息begin************************************************************************************** 
  	var user_phone_call_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
		fields: ['mar_s_contId','mar_s_contName','mar_s_status','mar_s_contType','mar_s_contProvider','mar_s_contIntro','cont_is_url_used','cont_superscript'],
		proxy: {
			type: 'ajax',
			url: 'contvideo/query_cont_for_mar.do?auth=1',
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'mar_s_contId'
			},
			actionMethods:{
				read: 'POST'
			}
		}
  	});
  	
  	user_phone_call_grid_store.proxy.extraParams={
    	contType: "",
    	contProvider: "",
    	keyWord: "",
    	contStatus: contStatus,
    	startTime: tmpStartTime,
    	endTime: "",
    	menuId:menuId_temp
    }

	var user_phone_call_grid = new Ext.grid.GridPanel({
		//title:'通话信息',
	    store: user_phone_call_grid_store,
	    region: 'west',
	    //width:560,
	    height:150,
	    //autoHeight: false,
		autoScroll: true,
	    frame:true,
		//collapsible : true,
		animate : true,
		border : true,
		titleCollapse : true,
		loadMask:{
			msg:'数据加载中...'
		},
	    //iconCls:'icon-grid',
		bodyStyle:'padding:0 10 1 0',
	    selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
        viewConfig: {
        	stripeRows: true,
        	enableTextSelection:true
        },
	    columns: [//主叫号码	目的码	来电时间	开始时间	通话时长	结束时间	用户ID	用户姓名	类型	用户ID	备注
		          { header: '主叫号码', width: 80, sortable: true, dataIndex: 'mar_s_contName',locked: true },
				  { header: "用户ID",width: 80, sortable: true, dataIndex: 'mar_s_contId',locked: true},
				  { header: '用户姓名', width: 100, sortable: true, dataIndex: 'mar_s_contName',locked: true },
				  { header: '客服号码', width: 80, sortable: true, dataIndex: 'mar_s_status' },
				  { header: "来电时间", width: 80, sortable: true, dataIndex: 'mar_s_contType' },
				  { header: "开始时间", width: 80, sortable: true, dataIndex: 'mar_s_contProvider' },
				  { header: "通话时长", width: 80, sortable: true, dataIndex: 'mar_s_contIntro' },
				  { header: "结束时间", width: 80, dataIndex: 'cont_is_url_used' },
				  { header: "类型", width: 80, dataIndex: 'cont_is_url_used' },
				  { header: "备注", width: 80, dataIndex: 'cont_superscript' }, {
						header: '',
				        xtype: 'actioncolumn',
				        locked: true,
				        width: 100,
				        sortable: false,
				        menuDisabled: true,
				        align: 'center',
				        items: [' ', {
				        	iconCls: 'edit',
				        	tooltip: '编辑条目',
				            handler: onModify
				        }, ' ', {
				        	iconCls: 'delete',
				        	tooltip: '删除条目',
				            handler: onDelete
				        }]
				    }
		],
      	bbar: new Ext.PagingToolbar({
          	pageSize: pgSize,
          	store: user_phone_call_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条  共 {2}条',
			//autoScroll: true,
          	emptyMsg: '没有记录' 
      	})
	});
//通话信息end************************************************************************************** 

//查询条件begin************************************************************************************** 
	var mar_site_name_form = new Ext.form.TextField({
		xtype:'textfield',
		fieldLabel:' *客户号码',
		hideLabel: true,
		emptyText:'客户号码',
		allowBlank: true,
		//id:'mar_site_name',
		name:'mar_site_name',
		width:90
	});
	var mar_site_search = function() {
		var contKeyword = mar_site_name_form.getValue();
			
		user_phone_call_grid_store.on('beforeload',function(){        // =======翻页时 查询条件
			user_phone_call_grid_store.proxy.extraParams={
					keyWord: contKeyword,
					startTime:'2010-12-12',
					menuId:-1
			   }
			});
		user_phone_call_grid_store.load({                                      // =======翻页时分页参数
				params:{
					start: 0,
					limit: pgSize
				}
			});

		user_address_grid_store.on('beforeload',function(){        // =======翻页时 查询条件
			user_address_grid_store.proxy.extraParams={
					keyWord: contKeyword,
					startTime:'2010-12-12',
					menuId:-1
			   }
			});
		user_address_grid_store.load({                                      // =======翻页时分页参数
				params:{
					start: 0,
					limit: pgSize
				}
			});

		user_order_grid_store.on('beforeload',function(){        // =======翻页时 查询条件
			user_order_grid_store.proxy.extraParams={
					keyWord: contKeyword,
					startTime:'2010-12-12',
					menuId:-1
			   }
			});
		user_order_grid_store.load({                                      // =======翻页时分页参数
				params:{
					start: 0,
					limit: pgSize
				}
			});

	}
	var amr_menu_tree_tbar = new Ext.Toolbar({
		items: [{
			text: '搜索正在拨入的客户',
			style:'background-color:#99BBE8',
			//bodyStyle : 'padding:0 12 0 12',
			handler: mar_site_search,
			//scope: this,
			width:180
		},{
			xtype : 'label',
			name : 'phone_no',
			text : '号码为：暂无',
			style : 'background-color:#ffff00;font-size: 13px; margin-right: 20px;'
		},'搜索其他客户号码：', mar_site_name_form, {
			text: '搜索',
			style:'background-color:#99BBE8',
			handler: mar_site_search,
			//scope: this,
			width:40
		}]
	});
//查询条件end************************************************************************************** 

    this.tbar = amr_menu_tree_tbar;
//    this.items = [user_phone_call_grid, cont_sales_grid, {
//		xtype: 'fieldset',
//		title: '订单信息',
//		collapsible: true,
//		defaultType: 'textfield',
//		fieldName: 'serviceinfo',
//		margin: '10, 2, 0, 0',
//		items: [user_order_grid]}, user_address_grid];
    this.items = [{
    	xtype: 'fieldset',
    	title: '通话信息',
    	collapsible: true,
    	defaultType: 'textfield',
    	margin: '10, 2, 0, 0',
		border : false,
    	items: [user_phone_call_grid]
    }, {
    	xtype: 'fieldset',
    	title: '订单信息(搜索商品信息)',
    	collapsible: true,
    	defaultType: 'textfield',
    	margin: '10, 2, 0, 0',
		border : false,
		items: [user_order_grid,{
			xtype: 'fieldset',
			title: '用户信息',
			collapsible: true,
			defaultType: 'textfield',
			margin: '10, 2, 0, 100',
			border : false,
			items: [user_address_grid]
		}]
    }];
	this.callParent();
    }
});