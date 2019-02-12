document.documentElement.classList.remove('no-js');
document.documentElement.classList.add('js');
window.addEventListener("DOMContentLoaded", function(){
	document.documentElement.classList.add('ready');
});
window.addEventListener("load", function(){
	document.documentElement.classList.add('loaded');
});