var config = {
    apiKey: "AIzaSyAriDVg_p835kTHgEp2K-DKeogJpq_G9N4",
    authDomain: "corporate-super-league.firebaseapp.com",
    databaseURL: "https://corporate-super-league.firebaseio.com/",
    storageBucket: "corporate-super-league.appspot.com"
};
var afirst = true;
var bfirst = true;
var firebaseApp;
try {
  firebaseApp = firebase.app();
}catch(e) {
  firebaseApp = firebase.initializeApp(config);
}
var database = firebase.database();
var app = angular.module('bannerTwoApp', []);
app.controller('bannerTwoCtrl', function($scope, $window,$http) {
  $http.get("/api/match/getAllRunning")
                   .then(function(response) {
                       if(response.status===200){
                          if(response.data[0]){

                              if(response.data[0].matchSlot=='B'){
                                  $scope.showMatch = true;
                                  $scope.match = response.data[0];
                              }
                          }
                          if(response.data[1]){
                              if(response.data[1].matchSlot=='B'){
                                  $scope.showMatch = true;
                                  $scope.match = response.data[1];
                              }
                          }
                       }else{
                          $window.alert(response.statusText);
                       }
   });
  database.ref('matchB/a_team_goals').on('value', function(snapshot) {
    if(!afirst){
      document.getElementById('id01').style.display='block'
    }
    document.getElementById("aTeamGoal").innerHTML = snapshot.val();
    afirst = false
  });
  database.ref('matchB/b_team_goals').on('value', function(snapshot) {
    if(!bfirst){
      document.getElementById('id01').style.display='block'
    }
    document.getElementById("bTeamGoal").innerHTML = snapshot.val();
    bfirst = false;
  });
  database.ref('logo_url').on('value', function(snapshot) {
      document.getElementById("leagueLogoImg").src = snapshot.val();
  });

  database.ref('league_name').on('value', function(snapshot) {
        document.getElementById("leagueName").innerHTML = snapshot.val();
  });

});
