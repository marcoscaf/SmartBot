(function () {
    var Message;
    Message = function (arg) {
        this.text = arg.text, this.message_side = arg.message_side;
        this.draw = function (_this) {
            return function () {
                var $message;
                $message = $($('.message_template').clone().html());
                $message.addClass(_this.message_side).find('.text').html(_this.text);
                $('.messages').append($message);
                return setTimeout(function () {
                    return $message.addClass('appeared');
                }, 0);
            };
        }(this);
        return this;
    };
    $(function () {
        var getMessageText, message_side, sendMessage;
        message_side = 'right';
        getMessageText = function () {
            var $message_input;
            $message_input = $('.message_input');
            var ret =  $message_input.val();
            return ret;
        };
        sendMessage = function (text) {
            var $messages, message;
            if (text.trim() === '') {
                return;
            }
            $('.message_input').val('');
            $messages = $('.messages');
            message_side = 'left';
            message = new Message({
                text: text,
                message_side: message_side
            });
            message.draw();
            return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
        };
        $('.send_message').click(function (e) {
        	
            return sendMessage(getMessageText());
        });
        $('.message_input').keyup(function (e) {
            if (e.which === 13) {
                return sendMessage(getMessageText());
            }
        });       
      
    });
    
    
    
    $(function () {
        var getMessageTextBot, message_side, sendMessage;
        message_side = 'right';
        getMessageTextBot = function () {
            var $message_input;
            $message_input = $('.message_input_bot');
            var ret = $message_input.val();
            return ret;
        };
        sendMessage = function (text) {
           
            
            
            var res = text.trim().split("\n"); 
        	for(var i=0; i<=res.length; i++){
        		
        		 var $messages, message;
                 if (text.trim() === '') {
                     return;
                 }
                 
                $('.message_input_bot').val('');
                $messages = $('.messages');
                message_side = 'right';
                message = new Message({
                    text: res[i],
                    message_side: message_side
                });
                message.draw();
                $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
                if(res.length == i){
                	return;
                }
        	}
            
        	
            return;
        };
        $('.fire_bot').click(function (e) {
            return sendMessage(getMessageTextBot());
        });
        $('.fire_bot').keyup(function (e) {
            if (e.which === 13) {
                return sendMessage(getMessageTextBot());
            }
        });       
      
    });
    
    
    
}.call(this));








function clickBotMessage(){
	
	document.getElementById('formId:message_input_bot').click(); 
	
}