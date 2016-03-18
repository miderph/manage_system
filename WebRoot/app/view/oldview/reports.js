Ext.define('app.view.oldview.reports', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.reports',
    initComponent: function() {
	//加上其它功能
	var urlconst = ReportUri;
	//报表地址
	//  var urlconst = "http://tv.duolebo.com/QDGH_birtApp/frameset?";//(原来配置)
	//	var urlconst = "http://192.168.1.145:8088/birtApp/frameset?";
	//	var urlconst = "http://192.168.1.167:8088/birtApp/frameset?";//报表地址
	var widthSize = document.body.clientWidth - 207;
	var heightSize = document.body.clientHeight - 53;
	var intervalHeight = 18;
	var pgSize = 10;
	//分页数
	var reportNameData = [
		['0','应用下载合计报表'],
		['1','应用下载日报表'],
		['2','应用下载明细报表'],
		['3','访问日志明细报表'],
		['4','用户记录明细报表'],
	];
	var reportNameStore = new Ext.data.SimpleStore({
		data: reportNameData,
		fields: ['value','text']
	});
	var reportNameComboBox = new Ext.form.ComboBox({
		store: reportNameStore,
		triggerAction: 'all',
		displayField: 'text',
		valueField: 'value',
		//id:'report_name',
		name: 'report_name',
		queryMode: 'local',
		width: 250,
		//readOnly:true,
		editable: false,
		fieldLabel: '报表名称',
		emptyText: '请选择报表名称',
		value: '0',
		listeners: {
			'select': function () {
				if (this.getValue() == 0) {
					reportTypeComboBox.show();
					reportTypeComboBox.setValue('0');
					app_name_form.show();
					tvbox_number_form.hide();
					channel_form.hide();
					changelable();
				} else if (this.getValue() == 1) {
					reportTypeComboBox.hide();
					
					app_name_form.show();
					
					tvbox_number_form.hide();
					channel_form.hide();
					changelable();
				} else if (this.getValue() == 2) {
					reportTypeComboBox.hide();
					
					app_name_form.show();
					
					tvbox_number_form.show();
					channel_form.hide();
					changelable();
				} else if (this.getValue() == 3) {
					reportTypeComboBox.hide();
					
					app_name_form.show();
					
					tvbox_number_form.show();
					channel_form.hide();
					changelable();
					resetdata();
				} else {
					reportTypeComboBox.hide();
					
					app_name_form.show();
					
					tvbox_number_form.show();
					channel_form.show();
					changelable(1)
				}
				resetdata();
			}
		}
	});
	var resetdata = function () {
		app_name_form.setValue('');
		tvbox_number_form.setValue('');
		
	}
	var changelable = function (type) {
		if(type == 1){
			app_name_form.setFieldLabel('设备型号:');
			tvbox_number_form.setFieldLabel('芯片型号:');
		}else{
			app_name_form.setFieldLabel('应用名称:');
			tvbox_number_form.setFieldLabel('机顶盒硬件编号:');
		}
	}
	var reportTypeData = [
		['0','列表'],
		['1','柱状图']
	];
	var reportTypeStore = new Ext.data.SimpleStore({
		data: reportTypeData,
		fields: [ 'value','text']
	});
	var reportTypeComboBox = new Ext.form.ComboBox({
		store: reportTypeStore,
		triggerAction: 'all',
		displayField: 'text',
		valueField: 'value',
		queryMode: 'local',
		//readOnly:true,
		editable: false,
		//id: 'report_type',
		name: 'report_type',
		//width:90,
		fieldLabel: '报表类型',
		emptyText: '请选择报表类型',
		value: '列表',
		listeners: {
			'select': function () {
				if (this.getValue() == 0) {
					app_name_form.show();
				} else {
					app_name_form.hide();
				}
			}
		}
	});
	var s_starttime_form = new Ext.form.DateField({
		xtype: 'datefield',
		format: 'Ymd',
		//      id: 's_starttime',
		name: 's_starttime',
		width: 150,
		fieldLabel: '开始时间',
		allowBlank: false,
		//readOnly: true,
		grow: true,
		blankText: '请选择开始时间'
	});
	var s_endtime_form = new Ext.form.DateField({
		xtype: 'datefield',
		format: 'Ymd',
		//  	id: 's_endtime',
		name: 's_endtime',
		width: 150,
		fieldLabel: '结束时间',
		allowBlank: false,
		//readOnly: true,
		grow: true,
		blankText: '请选择结束时间'
	});
	var app_name_form = new Ext.form.TextField({
		xtype: 'textfield',
		fieldLabel: ' 应用名称',
		blankText: '请填写应用名称',
		//id: 'app_name',
		name: 'app_name',
		width: 150
	});
	var tvbox_number_form = new Ext.form.TextField({
		xtype: 'textfield',
		fieldLabel: '机顶盒硬件编号',
		blankText: '请填写机顶盒硬件编号',
		//id: 'tvbox_number',
		name: 'tvbox_number',
		width: 150
	});
	
	var channel_form = new Ext.form.TextField({
		xtype: 'textfield',
		fieldLabel: '渠道编号',
		blankText: '请填写渠道编号',
		//id: 'tvbox_number',
		hidden: true,
		name: 'channel',
		width: 150
	});
	
	/*-----定义搜索方法,用于重置搜索条件，并重新加载树，以实现按条件搜索相应栏目-----*/
	var search_method = function () {
		var reportName_id = reportNameComboBox.getValue();
		var starttime = Ext.util.Format.date(s_starttime_form.getValue(), 'Ymd');
		var endtime = Ext.util.Format.date(s_endtime_form.getValue(), 'Ymd');
		var reportType = reportTypeComboBox.getValue();
		var app_name = Ext.String.trim(app_name_form.getValue());
		var tvbox_number = tvbox_number_form.getValue();
		var deviceId = app_name_form.getValue();
		var chipModelNum = tvbox_number_form.getValue();
		var channel = channel_form.getValue();
		if (starttime > endtime) {
			Ext.MessageBox.show({
				title: '提示',
				msg: '开始时间不能晚于结束时间',
				width: 180,
				buttons: Ext.Msg.OK
			});
			return null;
		}
		var r_type;
		if (reportType == 0) {
			r_type = 'table';
		} else {
			r_type = 'bar';
		}
		if (reportName_id == 0) {
			Ext.get('main') .dom.src = urlconst + '__report=download_log_count.rptdesign&starttime=' + starttime + '&endtime=' + endtime + '&app_name=' + escape(encodeURI(app_name)) + '&r_type=' + r_type;
		} else if (reportName_id == 1) {
			if (app_name == null || app_name == '') {
				Ext.MessageBox.show({
					title: '提示',
					msg: '应用名称不能为空',
					width: 180,
					buttons: Ext.Msg.OK
				});
				return null;
			}
			Ext.get('main') .dom.src = urlconst + '__report=download_log_count_detail.rptdesign&starttime=' + starttime + '&endtime=' + endtime + '&app_name=' + escape(encodeURI(app_name));
		} else if (reportName_id == 2) {
			Ext.get('main') .dom.src = urlconst + '__report=download_log_count_records.rptdesign&starttime=' + starttime + '&endtime=' + endtime + '&app_name=' + escape(encodeURI(app_name)) + '&r_type=table&tvid=' + escape(encodeURI(tvbox_number));
		} else if (reportName_id == 3) {
			Ext.get('main') .dom.src = urlconst + '__report=access_log_records.rptdesign&starttime=' + starttime + '&endtime=' + endtime + '&app_name=' + escape(encodeURI(app_name)) + '&r_type=table&tvid=' + escape(encodeURI(tvbox_number));
		} else if (reportName_id == 4) {
			Ext.get('main') .dom.src = urlconst + '__report=user_records.rptdesign&starttime=' + starttime + '&endtime=' + endtime + '&deviceId=' + deviceId + '&chipModelNum=' + chipModelNum+'&channel='+channel;
		}
		//      Ext.get('main').dom.src='http://tv.duolebo.com/QDGH_birtApp/frameset?__report=download_log_count.rptdesign&starttime='+s_starttime_form.getValue().format("Ymd") +'&endtime='+s_endtime_form.getValue().format("Ymd")+'&r_type=table&app_name=&__sessionId=20140325_101535_259';
		//        alert(s_starttime_form.getValue().format("Ymd")+":"+ s_endtime_form.getValue().format("Ymd"));

	};
	var logreportPanel = new Ext.Panel({
		title: '查询条件',
		frame: true,
		collapsible: true,
		animate: true,
		border: true,
		titleCollapse: 'true',
		region: 'north',
		//height:170,
		bodyStyle: 'padding:0 0 0 0',
		autoHeight: true,
		fieldDefaults: {
			labelAlign: 'center'
		},
		defaults: {
			baseCls: 'x-plain'
		},
		frame: true,
		border: false,
		items: [
			{
				xtype: 'panel',
				//layout:'form',
				border: false,
				bodyPadding: '0 10 0 10',
				defaults: {
					baseCls: 'x-plain'
				},
				items:
				[
					{
						xtype: 'panel',
						layout: 'column',
						border: false,
						items:
						[
							{
								columnWidth: 0.25,
								layout: 'form',
								border: false,
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 0',
								items: [
									reportNameComboBox
								]
							},
							{
								columnWidth: 0.25,
								layout: 'form',
								border: false,
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 0',
								items: [
									reportTypeComboBox
								]
							}
						]
					},
					{
						xtype: 'panel',
						layout: 'column',
						border: false,
						items:
						[
							{
								columnWidth: 0.25,
								layout: 'form',
								baseCls: 'x-plain',
								border: false,
								bodyStyle: 'padding:6 0 0 0',
								items: [
									s_starttime_form
								]
							},
							{
								columnWidth: 0.25,
								layout: 'form',
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 0',
								items: [
									s_endtime_form
								]
							},
							{
								columnWidth: 0.25,
								layout: 'form',
								baseCls: 'x-plain',
								border: false,
								bodyStyle: 'padding:6 0 0 0',
								items: [
									app_name_form
								]
							}
						]
					},
					{
						xtype: 'panel',
						layout: 'column',
						border: false,
						items:
						[
							{
								columnWidth: 0.25,
								layout: 'form',
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 0',
								items: [
									tvbox_number_form
								]
							}
							,{
								columnWidth: 0.25,
								layout: 'form',
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 0',
								border: false,
								items: [
									channel_form
								]
							},
							{
								columnWidth: 0.08,
								layout: 'form',
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 10',
								items: [
									{
										xtype: 'button',
										text: '查询',
										height: 30,
										waitMsg: '正在处理,请稍等',
										handler: search_method
									}
								]
							},
							{
								columnWidth: 0.08,
								layout: 'form',
								baseCls: 'x-plain',
								bodyStyle: 'padding:6 0 0 0',
								border: false,
								items: [
									{
										xtype: 'button',
										text: '重置',
										height: 30,
										handler: function () {
											s_starttime_form.setValue(lastMonthYestdy(new Date()));
											s_endtime_form.setValue(new Date());
											if (reportNameComboBox.getValue() == 0 && reportTypeComboBox.getValue() == 1) {
												reportTypeComboBox.setValue(0);
												app_name_form.show();
												app_name_form.getEl() .up('.x-form-item') .setDisplayed(true);
											}
											app_name_form.setValue('');
											tvbox_number_form.setValue('');
										}
									}
								]
							},
						]
					}
				]
			}
		]
	});
	var centerPanel = new Ext.FormPanel({
		region: 'center',
		plain: true,
		frame: true,
		baseCls: 'x-plain',
		bodyStyle: 'padding:6 0 0 0',
		items: [
			{
				title: '报表内容',
				//	         id:'report',  
				iconCls: 'tabs',
				autoScroll: true,
				autoWidth: true,
				closable: true,
				frame: true,
				html: '<iframe  id="main" name="main"src="' + '' + '" width="100%" height="400" frameborder="0" scrolling="auto"></iframe>'
			}
		],
		defaults: {
			autoScroll: true
		}
	});
	var accesslogreportPanel = new Ext.Panel({
		plain: true,
		frame: true,
		width: widthSize,
		height: heightSize,
		autoScroll: true,
		layout: 'form',
//		renderTo: 'report-basic',
		items: [
			logreportPanel,
			centerPanel,
		]
	});
	var lastMonthYestdy = function getLastMonthYestdy(date) {
        var daysInMonth = new Array([0],[31],[28],[31],[30],[31],[30],[31],[31],[30],[31],[30],[31]);
		var strYear = date.getFullYear();
		var strDay = date.getDate();
		var strMonth = date.getMonth() + 1;
		if (strYear % 4 == 0 && strYear % 100 != 0) {
			daysInMonth[2] = 29;
		}
		if (strMonth - 1 == 0)
		{
			strYear -= 1;
			strMonth = 12;
		} 
		else
		{
			strMonth -= 1;
		}
		strDay = daysInMonth[strMonth] >= strDay ? strDay : daysInMonth[strMonth];
		if (strMonth < 10)
		{
			strMonth = '0' + strMonth;
		}
		if (strDay < 10)
		{
			strDay = '0' + strDay;
		}
//		datastr = strYear+strMonth+strDay;
		datastr = strYear + ""+strMonth + ""+strDay;
		return datastr;
	}
	s_starttime_form.setValue(lastMonthYestdy(new Date()));
	s_endtime_form.setValue(new Date());
	//Ext.get('main') .dom.src = urlconst + '__report=download_log_count.rptdesign&starttime=' + Ext.util.Format.date(s_starttime_form.getValue(), 'Ymd') + '&endtime=' + Ext.util.Format.date(s_endtime_form.getValue(), 'Ymd') + '&r_type=table&app_name=' + escape(encodeURI(''));
	tvbox_number_form.hide();
	
	this.items = [accesslogreportPanel];
	this.callParent();
    }
});