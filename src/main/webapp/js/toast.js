risky.controller("ToastController", function ($scope,$timeout,Toast) {
    // Create toasts, set animations, create promises
    // Responsible for drawing
    $scope.toasts = [];
    $scope.$on("new-toast",function (event, toast) { 
        var data = {};
        data.type = toast.type;
        data.message = toast.message;
        data.id = $scope.toasts.length;
        data.values = [];
        data.selected = 1;

        if(toast.type!="success") { // Toast prompt
            $timeout(function() {$scope.toastClose(data.id)},3000);
        } else {
            for(var i = toast.values[0]; i <= toast.values[1]; i++) {
                data.values.push({
                    "value" : i,
                    "selected" : (i===1)?"active":"",
                    "valueOf" : function () {return this.value},
                    "toString" : function () {return this.value},
                });
            }
            data.firstvalue = "disabled";
            data.lastvalue = (toast.values[1]===1)?"disabled":"";
        }

        $scope.toasts.push(data);
    });
    $scope.selectValue = function (t,v) {
        v = (v>0)?v:$scope.toasts[t].values.length;
        for(var i = 0; i < $scope.toasts[t].values.length; i++) {
            if($scope.toasts[t].values[i] == v) {
                $scope.toasts[t].values[i].selected = "active";
            } else {
                $scope.toasts[t].values[i].selected = "";
            }
        }
        if(v === 1) {
            $scope.toasts[t].firstvalue = "disabled";
        } else {
            $scope.toasts[t].firstvalue = "";
        }
        if(v === $scope.toasts[t].values.length) {
            $scope.toasts[t].lastvalue = "disabled";
        } else {
            $scope.toasts[t].lastvalue = "";
        }
        $scope.toasts[t].selected = v;
    }
    $scope.toastReply = function (id) {
        $scope.$emit("toast-reply",$scope.toasts[id].selected); // Send reply to $rootScope/toast.request
        //alert($scope.selected);
    }
    $scope.toastClose = function (id) {
        // Close the toast
        var toast = document.getElementById("toast"+id);
        toast.style.animation="pop-out 0.8s ease-in ";
        $timeout(function() {toast.style.display="none";},750); // TODO: remove array element to prevent clogging of DOM
    }
});
