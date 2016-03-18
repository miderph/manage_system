Ext.define('app.view.module.contapp.ContAppRecommendSyncListWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.ca-recommendsynclist-window',
	
	reference: 'ca_recommendsynclist_window',
	
    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	height: 520,
	scrollable: true,
	title: '已同步应用资产列表',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		var pgSize = 20; // 分页数

		var recommend_sync_delete = function() {
			{
				var rows = recommend_sync_grid.getSelectionModel()
						.getSelection();// 返回值为 Record 数组
				if (rows.length == 0) {
					Ext.MessageBox.alert('警告', '最少选择一条记录!');
				} else {
					var ids = "";
					for (var i = 0; i < rows.length; i++) {
						ids = ids + rows[i].get('c_id') + ',';
					}
					if (ids.length > 0) {
						ids = ids.substring(0, ids.length - 1);
					}
					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
						if (btn == 'yes') {
							Ext.Msg.wait('处理中');
							Ext.Ajax.request({
										url : 'recommend_syncs/delete.do?ids='
												+ ids,
										waitMsg : '正在提交,请稍等',
										success : function(response) {
											recommend_sync_grid_store.load();
											var result = Ext
													.decode(response.responseText);
											if (result.success) {
												Ext.MessageBox.show({
															title : "提示",
															msg : "删除同步完成",
															width : 110,
															buttons : Ext.Msg.OK,
															icon : Ext.Msg.ERROR
														});

											} else {
												Ext.MessageBox.show({
															title : "提示",
															msg : "删除同步失败!",
															width : 110,
															buttons : Ext.Msg.OK
														});
											}
										},
										failure : function() {
											Ext.Msg.show({
														title : '错误提示',
														msg : '删除同步失败!',
														width : 110,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
										}
									});
						}
					});
				}
			}
		}
		var recommend_sync_grid_store = new Ext.data.JsonStore({
					pageSize : pgSize,
					fields : ['id', 'c_id', 'name', 'status', 'create_time',
							'update_time', 'extra_params'],
					proxy : {
						type : 'ajax',
						url : 'recommend_syncs/query.do?auth=1',
						reader : {
							totalProperty : "results",
							root : "datastr",
							idProperty : 'id'
						},
						actionMethods : {
							read : 'POST'
						}
					}
				});

		var recommend_sync_grid = new Ext.grid.GridPanel({
			frame: true,
		    columnLines: true,
			selModel : {
				selType : 'checkboxmodel',
				mode : 'SIMPLE'
				//mode : 'SINGLE'
			},
			store : recommend_sync_grid_store,
			columns : [{
				header : 'c_id',
				width : 80,
				sortable : true,
				dataIndex : 'c_id'
			}, {
				header : "名称",
				width : 100,
				sortable : true,
				dataIndex : 'name'
			}, {
				header : "状态",
				width : 40,
				sortable : true,
				dataIndex : 'status'
			}, {
				header : "创建时间",
				width : 100,
				sortable : true,
				dataIndex : 'create_time'
			}, {
				header : "修改时间",
				width : 100,
				sortable : true,
				dataIndex : 'update_time'
			}, {
				header : "扩展参数",
				width : 200,
				sortable : true,
				dataIndex : 'extra_params'
			}],
			tbar : [{
				text : '删除同步',
				handler : recommend_sync_delete
			}],
			bbar : new Ext.PagingToolbar({
				pageSize : pgSize,
				width : 440,
				store : recommend_sync_grid_store,
				displayInfo : true,
				displayMsg : '第 {0}-- {1}条    共 {2}条',
				emptyMsg : '没有记录'
			})
		});

		recommend_sync_grid_store.load({ // =======翻页时分页参数
			params : {
				start : 0,
				limit : pgSize
			}
		});		

		this.items = [recommend_sync_grid];
		
		this.buttons = [{
			text: '取消',
			scope: this,
			handler: function() {
				this.hide();
			}
		}];
		
		this.callParent();
	},
	items:[],
	buttonAlign: 'center'
});