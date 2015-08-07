$(function(){
    console.log("start");
    /*var dragSrcEl = null;

    $('[draggable]').bind('dragstart', function(event){
        event.originalEvent.dataTransfer.setData('Text', this.id);

        dragSrcEl = this;

         event.originalEvent.dataTransfer.effectAllowed = 'move';
         event.originalEvent.dataTransfer.setData('text/html', this.innerHTML);
    });
    $('[draggable]').bind('dragover', function(event){
         if (event.preventDefault) {
                event.preventDefault(); // Necessary. Allows us to drop.
              }

              event.originalEvent.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.

              return false;
    });
    $('[draggable]').bind('dragenter', function(event){
         this.classList.add('over');
    });
    $('[draggable]').bind('dragleave', function(event){
         this.classList.remove('over');
    });
    $('[draggable]').bind('drop', function(event){
             if (event.stopPropagation) {
                 event.stopPropagation(); // stops the browser from redirecting.
               }

               // Don't do anything if dropping the same column we're dragging.
                 if (dragSrcEl != this) {
                   // Set the source column's HTML to the HTML of the column we dropped on.
                   dragSrcEl.innerHTML = this.innerHTML;
                   this.innerHTML = event.originalEvent.dataTransfer.getData('text/html');
                 }
               return false;
        });
        $('[draggable]').bind('dragend', function(event){
             $('[draggable]').removeClass('over');
        });*/

        $('.sortable').sortable();

        $('#todolist, #inprogresslist').sortable({
        	connectWith: '.connected'
        });
        $('#donelist, #inprogresslist').sortable({
            connectWith: '.connected'
        });
});