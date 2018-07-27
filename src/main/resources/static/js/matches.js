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
    .controller('matchController', function ($scope, $http, $window, $route) {
        $scope.showStartMatch = false;
        $scope.showFinishMatch = false;
        $scope.showMatch = null;
        $scope.selectedTeamA = null;
        $scope.selectedTeamB = null;
        $scope.matchTypes = ["Group Stage", "Pre Quarter", "Quarter", "Semi Final", "Final"];
       $http.get("/api/match/getAll")
                 .then(function(response) {
                     if(response.status===200){
                        $scope.matches = response.data;
                     }else{
                        $window.alert(response.statusText);
                     }
        });
        $http.get("/api/team/getAll")
                         .then(function(response) {
                             if(response.status===200){
                                $scope.teams = response.data;
                             }else{
                                $window.alert(response.statusText);
                             }
                });
        $scope.createMatch = function() {
                if(($scope.selectedTeamA!=null)&&($scope.selectedTeamB!=null)){
                    if($scope.selectedTeamA.id != $scope.selectedTeamB.id){
                        var selectedDate = document.getElementById("matchDate").value;
                        if(selectedDate!=null&&selectedDate!=""){
                            if($scope.selectedMatchType){
                                $http.post('/api/match/addNew',
                                        {
                                        'matchDate' : selectedDate,
                                        'aTeamId' : $scope.selectedTeamA.id,
                                        'bTeamId' : $scope.selectedTeamB.id,
                                        'matchType' : $scope.selectedMatchType,
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
                             }else{
                                $window.alert("Select match type!!");
                             }
                        }else $window.alert("Select match date!!");
                    }else $window.alert("Select different teams!!");
                }else{
                    $window.alert("Select Team A and B!!");
                }
        };
        $scope.deleteMatch = function(idx) {
                deleteGrpConfirm = $window.confirm('Are you sure you want to delete the Match?');
                if(deleteGrpConfirm){
                    //var team = $scope.teams[idx]
                    $http.delete('/api/match/delete/'+idx).then(function(response){
                            $window.alert(response.data.message);
                            $route.reload();
                    });
                }
        };
        $scope.viewMatch = function(match){
            $scope.showStartMatch = false;
            $scope.showFinishMatch = false;
            $scope.matchFinished = false;
            document.getElementById('id01').style.display='block';
            $scope.showMatch = match;
            switch(match.matchStatus){
                case "scheduled" :
                        $scope.showStartMatch = true;
                        break;
                case "running" :
                        $scope.showFinishMatch = true;
                        break;
                case "finished" :
                        $scope.matchFinished = true;
                        break;
            }
        };
        $scope.startMatch = function(){
            if($scope.showMatch!=null){
                $http.post('/api/match/changeStatus/'+$scope.showMatch.id,
                                                {
                                                'type' : 'startMatch'
                                                 }
                       ).then(function(response) {
                          if(response.status===200){
                                switch(response.data.data){
                                    case 'A':{
                                        database.ref('matchA').set({
                                            'a_team_goals' : 0,
                                            'b_team_goals' : 0
                                        }).then(function(url) {
                                                  $route.reload();
                                                  $window.alert(response.data.message);
                                        });
                                        break;
                                    }
                                    case 'B':{
                                        database.ref('matchB').set({
                                            'a_team_goals' : 0,
                                            'b_team_goals' : 0
                                        }).then(function(url) {
                                                  $route.reload();
                                                  $window.alert(response.data.message);
                                        });
                                        break;
                                    }
                                }
                          }
                },function(errResp) {
                        $window.alert(errResp.data.message);
                });
            }
        }
});
