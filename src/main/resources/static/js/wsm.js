    var tagfilter=[];
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == "filter") 
        {
            var filterParam = sParameterName[1];
            tagfilter = filterParam.split('+');
        }
    }
    
    console.log(tagfilter);



$(".openWS").click(function(e){
    e.preventDefault();
    var name = $(this).attr('href');
    AppPresenter.loadSection("editor");
});

$(".editWS").click(function(e) {
    e.preventDefault();
    var name = $(this).attr('href');
    WorkspaceManager.setSelectedWorkspace(name);
    showContentAsModal("app/modalWindows/editWorkspace", function() {
            var workspaceName = $("#modalCreationField input").val();
            var description = $("#descriptionInput textarea").val();
            $("#workspacesNavContainer li").removeClass("active");
            WorkspaceManager.updateWorkspace(workspaceName,description);
             $(location).attr('href',"app/wsm"); 
    });
});

$('#tagFilter').keypress(function (e) {
    if (e.which == 13) {
        e.preventDefault();
        var filter = $('#tagFilter').val().replace(/\ /g, '+');
        console.log('Enter was pressed');
        $(location).attr('href',"app/wsm?filter=" + filter); 
        return false;
    }
});


$('.tagSearch').each(function(){
    if($.inArray($(this).text(), tagfilter)>=0){
        $(this).addClass('btn-primary');
        var deleteFilter = tagfilter.slice(0);
        console.log(tagfilter);
        deleteFilter.splice($.inArray($(this).text(), deleteFilter),1); 
        $(this).attr("href", "app/wsm?filter="+deleteFilter.toString().replace(/\,/g,"+"));
    }
    else{
        $(this).addClass('btn-default');
        var addFilter = tagfilter.slice(0);
        console.log(tagfilter);
        console.log(addFilter);
        addFilter.push($(this).text());
        $(this).attr("href", "app/wsm?filter="+addFilter.toString().replace(/\,/g,"+"));
    }
    
});

$(".updateDemo").click(function(e){
    e.preventDefault();
    var name = $(this).attr('href');
    WorkspaceManager.updateDemoWorkspace(name,refreshPage);
});

$(".importDemo").click(function(e){
    e.preventDefault();
    var name = $(this).attr('href');
    WorkspaceManager.importNewDemoWorkspace(name,refreshPage);
});

            
$(".download-ws").click(function(e) {
    e.preventDefault();
    var name = $(this).attr('href');
    WorkspaceManager.downloadAsZip(name);
});

$(".delete-ws").click(function(e) {
    e.preventDefault();
    var name = $(this).attr('href');
    WorkspaceManager.deleteWorkspace(name,refreshPage);
});

$(".publish-demo").click(function(e) {
    e.preventDefault();
    var name = $(this).attr('href');     
    WorkspaceManager.publishWorskspaceAsDemo(name,refreshPage);
});

$(".delete-demo").click(function(e) {
    e.preventDefault();
    var name = $(this).attr('href');
    WorkspaceManager.deleteDemoWorkspace(name,refreshPage);
});

function refreshPage(){
    $(location).attr('href',"app/editor"); 
    location.reload();  
};

// external js: isotope.pkgd.js

// init Isotope
var $wscards = $('.cards.workspaces');
var $democards=$('.cards.demos')
var filters = [];
var wsFilters = [];
    wsFilters.push(".workspace");
    wsFilters.push(".demoworkspace");
    wsFilters.push(".publicdemo");
var tagsFilters = [];

$wscards.isotope({
  itemSelector: '.wholeCard',
  layoutMode: 'fitRows'
});

        
$('.wsFilterLink').click(function(){
   tagFilters=[];  
    if($(this).parent().hasClass('active_prot_menu')){
        $(this).parent().removeClass('active_prot_menu');        
    }
    else{
        $( this ).parent().addClass('active_prot_menu');      
    }    
    $('.wsFilterLink').each(function(){
        if($(this).parent().hasClass('active_prot_menu'))
            tagFilters.push($( this ).attr('data-filter-ws')); 
    });      
    var selector = tagFilters.join(', ');        
    $wscards.isotope({
        filter: selector,
        itemSelector: '.wholeCard'
    });

    return false;
});

$('.demoFilterLink').click(function(){       
    tagFilters=[];  
    if($(this).parent().hasClass('active_prot_menu')){
        $(this).parent().removeClass('active_prot_menu');        
    }
    else{
        $( this ).parent().addClass('active_prot_menu');      
    }    
    $('.demoFilterLink').each(function(){
        if($(this).parent().hasClass('active_prot_menu'))
            tagFilters.push($( this ).attr('data-filter-demo')); 
    });      
    var selector = tagFilters.join(', ');        
    $democards.isotope({
        filter: selector,
        itemSelector: '.wholeCard'
    });

    return false;
});

function remove(arr, item) {
      for(var i = arr.length; i--;) {
          if(arr[i] === item) {
              arr.splice(i, 1);
          }
      }
  }
