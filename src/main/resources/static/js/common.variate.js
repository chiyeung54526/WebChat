//定义一些变量

var websocket = null;
var shakeList = ["","shake-hard","shake-slow","shake-little","shake-horizontal","shake-vertical","shake-rotate","shake-opacity","shake-crazy"];
var shakeChinese = ["","可劲儿摇","雪花飘","瑟瑟发抖","左右摇摆","上下跳动","跷跷板","飘忽不定","放弃治疗"];
var aa = '<div class="botui-message-left"><div class="botui-message-content-img" onclick="originalImage(this)">';
var b = '</div></div>';
var cc = '<div class="botui-message-right"><div class="botui-message-content2-img" onclick="originalImage(this)">';
var host = location.host;
var wsHost = "ws://"+host+"/websocket";
var focus = false;
var mute = 2;
var shieldMap = new Map();
var timer;
var shakeNum = 0;
var msgSwitchTips = 'Notification On/Off';
var emojiTips = 'Emoji';
var pictureTips = 'Send Pic';
var shakeTips = 'Shake!';
var clearTips = 'Clean';
var sendTips = 'Send';
var onerrorMsg = "Connection Broke";
var oncloseMsg = 'Connection Broke';
var unSupportWsMsg = "Browser Error, try change the browser";
var firstTips = "Welcome to WebChat!";
var emojiPath = 'dist/img/';
var emojiHead = '<img class="emoji_icon" src="'+emojiPath;
var textHead = '⇤';
var emojiFoot = '">';
var textFoot = '⇥';

