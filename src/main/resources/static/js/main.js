var mainApp = angular.module('mainApp', ['ngRoute']);

mainApp.config(function($routeProvider) {
            $routeProvider
                .when('/', {
                    templateUrl : 'pages/home.html',
                    controller  : 'homeControlle'
                })
                .when('/teams', {
                    templateUrl : 'pages/teams.html',
                    controller  : 'teamController'
                })
                .when('/matches', {
                    templateUrl : 'pages/matches.html',
                    controller  : 'matchController'
                })
                .when('/table', {
                    templateUrl : 'pages/table.html',
                    controller  : 'tableController'
                })
                .when('/matchA', {
                    templateUrl : 'pages/amatch.html',
                    controller  : 'matchAAdminController'
                })
                .when('/matchB', {
                    templateUrl : 'pages/bmatch.html',
                    controller  : 'matchBAdminController'
                })
                .when('/groups', {
                    templateUrl : 'pages/group.html',
                    controller  : 'groupController'
                });

    });


mainApp.controller('mainController', function($scope, $http, $route, $window) {
    $http.get("/api/auth/userAuth")
             .then(function(response) {
                 console.log(response);
                 if(response.status===200){
                    if(response.data.message === "success"){
                        document.getElementById('loginDialog').style.display='none';
                    }else{
                        document.getElementById('loginDialog').style.display='block';
                    }
                 }
             },function(errResp) {
                     document.getElementById('loginDialog').style.display='block';
                     $window.alert(errResp.data.message);
             }
    );

    $scope.loginUser = function() {
        if(!$scope.username) {
            $window.alert('Enter Username');
        }else if(!$scope.password){
            $window.alert('Enter Password');
        } else{
            $http.post('/api/auth/login',
                  {
                    'u_name' : $scope.username,
                    'pass' : $scope.password
                   })
                 .then(function(response) {
                    if(response.data.data==="true"){
                        document.getElementById('loginDialog').style.display='none';
                        $window.alert(response.data.message);
                        $route.reload();
                    }else{
                        $window.alert(response.data.message);
                    }
                 },function(errResp) {
                         $window.alert(errResp.data.message);
                 }
            );

        }
    };
});
