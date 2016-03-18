Ext.define('app.view.module.customer.Customer', {
	extend: 'Ext.panel.Panel',
	requires: [
       'app.view.module.customer.CustomerController',
       'app.view.module.customer.CustomerModel'
    ],

	alias: 'widget.customerPanel',
	
	uses: ['app.view.module.customer.CallRecordToolbar', 
	       'app.view.module.customer.GoodsOrderToolbar'],
	
	title: '客服管理',
	controller: 'customer',
    viewModel: {
        type: 'customer'
    },
	
	initComponent: function() {
		
		if(Ext.grid.RowEditor) {
			Ext.apply(Ext.grid.RowEditor.prototype, {
				saveBtnText: '保存',
				cancelBtnText: '取消',
				errorsText: '错误信息',
				dirtyText: '已修改,你需要提交或取消变更'
			});
		}
		
		var crStore = Ext.create('app.store.CallRecordStore');
		var goStore = Ext.create('app.store.GoodsOrderStore');
		var uaStore = Ext.create('app.store.UserAddressStore');
		
		var provinceStore = Ext.create('Ext.data.Store', {
		    fields: ['p_id', 'p_name'],
		    data : [
		        {"p_id":"hn", "p_name":"湖南省", "code": "hns"},
		        {"p_id":"bjs", "p_name":"北京市", "code": "bjs"},
		        {"p_id":"hb", "p_name":"湖北省", "code": "hbs"},
		        {"p_id":"gd", "p_name":"广东省", "code": "gds"},
		        {"p_id":"gx", "p_name":"广西省", "code": "gxs"},
		        {"p_id":"henan", "p_name":"河南省", "code": "hns"},
		        {"p_id":"hebei", "p_name":"河北省", "code": "hbs"}
		    ]
		});
		
		this.items = [{
			xtype: 'grid',
			/*title: '通话记录',*/
			frame: true,
		    columnLines: true,
			height:150,
			store: crStore,
			reference: 'call_record_grid',
			bbar: {
				xtype: 'pagingtoolbar',
		        pageSize: crStore.getPageSize(),
		        store: crStore,
		        displayInfo: true,
		        plugins: Ext.create('app.ux.ProgressBarPager')
            },
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            },
			columns: [{
				header: '主叫号码',
				dataIndex: 'call_from',
				align: 'center',
				width: 100
			}, {
				header: '被叫号码',
				dataIndex: 'call_to',
				align: 'center',
				width: 100
			}, {
				header: '客服号码',
				dataIndex: 'oper_id',
				align: 'center',
				width: 70
			}, {
				xtype: 'datecolumn',
				/*format: 'Y-m-d H:i:s',*/
				header: '通话日期',
				dataIndex: 'call_year',
				align: 'center',
				width: 80,
				renderer: function(v,m,record,ri,ci) {
					var ct = record.get('call_time');
					var y = '';
					if(!Ext.isEmpty(ct)) {
						var dt = new Date(ct);
						if(!Ext.isEmpty(dt)) {
							y = Ext.Date.format(dt, 'Y-m-d')
						}
					}
					
					return y;
				}
			}, {
				xtype: 'datecolumn',
				format: 'Y-m-d H:i:s',
				header: '来电时间',
				dataIndex: 'call_time',
				align: 'center',
				width: 70,
				renderer: function(v,m,record,ri,ci) {
					var t = '';
					if(!Ext.isEmpty(v)) {
						var dt = new Date(v);
						if(!Ext.isEmpty(dt)) {
							t = Ext.Date.format(dt, 'H:i:s')
						}
					}
					
					return t;
				}
			}, {
				xtype: 'datecolumn',
				format: 'Y-m-d H:i:s',
				header: '接通时间',
				dataIndex: 'call_in_time',
				align: 'center',
				width: 70,
				renderer: function(v,m,record,ri,ci) {
					var t = '';
					if(!Ext.isEmpty(v)) {
						var dt = new Date(v);
						if(!Ext.isEmpty(dt)) {
							t = Ext.Date.format(dt, 'H:i:s')
						}
					}
					
					return t;
				}
			}, {
				xtype: 'datecolumn',
				format: 'Y-m-d H:i:s',
				header: '结束时间',
				dataIndex: 'call_off_time',
				align: 'center',
				width: 70,
				renderer: function(v,m,record,ri,ci) {
					var t = '';
					if(!Ext.isEmpty(v)) {
						var dt = new Date(v);
						if(!Ext.isEmpty(dt)) {
							t = Ext.Date.format(dt, 'H:i:s')
						}
					}
					
					return t;
				}
			}, {
				header: '通话时长',
				dataIndex: 'call_length',
				align: 'center',
				width: 80
			}, {
				header: '用户（ID|姓名|类型）',
				dataIndex: 'user_info',
				align: 'center',
				width: 150,
				renderer: function(v,m,record,ri,ci) {
					return record.get('user_id') + ' | ' + record.get('user_name') + ' | ' + record.get('isnew');
				}
			}/*, {
				header: '用户ID',
				dataIndex: 'user_id',
				align: 'center',
				width: 80
			}, {
				header: '用户姓名',
				dataIndex: 'user_name',
				align: 'center',
				width: 80
			}, {
				header: '用户类型',
				dataIndex: 'isnew',
				align: 'center',
				width: 80
			}*/, {
				header: '备注',
				dataIndex: 'memo',
				align: 'center',
				flex: 1,
				editor: {
					xtype: 'textfield'
				}
			}],
			plugins: [{
				/*ptype: 'rowediting',*/
				ptype: 'cellediting',
		        clicksToEdit: 2
			}],
			listeners: {
				edit: 'onModifyCallRecord'
			}
		}, {
			xtype: 'grid',
			/*title: '订单信息',*/
			frame: true,
		    columnLines: true,
			height: 250,
			store: goStore,
			reference: 'goods_order_grid',
			bbar: {
				xtype: 'pagingtoolbar',
		        pageSize: goStore.getPageSize(),
		        store: goStore,
		        displayInfo: true,
		        plugins: Ext.create('app.ux.ProgressBarPager')
            },
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            },
			columns: [{
				header: '订单号',
				dataIndex: 'order_id',
				align: 'center',
				width: 50
			}, {
				header: '支付时间',
				dataIndex: 'order_pay_time',
				align: 'center',
				width: 60,
				locked: true
			}, {
				header: '商品编号',
				dataIndex: 'cs_sales_no',
				align: 'center',
				width: 60,
				locked: true
			}, {
				header: '商品ID',
				dataIndex: 'c_id',
				align: 'center',
				width: 100,
				locked: true
			}, {
				header: '商品名称',
				dataIndex: 'c_name',
				align: 'left',
				width: 160,
				locked: true
			}],
			dockedItems: [{
		        xtype: 'goods-order-toolbar',
		        dock: 'top'
		    }]
		}, {
			xtype: 'grid',
			title: '收货地址',
			frame: true,
		    columnLines: true,
			collapsible: true,
			height: 250,
			store: uaStore,
			reference: 'user_address_grid',
			bbar: {
				xtype: 'pagingtoolbar',
		        pageSize: uaStore.getPageSize(),
		        store: uaStore,
		        displayInfo: true,
		        plugins: Ext.create('app.ux.ProgressBarPager')
            },
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            },
			columns: [{
				header: '收货人',
				dataIndex: 'user_name',
				align: 'center',
				width: 80,
				editor: {
					xtype: 'textfield'
				}
			}, {
				text: '<div style="text-align:center">所在地区</div>',
				columns: [{
					header: '省份',
					dataIndex: 'province_id',
					align: 'center',
					width: 60,
					renderer: function(v, m, record, ri, ci) {
						
						if(!Ext.isEmpty(v)) {
							var index = provinceStore.find('p_id', v);
	                        if(index != -1) {
	                            return provinceStore.getAt(index).data.p_name;  
	                        }
						}
						
                        return v;
					},
					editor: {
						xtype: 'combo',
						store: provinceStore,
						typeAhead: true,
	                    triggerAction: 'all',
					    queryMode: 'local',
					    displayField: 'p_name',
						valueField:'p_id',
						listeners: {
							beforequery: function(e) {
								var combo = e.combo;
								if (!e.forceAll) {
									combo.store.clearFilter();
									var input = e.query;
									var regExp = new RegExp('^' + input + '.*', 'i');
									combo.store.filterBy(function(record, id) {
					                    var text = record.get('code');
					                    return regExp.test(text);
					                });
									
									combo.expand();
					                
					                return false;
								}
							}
						},
					    emptyText: "省份"
					}
				}, {
					header: '市区',
					dataIndex: 'downtown_id',
					align: 'center',
					width: 60,
					editor: {
						xtype: 'combo',
						store: provinceStore,
					    queryMode: 'local',
					    displayField: 'p_name',
					    valueField: 'p_id',
					    emptyText: "市区"
					}
				}, {
					header: '县级',
					dataIndex: 'county_id',
					align: 'center',
					width: 60,
					editor: {
						xtype: 'combo',
						store: provinceStore,
					    queryMode: 'local',
					    displayField: 'p_name',
					    valueField: 'p_id',
					    emptyText: "县级"
					}
				}, {
					header: '乡镇',
					dataIndex: 'township_id',
					align: 'center',
					width: 60,
					editor: {
						xtype: 'combo',
						store: provinceStore,
					    queryMode: 'local',
					    displayField: 'p_name',
					    valueField: 'p_id',
					    emptyText: "乡镇"
					}
				}]
				/*dataIndex: 'order_pay_time',
				width: 180*/
			}, {
				header: '<div style="text-align:center">详细地址</div>',
				dataIndex: 'contact_addr',
				width: 320,
				editor: {
					xtype: 'textfield'
				}
			}, {
				header: '本机号码',
				dataIndex: 'mobile_no',
				align: 'center',
				width: 100
			}, {
				header: '联系电话一',
				dataIndex: 'contact_no',
				align: 'center',
				width: 100,
				editor: {
					xtype: 'textfield'
				}
			}, {
				header: '联系电话二',
				dataIndex: 'contact_no_other',
				align: 'left',
				width: 100,
				editor: {
					xtype: 'textfield'
				}
			}, {
				header: '备注',
				dataIndex: 'memo',
				align: 'left',
				flex: 1,
				editor: {
					xtype: 'textfield'
				}
			}, {
		        xtype: 'actioncolumn',
		        locked: true,
		        width: 120,
		        sortable: false,
		        menuDisabled: true,
		        align: 'center',
		        items: [{
		        	iconCls: 'edit',
		        	tooltip: '修改',
		            handler: 'onModifyClick'
		        }, ' ', {
		        	iconCls:'delete',
		            tooltip: '删除',
		            handler: 'onRemoveClick'
		        }]
		    }],
			plugins: [{
				ptype: 'rowediting',
				/*ptype: 'cellediting',*/
		        clicksToEdit: 2
			}],
			listeners: {
				edit: 'onModifyUserAddress'
			},
		    tbar: [{
		    	text: '添加',
		    	iconCls: 'add',
		    	handler: 'onAddUserAddressBtn'
		    }]
		}];
		
        this.callParent();
	},
	items: [],
	dockedItems: [{
        xtype: 'call-record-toolbar',
        dock: 'top'
    }],
	
	iconCls: 'icon-grid'
});