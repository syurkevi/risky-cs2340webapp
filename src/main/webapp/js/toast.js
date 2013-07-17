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

        if(toast.type!="success") { // Toast prompt
            $timeout(function() {$scope.closeToast(data.id)},3000);
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
            data.lastvalue = "";
        }

        $scope.toasts.push(data);
    });
    $scope.selectValue = function (t,v) {
        for(var i = 0; i < $scope.toasts[t].values.length; i++) {
            if($scope.toasts[t].values[i] === v) {
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
    }
    $scope.toastReply = function (t) {
        $scope.$emit("toast-reply",$scope.toasts[t].values.indexOf("active")); // Send reply to $rootScope/toast.request
        alert($scope.toasts[t].values.indexOf("active"));
    }
    $scope.closeToast = function (id) {
        // Close the toast
        var toast = document.getElementById("toast"+id);
        toast.style.animation="pop-out 0.8s ease-in ";
        $timeout(function() {toast.style.display="none";},750); // TODO: remove array element to prevent clogging of DOM
    }
});
