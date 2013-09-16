function MainCtrl(Navigation, WorkgroupService, $filter, $route, $routeParams, $scope, $location, $http) {

  $scope.WorkgroupService = WorkgroupService;
  $scope.workgroups = WorkgroupService.workgroups;
  
  $scope.init = function() {
    
  };
  
  $scope.$on('WorkgroupLoaded', function(event, msg) {
    $scope.initWorkgroups();
  });
  
  $scope.initWorkgroups = function() {
    $scope.workgroups = WorkgroupService.workgroups;
  };
  
  $scope.getBadgeClass = function(region) {
    region = $filter('lowercase')(region);
    if (region == 'central') return 'label-primary';
    if (region == 'eastern') return 'label-success';
    if (region == 'northern') return 'label-info';
    if (region == 'southern') return 'label-warning';
    if (region == 'western') return 'label-danger';
    return 'label-default';
  };
}