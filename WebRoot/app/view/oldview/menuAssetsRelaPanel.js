Ext.define('app.view.oldview.menuAssetsRelaPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.menuAssetsRelaPanel',
    layout:'column',
	frame: true,
    columnLines: true,
	iconCls: 'icon-grid',
	
    initComponent: function() {
    	var thisWindow = this;
	var eids = '';
	var widthSize = document.body.clientWidth-150;
	var heightSize = document.body.clientHeight-53;
	
	var pgSize = 100;  //分页数
    var tmpStartTime = "2010-01-01"; //默认开始时间
	var menuId_temp = "-1";
	var menuName_temp = "";
  	var contStatus = "-10000";
  	var contMarFilterStatusType = "1";//1 有效，-1 无效，0 其他状态
  	var contMarFilterStatus = "-10000";
  	var selectedId = "0";
//************************设置角标弹出窗体***********************************************
	var superscriptUtil={

	showSuperscriptWindows : function (title,ids) {
	var superscriptPgSize = 100;  //分页数

	var superscript_cont_s_grid_store = new Ext.data.Store({
  		pageSize:superscriptPgSize,
		fields: ['mar_s_contId','mar_s_contName','mar_s_contType','mar_s_contProvider','mar_s_contIntro'],
		proxy: {
			type: 'ajax',
			url: 'contvideo/query_superscript.do',
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'mar_s_contId'
			}
		}
  	});
	
	superscript_cont_s_grid_store.load({                                      // =======翻页时分页参数
  		params:{
        	start: 0,
        	limit: superscriptPgSize
		}
	});
	
	//var superscript_cont_s_grid_sm = new Ext.grid.CheckboxSelectionModel();
   
	var superscript_cont_grid = new Ext.grid.GridPanel({
	    store: superscript_cont_s_grid_store,
	    autoHeight: false,
	    columns: [
	        { header: "ID",width: 90, sortable: true, dataIndex: 'mar_s_contId' },
	        { header: '名称', width: 130, sortable: true, dataIndex: 'mar_s_contName' },
	        { header: "类型", width: 110, sortable: true, dataIndex: 'mar_s_contType' },
	        { header: "提供商", width: 110, sortable: true, dataIndex: 'mar_s_contProvider' },
	        { header: "描述", width: 180, sortable: true, dataIndex: 'mar_s_contIntro' }
	    ],
	    selModel: {selType: 'checkboxmodel',mode: 'SINGLE'},
	    width:720,
	    height:350,
	    frame:true,
		loadMask:{
			msg:'数据加载中...'
		},
	    title:'角标列表',
	    iconCls:'icon-grid',
      	bbar: new Ext.PagingToolbar({
          	pageSize: superscriptPgSize,
          	width: 720,
          	store: superscript_cont_s_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条    共 {2}条',
          	emptyMsg: '没有记录' 
      	})
	});
	
	
	var setsuperscriptPanel = new Ext.FormPanel({
			bodyStyle:'padding:0 0 0 5',
			width:730,
			//layout:'form',
			//height:365,
			items: superscript_cont_grid
	});
	  var setsuperscriptwinform = new Ext.FormPanel({    
	                border : true,    
	                frame : true,
					fieldDefaults: {
						labelAlign : "right"
					},
	                buttonAlign : 'right',    
	                layout : 'column',    
	                width : 780, 
					height: 540,
	                items : setsuperscriptPanel ,      
	                buttons : [{    
	                    xtype : 'button',    
	                    text : '确定',    
	                    handler : function() {     
                            //Ext.Msg.alert("消息", "选中状态的id为:" + id.toString() + name.toString());
	                        var rows=superscript_cont_grid.getSelectionModel().getSelection();
	                        var contId='';
	                        if(!(rows.length==1)){ 
								Ext.MessageBox.confirm('警告', '请选择一条记录!,不选、多选默认为空',function(btn){ 
								if(btn=='yes'){ 
									if(rows){
										 submintsuperscript();
									} 
								}
							});  
							}else{ 
								Ext.MessageBox.confirm('提示框', '您确定选择该条记录？',function(btn){ 
								if(btn=='yes'){ 
									if(rows){
										contId = rows[0].get('mar_s_contId');
										submintsuperscript();
								} 
								}
							}); 
							}
	                        function submintsuperscript(){
	                        	Ext.Ajax.request({
										url:'contvideo/superscript_cont_video.do',
										params:{
											ids:ids,
											contId:contId
										},
										success:function(response) {
											mar_cont_m_grid_store.load();
											//mar_cont_s_grid_store.load();
										    //mar_menu_s_grid_store.load();
									        var result = Ext.decode(response.responseText);
									        if(result.success){
										        Ext.MessageBox.show({
													title:"提示",
													msg:"设置角标成功",
													width:110,
													buttons:Ext.Msg.OK
												});
											}else{
												Ext.MessageBox.show({
												title:"提示",
												msg:"设置角标失败",
												width:110,
												buttons:Ext.Msg.OK
											});
											}
										},
										failure:function(){
											Ext.MessageBox.show({
												title:"提示",
												msg:"设置角标失败",
												width:110,
												buttons:Ext.Msg.OK
											});
										}
									});
										
										setsuperscriptWindows.destroy();
	                        }
	                    }    
	   
	                }, {    
	                    xtype : 'button',    
	                    text : '取消',    
	                    handler : function() {    
	                    setsuperscriptWindows.destroy();    
               			}    
		}]    
	});    
	 var setsuperscriptWindows = new Ext.Window({    
	                modal : true,    
	                layout : 'fit',    
	                title : '选择角标',    
	                width : 780,    
	                height : 540,    
	                plain : true,    
	                items : [setsuperscriptwinform]    
	            });
	 setsuperscriptWindows.show();
	 }
	}
//栏目混排begin**************************************************************************************
  	var mar_menu_s_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
  		fields: ['id','text','menu_task_structId','qtip'],
		autoLoad: true,
      	proxy: {
			type: 'ajax',
			url: 'menus/query_menu_for_mar.do?auth=1&site_id=-1',
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'id'
			},
			extraParams: {
				parent_id : menuId_temp
			}
		},
	    listeners: {
	    	load: function(self, records, successful, operation, eOpts ) {
	           console.log("currentPage="+self.currentPage+",records=" + records.length);
	           if (records.length == 0 && self.currentPage>1){//解决最后一页在删除记录后会出现空白列表的问题
	        	   self.currentPage = 1;
	        	   self.load();
	           }
	    	}
	    }
		
  	});
	  	
	var mar_menu_s_grid = new Ext.grid.GridPanel({
	    store: mar_menu_s_grid_store,
	    autoHeight: false,
	    region: 'center',
	    //width:300,
	    height:200,
	    frame:true,
		loadMask:{
		  	msg:'数据加载中...'
		},
	    title:'要混排的栏目列表',
	    iconCls:'icon-grid',
		bodyStyle:'padding:0 10 1 0',
	    selModel: {selType:'checkboxmodel',mode : 'SIMPLE'},
	    columns:[
	             { header: "ID",width: 60, sortable: true, dataIndex: 'id' },
	             { header: '名称', width: 100, sortable: true, dataIndex: 'text' },
	             { header: "栏目结构类型id", width: 80, sortable: true, dataIndex: 'menu_task_structId',hidden:true },
	             { header: "栏目结构类型", width: 80, sortable: true, dataIndex: 'qtip' }
	    ],
      	bbar: new Ext.PagingToolbar({
          	pageSize: pgSize,
          	//width: 318,
          	store: mar_menu_s_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条    共 {2}条',
          	emptyMsg: '没有记录' 
      	})
	});
