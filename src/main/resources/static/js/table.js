angular.module('mainApp')
    .controller('tableController', function ($scope, $http, $window, $route, $q) {
       var allGroups = [];
       $http.get("/api/group/getAll")
                 .then(function(response) {
                     if(response.status===200){
                         var requests = [];
                         allGroups = response.data;
                         //$scope.groups = chunk(response.data,3);

                         angular.forEach(response.data, function(value, key) {
                                requests.push($http.get("api/team/getByGroup/"+value.id));
                         });
                         $q.all(requests).then(function(results){
                           for(var i=0;i<requests.length;i++){
                                allGroups[i].teams = results[i].data;
                           }
                           $scope.groups = chunk(allGroups,3);
                           console.log("scope",$scope.groups);
                         });

                     }else{
                        $window.alert(response.statusText);
                     }
       });
 });

 var chunk = function(arr, size) {
    var newArr = [];
       for (var i=0; i<arr.length; i+=size) {
           newArr.push(arr.slice(i, i+size));
       }
    return newArr;
 };