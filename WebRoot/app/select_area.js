var SelectArea={
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
	show : function (callbackIds,callbackNames,form) {
		var provStore = new Ext.data.JsonStore({
			fields: ['id','name','description'],
			autoLoad : true,
			proxy: {
				type: 'ajax',
				url : 'area/query_prov.do',
				reader: {
					totalProperty : "total",
					root : 'data'
				},
				actionMethods:{
					read: 'POST'
				}
			},
			sortInfo : {// 排序
	            field : 'id',
	            direction : 'DESC'
	        }
	    });
	    var cityStore = new Ext.data.JsonStore({
			fields: ['id','pname','name','description'],
			autoLoad : true,
			proxy: {
				type: 'ajax',
				url : 'area/query_city.do',
				reader: {
					totalProperty : "total",
					root : 'data'
				},
				actionMethods:{
					read: 'POST'
				}
			},
			sortInfo : {// 排序
	            field : 'id',
	            direction : 'DESC'
	        }
	    });

		callbackIds =form.form.findField(callbackIds);
		callbackNames =form.form.findField(callbackNames)
		var old_ids = ','+callbackIds.getValue()+',';
		var old_names = callbackNames.getValue();

		var prov_grid = new Ext.grid.GridPanel({
			frame:true,
			loadMask:{
				msg:'数据加载中...'
			},
			//width:270,
			height:400,
			//autoHeight: true,
			collapsible:true,
			titleCollapse:'true',
			title:'省份列表过滤',
			iconCls:'icon-grid',
			//bodyStyle:'width:100%', 
			//viewConfig: {forceFit:true}, 
			//stripeRows:true,
			enableHdMenu:false,
			store: provStore,
			selModel: {selType: 'checkboxmodel'},
			columns: [
			          { header: "ID", width: 80, dataIndex: 'id' },
			          { header: "省", width: 80, dataIndex: 'pname'}
			]
		});
		var city_grid = new Ext.grid.GridPanel({
			frame:true,
			loadMask:{
				msg:'数据加载中...'
			},
			//width:300,
			height:400,
			autoHeight: true,
			collapsible:true,
			titleCollapse:'true',
			title:'城市列表过滤',
			iconCls:'icon-grid',
			//bodyStyle:'width:100%', 
			//viewConfig: {forceFit:true}, 
			//stripeRows:true,
			enableHdMenu:false,
			store: cityStore,
			selModel: {selType: 'checkboxmodel'},
			columns: [
			          { header: "ID", width: 80, dataIndex: 'id' },
			          { header: "省", width: 80, dataIndex: 'pname'},
			          { header: "市", width: 120, dataIndex: 'name'},
			          { header: "市", width: 0, dataIndex: 'description',hidden:true}
			]
		});
		var setData = function () {
			setOneGridData(prov_grid);
			setOneGridData(city_grid);
		}
		function setOneGridData(grid) {
			var model = grid.getSelectionModel();
			var iCount = grid.store.getCount();
			for (var ii = 0; ii < iCount; ii++){
				var id = grid.store.getAt(ii).get("id");
				if (old_ids.indexOf(','+id+',')>=0){
					model.selectRange(ii,ii,true);
				}else{
					model.deselectRange(ii,ii);
				}
					
			}
		}
		provStore.load();cityStore.load();
		provStore.on('load',function (){
			setOneGridData(prov_grid);
		});
		cityStore.on('load',function (){
			setOneGridData(city_grid);
		});

	    var form = new Ext.FormPanel({    
	    	border : true,    
	    	frame : true,  
	    	fieldDefaults:{
	    		labelAlign : "right"
	    	},
	    	buttonAlign : 'center',    
	    	//layout : 'form',    
	    	width : 690,
	    	items : [{
   		   		layout: 'column',
				border: false,
				baseCls:'x-plain',
   		    	items : [
   		    		{
   		    			columnWidth:.4,
						layout:'form',
						baseCls:'x-plain',
						border: false,
	   		    		items: [prov_grid]
   		    		},{
   		    			columnWidth:.6,
						layout:'form',
						border: false,
						baseCls:'x-plain',
   		    			bodyStyle:'padding:0 0 0 10',
   		    			items: [city_grid]
   		    		}
   		    	]
   		    }],
	    	buttons : [{    
	    		xtype : 'button',
	    		text : '确定',    
	    		handler : function() {
	    			var ids = [];
	    			var names = [];
	    			var model = prov_grid.getSelectionModel();
	    			var iCount = prov_grid.store.getCount();
	    			for (var ii = 0; ii < iCount; ii++){
	    				if (model.isRangeSelected(ii,ii)){
	    					ids.push(prov_grid.store.getAt(ii).get("id"));
	    					names.push(prov_grid.store.getAt(ii).get("pname"));
	    				}
	    			}

	    			model = city_grid.getSelectionModel();
	    			iCount = city_grid.store.getCount();
	    			for (var ii = 0; ii < iCount; ii++){
	    				if (model.isRangeSelected(ii,ii)){
	    					ids.push(city_grid.store.getAt(ii).get("id"));
	    					//names.push(city_grid.store.getAt(ii).get("pname")+city_grid.store.getAt(ii).get("name"));
	    					names.push(city_grid.store.getAt(ii).get("name"));
	    				}
	    			}

	    			win.destroy();
	    			//Ext.Msg.alert("消息", "选中状态的id组合字符串为:" + ids.toString() + "\n"+names.toString());   
	    			callbackIds.setValue(ids.toString());
	    			callbackNames.setValue(names.toString());
	    		}    

	    	}, {
	    		xtype : 'button',    
	    		text : '取消',    
	    		handler : function() {    
	    			win.destroy();    
	    		}    
	    	}, {
	    		xtype : 'button',    
	    		text : '重置',
	    		handler : setData
	    	}]    

	    });
	    var win = new Ext.Window({    
	    	modal : true,    
	    	layout : 'fit',    
	    	title : "选择地区",
	    	width : 700,    
	    	height : 470,    
	    	plain : true,    
	    	items : [form]    
	    });
	    win.show();    
	}
}