//栏目混排end************************************************************************************** 

//************************************************************************************** 
  	var mar_cont_s_grid_store = new Ext.data.Store({
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
		},
	    listeners: {
	    	load: function(self, records, successful, operation, eOpts ) {
	           console.log("currentPage="+self.currentPage+",records=" + records.length);
	           if (records.length == 0 && self.currentPage>1){//解决最后一页在删除记录后会出现空白列表的问题
	        	   self.currentPage = 1;
	        	   self.load();
	           }
	    	}
	    }
  	});
  	
  	mar_cont_s_grid_store.proxy.extraParams={
    	contType: "",
    	contProvider: "",
    	keyWord: "",
    	contStatus: contStatus,
    	startTime: tmpStartTime,
    	endTime: "",
    	menuId:menuId_temp
    }
   	/*
	mar_cont_s_grid_store.load({                                      // =======翻页时分页参数
  		params:{
        	start: 0,
        	limit: pgSize
		}
	});*/
  	
  	//var mar_cont_s_grid_sm = new Ext.grid.CheckboxSelectionModel();
   
	var mar_cont_s_grid = new Ext.grid.GridPanel({
	    store: mar_cont_s_grid_store,
	    region: 'west',
	    width:560,
	    height:365,
	    //autoHeight: false,
		autoScroll: true,
	    frame:true,
		loadMask:{
			msg:'数据加载中...'
		},
	    title:'要编排的资产列表',
	    iconCls:'icon-grid',
		bodyStyle:'padding:0 10 1 0',
	    selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
	    columns: [
			  { header: "ID",width: 60, sortable: true, dataIndex: 'mar_s_contId' },
			  { header: '名称', width: 100, sortable: true, dataIndex: 'mar_s_contName' },
			  { header: '状态', width: 60, sortable: true, dataIndex: 'mar_s_status' },
			  { header: "类型", width: 150, sortable: true, dataIndex: 'mar_s_contType' },
			  { header: "提供商", width: 80, sortable: true, dataIndex: 'mar_s_contProvider' },
			  { header: "描述", width: 80, sortable: true, dataIndex: 'mar_s_contIntro' },
			  { header: "默认图片类型", width: 80, dataIndex: 'cont_is_url_used' },
			  { header: "角标", width: 80, dataIndex: 'cont_superscript' }
		],
      	bbar: new Ext.PagingToolbar({
          	pageSize: pgSize,
          	store: mar_cont_s_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条  共 {2}条',
			//autoScroll: true,
          	emptyMsg: '没有记录' 
      	})
	});
    function checkSelected(){
    	if (selectedId > 0){
    	   var model = mar_cont_m_grid_panel.getSelectionModel();
    	   var store = mar_cont_m_grid_panel.store;
    	   var iCount = store.getCount();
			for (var ii = 0; ii < iCount; ii++){
				var id = store.getAt(ii).get("mar_s_contId");
				if (id==selectedId){
					model.selectRange(ii,ii,true);
					//window.alert("found:contid=" + selectedId);
					break;
				}
			}
    	}
    }
  	var mar_cont_m_grid_store = new Ext.data.Store({
  		pageSize:pgSize,
      	fields: ['mar_s_relaId','mar_s_contId','mar_s_status','mar_s_status_valid','mar_s_contName','mar_s_contType','mar_s_contProvider','mar_s_date_time',
      	    'mar_s_active_time','mar_s_deactive_time','mar_s_create_time','mar_s_modify_time',
			'mar_s_contIntro','locked','is_url_used','cont_is_url_used','cont_superscript','is_url_used_id','cont_is_url_used_id'],
		autoLoad: false,
		proxy: {
			type: 'ajax',
			url: 'relamenu/query_cont.do',
			reader: {
				totalProperty: "results",
				root: "datastr",
				idProperty: 'mar_s_contId'
			},
			extraParams: {
				menuId: -1
			}
		},
  		pruneModifiedRecords:true,
  		listeners :{
	    	load: function(self, records, successful, operation, eOpts ) {
  				checkSelected();
  				console.log("currentPage="+self.currentPage+",records=" + records.length);
  				if (records.length == 0 && self.currentPage>1){//解决最后一页在删除记录后会出现空白列表的问题
  					self.currentPage = 1;
  					self.load();
  				}
  			}
  		}
  	});
  	
  
  	var comboData=[
  		['-1','请选择'],
		['0','竖图'],
		['1','横图'],
		['2','小方图'],
		['3','四格图'],
		['4','六格图']
		];
   var comboStore=new Ext.data.SimpleStore({
		data:comboData,
		fields:['value','text']
	});
   var comboBox=new Ext.form.ComboBox({
		store:comboStore,
		triggerAction:'all',
		displayField:'text',
		valueField:'value',
		queryMode:'local',
		//readOnly:true,
		editable: false,
		fieldLabel: '图片类型',
		emptyText:'请选择图类型'
		});
   
   var copyWindows = new Ext.Window({
            title : '设置图片类型',   
            bodyStyle:'padding:10 10 10 10',
            //id:'copyWindows',
//          width : 420,
//          height : 520,
            plain : true,
            modal: true,
            resizable: false,
			closable: true,
            draggable: false,
            autoDestroy: true, 
            closeAction : 'hide',  // 关闭窗口  
            maximizable : false,  // 最大化控制 值为true时可以最大化窗体   
            //layout:'form',
            items:[comboBox],
            buttons: [{
                text:'提交',
                handler: copyInfo
            }, {    
	            xtype : 'button',    
	            text : '取消',    
	            handler : function() {
	            	copyWindows.close();
	   			}    
	        }]
       })
    //设置角标提交函数
	function copyInfo(btn) 
     {
	    var is_url = comboBox.getValue()
	   	copyWindows.hide();
    	Ext.Ajax.request({
			url:'relamenu/is_url_used_rela_menu_cont.do',
			params:{
				ids:eids,
				comboBox:is_url,
				menuId:menuId_temp
			},
			success:function(response, options) {
				var txt=Ext.decode(response.responseText);
				if(txt.success){
					mar_cont_m_grid_store.load();
				    //mar_cont_s_grid_store.load();
					Ext.Msg.alert('Success', '设置成功');
				}else{
					Ext.Msg.alert('Failed', '设置失败');
				}
//				copyWindows.hide();
//				mar_cont_m_grid_store.reload();
//			    mar_cont_s_grid_store.reload();
//				Ext.Msg.alert('Success', '设置成功');
			},
			failure:function(){
//				copyWindows.hide();
				Ext.MessageBox.show({
					title:"提示",
					msg:"保存失败",
					width:110,
					buttons:Ext.Msg.OK
				});
			}
	});
    }
   
	var mar_cont_typeStore = new Ext.data.JsonStore({
		
		
		fields:['s_id', 's_name'],
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'status/query_for_conttype.do',
			reader: {
				root:'data'
			}
		}
	});
	
	var mar_cont_type_form = new Ext.form.ComboBox({
		fieldLabel: '资产类型',
		emptyText: '请选择资产类型...',
		baseCls:'x-plain',
		xtype:'combo',
        hiddenName: 'contTypeId',
        //width:120,
		//maxHeight:180,
		hideLabel:false,
		displayField:'s_name',
		valueField:'s_id',
		//readOnly:true,
		editable: false,
		queryMode:'local',
		triggerAction:'all',
		value:'-10000',
		store:mar_cont_typeStore
	});
	var cont_video_statusStore = new Ext.data.JsonStore({
		
		
		fields:['s_id', 's_name'],
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'status/query_for_cont.do',
			reader: {
				root:'data'
			}
		}
	});
	var mar_cont_video_status_form = new Ext.form.ComboBox({
		fieldLabel: '资产状态',
		emptyText: '请选择资产状态...',
		baseCls:'x-plain',
		xtype:'combo',
        hiddenName: 'contStatusId',
        //width:120,
		//maxHeight:180,
		hideLabel:false,
		allowBlank:true,
		disabled:false,
		displayField:'s_name',
		valueField:'s_id',
		//readOnly:true,
		editable: false,
		queryMode:'local',
		triggerAction:'all',
		value:'-10000',
		store:cont_video_statusStore
	});
	mar_cont_video_status_form.on('select', function() {
		contStatus = this.getValue();
	});
	var mar_cont_providerStore = new Ext.data.JsonStore({
		
		fields:['providerId', 'providerName'],
		autoLoad: true,
		proxy: {
			type: 'ajax',
			url: 'menus/provider_menu.do?siteId=-1&auth=1',
			reader: {
				root:'provider_data'
			}
		}
	});
	
	var mar_cont_provider_form = new Ext.form.ComboBox({
		fieldLabel: '提供商',
		emptyText: '请选择资产提供商...',
		baseCls:'x-plain',
		xtype:'combo',
        hiddenName: 'providerId',
        //width:120,
		//maxHeight:180,
		hideLabel:false,
		displayField:'providerName',
		valueField:'providerId',
		//readOnly:true,
		editable:false,
		queryMode:'local',
		triggerAction:'all',
		value:'-10000',
		store:mar_cont_providerStore
	});

	var addAssetsToTheCatalogPanel_north = new Ext.FormPanel({
		title:'查询条件',
		region:'north',
		frame: true,
		collapsible: true,
		titleCollapse: true,
		border: true,
		//width: widthSize-90,
		autoHeight: true,
		autoScroll: false,
		fieldDefaults: {
			labelAlign:'right'
		},
		defaults: {
			baseCls: 'x-plain'
		},
		items:[{
			//xtype:'panel',
			layout:'column',
			border: false,
			items:[{
				columnWidth:.5,
				//layout:'form',
				border: false,
				baseCls:'x-plain',
				bodyStyle:'padding:6 0 0 0',
				items:[mar_cont_provider_form]
			},{
				columnWidth:.5,
				//layout:'form',
				border: false,
				baseCls:'x-plain',
				bodyStyle:'padding:6 0 0 0',
				items:[mar_cont_type_form]
			}]
		},{
			//xtype:'panel',
			layout:'column',
			border: false,
			items:[{
				columnWidth:.5,		
				//layout:'form',
				border:false,
				baseCls:'x-plain',
				//bodyStyle:'padding:6 0 0 0',
				items: [mar_cont_video_status_form]
			    },{
				columnWidth:.5,
				//layout:'form',
				border:false,
				baseCls:'x-plain',
				//bodyStyle:'padding:6 0 0 0',
				items: [{
					xtype: 'textfield', 
					//id: 'keyword',
					name: 'keyword', 
					fieldLabel: '关键字',
					maxLength: 15,
					maxLengthText: '不能超过15个字符'
				}]
			}]
		},{
			//xtype:'panel',
			layout:'column',
			border: false,
			items:[{
				columnWidth:.5,
				//layout:'form',
				border:false,
				baseCls:'x-plain',
				//bodyStyle:'padding:6 0 0 0',
				items: [{
					xtype: 'datefield', 
					format: 'Y-m-d', 
					//id: 's_starttime',
					name: 's_starttime',
					//width: 255,
					fieldLabel: '创建时间', 
					allowBlank: false, 			          
					//grow: true,
					blankText: '请选择开始时间',
					value:  '2010-01-01',
					maxValue: new Date()
				}]
			},{
				columnWidth:.4,
				//layout:'form',
				border:false,
				baseCls:'x-plain',
				//bodyStyle:'padding:6 0 0 0',
				items:[{
					xtype: 'datefield', 
					format: 'Y-m-d', 
					//id: 's_endtime',
					//width: 255,
					name: 's_endtime', 
					fieldLabel: '至', 
					allowBlank: false,
					//grow: true,
					blankText: '请选择结束时间',
					value: new  Date()
				}]
			},{
				columnWidth:.1,
				//layout:'form',
				border:false,
				baseCls:'x-plain',
				//bodyStyle:'padding:6 0 0 0',
				items:[{
					xtype: 'button', 
					text: '搜索',
					//url: 'login.jsp',
					handler: function(){
						if(addAssetsToTheCatalogPanel_north.getForm().isValid()){
							
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
							
						}
					}
				}]
		      }]
		}]
	});
	
