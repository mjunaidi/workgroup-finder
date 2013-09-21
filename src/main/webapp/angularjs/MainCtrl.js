function MainCtrl(Navigation, WorkgroupService, filesService, UserService, AboutService, $filter, $route, $routeParams, $scope, $location, $http) {

  $scope.WorkgroupService = WorkgroupService;
  $scope.workgroups = WorkgroupService.workgroups;
  
  $scope.UserService = UserService;
  $scope.login = UserService.login;
  $scope.isLoggedIn = false;
  
  $scope.filesService = filesService;
  $scope.uploadedFiles = filesService.uploadedFiles;
  $scope.fileUploadResponse = false;
  $scope.theFile = false;
  $scope.fileUploadForm = false;
  
  $scope.AboutService = AboutService;
  $scope.about = AboutService.about;
  
  $scope.isIe = navigator.userAgent.indexOf('MSIE') >= 0;
  
  $scope.init = function() {
    $scope.initThemes();
  };
  
  /* Workgroup */
  $scope.$on('WorkgroupLoaded', function(event, msg) {
    $scope.initWorkgroups();
  });
  
  $scope.initWorkgroups = function() {
    $scope.workgroups = WorkgroupService.workgroups;
  };
  
  /* Login */
  $scope.$on('LoginChange', function(event, msg) {
    $scope.initLogin();
    
    // redirect to index page when logout
    if ($scope.login) {
      if (!$scope.login.isLoggedIn) {
        if ($location.url() == '/manage') {
          $location.url('/');
        }
      }
    }
  });
  
  $scope.initLogin = function() {
    $scope.login = UserService.login;
    if (!$scope.login) {
      $scope.isLoggedIn = false;
    } else {
      $scope.isLoggedIn = $scope.login.isLoggedIn;
    }
  };
  
  $scope.doLogin = function() {
    console.log('doLogin -->');
    if (this.username && this.password) {
      var _form = jQuery('#loginForm');
      if (_form.length > 0) {
        UserService.doLogin(_form);
      }
    }
  };
  
  $scope.doLogout = function() {
    console.log('doLogout -->');
    UserService.doLogout();
  }

  /* Files */
  $scope.$on('FilesChange', function(event, msg) {
    $scope.initUploadedFiles();
    $scope.initFileUploadResponse();
  });
  
  $scope.initUploadedFiles = function() {
    $scope.uploadedFiles = filesService.uploadedFiles;
  };
  
  $scope.initFileUploadResponse = function() {
    $scope.fileUploadResponse = filesService.response;
    $scope.resetUploadForm();
  };
  
  $scope.resetForm = function() {
    if (!$scope.fileUploadForm) {
      $scope.fileUploadForm = this;
      $scope.resetUploadForm();
      $scope.fileUploadForm = false;
    } else {
      $scope.resetUploadForm();
    }
  };
  
  // TODO: Resetting the upload form, below is not a good practice
  $scope.resetUploadForm = function() {
    $scope.theFile = false;
    //jQuery('#uploadForm')[0].reset();
    var _form = jQuery('#uploadForm');
    
    if (_form.length > 0) {
      _form[0].reset();
    }
    
    // somehow theFile is inside the form's scope
    if ($scope.fileUploadForm) {
      if ($scope.fileUploadForm.theFile) {
        $scope.fileUploadForm.theFile = false;
      }
    }
  };
  
  $scope.submitUploadForm = function() {
    $scope.fileUploadForm = this;
    if (this.theFile) {
      $scope.theFile = this.theFile;
      filesService.uploadFile(this.theFile);
    }
  };
  
  // alternative method to submitUploadForm
  $scope.uploadFile = function(file) {
    if (file) {
      filesService.uploadFile(file);
    }
  };
  
  $scope.deleteUploadedFile = function(filename) {
    if (filename) {
      filesService.deleteUploadedFile(filename);
    }
  };
  
  /* About */
  $scope.$on('AboutLoaded', function(event, msg) {
    $scope.initAbout();
  });
  
  $scope.initAbout = function() {
    $scope.about = AboutService.about;
  };
  
  /* Themes */
  $scope.initThemes = function() {
    $scope.themes = ["default", "amelia", "cerulean", "cosmo", "cyborg", "flatly", "journal", "readable", "simplex", "slate", "spacelab", "united"]; // zero-index
    $scope.themeIndex = 2; // set initial theme index
    if ($scope.isIe) {
      $scope.themes = _.without($scope.themes, "cerulean", "slate", "spacelab");
    }
    $scope.themeCount = $scope.themes.length;
    
    if ($scope.themeIndex >= $scope.themeCount) {
      $scope.themeIndex = 0;
    }
    if ($scope.themeCount > 0) {
      $scope.theme = $scope.themes[$scope.themeIndex];
    }
  };
  
  $scope.setTheme = function(theme) {
    $scope.theme = theme;
  };
  
  /**
   * Register below method to ng-click in order to have the theme change on a click of a button.
   */
  $scope.changeTheme = function() {
    $scope.themeIndex = $scope.themeIndex + 1;
    if ($scope.themeIndex >= $scope.themeCount) {
      $scope.themeIndex = 0;
    }
    $scope.setTheme($scope.themes[$scope.themeIndex]);
  };

  /* Misc */
  $scope.getBadgeClass = function(region) {
    region = $filter('lowercase')(region);
    if (region == 'central') return 'label-primary';
    if (region == 'eastern') return 'label-success';
    if (region == 'northern') return 'label-info';
    if (region == 'southern') return 'label-warning';
    if (region == 'western') return 'label-danger';
    if (region == 'sabah') return 'label-danger';
    if (region == 'sarawak') return 'label-default';
    return 'label-default';
  };
  $scope.getAlertClass = function(status) {
    status = $filter('uppercase')(status);
    if (status == 'OK') return 'alert-success';
    if (status == 'FAIL') return 'alert-danger';
    return 'alert-warning';
  };
}