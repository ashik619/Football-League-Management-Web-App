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
var database = firebase.database();
var app = angular.module('tableApp', []);
app.controller('tableController', function($scope, $window,$http,$q) {
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
  database.ref('logo_url').on('value', function(snapshot) {
      document.getElementById("leagueLogoImg").src = snapshot.val();
  });

  database.ref('league_name').on('value', function(snapshot) {
        document.getElementById("leagueName").innerHTML = snapshot.val();
  });

});
var chunk = function(arr, size) {
    var newArr = [];
       for (var i=0; i<arr.length; i+=size) {
           newArr.push(arr.slice(i, i+size));
       }
    return newArr;
};
