var customET = {};
var changeDir = true;
var saveStateDivId = "CFMaaS1994State";

customET.currentSelection = {
     "startContainer": 0,
     "startOffset": 0,
     "endContainer": 0,
     "endOffset": 0};

customET.editor = document.getElementById('editor');

customET.setHtml = function(contents) {
     customET.editor.innerHTML = "";
     var div = document.createElement('div');
     div.dir = "ltr";
     div.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
     customET.editor.appendChild(div);
     customET.setCaretAtStart(div);
 }

customET.setHtmlAndPath = function(contents, path) {
     customET.editor.innerHTML = "";
     var div = document.createElement('div');
     div.dir = "ltr";
     div.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
     customET.editor.appendChild(div);
     customET.setCaretAtStart(div);

     if (path != null && path != "") {
         var node = document.getElementById(saveStateDivId);
         if (node == null) {
             node = div;
             node.id = saveStateDivId;
         }
         var pathArray = path.split(',');
         var length = pathArray.length;
         for(var i = 0; i < length - 1; i++) {
             var index = pathArray[i];
             node = node.childNodes[index];
         }

         var currentIndex = pathArray[length - 1];
         customET.setCaretAtPosition(node, currentIndex);

         var range = document.createRange();
         range.setStart(node, currentIndex);
         range.setEnd(node, currentIndex);
         var y = customET.getY(range);
         customET.blockAllItems(y, path);
     }
 }

customET.getHtml = function() {
     return customET.editor.innerHTML;
 }

customET.getText = function() {
     return customET.editor.innerText;
 }

customET.setBaseTextColor = function(color) {
     customET.editor.style.color  = color;
 }

customET.setBaseFontSize = function(size) {
     customET.editor.style.fontSize = size;
 }

customET.setPadding = function(left, top, right, bottom) {
   customET.editor.style.paddingLeft = left;
   customET.editor.style.paddingTop = top;
   customET.editor.style.paddingRight = right;
   customET.editor.style.paddingBottom = bottom;
 }

customET.setBackgroundColor = function(color) {
     document.body.style.backgroundColor = color;
 }

customET.insertImage = function(url, alt) {
     var html = '<img src="' + url + '" alt="' + alt + '" /><br><br>';
     customET.insertHTML(html);
 }

customET.setWidth = function(size) {
     customET.editor.style.minWidth = size;
 }

customET.setHeight = function(size) {
     document.body.style.minHeight = size;
 }

customET.setTextAlign = function(align) {
     customET.editor.style.textAlign = align;
 }

customET.setVerticalAlign = function(align) {
     customET.editor.style.verticalAlign = align;
 }

customET.setPlaceholder = function(placeholder) {
     customET.editor.setAttribute("placeholder", placeholder);
 }

customET.setBold = function() {
     document.execCommand('bold', false, null);
 }

customET.setItalic = function() {
     document.execCommand('italic', false, null);
 }

customET.setStrikeThrough = function() {
     document.execCommand('strikeThrough', false, null);
 }

customET.setUnderline = function() {
     document.execCommand('underline', false, null);
 }

customET.setJustifyLeft = function() {
     document.execCommand('justifyLeft', false, null);
 }

customET.setJustifyCenter = function() {
     document.execCommand('justifyCenter', false, null);
 }

customET.setJustifyRight = function() {
     document.execCommand('justifyRight', false, null);
 }

customET.insertHTML = function(html) {
     customET.restorerange();
     document.execCommand('insertHTML', false, html);
 }

customET.prepareInsert = function() {
     customET.backuprange();
 }

customET.backuprange = function(){
     var selection = window.getSelection();
     if (selection.rangeCount > 0) {
       var range = selection.getRangeAt(0);
       customET.currentSelection = {
           "startContainer": range.startContainer,
           "startOffset": range.startOffset,
           "endContainer": range.endContainer,
           "endOffset": range.endOffset};
     }
 }

customET.restorerange = function(){
     var selection = window.getSelection();
     selection.removeAllRanges();
     var range = document.createRange();
     range.setStart(customET.currentSelection.startContainer, customET.currentSelection.startOffset);
     range.setEnd(customET.currentSelection.endContainer, customET.currentSelection.endOffset);
     selection.addRange(range);
 }

customET.focus = function() {
     var range = document.createRange();
     range.selectNodeContents(customET.editor);
     range.collapse(false);
     var selection = window.getSelection();
     selection.removeAllRanges();
     selection.addRange(range);
     customET.editor.focus();
 }

