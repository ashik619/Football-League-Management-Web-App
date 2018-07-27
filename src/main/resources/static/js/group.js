angular.module('mainApp')
    .controller('groupController', function ($scope, $http, $window, $route) {
       $http.get("/api/group/getAll")
                 .then(function(response) {
                     console.log(response);
                     if(response.status===200){
                        $scope.groups = response.data;
                     }else{
                        $window.alert(response.statusText);
                     }
        });
        $scope.addGroup = function() {
            if($scope.new_group_name){
                $http.post('/api/group/addNew',  { 'name' : $scope.new_group_name })
                     .then(function(response) {
                        if(response.status===200){
                            $window.alert(response.data.message);
                            $route.reload();
                        }else{
                            $window.alert(response.data.message);
                        }
                });
            }else {
                $window.alert("Enter new group name");
            }

        };
        $scope.deleteGrp = function(idx) {
                deleteGrpConfirm = $window.confirm('Are you sure you want to delete the Group?');
                if(deleteGrpConfirm){
                    var group = $scope.groups[idx]
                    $http.delete('/api/group/delete/'+group.id).then(function(response){
                            $window.alert(response.data.message);
                            $route.reload();
                    });
                }
        };
 });