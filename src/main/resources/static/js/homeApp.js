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

var storage = firebase.storage();
var storageRef = firebase.storage().ref();

var logo1Url = null;
var logo2Url = null;
var leagueLogoUrl = null;

function logo1Upload(){
    var file = document.getElementById("logo1FileId").files[0];
    const suffix = makeid();
    var teamLogoRef = storageRef.child(file.name);
    teamLogoRef.put(file).then(function(snapshot) {
      teamLogoRef.getDownloadURL().then(function(url) {
        alert("Logo Uploaded!!");
        logo1Url = url;
      });
    });
}
function logo2Upload(){
    var file = document.getElementById("logo2FileId").files[0];
    const suffix = makeid();
    var teamLogoRef = storageRef.child(file.name);
    teamLogoRef.put(file).then(function(snapshot) {
      teamLogoRef.getDownloadURL().then(function(url) {
        alert("Logo Uploaded!!");
        logo2Url = url;
      });
    });
}
function leagueLogoUpload(){
    var file = document.getElementById("leagueLogoFileId").files[0];
    const suffix = makeid();
    var teamLogoRef = storageRef.child(file.name);
    teamLogoRef.put(file).then(function(snapshot) {
      teamLogoRef.getDownloadURL().then(function(url) {
        alert("Logo Uploaded!!");
        leagueLogoUrl = url;
      });
    });
}

angular.module('mainApp')
    .controller('homeControlle', function ($scope, $http, $window, $route) {
        database.ref('league_name').on('value', function(snapshot) {
            document.getElementById("leagueName").innerHTML = snapshot.val();
        });
        database.ref('brand_url_2').on('value', function(snapshot) {
            document.getElementById("brandLogo2").src = snapshot.val();
        });
        database.ref('brand_url_1').on('value', function(snapshot) {
            document.getElementById("brandLogo1").src = snapshot.val();
        });
        database.ref('logo_url').on('value', function(snapshot) {
                    document.getElementById("leagueLogo").src = snapshot.val();
                });


        $scope.updateName = function() {
            database.ref().update({ league_name: $scope.new_league_name }).then(function(url) {
               $window.alert("Name Updated");
            });
        };
        $scope.updateLogo1 = function() {
            database.ref().update({ brand_url_1: logo1Url }).then(function(url) {
               $window.alert("Logo 1 Updated");
            });
        };
        $scope.updateLogo2 = function() {
            database.ref().update({ brand_url_2: logo2Url }).then(function(url) {
               $window.alert("Logo 2 Updated");
            });
        };
        $scope.updateLeagueLogo = function() {
           database.ref().update({ logo_url: leagueLogoUrl }).then(function(url) {
              $window.alert("League Logo Updated");
           });
        };

});

function makeid() {
   var text = "";
   var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
   for (var i = 0; i < 5; i++)
     text += possible.charAt(Math.floor(Math.random() * possible.length));
   return text;
}