customET.blockAllItems = function(y, saveStatePath) {
     var enabledItems = [];
     if (document.queryCommandState('bold')) {
         enabledItems.push('BOLD');
     }
     if (document.queryCommandState('italic')) {
         enabledItems.push('ITALIC');
     }
     if (document.queryCommandState('underline')) {
         enabledItems.push('UNDERLINE');
     }
     if(document.queryCommandState('strikeThrough')) {
         enabledItems.push('STRIKETHROUGH')
     }

     var enabledEditableItems = encodeURI(enabledItems.join(','));

     JSInterface.callback("~!~!~!" + enabledEditableItems + "~!~!~!" + y + "~!~!~!" + saveStatePath + "~!~!~!" + encodeURI(customET.getHtml()));
 }

customET.allowAllItems = function(y, saveStatePath) {
     var allowedItems = [];
     allowedItems.push('BOLD');
     allowedItems.push('ITALIC');
     allowedItems.push('UNDERLINE');
     allowedItems.push('STRIKETHROUGH')
     allowedItems.push('FORECOLOR');
     allowedItems.push('IMAGE');

     var allowedEditableItems = encodeURI(allowedItems.join(','));

     var enabledItems = [];
     if (document.queryCommandState('bold')) {
         enabledItems.push('BOLD');
     }
     if (document.queryCommandState('italic')) {
         enabledItems.push('ITALIC');
     }
     if (document.queryCommandState('underline')) {
         enabledItems.push('UNDERLINE');
     }
     if(document.queryCommandState('strikeThrough')) {
         enabledItems.push('STRIKETHROUGH')
     }

     var enabledEditableItems = encodeURI(enabledItems.join(','));

     JSInterface.callback(allowedEditableItems + "~!~!~!" + enabledEditableItems + "~!~!~!" + y + "~!~!~!" + saveStatePath + "~!~!~!" + encodeURI(customET.getHtml()));
 }

customET.getY = function(range) {
     var newRange = range.cloneRange();
     newRange.collapse(false);
     var span = document.createElement("span");
     span.appendChild( document.createTextNode("\u200b") );
     newRange.insertNode(span);
     var y = span.offsetTop + (span.offsetHeight / 2);
     var spanOffsetParent = span.offsetParent;
     while (spanOffsetParent != null) {
         y = y + spanOffsetParent.offsetTop;
         spanOffsetParent = spanOffsetParent.offsetParent;
     }
     var spanParent = span.parentNode;
     spanParent.removeChild(span);
     spanParent.normalize();
     return y;
 }

customET.getSaveStatePath = function(index) {
     var saveStateDiv = document.getElementById(saveStateDivId);
     if (saveStateDiv != null) {
         saveStateDiv.id = "";
     }
     var node = window.getSelection().anchorNode;
     var path = "";
     if (node != null) {
         path = index + path;
         while (node.nodeName != "DIV") {
             var child = node;
             var i = 0;
             while( (child = child.previousSibling) != null ) {
                 i++;
             }
             path = i + "," + path;
             node = node.parentNode;
         }
         var curDiv = node;
         if (curDiv.id != "editor") {
             curDiv.id = saveStateDivId;
         }
     }
     return path;
 }

 // Event Listeners
customET.editor.addEventListener("click", function(e) {
     var selection = window.getSelection();
     if (selection.rangeCount > 0) {
         var range = selection.getRangeAt(0);
         var text = range.startContainer.data;
         var index = range.endOffset;

         var y = customET.getY(range);
         var path = customET.getSaveStatePath(index);

         if (typeof text === 'undefined' && index == 0) {
             // Click on beginning of html
             customET.allowAllItems(y, path);
         }
         else if (index > 0 && (text[index - 1] == ' ' || text.charCodeAt(index - 1) == 160)) {
             // Click after a space
             customET.allowAllItems(y, path);
         }
         else {
             customET.blockAllItems(y, path);
         }
     }
 });

