Ext.application({
	name: 'app',
	
	controllers: ['Root'],
	
	launch: function() {
		Ext.getDoc().on('contextmenu', function(e, t, eOpts) {
    		e.stopEvent();
    	})
	}
});