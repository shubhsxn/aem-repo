/**
  stickybits - Stickybits is a lightweight alternative to `position: sticky` polyfills
  @version v3.3.3
  @link https://github.com/dollarshaveclub/stickybits#readme
  @author Jeff Wainwright <yowainwright@gmail.com> (https://jeffry.in)
  @license MIT
**/
!function(t,s){"object"==typeof exports&&"undefined"!=typeof module?module.exports=s():"function"==typeof define&&define.amd?define(s):t.stickybits=s()}(this,function(){"use strict";var t=function(){function t(t,s){var e=void 0!==s?s:{};this.version="3.3.3",this.userAgent=window.navigator.userAgent||"no `userAgent` provided by the browser",this.props={customStickyChangeNumber:e.customStickyChangeNumber||null,noStyles:e.noStyles||!1,stickyBitStickyOffset:e.stickyBitStickyOffset||0,parentClass:e.parentClass||"js-stickybit-parent",scrollEl:document.querySelector(e.scrollEl)||window,stickyClass:e.stickyClass||"js-is-sticky",stuckClass:e.stuckClass||"js-is-stuck",stickyChangeClass:e.stickyChangeClass||"js-is-sticky--change",useStickyClasses:e.useStickyClasses||!1,useFixed:e.useFixed||!1,verticalPosition:e.verticalPosition||"top"};var i=this.props;i.positionVal=this.definePosition()||"fixed";var n=i.verticalPosition,o=i.noStyles,a=i.positionVal;this.els="string"==typeof t?document.querySelectorAll(t):t,"length"in this.els||(this.els=[this.els]),this.instances=[];for(var r=0;r<this.els.length;r+=1){var l=this.els[r],c=l.style;if(c[n]="top"!==n||o?"":i.stickyBitStickyOffset+"px",c.position="fixed"!==a?a:"","fixed"===a||i.useStickyClasses){var f=this.addInstance(l,i);this.instances.push(f)}}return this}var s=t.prototype;return s.definePosition=function(){var t;if(this.props.useFixed)t="fixed";else{for(var s=["","-o-","-webkit-","-moz-","-ms-"],e=document.head.style,i=0;i<s.length;i+=1)e.position=s[i]+"sticky";t=e.position?e.position:"fixed",e.position=""}return t},s.addInstance=function(t,s){var e=this,i={el:t,parent:t.parentNode,props:s};this.isWin=this.props.scrollEl===window;var n=this.isWin?window:this.getClosestParent(i.el,i.props.scrollEl);return this.computeScrollOffsets(i),i.parent.className+=" "+s.parentClass,i.state="default",i.stateContainer=function(){return e.manageState(i)},n.addEventListener("scroll",i.stateContainer),i},s.getClosestParent=function(t,s){var e=s,i=t;if(i.parentElement===e)return e;for(;i.parentElement!==e;)i=i.parentElement;return e},s.getOffsetTop=function(t){var s=0;do{s=t.offsetTop+s}while(t=t.offsetParent);return s},s.computeScrollOffsets=function(t){var s=t,e=s.props,i=s.el,n=s.parent,o=!this.isWin&&"fixed"===e.positionVal,a="bottom"!==e.verticalPosition,r=o?this.getOffsetTop(e.scrollEl):0,l=o?this.getOffsetTop(n)-r:this.getOffsetTop(n),c=null!==e.customStickyChangeNumber?e.customStickyChangeNumber:i.offsetHeight;return s.offset=r+e.stickyBitStickyOffset,s.stickyStart=a?l-s.offset:0,s.stickyChange=s.stickyStart+c,s.stickyStop=a?l+n.offsetHeight-(s.el.offsetHeight+s.offset):l+n.offsetHeight,s},s.toggleClasses=function(t,s,e){var i=t,n=i.className.split(" ");e&&-1===n.indexOf(e)&&n.push(e);var o=n.indexOf(s);-1!==o&&n.splice(o,1),i.className=n.join(" ")},s.manageState=function(t){var s=t,e=s.el,i=s.props,n=s.state,o=s.stickyStart,a=s.stickyChange,r=s.stickyStop,l=e.style,c=i.noStyles,f=i.positionVal,u=i.scrollEl,p=i.stickyClass,h=i.stickyChangeClass,y=i.stuckClass,d=i.verticalPosition,k=function(t){t()},m=this.isWin&&(window.requestAnimationFrame||window.mozRequestAnimationFrame||window.webkitRequestAnimationFrame||window.msRequestAnimationFrame)||k,g=this.toggleClasses,v=this.isWin?window.scrollY||window.pageYOffset:u.scrollTop,C=v<=o&&"sticky"===n,S=v>=r&&"sticky"===n;v>o&&v<r&&("default"===n||"stuck"===n)?(s.state="sticky",m(function(){g(e,y,p),l.position=f,c||(l.bottom="",l[d]=i.stickyBitStickyOffset+"px")})):C?(s.state="default",m(function(){g(e,p),"fixed"===f&&(l.position="")})):S&&(s.state="stuck",m(function(){g(e,p,y),"fixed"!==f||c||(l.top="",l.bottom="0",l.position="absolute")}));var w=v>=a&&v<=r;return v<a||v>r?m(function(){g(e,h)}):w&&m(function(){g(e,"stub",h)}),s},s.update=function(){for(var t=0;t<this.instances.length;t+=1){var s=this.instances[t];this.computeScrollOffsets(s)}return this},s.removeInstance=function(t){var s=t.el,e=t.props,i=this.toggleClasses;s.style.position="",s.style[e.verticalPosition]="",i(s,e.stickyClass),i(s,e.stuckClass),i(s.parentNode,e.parentClass)},s.cleanup=function(){for(var t=0;t<this.instances.length;t+=1){var s=this.instances[t];s.props.scrollEl.removeEventListener("scroll",s.stateContainer),this.removeInstance(s)}this.manageState=!1,this.instances=[]},t}();return function(s,e){return new t(s,e)}});
/**
  stickybits - Stickybits is a lightweight alternative to `position: sticky` polyfills
  @version v3.3.3
  @link https://github.com/dollarshaveclub/stickybits#readme
  @author Jeff Wainwright <yowainwright@gmail.com> (https://jeffry.in)
  @license MIT
**/
!function(t,s){"object"==typeof exports&&"undefined"!=typeof module?s():"function"==typeof define&&define.amd?define(s):s()}(0,function(){"use strict";var t=function(){function t(t,s){var e=void 0!==s?s:{};this.version="3.3.3",this.userAgent=window.navigator.userAgent||"no `userAgent` provided by the browser",this.props={customStickyChangeNumber:e.customStickyChangeNumber||null,noStyles:e.noStyles||!1,stickyBitStickyOffset:e.stickyBitStickyOffset||0,parentClass:e.parentClass||"js-stickybit-parent",scrollEl:document.querySelector(e.scrollEl)||window,stickyClass:e.stickyClass||"js-is-sticky",stuckClass:e.stuckClass||"js-is-stuck",stickyChangeClass:e.stickyChangeClass||"js-is-sticky--change",useStickyClasses:e.useStickyClasses||!1,useFixed:e.useFixed||!1,verticalPosition:e.verticalPosition||"top"};var i=this.props;i.positionVal=this.definePosition()||"fixed";var n=i.verticalPosition,o=i.noStyles,a=i.positionVal;this.els="string"==typeof t?document.querySelectorAll(t):t,"length"in this.els||(this.els=[this.els]),this.instances=[];for(var r=0;r<this.els.length;r+=1){var l=this.els[r],c=l.style;if(c[n]="top"!==n||o?"":i.stickyBitStickyOffset+"px",c.position="fixed"!==a?a:"","fixed"===a||i.useStickyClasses){var f=this.addInstance(l,i);this.instances.push(f)}}return this}var s=t.prototype;return s.definePosition=function(){var t;if(this.props.useFixed)t="fixed";else{for(var s=["","-o-","-webkit-","-moz-","-ms-"],e=document.head.style,i=0;i<s.length;i+=1)e.position=s[i]+"sticky";t=e.position?e.position:"fixed",e.position=""}return t},s.addInstance=function(t,s){var e=this,i={el:t,parent:t.parentNode,props:s};this.isWin=this.props.scrollEl===window;var n=this.isWin?window:this.getClosestParent(i.el,i.props.scrollEl);return this.computeScrollOffsets(i),i.parent.className+=" "+s.parentClass,i.state="default",i.stateContainer=function(){return e.manageState(i)},n.addEventListener("scroll",i.stateContainer),i},s.getClosestParent=function(t,s){var e=s,i=t;if(i.parentElement===e)return e;for(;i.parentElement!==e;)i=i.parentElement;return e},s.getOffsetTop=function(t){var s=0;do{s=t.offsetTop+s}while(t=t.offsetParent);return s},s.computeScrollOffsets=function(t){var s=t,e=s.props,i=s.el,n=s.parent,o=!this.isWin&&"fixed"===e.positionVal,a="bottom"!==e.verticalPosition,r=o?this.getOffsetTop(e.scrollEl):0,l=o?this.getOffsetTop(n)-r:this.getOffsetTop(n),c=null!==e.customStickyChangeNumber?e.customStickyChangeNumber:i.offsetHeight;return s.offset=r+e.stickyBitStickyOffset,s.stickyStart=a?l-s.offset:0,s.stickyChange=s.stickyStart+c,s.stickyStop=a?l+n.offsetHeight-(s.el.offsetHeight+s.offset):l+n.offsetHeight,s},s.toggleClasses=function(t,s,e){var i=t,n=i.className.split(" ");e&&-1===n.indexOf(e)&&n.push(e);var o=n.indexOf(s);-1!==o&&n.splice(o,1),i.className=n.join(" ")},s.manageState=function(t){var s=t,e=s.el,i=s.props,n=s.state,o=s.stickyStart,a=s.stickyChange,r=s.stickyStop,l=e.style,c=i.noStyles,f=i.positionVal,u=i.scrollEl,p=i.stickyClass,h=i.stickyChangeClass,d=i.stuckClass,y=i.verticalPosition,k=function(t){t()},m=this.isWin&&(window.requestAnimationFrame||window.mozRequestAnimationFrame||window.webkitRequestAnimationFrame||window.msRequestAnimationFrame)||k,g=this.toggleClasses,v=this.isWin?window.scrollY||window.pageYOffset:u.scrollTop,C=v<=o&&"sticky"===n,w=v>=r&&"sticky"===n;v>o&&v<r&&("default"===n||"stuck"===n)?(s.state="sticky",m(function(){g(e,d,p),l.position=f,c||(l.bottom="",l[y]=i.stickyBitStickyOffset+"px")})):C?(s.state="default",m(function(){g(e,p),"fixed"===f&&(l.position="")})):w&&(s.state="stuck",m(function(){g(e,p,d),"fixed"!==f||c||(l.top="",l.bottom="0",l.position="absolute")}));var S=v>=a&&v<=r;return v<a||v>r?m(function(){g(e,h)}):S&&m(function(){g(e,"stub",h)}),s},s.update=function(){for(var t=0;t<this.instances.length;t+=1){var s=this.instances[t];this.computeScrollOffsets(s)}return this},s.removeInstance=function(t){var s=t.el,e=t.props,i=this.toggleClasses;s.style.position="",s.style[e.verticalPosition]="",i(s,e.stickyClass),i(s,e.stuckClass),i(s.parentNode,e.parentClass)},s.cleanup=function(){for(var t=0;t<this.instances.length;t+=1){var s=this.instances[t];s.props.scrollEl.removeEventListener("scroll",s.stateContainer),this.removeInstance(s)}this.manageState=!1,this.instances=[]},t}();if("undefined"!=typeof window){var s=window.$||window.jQuery||window.Zepto;s&&(s.fn.stickybits=function(s){new t(this,s)})}});