customET.editor.addEventListener("input", function(e) {
     var selection = window.getSelection();
     if (selection.rangeCount > 0) {
         var range = selection.getRangeAt(0);
         var text = range.startContainer.data;
         var index = range.endOffset;

         var y = customET.getY(range);
         var path = customET.getSaveStatePath(index);

         if (typeof text != 'undefined' && index > 0) {
             var char = text.charAt(index - 1);
             var letter = customET.checkLetter(char);
             if (letter && changeDir) {
                 var rtl = customET.checkRTL(char);
                 if (rtl) {
                     customET.setDirection("rtl", char);
                 }
                 else {
                     customET.setDirection("ltr", char);
                 }
                 changeDir = false;
             }
         }

         if (typeof text == 'undefined') {
             // User just entered newline or something unknown
             customET.allowAllItems(y, path);
             changeDir = true;
         }
         else if (index > 0 && text.charCodeAt(index - 1) == 160) {
             // User just entered space
             customET.allowAllItems(y, path);
         }
         else {
             customET.blockAllItems(y, path);
         }
     }
 });

document.addEventListener("selectionchange", function() {
     customET.backuprange();
     var selection = window.getSelection();
     if (selection.rangeCount > 0) {
         var range = selection.getRangeAt(0);
         var start = range.startOffset;
         var end = range.endOffset;

         var y = customET.getY(range);

         if (start >= 0 && end >= 0 && end - start > 0) {
             customET.allowAllItems(y, "");
         }
     }
 });

customET.checkRTL = function(s) {
      var ltrChars        = 'A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02B8\u0300-\u0590\u0800-\u1FFF'+'\u2C00-\uFB1C\uFDFE-\uFE6F\uFEFD-\uFFFF',
          rtlChars        = '\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC',
          rtlDirCheck     = new RegExp('^[^'+ltrChars+']*['+rtlChars+']');

      return rtlDirCheck.test(s);
  };

customET.checkLetter = function(s) {
      var allChars        = 'A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02B8\u0300-\u0590\u0800-\u1FFF'+'\u2C00-\uFB1C\uFDFE-\uFE6F\uFEFD-\uFFFF'+'\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC',
          allCharsCheck     = new RegExp('['+allChars+']');

      return allCharsCheck.test(s);
  }

customET.setDirection = function(direction, newChar) {
      var currentNode = document.getSelection().anchorNode;
      var node = document.getSelection().anchorNode;
      if (node != null) {
          while (node.nodeName != "DIV") {
              node = node.parentNode;
          }
          var curDiv = node;
          if (curDiv.dir == direction) {
              // Direction is correct, do nothing
              return;
          }

          var curDivContentHtml = curDiv.innerHTML.trim();
          if ((curDiv.dir == '' || curDivContentHtml == '' || curDivContentHtml == newChar) && curDiv.id != "editor") {
              // Just update direction of current div (if not the root div)
              curDiv.dir = direction;
              return;
          }

          var curDivContentText = curDiv.innerText.trim();
          // Create a new div with appropriate direction
          var endIndex = curDivContentText.indexOf(newChar);
          var index = endIndex - 1;
          while (index >= 0) {
              if (curDivContentText.charCodeAt(index) == 10) {
                  break;
              }
              var letter = customET.checkLetter(curDivContentText.charAt(index));
              if (!letter) {
                  index--;
                  continue;
              }
              var rtl = customET.checkRTL(curDivContentText.charAt(index));
              if (direction == "ltr" && rtl == true) {
                  break;
              }
              else if (direction == "rtl" && rtl == false) {
                  break;
              }
              index--;
          }

          var newChars = curDivContentText.substring(index + 1, endIndex + 1);
          curDivContentHtml = curDivContentHtml.replace(newChars, '');
          curDiv.innerHTML = curDivContentHtml;

          var newDivContent = newChars;
          var div = document.createElement('div');
          div.dir = direction;
          div.innerHTML = newDivContent;
          curDiv.appendChild(div);
          customET.setCaretAtEnd(div);
      }
  }

customET.setCaretAtEnd = function(node) {
      var range = document.createRange();
      var sel = window.getSelection();
      range.selectNodeContents(node);
      range.collapse(false);
      sel.removeAllRanges();
      sel.addRange(range);
      node.focus();
  }

customET.setCaretAtStart = function(node) {
      var range = document.createRange();
      var sel = window.getSelection();
      range.selectNodeContents(node);
      range.collapse(true);
      sel.removeAllRanges();
      sel.addRange(range);
      node.focus();
}

customET.setCaretAtPosition = function(node, pos) {
      var range = document.createRange();
      var sel = window.getSelection();
      range.setStart(node, pos);
      range.collapse(true);
      sel.removeAllRanges();
      sel.addRange(range);
  }