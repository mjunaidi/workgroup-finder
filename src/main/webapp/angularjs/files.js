'use strict';

/* Directives */
var filesModule = angular.module('myApp.files', []);

filesModule.run(['filesService', function(filesService) {
  filesService.init();
}]);

filesModule.factory('filesService', ['$rootScope', '$http', '$location', 'Storage', function($rootScope, $http, $location, Storage) {
  var filesService = {};
  
  filesService.uploadedFiles = false;
  filesService.response = false;
  filesService.url = "/ajax/";
  
  filesService.init = function() {
    filesService.fetchUploadedFiles();
  };
  
  filesService.filesChanged = function(loaded) {
    $rootScope.$broadcast('FilesChange');
  };
  
  filesService.setUploadedFiles = function(files) {
    filesService.uploadedFiles = files;
    filesService.filesChanged();
  };
  
  filesService.fetchUploadedFiles = function() {
    var url = filesService.url + "api/uploadedFiles";
    $http.get(url).
      success(function(data, status, headers, config) {
        filesService.setUploadedFiles(data);
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  filesService.uploadFile = function(file) {
    var url = filesService.url + "uploadFile";
    
    var fd = new FormData();
    fd.append("file", file);
    
    var config = {
      withCredentials: true,
      headers: {'Content-Type': undefined },
      transformRequest: angular.identity
    };
    
    $http.post(url, fd, config).
      success(function(data, status, headers, config) {
        filesService.response = data;
        filesService.fetchUploadedFiles();
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  filesService.deleteUploadedFile = function(filename) {
    var url = filesService.url + "uploadFile/delete/" + filename;
    
    $http.get(url).
      success(function(data, status, headers, config) {
        filesService.response = data;
        filesService.fetchUploadedFiles();
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  return filesService;
  
}]);

filesModule.directive('ngFileupload', function($q) {
  return {
    restrict: 'A',
    scope: {
      ngFileupload: '='
    },
    link: function postLink(scope, element, attrs, ctrl) {
      var applyScope = function(file) {
        console.log(file);
        console.log(scope.ngFileupload);
        console.log(scope.theFile);
        scope.ngFileupload = file;
        scope.$apply(function() {
        });
        scope.$apply();
        console.log(scope.ngFileupload);
        console.log(scope.theFile);
      };
      element.bind('change', function (evt) {
        var files = evt.target.files;
        for(var i = 0; i < files.length; i++) {
          //applyScope(files[i]);
          scope.$apply(function() {
            scope.ngFileupload = files[i];
          });
        }
      });
    }
  };
});