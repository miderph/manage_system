
function objToStr(o) {
	    if (o == undefined) {
	        return "";
	    }
	    var r = [];
	    if (typeof o == "string") return "\"" + o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	    if (typeof o == "object") {
	        if (!o.sort) {
	            for (var i in o)
	                r.push("\"" + i + "\":" + objToStr(o[i]));
	            if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
	                r.push("toString:" + o.toString.toString());
	            }
	            r = "{" + r.join() + "}"
	        } else {
	            for (var i = 0; i < o.length; i++)
	                r.push(objToStr(o[i]))
	            r = "[" + r.join() + "]";
	        }
	        return r;
	    }
	    return o.toString().replace(/\"\:/g, '":""');
}

//比较时间大小
function comptime(beginTime,endTime) {
	if(beginTime&&endTime){
	    var beginTimes = beginTime.substring(0, 10).split('-');
	    var endTimes = endTime.substring(0, 10).split('-');
	    var beginTime2 = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
	    var endTime2 = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0] + ' ' + endTime.substring(10, 19);
	
	    var a = (Date.parse(endTime2) - Date.parse(beginTime2)) / 3600 / 1000;
	    if (a < 0) {
	       // alert("endTime小！");
	       return false;
	    } else if (a > 0) {
	        //alert("endTime大！");
	        return true;
	    } else if (a == 0) {
	        //alert("时间相等！");
	        return false;
	    } else {
	    	return false;
	        //return 'exception'
	    }
	  }else{
			return true;
	  }
}
Array.prototype.contains = function(e){
		for(i=0;i<this.length;i++){
			if(this[i] == e){
				return true;
			}
		}
		return false;
}
var Utils={
    checkStorePageNo:function(store,delCount){
    	//--------------------修正currentPage数值-----------------------------------------------              
    	//?这里的处理目的在于解决将最后一页全删除后，Ext默认的当前页（currentPage）值不变化，造成加载到store中的数据为空  
    	//?当删除的数据不是最后一页时，currentPage不需要变化，可以直接加载到后面的数据，store不为空  
    	//?但是当删除最后一页的数据时，若currentPage不变化，由于后面没有数据，会造成store为空，  
    	//在Paging.js中会将当前页，总页等一起设置为0，造成表格不可用  
    	//?当删除的即是最后一页，也是第一页时（即所有数据都展示在一页上）时，也不需要处理。  
    	var totalCount = store.getTotalCount(); // 所有的记录数，不单单是当前页展示的数据  
    	var pageSize = store.pageSize; // 一页上面展示的记录条数  
    	var curPage = store.currentPage; // 当前页码  
    	var fromRecord = ((curPage - 1) * pageSize) + 1; // 当前页展示的起始记录号  
    	var toRecord = Math.min(curPage * pageSize, totalCount); // 当前页展示的结尾记录号  
    	var totalOnCurPage = toRecord - fromRecord + 1; // 当前页展示的记录条数  
    	var totalPageCount = Math.ceil(totalCount / pageSize); // 总的页数  
    	//var delCount = length; // 删除的记录条数  
    	//若当前页是最后一页，且不是仅有的一页，且删除的记录数是当前页上的所有记录数  
    	if (curPage === totalPageCount && totalPageCount != 1 && delCount === totalOnCurPage){  
    		store.currentPage--;
    	}  
    	//----------------------------------------------------------------------------------------------------   
    },
	htmlEncode:function(value){
		return $('<div/>').text(value).html();
	},
	htmlDecode:function(html){
		return $('<div/>').html(html).text();
	},
	//js获取url参数值
	getParameter:function(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) return unescape(r[2]); return null;
	},
	showCheckboxGroupWindow : function (store, valueName,lableName,title,callbackIds,callbackNames, callbackFunc) {
		//object name to object
		//callbackIds =Ext.getCmp(form).getForm().findField(callbackIds);
		//callbackNames =Ext.getCmp(form).getForm().findField(callbackNames)
		var count = store.getCount();  
	    var column = 3;
	    column = count < column ? count : column;
	    var myCheckboxItems = []; 
	    var checkedIds = callbackIds.getValue().split(',');
	    var checked_flag=false;
	    for (var i = 0; i < count; i++) {    
	        var boxLabel = store.getAt(i).get(lableName);    
	        var name = store.getAt(i).get(valueName); 
	        checked_flag = checkedIds.contains(name) ? true: false;
	        if(boxLabel != '请选择'){
	        	myCheckboxItems.push({    
	                    boxLabel : boxLabel,    
	                    name : name,
	                    checked: checked_flag
	                });  
	        }
	    } 
	    
	    var myCheckboxGroup = new Ext.form.CheckboxGroup({   
            xtype : 'checkboxgroup',    
            itemCls : 'x-check-group-alt',    
            columns : column,    
            items : myCheckboxItems    
        }); 
	    
	    var radioCheck = function(element,checked){
	    	if(checked){
				var items = myCheckboxGroup.items;
				if(element.inputValue==1){
					items.each(function(item){
						item.setValue(true);
					});
				}else if(element.inputValue==2){
					items.each(function(item){
						if(item.getValue()==false){
							item.setValue(true);
						}else{
							item.setValue(false);
						}
					});
				}
			}
	    };
	    var myRadioGroup =  new Ext.form.RadioGroup({
            fieldLabel : "选择",
            items : [
	            {boxLabel : '全选', name : "check_radioGroup",id : "check_radioGroup_0",inputValue : 1,listeners : { change : radioCheck }},
	            {boxLabel : '反选', name : "check_radioGroup",id : "check_radioGroup_1",inputValue : 2,listeners : { change : radioCheck }}
            ]
        });
	    var form = new Ext.FormPanel({    
	                border : true,    
	                frame : true,
	                autoScroll:true,
					fieldDefaults:{
						labelAlign : "right"
					},
	                buttonAlign : 'center',    
	                layout : 'form',    
	                width : 690,    
	                items : [myRadioGroup,myCheckboxGroup],    
	                buttons : [{    
	                    xtype : 'button',    
	                    text : '确定',    
	                    handler : function() {    
	                        var ids = [];  
	                        var names = [];
	                        var cbitems = myCheckboxGroup.items;    
	                        for (var i = 0; i < cbitems.length; i++) {    
	                            if (cbitems.items[i].checked) {    
	                                ids.push(cbitems.items[i].name);  
	                                names.push(cbitems.items[i].boxLabel)
	                            }    
	                        }    
	                        win.destroy();    
                            //Ext.Msg.alert("消息", "选中状态的id组合字符串为:" + ids.toString() + names.toString());   
                            callbackIds.setValue(ids.toString());
                            callbackNames.setValue(names.toString());
                            if (callbackFunc != null){
                            	callbackFunc();
                            }
	                    }    
	   
	                }, {    
	                    xtype : 'button',    
	                    text : '取消',    
	                    handler : function() {    
	                        win.destroy();    
	                    }    
	                }]    
	   
	            });
	    var win = new Ext.Window({
	                modal : true,
	                layout : 'fit',
	                title : title,
	                width : 700,
	                height : 350,
	                plain : true,
	                resizable: true,
	                items : [form]
	            });    
	    win.show();    
	},

	showCheckboxGroup : function (store, valueName,lableName,title,callbackIds,callbackNames,form) {
		//object name to object
		callbackIds =form.form.findField(callbackIds);
		callbackNames =form.form.findField(callbackNames)
	    Utils.showCheckboxGroupWindow(store,valueName,lableName,title,callbackIds,callbackNames);
	},
}

	// ******************WinUtil_selectCont*begin****************************************************************
	var WinUtil_selectCont = { // 快捷方式关联内容选择
		showWindows : function(title, callbackName, callbackId, form) {
			// object name to object
			callbackName = form.form.findField(callbackName);
			callbackId = form.form.findField(callbackId);

			var menuTmpStartTime = "2010-01-01"; // 默认开始时间
			var menuPgSize = 100; // 分页数
			// var menuTmpStartTime = "2010-01-01"; //默认开始时间
			var menuId_temp = "-1";
			var menuName_temp = "";
			var contStatus = "-10000";
			var menu_mar_cont_typeStore = new Ext.data.JsonStore({
						fields : ['contTypeId', 'contTypeName'],
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : 'cont_type.do',
							reader : {
								root : 'contType_data'
							}
						}
					});

			var menu_mar_cont_type_form = new Ext.form.ComboBox({
						fieldLabel : '资产类型',
						emptyText : '请选择资产类型...',
						baseCls : 'x-plain',
						xtype : 'combo',
						name : 'contTypeId',
						hiddenName : 'contTypeId',
						// width:120,
						maxHeight : 180,
						hideLabel : false,
						displayField : 'contTypeName',
						valueField : 'contTypeId',
						// readOnly:true,
						editable : false,
						queryMode : 'local',
						triggerAction : 'all',
						store : menu_mar_cont_typeStore
					});
			var menu_cont_video_statusStore = new Ext.data.JsonStore({

						fields : ['s_id', 's_name'],
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : 'status/query_for_cont.do',
							reader : {
								root : 'data'
							}
						}
					});
			var menu_mar_cont_video_status_form = new Ext.form.ComboBox({
						fieldLabel : '资产状态',
						emptyText : '请选择资产状态...',
						baseCls : 'x-plain',
						xtype : 'combo',
						name : 'contStatusId',
						hiddenName : 'contStatusId',
						// width:120,
						maxHeight : 180,
						hideLabel : false,
						allowBlank : true,
						disabled : false,
						displayField : 's_name',
						valueField : 's_id',
						// readOnly:true,
						editable : false,
						queryMode : 'local',
						triggerAction : 'all',
						store : menu_cont_video_statusStore
					});
			menu_mar_cont_video_status_form.on('select', function() {
						contStatus = this.getValue();
					});
			var menu_mar_cont_providerStore = new Ext.data.JsonStore({

						fields : ['providerId', 'providerName'],
						autoLoad : true,
						proxy : {
							type : 'ajax',
							url : 'menus/provider_menu.do?siteId=-1&auth=1',
							reader : {
								root : 'provider_data'
							}
						}
					});

			var menu_mar_cont_provider_form = new Ext.form.ComboBox({
						fieldLabel : '提供商',
						emptyText : '请选择资产提供商...',
						baseCls : 'x-plain',
						xtype : 'combo',
						name : 'providerId',
						hiddenName : 'providerId',
						// width:120,
						maxHeight : 180,
						hideLabel : false,
						displayField : 'providerName',
						valueField : 'providerId',
						// readOnly:true,
						editable : false,
						queryMode : 'local',
						triggerAction : 'all',
						store : menu_mar_cont_providerStore
					});
			var keyword_form = new Ext.form.TextField({
						xtype : 'textfield',
						// id: 'keyword',
						name : 'keyword',
						width : 120,
						fieldLabel : '关键字',
						maxLength : 15,
						maxLengthText : '不能超过15个字符'
					});
			var s_starttime_form = new Ext.form.DateField({
						xtype : 'datefield',
						format : 'Y-m-d',
						width : 120,
						// id: 's_starttime',
						name : 's_starttime',
						fieldLabel : '创建时间',
						allowBlank : false,
						// readOnly: true,
						grow : true,
						blankText : '请选择开始时间',
						value : menuTmpStartTime
					});
			var s_endtime_form = new Ext.form.DateField({
						xtype : 'datefield',
						format : 'Y-m-d',
						// id: 's_endtime',
						width : 120,
						name : 's_endtime',
						fieldLabel : '至',
						allowBlank : false,
						readOnly : true,
						grow : true,
						blankText : '请选择结束时间',
						value : new Date()
					});

			var addAssetsToTheCatalogPanel_north = new Ext.FormPanel({
				region : 'north',
				// height:90,
				width : 750,
				height : 150,
				fieldDefaults : {
					labelAlign : 'right'
				},
				items : [{
					xtype : 'panel',
					// layout : 'form',
					border : false,
					items : [{
								xtype : 'panel',
								layout : 'column',
								border : false,
								items : [{
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : [menu_mar_cont_provider_form]
										}, {
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : [menu_mar_cont_type_form]
										}, {
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : [menu_mar_cont_video_status_form]
										}]
							}, {
								xtype : 'panel',
								layout : 'column',
								border : false,
								items : [{
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : [keyword_form]
										}, {
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : [s_starttime_form]
										}, {
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : [s_endtime_form]
										}]

							}, {
								xtype : 'panel',
								layout : 'column',
								border : false,
								items : [{
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : []
										}, {
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 0 0',
											items : []
										}, {
											columnWidth : .3,
											// layout:'form',
											baseCls : 'x-plain',
											border : false,
											bodyStyle : 'padding:6 0 60 60',
											items : [{
												xtype : 'button',
												text : '搜索',
												align : 'right',
												// url: 'login.jsp',
												handler : function() {
													if (addAssetsToTheCatalogPanel_north
															.getForm()
															.isValid()) {

														var contType = menu_mar_cont_type_form
																.getValue();
														var contProvider = menu_mar_cont_provider_form
																.getValue();
														var contKeyword = Ext.String
																.trim(keyword_form
																		.getValue());
														mar_cont_s_grid_store
																.on(
																		'beforeload',
																		function() { // =======翻页时
																			// 查询条件
																			mar_cont_s_grid_store.proxy.extraParams = {
																				contType : contType,
																				contProvider : contProvider,
																				keyWord : contKeyword,
																				contStatus : contStatus,
																				startTime : s_starttime_form
																						.getValue(),

																				endTime : s_endtime_form
																						.getValue(),

																				menuId : menuId_temp
																			}
																		});
														mar_cont_s_grid_store
																.load({ // =======翻页时分页参数
																	params : {
																		start : 0,
																		limit : menuPgSize
																	}
																});

													}
												}
											}]
										}]
							}]
				}]
			});

			var mar_cont_s_grid_store = new Ext.data.Store({
						pageSize : menuPgSize,
						fields : ['mar_s_contId', 'mar_s_contName',
								'mar_s_contType', 'mar_s_contProvider',
								'mar_s_contIntro'],
						proxy : {
							type : 'ajax',
							url : 'contvideo/query_cont_for_mar.do?auth=1',
							reader : {
								totalProperty : "results",
								root : "datastr",
								idProperty : 'mar_s_contId'
							}
						}
					});

			mar_cont_s_grid_store.proxy.extraParams = {
				contType : "",
				contProvider : "",
				keyWord : "",
				contStatus : contStatus,
				startTime : menuTmpStartTime,
				endTime : "",
				menuId : menuId_temp
			}

			mar_cont_s_grid_store.load({ // =======翻页时分页参数
				params : {
					start : 0,
					limit : menuPgSize
				}
			});

			// var mar_cont_s_grid_sm = new Ext.grid.CheckboxSelectionModel();
			var mar_cont_s_grid = new Ext.grid.GridPanel({
						store : mar_cont_s_grid_store,
						autoHeight : false,
						columns : [{
									header : "ID",
									width : 90,
									sortable : true,
									dataIndex : 'mar_s_contId'
								}, {
									header : '名称',
									width : 130,
									sortable : true,
									dataIndex : 'mar_s_contName'
								}, {
									header : "类型",
									width : 110,
									sortable : true,
									dataIndex : 'mar_s_contType'
								}, {
									header : "提供商",
									width : 110,
									sortable : true,
									dataIndex : 'mar_s_contProvider'
								}, {
									header : "描述",
									width : 180,
									sortable : true,
									dataIndex : 'mar_s_contIntro'
								}],
						selModel : {
							selType : 'checkboxmodel',
							mode : 'single'
						},
						width : 700,
						height : 450,
						autoScroll : true,
						frame : true,
						loadMask : {
							msg : '数据加载中...'
						},
						title : '要编排的资产列表',
						iconCls : 'icon-grid',
						bbar : new Ext.PagingToolbar({
									pageSize : menuPgSize,
									width : 700,
									store : mar_cont_s_grid_store,
									displayInfo : true,
									displayMsg : '第 {0}-- {1}条    共 {2}条',
									autoScroll : true,
									emptyMsg : '没有记录'
								})
					});

			var addAssetsToTheCatalogPanel_west = new Ext.FormPanel({
						bodyStyle : 'padding:0 0 0 5',
						width : 730,
						// layout : 'form',
						// height:365,
						items : mar_cont_s_grid
					});

			var addAssetsToTheCatalogPanel_west_center = new Ext.Panel({
						bodyStyle : 'padding:10 0 10 0',
						layout : 'column',
						height : 450,
						width : 740,
						items : [{
									width : 730,
									// layout : 'form',
									baseCls : 'x-plain',
									items : [addAssetsToTheCatalogPanel_west]
								}]
					});

			var winform = new Ext.FormPanel({
						border : true,
						frame : true,
						fieldDefaults : {
							labelAlign : "right"
						},
						buttonAlign : 'right',
						layout : 'column',
						width : 780,
						height : 650,
						items : [{
									width : 750,
									height : 100,
									// layout:'form',
									baseCls : 'x-plain',
									items : [addAssetsToTheCatalogPanel_north]
								}, {
									width : 750,
									height : 450,
									// layout:'form',
									baseCls : 'x-plain',
									items : [addAssetsToTheCatalogPanel_west_center]
								}

						// addAssetsToTheCatalogPanel_west_center
						],
						buttons : [{
							xtype : 'button',
							text : '确定',
							handler : function() {
								var id = '';
								var name = '';
								// Ext.Msg.alert("消息", "选中状态的id为:" +
								// id.toString() + name.toString());
								var rows = mar_cont_s_grid.getSelectionModel()
										.getSelection();
								if (!(rows.length == 1)) {
									Ext.MessageBox.confirm('警告',
											'请选择一条记录!,不选、多选默认为空',
											function(btn) {
												if (btn == 'yes') {
													if (rows) {
														callbackName
																.setValue('');
														callbackId.setValue('');
														win.destroy();
													}
												}
											});
								} else {
									var ids = '';
									Ext.MessageBox.confirm('提示框', '您确定选择该条记录？',
											function(btn) {
												if (btn == 'yes') {
													if (rows) {
														callbackName
																.setValue(rows[0]
																		.get('mar_s_contName')
																		.toString());
														callbackId
																.setValue(rows[0]
																		.get('mar_s_contId')
																		.toString());
														win.destroy();
													}
												}
											});
								}
							}

						}, {
							xtype : 'button',
							text : '取消',
							handler : function() {
								win.destroy();
							}

						}]

					});
			var win = new Ext.Window({
						modal : true,
						layout : 'fit',
						title : title,
						width : 780,
						height : 650,
						plain : true,
						items : [winform]
					});
			win.show();

		}
	}
	// *******WinUtil_selectCont*end***************************************************************************

	// *******WinUtil_selectShortCutMenu*begin***************************************************************************
	// **begin：快捷方式：选择栏目链接id***********************************************************************************************
   var WinUtil_selectShortCutMenu={
	showWindows: function(title, shortcut_contname, shortcut_contid, form){
	shortcut_contid = form.form.findField(shortcut_contid);
	shortcut_contname = form.form.findField(shortcut_contname);
	
	var select_link_menu_id = '';
	var select_link_menu_name = '';
	var select_link_menu_tree_store = Ext.create('Ext.data.TreeStore', {
				fields : ['id', 'text', 'leaf', 'taskid', 'menu_task_structId',
						'singleClickExpand', 'iconCls', 'qtip'],
				autoLoad : false,
				proxy : {
					type : 'ajax',
					url : 'menus/query_menutree_hasname.do?auth=1',
					extraParams : {}
				},
				listeners : {
					beforeload : function(store, operation, opts) {
						var site_id = 0;
						var name = '';
						var parent_id = operation.node.data.taskid;
						if (parent_id == null || parent_id == '') {
							parent_id = 0;
						}
						store.proxy.extraParams = {
							site_id : site_id,
							name : name,
							parent_id : parent_id,
							ignore_id : -1
						};

					}
				}
			});

	/*-----定义栏目结构树面板，用以装载栏目结构树-----*/
	var select_link_menu_tree = Ext.create('Ext.tree.Panel', {
				title : '栏目结构树',
				frame : true,
				columnWidth : .29,
				width : 380,
				height : 360,
				animate : true,
				autoScroll : true,
				containerScroll : true,
				titleCollapse : 'true',
				collapsible : false,
				enableDD : false,
				enableDrag : false,
				rootVisible : true,
				lines : true,
				border : true,
				style : 'background:#ffffff;',
				store : select_link_menu_tree_store,
				root : {
					// id : '-1',
					text : '<span style="font-size:16px; color:#0099CC">栏目链接站点</span>',
					allowDrag : false,
					singleClickExpand : true,
					expanded : true,
					iconCls : 'no-node-icon',
					taskid : '0',
					menu_task_structId : '3'
				}
			});
	select_link_menu_tree.on('itemcontextmenu', function(view, record, item,
			index, e) { // 声明菜单类型
				// selectcontextNode = record;
				// selectpublicNode = record;
				// selectcatalog_id = selectpublicNode.raw.taskid;
				e.preventDefault(); // 阻止浏览器默认右键菜单显示
				e.stopEvent();
				// selectrightClick.showAt(e.getXY()); // 取得鼠标点击坐标，展示菜单
		});
	// 设置树的点击事件
	function select_link_menu_treeClick(view, record, item, index, e) {
		if (record.raw.taskid != 0) {
			select_link_menu_id = record.raw.id;
			select_link_menu_name = record.raw.text;
		}
	}

	// 增加鼠标单击事件
	select_link_menu_tree.on('itemclick', select_link_menu_treeClick);

	// 显示窗体
	var select_link_menu_Windows = new Ext.Window({
				title : title,
				bodyStyle : 'padding:10 10 10 10',
				width : 420,
				// height : 520,
				autoHeight : true,
				plain : true,
				modal : true,
				autoDestroy : true,
				closeAction : 'hide',
				// 关闭窗口
				maximizable : false,
				// 最大化控制 值为true时可以最大化窗体
				// layout : 'form',
				items : [select_link_menu_tree],
				buttons : [{
					text : '确定',
					handler : function() {
						select_link_menu_Windows.hide();
						// window.alert("select_link_menu_name="+select_link_menu_name+",select_link_menu_id="+select_link_menu_id);
						shortcut_contname.setValue(select_link_menu_id + "|" + select_link_menu_name);
						shortcut_contid.setValue(select_link_menu_id);

						// Ext.getCmp("shortcut_contname").setValue(select_link_menu_name);
						// Ext.getCmp("shortcut_contid").setValue(select_link_menu_id);
						return null;
					}
				}, {
					text : '取消',
					handler : function() {
						select_link_menu_Windows.hide();
						return null;
					}
				}]
			});
			select_link_menu_tree.getStore().load();
			select_link_menu_Windows.show();
	
	}
}
	
	// **end：快捷方式：选择栏目链接id***********************************************************************************************
	// *******WinUtil_selectShortCutMenu*end***************************************************************************

	// *******WinUtil_setSuperScript*begin***************************************************************************
	var WinUtil_setSuperScript={
			
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
	    selModel: {selType: 'checkboxmodel',model: 'single'},
	    width:720,
	    height:450,
	    frame:true,
		loadMask:{ msg:'数据加载中...'  },
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
					fieldDefaults:{
						labelAlign : "right"
					},
	                buttonAlign : 'right',    
	                layout : 'column',    
	                width : 780, 
					height: 540,
	                items :[
						setsuperscriptPanel
	                ],      
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
									        var result = Ext.decode(response.responseText);
									        if(result.success){
										        Ext.MessageBox.show({
													title:"提示",
													msg:"设置角标成功",
													width:110,
													buttons:Ext.Msg.OK
												});
										        contVideoPanel_grid_store.load();
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
	// *******WinUtil_setSuperScript*end***************************************************************************

	// *******WinUtil_selectSuperScript*begin***************************************************************************
	var WinUtil_selectSuperScript = {
			
	showWindows : function (title,callbackName,callbackId,form) {
	//object name to object
	callbackName = form.form.findField(callbackName);
	callbackId = form.form.findField(callbackId);
	var menuPgSize = 100;  //分页数

	var mar_cont_s_grid_store = new Ext.data.Store({
  		pageSize:menuPgSize,
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
	
	
	mar_cont_s_grid_store.load({                                      // =======翻页时分页参数
  		params:{
        	start: 0,
        	limit: menuPgSize
		}
	});
	
	//var mar_cont_s_grid_sm = new Ext.grid.CheckboxSelectionModel();
   
	var mar_cont_s_grid = new Ext.grid.GridPanel({
	    store: mar_cont_s_grid_store,
	    autoHeight: false,
	    columns: [
	        { header: "ID",width: 90, sortable: true, dataIndex: 'mar_s_contId' },
	        { header: '名称', width: 130, sortable: true, dataIndex: 'mar_s_contName' },
	        { header: "类型", width: 110, sortable: true, dataIndex: 'mar_s_contType' },
	        { header: "提供商", width: 110, sortable: true, dataIndex: 'mar_s_contProvider' },
	        { header: "描述", width: 180, sortable: true, dataIndex: 'mar_s_contIntro' }
	    ],
	    selModel: {selType: 'checkboxmodel',model: 'single'},
	    width:720,
	    height:450,
	    frame:true,
		loadMask:{
		  	msg:'数据加载中...'
		},
	    title:'角标列表',
	    iconCls:'icon-grid',
      	bbar: new Ext.PagingToolbar({
          	pageSize: menuPgSize,
          	width: 720,
          	store: mar_cont_s_grid_store,
          	displayInfo: true,
          	displayMsg: '第 {0}-- {1}条    共 {2}条',
          	emptyMsg: '没有记录' 
      	})
	});
	
	var addAssetsToTheCatalogPanel_west = new Ext.FormPanel({
			bodyStyle:'padding:0 0 0 5',
			width:730,
			layout:'form',
			//height:365,
			items: mar_cont_s_grid
	});
	
	
	var addAssetsToTheCatalogPanel_west_center = new Ext.Panel({
		bodyStyle:'padding:10 0 10 0',
		layout:'column',
		height: 450,
		width:740,
		items:[addAssetsToTheCatalogPanel_west]	
	});
			
			
	    var winform = new Ext.FormPanel({    
	                border : true,    
	                frame : true,  
					fieldDefaults: {
						labelAlign : "right"
					},    
	                buttonAlign : 'right',    
	                layout : 'column',    
	                width : 780, 
					height: 540,
	                items : [
						addAssetsToTheCatalogPanel_west_center
	                ],      
	                buttons : [{    
	                    xtype : 'button',    
	                    text : '确定',    
	                    handler : function() {    
	                        var id = '';  
	                        var name = '';   
	                           
                            //Ext.Msg.alert("消息", "选中状态的id为:" + id.toString() + name.toString());
	                        var rows=mar_cont_s_grid.getSelectionModel().getSelection();
	                        if(!(rows.length==1)){ 
								Ext.MessageBox.confirm('警告', '请选择一条记录!,不选、多选默认为空',function(btn){ 
								if(btn=='yes'){ 
									if(rows){
                    					callbackName.setValue('');
                    					callbackId.setValue('');
										win.destroy();
									} 
								}
							});  
							}else{ 
							var ids = '';
							Ext.MessageBox.confirm('提示框', '您确定选择该条记录？',function(btn){ 
								if(btn=='yes'){ 
									if(rows){
                    					callbackName.setValue(rows[0].get('mar_s_contName').toString());
                    					callbackId.setValue(rows[0].get('mar_s_contId').toString());
										win.destroy();
									} 
								}
							}); 
							}
	                    }    
	   
	                }, {    
	                    xtype : 'button',    
	                    text : '取消',    
	                    handler : function() {    
	                        win.destroy();    
	                    }    
	   
	                }]    
	   
	            });
	    var win = new Ext.Window({    
	                modal : true,    
	                layout : 'fit',    
	                title : title,    
	                width : 780,    
	                height : 540,    
	                plain : true,    
	                items : [winform]    
	            });    
	    win.show();
	}
}
	// *******WinUtil_selectSuperScript*end***************************************************************************
	