//	var addAssetsToTheCatalogPanel_menutree = new Ext.FormPanel({
//			bodyStyle:'padding:0 0 0 5;background-color:DFE9F6',
//			//layout:'form',
//			border:false,
//			/*tbar: [mar_site_combo, mar_site_name_form, {
//				text: '搜索',
//				style:'background-color:#99BBE8',
//				handler: mar_site_search,
//				//scope: this,
//				width:40
//			}],*/
//			items: [{
//            	baseCls:'x-plain',
//				border: false,
//				bodyStyle:'padding:6 0 0 0',
//            	items:[amr_menu_tree]
//            }]
//	});
	
	var bindingContPanel = new Ext.FormPanel({    
        border : true,    
        frame : true,
		fieldDefaults: {
			labelAlign : "right"
		},
        buttonAlign : 'right',    
        layout : 'border',    
        width : 780, 
		height: 540,
        items : [addAssetsToTheCatalogPanel_north, mar_cont_s_grid, mar_menu_s_grid] ,      
        buttons : [{    
            xtype : 'button',    
            text : '提交',
            handler : function() {
				var gridselectrows = mar_cont_s_grid.getSelectionModel().getSelection();
				var menuGridselectrows = mar_menu_s_grid.getSelectionModel().getSelection();
				if(gridselectrows.length<=0 && menuGridselectrows.length<=0) {
					Ext.MessageBox.alert('提示', '至少中一个资产或栏目');
				} else {
				Ext.MessageBox.confirm('提示框', '您确定要绑定到栏目['+menuName_temp+']？',function(btn){ 
					if(btn=='yes'){ 
						var ids = "";//资产
						for(var i=0; i<gridselectrows.length; i++){
							ids = ids + gridselectrows[i].get('mar_s_contId') + ',';
						}
						ids = ids.substring(0, ids.length-1);
						
						var m_ids = "";//栏目
						for(var i=0; i<menuGridselectrows.length; i++){
							m_ids = m_ids + menuGridselectrows[i].get('id') + ',';
						}
						im_ids = m_ids.substring(0, m_ids.length-1);
						
						if(menuId_temp == '-1' || menuId_temp == '0') {
							return null;
						}
						
						Ext.Ajax.request({
							url:'relamenu/save_rela_menu_cont.do',
							params:{
								ids:ids,
								m_ids:m_ids,
								menuId:menuId_temp
							},
							success:function(response) {
								var result = Ext.decode(response.responseText);
								if (result.success) {
									mar_cont_m_grid_store.load();
									Utils.checkStorePageNo(mar_cont_s_grid_store, gridselectrows.length);

									contMarFilterStatusType = status_type_for_cont.getValue();
							    	contMarFilterStatus = status_combo_for_cont.getValue();
							    	mar_cont_m_grid_store.load({                                      // =======翻页时分页参数
							    		params:{
							    			start: 0,
							    			limit: pgSize,
							    			status_type : contMarFilterStatusType,
							    			status : contMarFilterStatus
							    		}
							    	});
									//Utils.checkStorePageNo(mar_menu_s_grid_store, menuGridselectrows.length);
									mar_menu_s_grid_store.load();
									Ext.Msg.alert('Success', '绑定成功');
								} else {
									Ext.MessageBox.show({
												title : "错误提示",
												msg : result.info,
												width : 110,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
								}
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
					}});
				}
				
				return null;
            }    
	
	        }, {    
	            xtype : 'button',    
	            text : '关闭',    
	            handler : function() {    
	            	bindingContWindows.close();  
	   			}    
	        }]    
	});
	
	var bindingContWindows = new Ext.Window({    
        modal : true,    
        layout : 'fit',    
        title : '绑定资产',
        width: widthSize*0.7,
		height:heightSize*0.7,  
        plain : true,
        closeAction: 'hide',
        items : [bindingContPanel]    
    });
	//setsuperscriptWindows.destroy();
	function lockRelaCont(locked){
		var rows = mar_cont_m_grid_panel.getSelectionModel().getSelection();// 返回值为 Record 数组 
		if(rows.length==0){ 
			Ext.MessageBox.alert('警告', '最少选择一条记录，进行锁定!'); 
		}else{ 
			var ids = '';
			var oper = "锁定";
			if (locked == 0){
				oper = "解锁";
			}
				
			Ext.MessageBox.confirm('提示框', '您确定要进行'+oper+'操作？',function(btn){ 
				if(btn=='yes'){ 
					if(rows){ 
						for (var i = 0; i < rows.length; i++) { 
							ids = ids + rows[i].get('mar_s_contId') + ',';
						}

						if(ids.length>0) {
							ids = ids.substring(0, ids.length-1);
						}

						Ext.Ajax.request({
							url:'relamenu/lock_rela_menu_cont.do',
							params:{
								locked:locked,
								ids:ids,
								menuId:menuId_temp
							},
							success:function(response) {
								Utils.checkStorePageNo(mar_cont_m_grid_store, rows.length);
								mar_cont_m_grid_store.load();
								//mar_cont_s_grid_store.load();
								//mar_menu_s_grid_store.load();
								Ext.Msg.alert('Success', oper+'成功');
							},
							failure:function(){
								Ext.MessageBox.show({
									title:"提示",
									msg:oper+"失败",
									width:110,
									buttons:Ext.Msg.OK
								});
							}
						});

					} 
				} 
			}); 
		}                // 弹出对话框警告 
	};
    function onPreviewClick(tree, row, col, item, e, record) {
    	thisWindow.mask('载入图片...');

    	Ext.Ajax.request({
    		url: 'img/query_by_cid.do?type=1',
    		async: true,
    		params: {
    			c_id: record.raw.mar_s_contId
    		},
    		scope: this,
    		success: function(resp, opt) {
    			thisWindow.unmask();
    			var respJson = Ext.JSON.decode(resp.responseText);
    			if(respJson.issuc) {
    				imgObj = {
    	    				c_img_url: respJson.imgData.c_img_url,
    	    				c_img_little_url: respJson.imgData.c_img_little_url,
    	    				c_img_icon_url: respJson.imgData.c_img_icon_url,
    	    				c_img_4_squares_url: respJson.imgData.c_img_4_squares_url,
    	    				c_img_6_squares_url: respJson.imgData.c_img_6_squares_url
    				};

    				record.raw.c_img_rec_postion = respJson.imgData.c_img_rec_postion;
    				showPreviewWin(imgObj);
    			} else {
    				Ext.toast({
         				html: respJson.msg,
         				title: '预览失败',
         				saveDelay: 10,
         				align: 'tr',
         				closable: true,
         				width: 200,
         				useXAxis: true,
         				slideInDuration: 500
         			});
    			}
    		},
    		failure : function(form, action) {
    			thisWindow.unmask();
    				Ext.toast({
    					title: '提示',
         				html: '预览失败',
         				saveDelay: 10,
         				align: 'tr',
         				closable: true,
         				width: 200,
         				useXAxis: true,
         				slideInDuration: 500
         			});
			}
    	});
    }
    var imgUrlPreview = new Ext.Img({
		xtype: 'image',
		src: Ext.BLANK_IMAGE_URL,
		style: 'height: 100px; width: 200px;',
		margin: '0, 0, 0, 5'
	});
    var imgLittlePreview = new Ext.Img({
		xtype: 'image',
		src: Ext.BLANK_IMAGE_URL,
		style: 'height: 200px; width: 150px;',
		margin: '0, 0, 0, 5'
	});
    var imgIconPreview = new Ext.Img({
		xtype: 'image',
		src: "app/resources/images/loading.JPG",
		style: 'height: 100px; width: 100px;',
		margin: '0, 0, 0, 5'
	});
    var img4Preview = new Ext.Img({
		xtype: 'image',
		src: "app/resources/images/loading.JPG",
		style: 'height: 200px; width: 200px;',
		margin: '0, 0, 0, 5'
	});
    var img6Preview = new Ext.Img({
		xtype: 'image',
		src: "app/resources/images/loading.JPG",
		style: 'height: 200px; width: 300px;',
		margin: '0, 0, 0, 5'
	});
    var img_rec_postion = new Ext.form.RadioGroup({
		xtype: 'radiogroup',
		fieldLabel: '推荐位图片类型',
		margin: '10, 0, 0, 0',
		enableFocusableContainer: false,
		hideLabel: true,
		//disabled: true,
		flex: 1,
		items: [
            {boxLabel: '横图', name: 'c_img_rec_postion', inputValue:'1'},
		    {boxLabel: '竖图', name: 'c_img_rec_postion', inputValue:'0'},
			{boxLabel: '小方图', name: 'c_img_rec_postion', inputValue:'2'},
			{boxLabel: '四格图', name: 'c_img_rec_postion', inputValue:'3'},
			{boxLabel: '六格图', name: 'c_img_rec_postion', inputValue:'4'}
        ]
	});
	var winPreview = new Ext.Window({
        modal : true,    
        layout : 'fit',    
        title : '预览图片',
        //width: 500,//widthSize*0.7,
		//height: 350,
		autoShow: false,
        plain : true,
        closeAction: 'hide',
        items : [{
        	xtype: 'form',
        	bodyPadding: 10,
        	items: [img_rec_postion,
        	        {
        	        	xtype: 'container',
        	        	layout: 'hbox',
        	        	margin: '10, 0, 0, 0',
        	        	defaultType: 'datefield',
        	        	items: [imgUrlPreview,imgLittlePreview,imgIconPreview,img4Preview,img6Preview]
        	        }]
        }],
		buttons: [{
			text: '取消',
			scope: this,
			handler: function() {
				imgUrlPreview.setSrc(Ext.BLANK_IMAGE_URL);
		    	imgIconPreview.setSrc("app/resources/images/loading.JPG");
		    	winPreview.hide();
			}
		}]
    });
    function showPreviewWin(columnImg) {
		imgUrlPreview.setSrc(columnImg.c_img_url);
		imgLittlePreview.setSrc(columnImg.c_img_little_url);
		imgIconPreview.setSrc(columnImg.c_img_icon_url);
		img4Preview.setSrc(columnImg.c_img_4_squares_url);
		img6Preview.setSrc(columnImg.c_img_6_squares_url);
		img_rec_postion.setValue({c_img_rec_postion: columnImg.c_img_rec_postion});

    	winPreview.show();
    }
	var mar_cont_m_grid_panel = new Ext.grid.GridPanel({
		store: mar_cont_m_grid_store,
		autoHeight: false,
		columns: [
		          { header: "绑定ID", width: 40, dataIndex: 'mar_s_relaId', hidden: true },
		          { header: "锁定", width: 40, dataIndex: 'locked',locked: true },
		          { header: "ID", width: 80, dataIndex: 'mar_s_contId' },
		          { header: '名称', width: 100, dataIndex: 'mar_s_contName',locked: true },
		          { header: '状态', width: 60, sortable: true, dataIndex: 'mar_s_status' },
		          { header: '状态', width: 60, sortable: true, dataIndex: 'mar_s_status_valid', hidden: true },
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
		          { header: "修改时间", width: 120, sortable: true, dataIndex: 'mar_s_modify_time' },
		          {
						header: '',
				        xtype: 'actioncolumn',
				        locked: true,
				        width: 30,
				        sortable: false,
				        menuDisabled: true,
				        align: 'center',
				        items: [{
				        	iconCls:'preview',
				            tooltip: '预览图片',
				            handler: onPreviewClick
				        }]
				    }
		],
		selModel: {selType: 'checkboxmodel',mode : 'SIMPLE'},
        viewConfig:
        {
        	stripeRows: true,
        	enableTextSelection:true,
        	forceFit : true,
        	getRowClass : function(record,rowIndex,rowParams,store){
        		console.log("c_id="+record.raw.mar_s_contId+",status=" + record.raw.mar_s_status);
        		if(record.raw.mar_s_status_valid=='0'){
        			return 'row_invalid';
        		}
        	}
        },
		height:450,
		frame:true,
		loadMask:{msg:'数据加载中...'},
		title:'栏目下的资产列表',
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
							status_type: contMarFilterStatusType,
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
							status_type: contMarFilterStatusType,
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
							status_type: contMarFilterStatusType,
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
							status_type: contMarFilterStatusType,
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
							status_type: contMarFilterStatusType,
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
		}],
		bbar: new Ext.PagingToolbar({
			pageSize: pgSize,
			width: 420,
			store: mar_cont_m_grid_store,
			displayInfo: true,
			displayMsg: '第 {0}-- {1}条    共 {2}条',
			autoScroll: true,
			emptyMsg: '没有记录' 
		})
	});
	
	var mar_site_combo = new Ext.form.ComboBox({
		baseCls:'x-plain',
		xtype:'combo',
		maxHeight:120,
		width:200,
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
	var mar_site_name_form = new Ext.form.TextField({
		xtype:'textfield',
		fieldLabel:' *栏目名称',
		hideLabel: true,
		emptyText:'栏目名称',
		allowBlank: true,
		//id:'mar_site_name',
		name:'mar_site_name',
		width:90
	});
	/*-----定义组下拉列表，用以搜索组下的相应栏目-----*/
	var status_combo = new Ext.form.ComboBox({
				baseCls : 'x-plain',
				xtype : 'combo',
				maxHeight : 120,
				width:120,
				hideLabel : true,
				displayField : 'statusName',
				valueField : 'statusId',
				editable : false,
				// readOnly:true,
				queryMode : '',
				triggerAction : 'all',
				emptyText : '请选择栏目状态',
				store : new Ext.data.JsonStore({

							fields : ['statusId', 'statusName'],
							autoLoad : true,
							proxy : {
								type : 'ajax',
								url : "menus/status_menu.do",
								reader : {
									root : 'status_data'
								}
							},
			           listeners :{
				           select:{
				              fn:function(combo,value,opts) {
                              }
				           },
				           load: function (oStore, ayRecords, successful,oOptions ) {
                              status_combo.setValue("1");
				           }
			          }
			  })
	});
	var mar_site_search = function() {
		
		//Ext.getCmp("bandButton").disable();
		//var selected_index = mar_site_combo.getStore().find('siteId', mar_site_combo.getValue());
		//amr_menu_tree_root.load();
		//var selected_siteName = mar_site_combo.getStore().getAt(selected_index).get('siteName');
		var selected_siteName = mar_site_combo.getDisplayValue();
		var siteNameStr = '<span style="font-size:16px; color:#0099CC">' + selected_siteName + '站点</span>'
		amr_menu_tree.getStore().root.text = siteNameStr;
		amr_menu_tree.getStore().load();
	}
	var amr_menu_tree_tbar = new Ext.Toolbar({
		items: ['按条件搜索栏目：',{
							xtype : 'panel',
							baseCls : 'x-plain',
							width : 12
						}, mar_site_combo, ' 状态：', status_combo, ' ', mar_site_name_form, {
			text: '搜索',
			style:'background-color:#99BBE8',
			handler: mar_site_search,
			//scope: this,
			width:40
		}]
	});
	
	/*-----定义栏目结构树面板，用以装载栏目结构树-----*/
	//var amr_menu_loader = new Ext.tree.TreeLoader({});
	Ext.define('Cont.Site',{
		extend: 'Ext.data.Model',
		fields : [
			{name:'id',type: 'string',defaultValue: null},
			{name: 'singleClickExpand',type: 'boolean',defaultValue: false},
			{name : 'leaf', type : 'bool',defaultValue : false  },
			{name:'taskid',mapping: 'taskid',type: 'string',defaultValue: null},
			'text','menu_task_structId','iconCls','qtip']
	});
	var amr_menu_tree_store =  Ext.create('Ext.data.TreeStore',{
		model: 'Cont.Site',
		autoLoad: false,
		proxy: {
			type: 'ajax',
			url: 'menus/query_menutree_hasname.do?auth=1',
			extraParams: {}
		},
		//nodeParam:"parent_id", 
		root:  {
			id:'-1',
			text:'<span style="font-size:16px; color:#0099CC">站点</span>',
			allowDrag:false,
			singleClickExpand:true,
			expanded:true,
			iconCls:'no-node-icon',
			taskid:'0',
			menu_task_structId:'3'
		},
		listeners : {
			 beforeload : function(store, operation) {  
				var site_id = mar_site_combo.getValue();
				if(site_id == null || site_id=='' ) {
					site_id = -9999;
				}
				var status = status_combo.getValue();
				if(status == null){
					status = '';
				}
				var name = mar_site_name_form.getValue();
				if(name == null){
					name = '';
				}
				var parent_id = operation.node.data.taskid;
				if( parent_id ==null || parent_id == ''){
					parent_id = 0;
				}
				
				var newParams = {
						site_id : site_id,
						status_id : status,
						name : name,
						parent_id: parent_id
				};
				Ext.apply(store.proxy.extraParams , newParams);
			 }
		}
	});
	
	var amr_menu_tree = new Ext.tree.TreePanel({
		title:'栏目结构树',
		frame:true,
		//width:200,
		columnWidth:.3,
		//singleClickExpand: true,
		//height:580,
		height:500,
		animate:true,
		autoScroll:true,
		containerScroll:true,
		titleCollapse:'true',
		loadMask:{
		  	msg:'数据加载中...'
		 },
		collapsible:true,
		enableDD:false,
		enableDrag:false,
		rootVisible:true,
		lines:true,
		border:true,
		style:'background:#ffffff;',
		store: amr_menu_tree_store
	});
	
	// 设置树的点击事件
    function treeClick(view,record,item,index,e) {
    	e.stopEvent;
    	
    	var nodeId = record.raw.id;
    	//var bandButton = Ext.getCmp("bandButton");
    	
    	if(nodeId==0) {
    		//bandButton.disable();
    	} else {
			
    		var menu_task_structId =  record.raw.menu_task_structId;
    		
    		if(menu_task_structId==1) {
    			//bandButton.disable();
    		} else {
    			//bandButton.enable();
    		}
    		
    		menuId_temp = nodeId;
    		menuName_temp = record.raw.text;
			
			
			Ext.apply(mar_cont_m_grid_store.proxy.extraParams,{
				menuId: nodeId
			});
			
			//mar_cont_m_grid_store.proxy.extraParams={menuId: nodeId};
	    	mar_cont_m_grid_store.load({                                      // =======翻页时分页参数
	      		params:{
	            	start: 0,
	            	limit: pgSize,
	            	status_type: contMarFilterStatusType,
	            	status: contMarFilterStatus
				}
			});
	    	
	    	var contType = mar_cont_type_form.getValue();
   			var contProvider = mar_cont_provider_form.getValue();
   			var contKeyword = addAssetsToTheCatalogPanel_north.form.findField("keyword").getValue();
    	}
    }

    // 增加鼠标单击事件
    amr_menu_tree.on('itemclick', treeClick);

    var status_type_for_cont = new Ext.form.RadioGroup({
		xtype: 'radiogroup',
		fieldLabel: '',
		margin: '10, 0, 0, 0',
		flex: 1,
		items: [
		    {boxLabel: '有效 ', name: 'c_status_type', inputValue:'1',checked:true},
            {boxLabel: '失效 ', name: 'c_status_type', inputValue:'-1'},
			{boxLabel: '其他', name: 'c_status_type', inputValue:'0'}
        ]
	});

	/*-----定义组下拉列表，用以搜索组下的相应栏目-----*/
	var status_combo_for_cont = new Ext.form.ComboBox({
					baseCls : 'x-plain',
					xtype : 'combo',
					maxHeight : 120,
					width:120,
					hideLabel : true,
					displayField : 's_name',
					valueField : 's_id',
					// readOnly:true,
					editable : false,
					queryMode : 'local',
					triggerAction : 'all',
					emptyText : '请选择状态',
					store : new Ext.data.JsonStore({
								fields : ['s_id', 's_name'],
								autoLoad : true,
								proxy : {
									type : 'ajax',
									url : "status/query_for_cont.do",
									reader : {
										root : 'data'
									}
								}
							}),
					listeners: {
						change: function(combo, newValue, oldValue, eOpts ) {
							status_type_for_cont.setValue({c_status_type:0});
						}
					}
	});
    function filter_mar_cont(){
    	//var menuId_temp = mar_cont_m_grid_store.proxy.extraParams.menuId;
    	if(menuId_temp == '-1' || menuId_temp == '0') {
			Ext.MessageBox.alert('警告', '请选择一个栏目，再进行过滤!');
			return ;
		}
    	contMarFilterStatusType = status_type_for_cont.getValue();
    	contMarFilterStatus = status_combo_for_cont.getValue();
    	console.log("status_type=" + contMarFilterStatusType);
    	console.log("status=" + contMarFilterStatus);

    	mar_cont_m_grid_store.load({                                      // =======翻页时分页参数
    		params:{
    			start: 0,
    			limit: pgSize,
    			status_type : contMarFilterStatusType,
    			status : contMarFilterStatus
    		}
    	});
    }
	var mar_cont_m_grid_panel_tbar = new Ext.toolbar.Toolbar({
		width : 450,
		items: ['按状态过滤资产：', status_type_for_cont, status_combo_for_cont, '', {
			text: '过滤',
			style:'background-color:#99BBE8',
			handler: filter_mar_cont
		},'']
	});
	var	contSalesPanel_form = new Ext.FormPanel({
					title : '栏目下的资产列表',
					frame : true,
					collapsible : true,
					animate : true,
					border : true,
					titleCollapse : 'true',
					fieldDefaults : {
						labelAlign : 'right'
					},
					border : false,
					buttonAlign : 'center',
					columnWidth:.7,
					//width : 600,
					tbar:[mar_cont_m_grid_panel_tbar],
					items : [mar_cont_m_grid_panel]
				});
//	var rootPanel = new Ext.Panel({
//		plain:true,
//		frame:true,
//		autoScroll:true,
//		layout:'column',
//		//renderTo:'addAssetsToTheCatalog-basic',
//		tbar: amr_menu_tree_tbar,
//		items:[amr_menu_tree, contSalesPanel_form]
//	});
    this.tbar = amr_menu_tree_tbar;
    this.items = [amr_menu_tree, contSalesPanel_form];
	this.callParent();
    }
});