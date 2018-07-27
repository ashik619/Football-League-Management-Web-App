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
angular.module('mainApp')
    .controller('matchAAdminController', function ($scope, $http, $window, $route) {
        var cATeamGoal = 0;
        var cBTeamGoal = 0;
        $scope.showMatchA = false;
        database.ref('matchA/a_team_goals').on('value', function(snapshot) {
            cATeamGoal = snapshot.val();
            document.getElementById("aTeamGoals").innerHTML = cATeamGoal;
        });
        database.ref('matchA/b_team_goals').on('value', function(snapshot) {
            cBTeamGoal = snapshot.val();
            document.getElementById("bTeamGoals").innerHTML = cBTeamGoal;
        });
        $http.get("/api/match/getAllRunning")
                 .then(function(response) {
                     if(response.status===200){
                        if(response.data[0]){
                            if(response.data[0].matchSlot=='A'){
                                $scope.showMatchA = true;
                                $scope.matchA = response.data[0];
                            }
                        }
                        if(response.data[1]){
                            if(response.data[1].matchSlot=='A'){
                                $scope.showMatchA = true;
                                $scope.matchA = response.data[1];
                            }
                        }
                        $scope.results = [$scope.matchA.aTeam.team_name+ " Won match", $scope.matchA.bTeam.team_name+" Won match","Draw or No result"]
                     }else{
                        $window.alert(response.statusText);
                     }
        });
        $scope.finishMatch = function(){
            if($scope.result){
                console.log($scope.result);
                if(isNaN($scope.result)) {
                    console.log("not num");
                    }
                var resultCode = "D";
                switch(parseInt($scope.result)){
                    case 0 :
                       resultCode = "AW";
                       break;

                    case 1 :
                       resultCode = "BW";
                       break;

                    case 2 :
                       resultCode = "D";
                       break;

                }
                console.log(resultCode);
                $http.post('/api/match/changeStatus/'+$scope.matchA.id,
                                                {
                                                'type' : 'endMatch',
                                                'result' : resultCode,
                                                'aGoals' : cATeamGoal,
                                                'bGoals' : cBTeamGoal
                                                 }
                       ).then(function(response) {
                          if(response.status===200){
                                 $route.reload();
                                 $window.alert(response.data.message);
                          }
                       },function(errResp) {
                               $window.alert(errResp.data.message);
                       }
                );
            }else $window.alert("Select a result to end match");
        }

          $scope.addAteamGoal = function() {
            database.ref('matchA/a_team_goals').once('value').then(function(snapshot) {
              var current = snapshot.val();
              database.ref('matchA').update({ a_team_goals: current+1 }).then(function(url) {
                  $window.alert("Goal Updated");
              });
            });
          };
          $scope.decAteamGoal = function() {
            database.ref('matchA/a_team_goals').once('value').then(function(snapshot) {
              var current = snapshot.val();
              database.ref('matchA').update({ a_team_goals: current-1 }).then(function(url) {
                  $window.alert("Goal Updated");
              });
            });
          };
          $scope.addBteamGoal = function() {
            database.ref('matchA/b_team_goals').once('value').then(function(snapshot) {
              var current = snapshot.val();
              database.ref('matchA').update({ b_team_goals: current+1 }).then(function(url) {
                  $window.alert("Goal Updated");
              });
            });
          };
          $scope.decBteamGoal = function() {
            database.ref('matchA/b_team_goals').once('value').then(function(snapshot) {
              var current = snapshot.val();
              database.ref('matchA').update({ b_team_goals: current-1 }).then(function(url) {
                  $window.alert("Goal Updated");
              });
            });
          };
    })
    .controller('matchBAdminController', function ($scope, $http, $window, $route) {
            var cATeamGoal = 0;
             var cBTeamGoal = 0;
             $scope.showMatchB = false;
             database.ref('matchB/a_team_goals').on('value', function(snapshot) {
                 cATeamGoal = snapshot.val();
                 document.getElementById("aTeamGoals").innerHTML = cATeamGoal;
             });
             database.ref('matchB/b_team_goals').on('value', function(snapshot) {
                 cBTeamGoal = snapshot.val();
                 document.getElementById("bTeamGoals").innerHTML = cBTeamGoal;
             });
            $http.get("/api/match/getAllRunning")
                      .then(function(response) {
                          if(response.status===200){
                             console.log(response.data);
                             if(response.data[0]){
                                 if(response.data[0].matchSlot=='B'){
                                     $scope.showMatchB = true;
                                     $scope.matchB = response.data[0];
                                     $scope.results = [$scope.matchB.aTeam.team_name+ " Won match", $scope.matchB.bTeam.team_name+" Won match","Draw or No result"]
                                 }
                             }
                             if(response.data[1]){
                                 if(response.data[1].matchSlot=='B'){
                                     $scope.showMatchB = true;
                                     $scope.matchB = response.data[1];
                                     $scope.results = [$scope.matchB.aTeam.team_name+ " Won match", $scope.matchB.bTeam.team_name+" Won match","Draw or No result"]
                                 }
                             }
                          }else{
                             $window.alert(response.statusText);
                          }
             });
             $scope.finishMatch = function(){
                 if($scope.result){
                     var resultCode = "D"
                     switch($scope.result){
                         case 0:{
                            resultCode = "AW";
                            break;
                         }
                         case 1:{
                            resultCode = "BW";
                            break;
                         }
                         case 2:{
                            resultCode = "D";
                            break;
                         }
                     }
                     $http.post('/api/match/changeStatus/'+$scope.matchB.id,
                                                     {
                                                     'type' : 'endMatch',
                                                     'result' : resultCode,
                                                     'aGoals' : cATeamGoal,
                                                     'bGoals' : cBTeamGoal
                                                      }
                            ).then(function(response) {
                               if(response.status===200){
                                      $route.reload();
                                      $window.alert(response.data.message);
                               }
                            },function(errResp) {
                                     $window.alert(errResp.data.message);
                            }
                     );
                 }else $window.alert("Select a result to end match");
             }

               $scope.addAteamGoal = function() {
                 database.ref('matchB/a_team_goals').once('value').then(function(snapshot) {
                   var current = snapshot.val();
                   database.ref('matchB').update({ a_team_goals: current+1 }).then(function(url) {
                       $window.alert("Goal Updated");
                   });
                 });
               };
               $scope.decAteamGoal = function() {
                 database.ref('matchB/a_team_goals').once('value').then(function(snapshot) {
                   var current = snapshot.val();
                   database.ref('matchB').update({ a_team_goals: current-1 }).then(function(url) {
                       $window.alert("Goal Updated");
                   });
                 });
               };
               $scope.addBteamGoal = function() {
                 database.ref('matchB/b_team_goals').once('value').then(function(snapshot) {
                   var current = snapshot.val();
                   database.ref('matchB').update({ b_team_goals: current+1 }).then(function(url) {
                       $window.alert("Goal Updated");
                   });
                 });
               };
               $scope.decBteamGoal = function() {
                 database.ref('matchB/b_team_goals').once('value').then(function(snapshot) {
                   var current = snapshot.val();
                   database.ref('matchB').update({ b_team_goals: current-1 }).then(function(url) {
                       $window.alert("Goal Updated");
                   });
                 });
               };
    });

