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
    	var win = this;
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

    function onModify(tree, row, col, item, e, record) {
  		console.log("modify record id="+record.raw.c_id);
		Ext.MessageBox.show({
			title : "提示",
			msg : "修改通话记录",
			width : 110,
			buttons : Ext.Msg.OK,
			icon : Ext.Msg.INFO
		});
    }
  	function onDelete(tree, row, col, item, e, record) {
  		console.log("delete record id="+record.get("c_id"));
        var store = tree.store;
        store.proxy.extraParams.ids = "";
    	store.removeAt(row);
  	}
  	function onAddBtn(tree, row, col, item, e, record) {
  		console.log("add record id="+record.get("c_id")+",row="+row);
        var store = tree.store;
        store.proxy.extraParams.ids = record.get("c_id");
        if (row < store.getCount()-1){
        	store.removeAt(row+1,store.getCount()-row);
        }
        if (row > 0){
        	store.removeAt(0,row);
        }
  	}
  	function submitOrder(record, c_id, addr_id, tiket_no){
  		console.log("submitOrder record c_id="+c_id +",addr_id=" +addr_id+",tiket_no="+tiket_no);
  		win.mask('正在生成订单...');
		
    	Ext.Ajax.request({
    		url: 'orders/save.do',
    		async: true,
    		params: {
    			c_id: c_id,
    			addr_id: addr_id,
    			tiket_no: tiket_no
    		},
			
    		scope: this,
    		success: function(resp, opt) {
    			win.unmask();

    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
        			record.set('order_id', '1001');
        			record.commit();
        			Ext.MessageBox.show({
        				title : "提示",
        				msg : "生成订单成功！订单号："+respJson.order_id+"，商品名："+record.get("c_name")+"，金额："+record.get("order_price"),
        				width : 110,
        				buttons : Ext.Msg.OK,
        				icon : Ext.Msg.INFO
        			});
    			}
    			else{
        			Ext.MessageBox.show({
        				title : "错误提示",
        				msg : "订购失败！"+respJson.msg,
        				width : 110,
        				buttons : Ext.Msg.OK,
        				icon : Ext.Msg.ERROR
        			});
    			}
    		}
    	});

  	}
  	function onAddToOrder(tree, row, col, item, e, record) {
  		console.log("add record id="+record.get("c_id") +"," +record.get("c_name"));
  		if (record.get("order_id") != ""){
			Ext.MessageBox.show({
				title : "错误提示",
				msg : "此商品已生成订单！如要再次生成订单，请重新查询。如要修改订单，请到订单管理模块！",
				width : 110,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.ERROR
			});
			return;
  		}
  		var store = tree.store;
  		if (store.getCount()>1){
			Ext.MessageBox.show({
				title : "错误提示",
				msg : "目前订单只支持有一种商品！",
				width : 110,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.ERROR
			});
			return;
  		}
  		
  		var addr = user_address_grid.getSelectionModel().lastSelected;
		var rows = user_address_grid.getSelectionModel().getCount();// 返回值为Record数组
		if(rows != 1) {
			Ext.MessageBox.show({
				title : "错误提示",
				msg : "请选择一个收货人地址！",
				width : 110,
				buttons : Ext.Msg.OK,
				icon : Ext.Msg.ERROR
			});
			return;
		}

		var c_id = record.get("c_id");
		var addr_id = addr.get("a_id");
		var tiket_no = record.get("tiket_no");
  		if (tiket_no == ""){
  	    	Ext.Msg.show({
  			    title:'优惠券有空',
  			    message: "优惠券有空,是否继续？",
  			    buttons: Ext.Msg.YESNO,
  			    icon: Ext.Msg.QUESTION,
  			    scope: this,
  			    fn: function(btn) {
  			        if (btn === 'yes') {
  			  			submitOrder(record, c_id, addr_id, tiket_no);  			
  			        }
  			    }
  			});
  		}else{
	  			submitOrder(record, c_id, addr_id, tiket_no);  			
  		}
  	}
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
		        {name: 'order_price', type: 'string'},//订单应付金额
		        {name: 'channel_order_id', type: 'string'},//渠道
		        {name: 'order_pay_time', type: 'string'},//订单支付时间
		        {name: 'order_cont_amount', type: 'string'},//商品数量
		        {name: 'channel_name', type: 'string'},//渠道名称
		        {name: 'rewards', type: 'string'},//积分
		        {name: 'tiket_no', type: 'string'},//优惠券编码
		        {name: 'tiket_reward', type: 'string'}//扣除礼金

  	    ]
  	});
  	var user_order_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
  		model: 'user_order_grid_store_model',
