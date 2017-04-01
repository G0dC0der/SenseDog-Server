function AddController($scope, DataService) {
    $scope.urls = [];
    $scope.addInData = {};
    $scope.addOutData = {};

    $scope.submitAdd = function () {
        $scope.loading  = true;
        $scope.addOutData = {};
        
        DataService.add($scope.addInData).then(function (url) {
            $scope.urls.push(url);
            $.notify("Success!",  { position:"top center", className:'success', autoHideDelay: 2500 });
        }).then(undefined, function (errorEntry) {
            $scope.addOutData = errorEntry;
            $.notify("Failed to add the URL.",  { position:"top center", className:'error', autoHideDelay: 2500 });
        }).finally(function () {
            $scope.loading = false;
        });
    };
    
    $scope.copyToClipboard = function (url) {
        // var copyData = $("a[href='" + url + "']");
        // copyData.parent().select();
        // // var input = $('<input>').val(url);//.css('display', 'none');
        // // $('body').append(input[0]);
        // // input.select();
        //
        //
        // $.notify("URL copied to clipboard!",  { position:"top center", className:'info', autoHideDelay: 2500 });
        $.notify("Copy failed.",  { position:"top center", className:'warning', autoHideDelay: 2500 });
    };
}

angular.module('ClickCount').controller('AddController', [
    '$scope',
    'DataService',
    AddController]);