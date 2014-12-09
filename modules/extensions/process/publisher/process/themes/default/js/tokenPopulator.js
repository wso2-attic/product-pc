   $(document).ready(function() {



     var url = "https://localhost:9443/publisher/asts/process/apis/processes?type=process";


     //Predecessors Loading
     $("#properties_predecessors").tokenInput(url, {
       preventDuplicates: true, theme:"facebook",

       onResult: function(results) {
         var assets = {
           data: []
         }
         $.each(results, function() {



           for (var i in results) {
             var item = results[i];

             assets.data.push({

               "path": item.path,
               "id": item.id,
               "name": item.attributes.overview_name
             });

           };
         });
         return assets.data;



         console.log('' + JSON.stringify(arguments));
       },
       tokenFormatter: function(item) {
         return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id + ">" + item.name + " </a></li>"
       }
     });



     //Sucessors Loading
     $("#properties_successors").tokenInput(url, {
       preventDuplicates: true,theme:"facebook",

       onResult: function(results) {
         var assets = {
           data: []
         }
         $.each(results, function() {



           for (var i in results) {
             var item = results[i];

             assets.data.push({

               "path": item.path,
               "id": item.id,
               "name": item.attributes.overview_name
             });

           };
         });
         return assets.data;



         console.log('' + JSON.stringify(arguments));
       },
       tokenFormatter: function(item) {
         return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id + ">" + item.name + " </a></li>"
       }
     });


     //Generaliztion Loading

     $("#properties_generalizations").tokenInput(url, {
       preventDuplicates: true,theme:"facebook",

       onResult: function(results) {
         var assets = {
           data: []
         }
         $.each(results, function() {



           for (var i in results) {
             var item = results[i];

             assets.data.push({

               "path": item.path,
               "id": item.id,
               "name": item.attributes.overview_name
             });

           };
         });
         return assets.data;



         console.log('' + JSON.stringify(arguments));
       },
       tokenFormatter: function(item) {
         return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id + ">" + item.name + " </a></li>"
       }
     });

     //specialization loading

     $("#properties_specializations").tokenInput(url, {
       preventDuplicates: true,theme:"facebook",

       onResult: function(results) {
         var assets = {
           data: []
         }
         $.each(results, function() {



           for (var i in results) {
             var item = results[i];

             assets.data.push({

               "path": item.path,
               "id": item.id,
               "name": item.attributes.overview_name
             });
           };
         });
         return assets.data;



         console.log('' + JSON.stringify(arguments));
       },
       tokenFormatter: function(item) {
         return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id + ">" + item.name + " </a></li>"
       }
     });



   });