//		fields: ['mar_s_contId','mar_s_contName','mar_s_status','mar_s_contType','mar_s_contProvider','mar_s_contIntro','cont_is_url_used','cont_superscript'
//		         ,'contact_name','contact_addr','contact_no','contact_post_code'],
		proxy: {
			type: 'ajax',
			url: 'orders/query_cont.do?auth=1',
	    	extraParams: {
	    		ids: '216323168',
	    		c_name: ''
	    	},
			reader: {
	    		type: 'json',
	    		totalProperty : "total",
	    		root: 'data'
			},
			actionMethods:{
				read: 'POST'
			}
		}
  	});

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
	var mar_code_combo = new Ext.form.ComboBox({
		baseCls:'x-plain',
		xtype:'combo',
		maxHeight:120,
		width:120,
		hideLabel:true,
		displayField:'tiket_desc',
		valueField:'t_id',
		//readOnly:true,
		editable: true,
		queryMode:'local',
		triggerAction:'all',
		emptyText:'请选择站点',
		store:new Ext.data.JsonStore({
			fields:['t_id', 'tiket_code', 'tiket_desc', 'reward'],
			autoLoad: true,
			proxy: {
				type: 'ajax',
				url:"orders/query_tiket.do",
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
			  { header: "订单号",width: 50, sortable: true, dataIndex: 'order_id' },
			  { header: '支付时间', width: 60, sortable: true, dataIndex: 'order_pay_time',locked: true },
			  { header: '商品编号', width: 60, sortable: true, dataIndex: 'cs_sales_no',locked: true },
			  { header: '商品id', width: 100, sortable: true, dataIndex: 'c_id',locked: true },
			  { header: '商品名称', width: 120, sortable: true, dataIndex: 'c_name',locked: true },
//			  { header: "邮编", width: 80, dataIndex: 'contact_post_code', editor: 'textfield'},
			  { header: "选择优惠券", width: 100, sortable: true, dataIndex: 'tiket', editor:mar_code_combo},
			  { header: "优惠券编码", width: 100, sortable: true, dataIndex: 'tiket_no'},
			  { header: "单价", width: 80, dataIndex: 'cs_sale_price' },
			  { header: "数量", width: 60, dataIndex: 'order_cont_amount' },
			  { header: "扣除礼金", width: 60, dataIndex: 'tiket_reward' },
			  { header: "实际价", width: 80, dataIndex: 'order_price' },
			  { header: "渠道ID", width: 80, dataIndex: 'cs_channel_id' },
			  { header: "渠道名称", width: 80, dataIndex: 'cs_channel_name' },
			  { header: "渠道订单号",width: 80, sortable: true, dataIndex: 'channel_order_id' },
			  { header: "获得积分", width: 80, dataIndex: 'rewards' }, {
					header: '',
			        xtype: 'actioncolumn',
			        locked: true,
			        width: 150,
			        sortable: false,
			        menuDisabled: true,
			        align: 'center',
			        items: [/*' ', {
			        	iconCls: 'delete',
			        	tooltip: '删除商品',
			            handler: onDelete
			        }, ' ', */{
			        	iconCls: 'confirm_cont',
			        	tooltip: '确认商品',
			            handler: onAddBtn
			        }, ' ', {
			        	iconCls: 'confirm_order',
			        	tooltip: '生成订单',
			            handler: onAddToOrder
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
		if (e.field == 'tiket'){
			var price = e.record.get('cs_sale_price');
			var tiket_reward = 31;
			var tiket_no = "31000";
			
//			var tiket_id = editor.getValue();
//			for (var record in editor.store.getData())   {
//				if (record.get("t_id") == tiket_id){
//					tiket_reward = record.get("reward");
//					break;
//				}
//			}
			e.record.set('tiket_no', tiket_no);//礼金券金额
			e.record.set('tiket_reward', tiket_reward);//礼金券金额
			e.record.set('order_price', price-tiket_reward);
		}
		e.record.commit();
	});
//订单信息end************************************************************************************** 
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
  		//收货人	收货地址		本机电话	联系电话一	联系电话二	确认按钮
		fields: ['a_id','user_name','contact_addr','mobile_no','contact_no','contact_no_other'],
		//model: 'user_address_grid_store_model',
		proxy: {
			type: 'ajax',
			url: 'orders/query_addr.do?auth=1',
			reader: {
				totalProperty: "total",
				root: "data",
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
//  	var user_info_grid = new Ext.grid.GridPanel({
//		//title:'用户信息',
//	    store: user_address_grid_store,
//	    region: 'west',
//	    //width:560,
//	    height:150,
//	    //autoHeight: false,
//		autoScroll: true,
//	    frame:true,
//		//collapsible : true,
//		animate : true,
//		border : false,
//		titleCollapse : true,
//		loadMask:{
//			msg:'数据加载中...'
//		},
//	    //iconCls:'icon-grid',
//		bodyStyle:'padding:0 10 1 0',
//	    selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
//        viewConfig: {
//        	stripeRows: true,
//        	enableTextSelection:true
//        },
//	    columns: [//用户ID	用户姓名	电话号码	性别	出生年月日	会员级别	身份证号	收货地址	联系人	收货电话	是否默认地址	消费金额	ClientID	MACID
//			  { header: "用户ID",width: 80, sortable: true, dataIndex: 'mar_s_contId',locked: true},
//			  { header: '用户姓名', width: 100, sortable: true, dataIndex: 'mar_s_contName',locked: true },
//			  { header: '电话号码', width: 80, sortable: true, dataIndex: 'mar_s_contName',locked: true },
//			  { header: '性别', width: 80, sortable: true, dataIndex: 'mar_s_status' },
//			  { header: "出生年月日", width: 80, sortable: true, dataIndex: 'mar_s_contType' },
//			  { header: "会员级别", width: 80, sortable: true, dataIndex: 'mar_s_contProvider' },
//			  { header: "身份证号", width: 80, sortable: true, dataIndex: 'mar_s_contIntro' },
//			  { header: "收货地址", width: 80, dataIndex: 'cont_is_url_used' },
//			  { header: "联系人", width: 80, dataIndex: 'cont_is_url_used' },
//			  { header: "收货电话", width: 80, dataIndex: 'cont_is_url_used' },
//			  { header: "是否默认地址", width: 80, dataIndex: 'cont_is_url_used' },
//			  { header: "消费金额", width: 80, dataIndex: 'cont_is_url_used' },
//			  { header: "ClientID", width: 80, dataIndex: 'cont_is_url_used' },
//			  { header: "MACID", width: 80, dataIndex: 'cont_superscript' }, {
//					header: '',
//			        xtype: 'actioncolumn',
//			        locked: true,
//			        width: 100,
//			        sortable: false,
//			        menuDisabled: true,
//			        align: 'center',
//			        items: [' ', {
//			        	iconCls: 'edit',
//			        	tooltip: '编辑条目',
//			            handler: onModify
//			        }, ' ', {
//			        	iconCls: 'delete',
//			        	tooltip: '删除条目',
//			            handler: onDelete
//			        }]
//			    }
//		],
//      	bbar: new Ext.PagingToolbar({
//          	pageSize: pgSize,
//          	store: user_address_grid_store,
//          	displayInfo: true,
//          	displayMsg: '第 {0}-- {1}条  共 {2}条',
//			//autoScroll: true,
//          	emptyMsg: '没有记录' 
//      	})
//	});
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
	    columns: [//收货人	收货地址		本机电话	联系电话一	联系电话二	确认按钮
			  { header: '收货人', width: 100, sortable: true, dataIndex: 'user_name',locked: true },
			  { header: '收货地址', width: 80, sortable: true, dataIndex: 'contact_addr',locked: true },
			  { header: '本机电话', width: 80, sortable: true, dataIndex: 'mobile_no' },
			  { header: "联系电话一", width: 80, sortable: true, dataIndex: 'contact_no' },
			  { header: "联系电话二", width: 80, sortable: true, dataIndex: 'contact_no_other' },
			  { header: "备注", width: 80, sortable: true, dataIndex: 'memo' }
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
//通话信息begin************************************************************************************** 
  	var user_phone_call_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
		fields: ['c_id','user_id','user_name','call_from','call_to','call_time','call_in_time','call_off_time','call_length','phone_type','memo','oper_id'],
		proxy: {
			type: 'ajax',
			url: 'orders/query_call.do?auth=1',
			reader: {
				totalProperty: "total",
				root: "data",
				idProperty: 'c_id'
			},
			actionMethods:{
				read: 'POST'
			}
		}
  	});
//  	
//  	user_phone_call_grid_store.proxy.extraParams={
//    	contType: "",
//    	contProvider: "",
//    	keyWord: "",
//    	contStatus: contStatus,
//    	startTime: tmpStartTime,
//    	endTime: "",
//    	menuId:menuId_temp
//    }

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
		          { header: '主叫号码', width: 100, sortable: true, dataIndex: 'call_from',locked: true },
				  { header: "目标号码",width: 100, sortable: true, dataIndex: 'call_to',locked: true},
				  { header: "来电时间", width: 80, sortable: true, dataIndex: 'call_time' },
				  { header: "接通时间", width: 80, sortable: true, dataIndex: 'call_in_time' },
				  { header: "通话时长", width: 80, sortable: true, dataIndex: 'call_length' },
				  { header: "结束时间", width: 80, dataIndex: 'call_off_time' },
				  { header: "用户ID",width: 80, sortable: true, dataIndex: 'user_id',locked: true},
				  { header: '用户姓名', width: 80, sortable: true, dataIndex: 'user_name',locked: true },
				  { header: "用户类型", width: 80, dataIndex: 'isnew' },
				  { header: '客服号码', width: 80, sortable: true, dataIndex: 'oper_id' },
				  { header: "备注", width: 120, dataIndex: 'memo' }, {
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

    this.tbar = amr_menu_tree_tbar;
//    this.items = [user_phone_call_grid, cont_sales_grid, {
//		xtype: 'fieldset',
//		title: '订单信息',
//		collapsible: true,
//		defaultType: 'textfield',
//		fieldName: 'serviceinfo',
//		margin: '10, 2, 0, 0',
//		items: [user_order_grid]}, user_address_grid];
    this.items = [
                  {
    	xtype: 'fieldset',
    	title: '通话信息',
    	collapsible: true,
    	defaultType: 'textfield',
    	margin: '10, 2, 0, 0',
		border : false,
    	items: [user_phone_call_grid]
    }, 
    {
    	xtype: 'fieldset',
    	title: '订单信息(搜索商品信息)',
    	collapsible: true,
    	defaultType: 'textfield',
    	margin: '10, 2, 0, 0',
		border : false,
		items: [user_order_grid
		        ,{
			xtype: 'fieldset',
			title: '用户信息',
			collapsible: true,
			defaultType: 'textfield',
			margin: '10, 2, 0, 0',
			border : false,
			items: [user_address_grid]
		}
		]
    }];
	this.callParent();
    }
});