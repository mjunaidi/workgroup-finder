'use strict';

/* Directives */
var userModule = angular.module('myApp.user', []);

userModule.run(['UserService', function(UserService) {
  UserService.init();
}]);

userModule.factory('UserService', ['$rootScope', '$http', '$location', 'Storage', function($rootScope, $http, $location, Storage) {
  var UserService = {};

  UserService.login = false;
  UserService.dir = 'data/';
  UserService.url = UserService.dir + 'users.json';
  
  UserService.init = function() {
  };
  
  UserService.loginChanged = function(loaded) {
    $rootScope.$broadcast('LoginChange');
  };
  
  UserService.doLogin = function(username, password) {
    var url = UserService.url;
    $http.get(url).
      success(function(data, status, headers, config) {
        console.log(data);
        
        UserService.setLogin(data);
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  return UserService;
  
}]);