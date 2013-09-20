'use strict';

/* Directives */
var aboutModule = angular.module('myApp.about', []);

aboutModule.run(['AboutService', function(AboutService) {
  AboutService.init();
}]);

aboutModule.factory('AboutService', ['$rootScope', '$http', '$location', 'Storage', function($rootScope, $http, $location, Storage) {
  var AboutService = {};

  AboutService.url = "/contentdata/about.json";
  AboutService.about = false;
  
  AboutService.init = function() {
    AboutService.loadAbout();
  };
  
  AboutService.aboutLoaded = function(loaded) {
    $rootScope.$broadcast('AboutLoaded');
  };
  
  AboutService.processCredits = function(about) {
    var credits = about.credits;
    var links = about.links;
    var template = '<a href="{url}" target="_blank">{label}</a>';
    
    for (var i in links) {
      var link = links[i];
      var a = template.replace('{url}', link.url).replace('{label}', link.name);
      credits = credits.replace(link.name, a);
    }
      
    return credits;
  };
  
  AboutService.setAbout = function(about) {    
    about.credits = AboutService.processCredits(about)
    
    AboutService.about = about;
    AboutService.aboutLoaded();
  };
  
  AboutService.loadAbout = function() {
    var url = AboutService.url
    $http.get(url).
      success(function(data, status, headers, config) {        
        AboutService.setAbout(data);
      }).
      error(function(data, status, headers, config) {
        console.log(data);
      });
  };
  
  return AboutService;
  
}]);