var config = {
    apiKey: "AIzaSyAriDVg_p835kTHgEp2K-DKeogJpq_G9N4",
    authDomain: "corporate-super-league.firebaseapp.com",
    databaseURL: "https://corporate-super-league.firebaseio.com/",
    storageBucket: "corporate-super-league.appspot.com"
};
var firebaseApp;
try {
  firebaseApp = firebase.app();
}catch(e) {
  firebaseApp = firebase.initializeApp(config);
}

var storage = firebase.storage();
var storageRef = firebase.storage().ref();

var logoUrl = null;
function teamLogoFile(){
    var file = document.getElementById("teamFileId").files[0];
    const suffix = makeid();
    console.log(suffix+file.name);
    var teamLogoRef = storageRef.child(file.name);
    teamLogoRef.put(file).then(function(snapshot) {
      teamLogoRef.getDownloadURL().then(function(url) {
        alert("Logo Uploaded!!");
        logoUrl = url;
      });
    });
}


angular.module('mainApp')
    .controller('teamController', function ($scope, $http, $window, $route) {
       $http.get("/api/team/getAll")
                 .then(function(response) {
                     if(response.status===200){
                        $scope.teams = response.data;
                     }else{
                        $window.alert(response.statusText);
                     }
        });
        $http.get("/api/group/getAll")
                         .then(function(response) {
                             if(response.status===200){
                                $scope.groups = response.data;
                             }else{
                                $window.alert(response.statusText);
                             }
                });
        $scope.addGroup = function() {
                if(logoUrl!=null){
                    if($scope.selectedGroup){
                        $http.post('/api/team/addNew',
                                {
                                'name' : $scope.new_group_name,
                                'group_id' : $scope.selectedGroup.id,
                                'url' : logoUrl,
                                 }
                             ).then(function(response) {
                                console.log(response);
                                if(response.status===200){
                                    $route.reload();
                                    $window.alert(response.data.message);
                                }else{
                                    $window.alert(response.data.message);
                                }
                        });
                    }else $window.alert("Select group!!");
                }else{
                    $window.alert("Upload team logo!!");
                }
        };
        $scope.deleteTeam = function(idx) {
                deleteGrpConfirm = $window.confirm('Are you sure you want to delete the Team?');
                if(deleteGrpConfirm){
                    //var team = $scope.teams[idx]
                    $http.delete('/api/team/delete/'+idx).then(function(response){
                            $window.alert(response.data.message);
                            $route.reload();
                    });
                }
        };
});

function makeid() {
   var text = "";
   var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
   for (var i = 0; i < 5; i++)
     text += possible.charAt(Math.floor(Math.random() * possible.length));
   return text;
}