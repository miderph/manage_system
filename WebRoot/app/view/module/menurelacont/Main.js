Ext.define('app.view.module.menurelacont.Main', {
	extend: 'Ext.Container',
	requires: [
       'app.view.module.menurelacont.Controller',
       'app.view.module.menurelacont.Model'
    ],
    
    uses: ['app.view.module.menurelacont.Toolbar'],
    
	alias: 'widget.menuAssetsRelaPanel',
	
	title: '栏目管理',
	controller: 'menurelacont',
    viewModel: {
        type: 'menurelacont'
    },
	
	frame: true,
//	useArrows: true,
    rootVisible: false,
    columnLines: true,
    
    dockedItems: [{
        xtype: 'menurelacont-toolbar',
        dock: 'top'
    }],
	
	initComponent: function() {
		
		var mainViewModel = this.up('app-main').getViewModel();
		this.getController().mainViewModel = mainViewModel;
		
		if(mainViewModel.get('siteAllStore') == null) {
			mainViewModel.set('siteAllStore', Ext.create('app.store.SiteAllStore'));
		}
		
		if(mainViewModel.get('columnStatusStore') == null) {
			mainViewModel.set('columnStatusStore', Ext.create('app.store.ColumnStatusStore'));
		}
		
		if(mainViewModel.get('contProviderByAuthStore') == null) {
			mainViewModel.set('contProviderByAuthStore', Ext.create('app.store.ContProviderByAuthStore'));
		}
		
		if(mainViewModel.get('columnTypeStore') == null) {
			mainViewModel.set('columnTypeStore', Ext.create('app.store.ColumnTypeStore'));
		}
		
		if(mainViewModel.get('columnActTypeStore') == null) {
			mainViewModel.set('columnActTypeStore', Ext.create('app.store.ColumnActTypeStore'));
		}
		
		if(mainViewModel.get('columnResTypeStore') == null) {
			mainViewModel.set('columnResTypeStore', Ext.create('app.store.ColumnResTypeStore'));
		}
		if(mainViewModel.get('contTypeStore') == null) {
			mainViewModel.set('contTypeStore', Ext.create('app.store.ContTypeStore'));
		}
		if(mainViewModel.get('contStatusStore') == null) {
			mainViewModel.set('contStatusStore', Ext.create('app.store.ContStatusStore'));
		}
		if(mainViewModel.get('menuRelaContStore') == null) {
			mainViewModel.set('menuRelaContStore', Ext.create('app.store.MenuRelaContStore'));
		}
		if(mainViewModel.get('menuRelaMenuStore') == null) {
			mainViewModel.set('menuRelaMenuStore', Ext.create('app.store.ColumnStore'));
		}
		
        this.callParent();
	},
	items:[{
		xtype: 'Ext.tree.Panel',
		store: '{menuRelaMenuStore}',
		columns: [{
			xtype: 'treecolumn',
			header: '栏目名称',
			dataIndex: 'title',
			flex: 1,
			width: 200,
			locked: true
		}, {
			header: '栏目ID',
			dataIndex: 'c_id',
			width: 80
		}, {
			header: '状态',
			align: 'center',
			dataIndex: 'status',
			renderer: 'parseStatusV',
			width: 60
		}, {
			header: '子栏目数量',
			dataIndex: 'sub_count',
			align: 'center',
			width: 70
		}, {
			header: '排序号',
			dataIndex: 'order_num',
			align: 'center',
			width: 60
		}, {
			header: '快捷方式',
			align: 'center',
			dataIndex: 'shortcut_contname',
//			renderer: function(val) {
//				var str = '否';
//				if(val == 1) {
//					str = '内容链接';
//				} else if(val == 2) {
//					str = '栏目链接';
//				}
//				
//				return str
//			},
			width: 80
		}, {
			header: '链接至',
			align: 'center',
			dataIndex: 'shortcut_linktoname',
			width: 100
		}, {
			header: '网卡地址白名单',
			dataIndex: 'usergroup_names_mac',
			width: 120
		}, {
			header: '网卡地址黑名单',
			dataIndex: 'usergroup_names_mac2',
			width: 120
		}, {
			header: '地区白名单',
			dataIndex: 'usergroup_names_zone',
			width: 120
		}, {
			header: '地区黑名单',
			dataIndex: 'usergroup_names_zone2',
			width: 120
		}, {
			header: '型号白名单',
			dataIndex: 'usergroup_names_model',
			width: 120
		}, {
			header: '型号黑名单',
			dataIndex: 'usergroup_names_model2',
			width: 120
		}, {
			header: '渠道白名单',
			dataIndex: 'usergroup_names_channel',
			width: 120
		}, {
			header: '渠道黑名单',
			dataIndex: 'usergroup_names_channel2',
			width: 120
		}, {
			header: '栏目类型',
			dataIndex: 'struct_type',
			renderer: 'parseTypeV',
			width: 90
		}, {
			header: '栏目下资产类型',
			dataIndex: 'resource_type',
			renderer: 'parseResourceTypeV',
			width: 120
		}, {
			header: '提供商',
			dataIndex: 'provider_id',
			renderer: 'parseProviderV',
			width: 120
		}, {
			header: '栏目动作类型',
			dataIndex: 'act_type',
			renderer: 'parseActTypeV',
			width: 130
		}, {
			header: '栏目版本',
			dataIndex: 'version',
			width: 120
		}, {
			xtype: 'datecolumn',
			format: 'Y-m-d H:i:s',
			header: '生效时间',
			dataIndex: 'active_time',
			align: 'center',
			width: 136
		}, {
			xtype: 'datecolumn',
			format: 'Y-m-d H:i:s',
			header: '失效时间',
			dataIndex: 'deactive_time',
			align: 'center',
			width: 136
		}, {
			xtype: 'datecolumn',
			format: 'Y-m-d H:i:s',
			header: '创建时间',
			dataIndex: 'create_time',
			align: 'center',
			width: 136
		}, {
			xtype: 'datecolumn',
			format: 'Y-m-d H:i:s',
			header: '修改时间',
			dataIndex: 'modify_time',
			align: 'center',
			width: 136
		},{
			header: '自动编排规则编号',
			dataIndex: 'rule_ids',
			//hidden : true,
			width : 120
		}, {
	        xtype: 'actioncolumn',
	        locked: true,
	        width: 30,
	        sortable: false,
	        menuDisabled: true,
	        align: 'center',
	        items: [{
	        	iconCls:'preview',
	            tooltip: '预览图片',
	            handler: 'onPreviewClick'
	        }]
	    }],
	    
	    listeners: {
	    	itemclick: function(view, record, item, index, e, eOpts) {
	    		view.toggleOnDblClick = false;
	    		this.getViewModel().set('column_c_id', record.raw.c_id);
	    	}
	    },
		
		iconCls: 'icon-grid'
	},{
		xtype: 'Ext.Container',
        title:'栏目下的资产列表',
		items:[{
			xtype: 'Ext.grid.Panel',
			store: '{menuRelaContStore}',
			autoHeight: false,
			bbar: new Ext.PagingToolbar({
				pageSize: 100,
				store: '{menuRelaContStore}',
				displayInfo: true,
				displayMsg: '第 {0}-- {1}条    共 {2}条',
				autoScroll: true,
				emptyMsg: '没有记录' 
			}),
			columns: [
			          { header: "绑定ID", width: 40, dataIndex: 'mar_s_relaId', hidden: true },
			          { header: "锁定", width: 40, dataIndex: 'locked' },
			          { header: "ID", width: 80, dataIndex: 'mar_s_contId' },
			          { header: '名称', width: 100, dataIndex: 'mar_s_contName' },
			          { header: '状态', width: 60, sortable: true, dataIndex: 'mar_s_status' },
			          { header: "类型", width: 150, dataIndex: 'mar_s_contType' },
			          { header: "提供商", width: 80, dataIndex: 'mar_s_contProvider' },
			          { header: "描述", width: 80, dataIndex: 'mar_s_contIntro' },
			          { header: "图片类型", width: 80, dataIndex: 'is_url_used' },
			          { header: "默认图片类型", width: 80, dataIndex: 'cont_is_url_used' },
			          { header: "角标", width: 80, dataIndex: 'cont_superscript' },
			          { header: "编排时间", width: 120, sortable: true, dataIndex: 'mar_s_date_time' },
			          { header: "生效时间", width: 120, sortable: true, dataIndex: 'mar_s_active_time' },
			          { header: "失效时间", width: 120, sortable: true, dataIndex: 'mar_s_deactive_time' },
			          { header: "创建时间", width: 120, sortable: true, dataIndex: 'mar_s_create_time' },
			          { header: "修改时间", width: 120, sortable: true, dataIndex: 'mar_s_modify_time' }
			          ],
			          selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
			          loadMask:{msg:'数据加载中...'},
			          iconCls:'icon-grid',
			          tbar: [{
			        	  text: '预览',
			        	  handler : function(){
			        		  if(menuId_temp == '-1' || menuId_temp == '0') {
			        			  Ext.MessageBox.alert('警告', '请选择一个栏目，再进行预览!');
			        		  } else {
			        			  Ext.MessageBox.wait('处理中，请稍后...');
			        			  Ext.Ajax.request({
			        				  url:'relamenu/preview.do',
			        				  params:{
			        					  menuId: menuId_temp,
			        					  status: contMarFilterStatus
			        				  },
			        				  success:function(response) {
			        					  var result = Ext.decode(response.responseText);
			        					  if (!result.success){
			        						  Ext.MessageBox.show({
			        							  title:"错误提示",
			        							  msg:result.info,
			        							  width:200,
			        							  buttons:Ext.Msg.OK,
			        							  icon:Ext.Msg.ERROR
			        						  });
			        					  }
			        					  else{

			        						  var app_img_window = new Ext.Window({
			        							  html: result.cells,
			        							  title:'图片预览',
			        							  resizable:false,
			        							  width: 900,
			        							  height:400,
			        							  autoScroll: true,
			        							  modal:true,
			        							  closable:true,
			        							  closeAction:'hide',
			        							  buttons:[{
			        								  text:'取消',
			        								  handler:function(){
			        									  app_img_window.hide();
			        								  }
			        							  }]
			        						  });
			        						  app_img_window.show();
			        						  Ext.MessageBox.hide();
			        					  }
			        				  },
			        				  failure:function(){
			        					  Ext.MessageBox.hide();
			        					  Ext.MessageBox.show({
			        						  title:"提示",
			        						  msg:"操作失败",
			        						  width:110,
			        						  buttons:Ext.Msg.OK
			        					  });
			        				  }
			        			  });
			        		  }
			        	  } 
			          },{
			        	  text: '绑定',
			        	  handler : function(){
			        		  if(menuId_temp == '-1' || menuId_temp == '0') {
			        			  Ext.MessageBox.alert('警告', '请选择一个栏目，再进行绑定!');
			        		  } else {
			        			  var contType = mar_cont_type_form.getValue();
			        			  var contProvider = mar_cont_provider_form.getValue();
			        			  var contKeyword = Ext.String.trim(addAssetsToTheCatalogPanel_north.form.findField("keyword").getValue());

			        			  mar_cont_s_grid_store.on('beforeload',function(){        // =======翻页时 查询条件
			        				  mar_cont_s_grid_store.proxy.extraParams={
			        						  contType: contType,
			        						  contProvider: contProvider,
			        						  keyWord: contKeyword,
			        						  contStatus: contStatus,
			        						  startTime:addAssetsToTheCatalogPanel_north.form.findField("s_starttime").getValue(),
			        						  endTime:addAssetsToTheCatalogPanel_north.form.findField("s_endtime").getValue(),
			        						  menuId:menuId_temp
			        				  }
			        			  });
			        			  mar_cont_s_grid_store.load({                                      // =======翻页时分页参数
			        				  params:{
			        					  start: 0,
			        					  limit: pgSize
			        				  }
			        			  });
			        			  mar_menu_s_grid_store.proxy.extraParams={
			        					  parent_id:menuId_temp
			        			  };

			        			  mar_menu_s_grid_store.load({                                    // =======翻页时分页参数
			        				  params:{
			        					  start: 0,
			        					  limit: pgSize
			        				  }
			        			  });
			        			  bindingContWindows.show();
			        		  }

			        	  } 
			          },//**************************************************************************************************************************
			          {
			        	  text: '解绑',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条记录，进行删除!'); 
			        		  }else{ 
			        			  var ids = '';
			        			  Ext.MessageBox.confirm('提示框', '您确定要进行解绑操作？',function(btn){ 
			        				  if(btn=='yes'){ 
			        					  if(rows){ 
			        						  for (var i = 0; i < rows.length; i++) { 
			        							  ids = ids + rows[i].get('mar_s_contId') + ',';
			        						  }

			        						  if(ids.length>0) {
			        							  ids = ids.substring(0, ids.length-1);
			        						  }

			        						  Ext.Ajax.request({
			        							  url:'relamenu/delete_rela_menu_cont.do',
			        							  params:{
			        								  ids:ids,
			        								  menuId:menuId_temp
			        							  },
			        							  success:function(response) {
			        								  Utils.checkStorePageNo(mar_cont_m_grid_store, rows.length);
			        								  mar_cont_m_grid_store.load();
			        								  //mar_cont_s_grid_store.load();
			        								  //mar_menu_s_grid_store.load();
			        								  Ext.Msg.alert('Success', '解绑成功');
			        							  },
			        							  failure:function(){
			        								  Ext.MessageBox.show({
			        									  title:"提示",
			        									  msg:"保存失败",
			        									  width:110,
			        									  buttons:Ext.Msg.OK
			        								  });
			        							  }
			        						  });

			        					  } 
			        				  } 
			        			  }); 
			        		  }                // 弹出对话框警告 
			        	  }
			          },{text: ' | ',disabled:true},//**************************************************************************************************************************
			          {
			        	  text: '锁定',
			        	  handler : function(){
			        		  lockRelaCont("1");
			        	  }
			          },{
			        	  text: '解锁',
			        	  handler : function(){
			        		  lockRelaCont("0");
			        	  }
			          },{text: ' | ',disabled:true},//**************************************************************************************************************************
			          {
			        	  text: '上移',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条数据进行操作!'); 
			        		  } else if(rows.length==1){
			        			  var id = rows[0].get('mar_s_contId');
			        			  selectedId = id;

			        			  Ext.Ajax.request({
			        				  url:'relamenu/modify_order.do',
			        				  params:{
			        					  tag:'up',
			        					  id:id,
			        					  menuId:menuId_temp,
			        					  status:contMarFilterStatus
			        				  },
			        				  success:function(response) {
			        					  mar_cont_m_grid_store.reload();
			        					  //mar_cont_s_grid_store.load();
			        					  //Ext.Msg.alert('Success', '操作成功');
			        				  },
			        				  failure:function(){
			        					  Ext.MessageBox.show({
			        						  title:"提示",
			        						  msg:"操作失败",
			        						  width:110,
			        						  buttons:Ext.Msg.OK
			        					  });
			        				  }
			        			  });
			        		  } else {
			        			  Ext.MessageBox.alert('警告', '只能对一条数据进行操作!');
			        		  }
			        	  } 
			          }, {
			        	  text: '下移',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条数据进行操作!'); 
			        		  } else if(rows.length==1){
			        			  var id = rows[0].get('mar_s_contId');
			        			  selectedId = id;

			        			  Ext.Ajax.request({
			        				  url:'relamenu/modify_order.do',
			        				  params:{
			        					  tag:'down',
			        					  id:id,
			        					  menuId:menuId_temp,
			        					  status:contMarFilterStatus
			        				  },
			        				  success:function(response) {
			        					  mar_cont_m_grid_store.reload();
			        					  //mar_cont_s_grid_store.load();
			        					  //Ext.Msg.alert('Success', '操作成功');
			        				  },
			        				  failure:function(){
			        					  Ext.MessageBox.show({
			        						  title:"提示",
			        						  msg:"操作失败",
			        						  width:110,
			        						  buttons:Ext.Msg.OK
			        					  });
			        				  }
			        			  });
			        		  }  else {
			        			  Ext.MessageBox.alert('警告', '只能对一条数据进行操作!');
			        		  }
			        	  } 
			          },
			          {
			        	  text: '置顶',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条数据进行操作!'); 
			        		  } else if(rows.length==1){
			        			  var id = rows[0].get('mar_s_contId');
			        			  selectedId = id;

			        			  Ext.Ajax.request({
			        				  url:'relamenu/modify_order.do',
			        				  params:{
			        					  tag:'top',
			        					  id:id,
			        					  menuId:menuId_temp,
			        					  status:contMarFilterStatus
			        				  },
			        				  success:function(response) {
			        					  mar_cont_m_grid_store.reload();
			        					  //mar_cont_s_grid_store.load();
			        					  //Ext.Msg.alert('Success', '操作成功');
			        				  },
			        				  failure:function(){
			        					  Ext.MessageBox.show({
			        						  title:"提示",
			        						  msg:"操作失败",
			        						  width:110,
			        						  buttons:Ext.Msg.OK
			        					  });
			        				  }
			        			  });
			        		  }  else {
			        			  Ext.MessageBox.alert('警告', '只能对一条数据进行操作!');
			        		  }
			        	  } 
			          }, {
			        	  text: '置底',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条数据进行操作!'); 
			        		  } else if(rows.length==1){
			        			  var id = rows[0].get('mar_s_contId');
			        			  selectedId = id;

			        			  Ext.Ajax.request({
			        				  url:'relamenu/modify_order.do',
			        				  params:{
			        					  tag:'bottom',
			        					  id:id,
			        					  menuId:menuId_temp,
			        					  status:contMarFilterStatus
			        				  },
			        				  success:function(response) {
			        					  mar_cont_m_grid_store.reload();
			        					  //mar_cont_s_grid_store.load();
			        					  //Ext.Msg.alert('Success', '操作成功');
			        				  },
			        				  failure:function(){
			        					  Ext.MessageBox.show({
			        						  title:"提示",
			        						  msg:"操作失败",
			        						  width:110,
			        						  buttons:Ext.Msg.OK
			        					  });
			        				  }
			        			  });
			        		  }  else {
			        			  Ext.MessageBox.alert('警告', '只能对一条数据进行操作!');
			        		  }
			        	  } 
			          },{text: ' | ',disabled:true},
			          {
			        	  text: '图片设置',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条记录，进行设置!'); 
			        		  }else{ 
			        			  //Ext.MessageBox.confirm('提示框', '您确定要进行图片设置操作？',function(btn){ 
			        			  //	if(btn=='yes'){ 
			        			  eids="";
			        			  var is_url_used_id = "";
			        			  var cont_is_url_used_id = "";
			        			  if(rows){ 
			        				  for (var i = 0; i < rows.length; i++) { 
			        					  eids = eids + rows[i].get('mar_s_contId') + ',';
			        					  if("" == is_url_used_id && "" != rows[i].get('is_url_used_id')) {
			        						  is_url_used_id = rows[i].get('is_url_used_id');
			        					  }
			        					  if("" == cont_is_url_used_id && "" != rows[i].get('cont_is_url_used_id')) {
			        						  cont_is_url_used_id = rows[i].get('cont_is_url_used_id');
			        					  }
			        				  }
			        				  if(eids.length>0) {
			        					  eids = eids.substring(0, eids.length-1);
			        				  }
			        				  if("" == is_url_used_id) {
			        					  is_url_used_id = cont_is_url_used_id;
			        				  }

			        				  if("" == is_url_used_id) {
			        					  is_url_used_id = -1;
			        				  }

			        				  comboBox.setValue(is_url_used_id);
			        				  copyWindows.show();
			        			  } 
			        			  //	}
			        			  //}); 
			        		  }                // 弹出对话框警告 
			        	  } 
			          },//**************************************************************************************************************************
			          {
			        	  text: '设角标',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条数据进行操作!'); 
			        		  }else{
			        			  var ids="";
			        			  for (var i = 0; i < rows.length; i++) { 
			        				  ids = ids + rows[i].get('mar_s_contId') + ',';
			        			  }
			        			  if(ids.length>0) {
			        				  ids = ids.substring(0, ids.length-1);
			        			  }
			        			  superscriptUtil.showSuperscriptWindows('角标内容选择',ids);
			        		  }
			        	  } 
			          },{
			        	  text: '删角标',
			        	  handler : function(){
			        		  var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
			        		  if(rows.length==0){ 
			        			  Ext.MessageBox.alert('警告', '最少选择一条数据进行操作!'); 
			        		  }else{
			        			  var ids="";
			        			  for (var i = 0; i < rows.length; i++) { 
			        				  ids = ids + rows[i].get('mar_s_contId') + ',';
			        			  }
			        			  if(ids.length>0) {
			        				  ids = ids.substring(0, ids.length-1);
			        			  }
			        			  Ext.MessageBox.confirm('提示框', '您确定要进行该操作？',function(btn){
			        				  if(btn=='yes'){
			        					  Ext.Ajax.request({
			        						  url:'contvideo/delete_cont_superscript.do?ids=' + ids,
			        						  waitMsg:'正在提交,请稍等',
			        						  success:function(response) {
			        							  var result = Ext.decode(response.responseText);
			        							  if(result.success){
			        								  mar_cont_m_grid_store.load();
			        								  //mar_cont_s_grid_store.load();
			        								  Ext.MessageBox.show({
			        									  title:"提示",
			        									  msg:"删除角标成功",
			        									  width:110,
			        									  buttons:Ext.Msg.OK
			        								  });
			        							  }else{
			        								  Ext.MessageBox.show({
			        									  title:"提示",
			        									  msg:"删除角标失败!",
			        									  width:110,
			        									  buttons:Ext.Msg.OK
			        								  });
			        							  }
			        						  },
			        						  failure:function() {
			        							  Ext.Msg.show({
			        								  title:'错误提示',
			        								  msg:'删除角标失败!',
			        								  width:110,
			        								  buttons:Ext.Msg.OK
			        							  });
			        						  }
			        					  });
			        				  }
			        			  });
			        		  }
			        	  } 
			          }]
		}]
	}]

});