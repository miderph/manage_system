Ext.define('app.controller.Root', {
	extend: 'Ext.app.Controller',

	uses: ['app.view.login.Login'],

	onLaunch: function() {
		Ext.create('app.view.login.Login', {});
	}
});