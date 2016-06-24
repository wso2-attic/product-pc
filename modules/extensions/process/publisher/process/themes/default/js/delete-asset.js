$(document).ready(function () {
   var currentLifecycleState = store.publisher.lifecycle.currentState;

   if(String(currentLifecycleState) === "Published") {
      var msg = "You cannot delete processes in published state.";
      disableDelete(msg);
   } else {
      enableDelete();
   }
});

var enableDelete = function () {
   var deletePanel = $('.message.message-danger');
   deletePanel.css('display','');
   $('#Delete').removeClass('not-active').removeAttr("title").unbind('click');
   $('#btn-delete-con').show();
};

var disableDelete = function (msg) {
   var deletePanel = $('.message.message-danger');
   deletePanel.removeClass('message-danger').addClass('message-warning');
   deletePanel.find('.fw.fw-error').removeClass('fw-error').addClass('fw-warning');
   deletePanel.css('display','');

   $('#btn-delete-con').hide();
   $('#delete-msg').text(msg);
};