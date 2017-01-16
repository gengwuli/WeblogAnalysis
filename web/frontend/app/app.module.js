(function() {
	'use strict';

	angular
		.module('app', ['ngRoute', 'ngResource'])	
		.config(config)

	config.$inject = ['$routeProvider']
	function config($routeProvider) {
		$routeProvider
			.when('/main', {
				templateUrl: 'app/main.html',
				controller: 'MainController',
				controllerAs: 'vm'
			})
			.otherwise({ redirectTo: '/main'})
	}

})()