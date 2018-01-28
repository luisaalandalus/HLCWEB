var Swiftype = window.Swiftype || {};
Swiftype.root_url = Swiftype.root_url || "//search-api.swiftype.com";
Swiftype.embedVersion = Swiftype.embedVersion || 'v1';
if (typeof Swiftype.renderStyle === 'undefined') {
  Swiftype.renderStyle = 'nocode';
}

Swiftype.isMobile = function() {
  var ua = window.navigator.userAgent;
  if(/iPhone|iPod/.test(ua) && ua.indexOf("AppleWebKit") > -1 ) {
    return true;
  }
  if (/Android/.test(ua) && /Mobile/i.test(ua) && ua.indexOf("AppleWebKit") > -1 ) {
    return true;
  }
  return false;
};

Swiftype.loadScript = function(url, callback) {
  var script = document.createElement('script');
  script.type = 'text/javascript';
  script.async = true;
  script.src = url;

  var entry = document.getElementsByTagName('script')[0];
  entry.parentNode.insertBefore(script, entry);

  if (script.addEventListener) {
    script.addEventListener('load', callback, false);
  } else {
    script.attachEvent('onreadystatechange', function() {
      if (/complete|loaded/.test(script.readyState))
        callback();
    });
  }
};

Swiftype.loadStylesheet = function(url) {
  var link = document.createElement('link');
  link.rel = 'stylesheet';
  link.type = 'text/css';
  link.href = url;
  (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(link);
};

Swiftype.loadSupportingFiles = function(callback) {
  if (Swiftype.renderStyle === false) {
    Swiftype.loadScript("//s.swiftypecdn.com/assets/swiftype_no_render-1dff58d092e8a2aa4a43bb2b0844bb62.js", callback);
    Swiftype.loadStylesheet("//s.swiftypecdn.com/assets/swiftype-372b0eab2251b5551acc7e940f39a528.css");
  } else if (Swiftype.isMobile() && !Swiftype.disableMobileOverlay) {
    Swiftype.loadScript("//s.swiftypecdn.com/assets/swiftype_nocode-d8273559be65c04593eb2a85dc0df637.js", callback);
    Swiftype.loadStylesheet("//s.swiftypecdn.com/assets/swiftype_nocode-862cc0feac61f00e170fbcc6360aeeb7.css");
  } else if (Swiftype.renderStyle === 'inline' || Swiftype.renderStyle === 'new_page') {
    Swiftype.loadScript("//s.swiftypecdn.com/assets/swiftype_onpage-02d2e72de5fcf9d8823ddf010d36df29.js", callback);
    Swiftype.loadStylesheet("//s.swiftypecdn.com/assets/swiftype-372b0eab2251b5551acc7e940f39a528.css");
  } else {
    Swiftype.loadScript("//s.swiftypecdn.com/assets/swiftype_nocode-d8273559be65c04593eb2a85dc0df637.js", callback);
    Swiftype.loadStylesheet("//s.swiftypecdn.com/assets/swiftype_nocode-862cc0feac61f00e170fbcc6360aeeb7.css");
  }
};

var Swiftype = (function(window, undefined) {
   if (Swiftype.embedVersion === 'v1') {
     Swiftype.loadSupportingFiles(function(){});
   }
   return Swiftype;
})(window);
