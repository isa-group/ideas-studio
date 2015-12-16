    var tagfilter=[]
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
    WorkspaceManager.setSelectedWorkspace(name);
    AppPresenter.loadSection("editor", name, function () {
        WorkspaceManager.loadWorkspace();
    });
});
$("#editWS").click(function(){	
    AppPresenter.setCurrentSection("wsm");
    AppPresenter.loadDynamically("#wsmMain", "workspace/edit")});

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
    //WorkspaceManager.deleteDemoWorkspace(name).then(location.reload());
    WorkspaceManager.deleteDemoWorkspace(name,refreshPage);
});

function refreshPage(){
    $(location).attr('href',"app/editor"); 
    location.reload();  
};

// external js: isotope.pkgd.js

// init Isotope
var $cards = $('.cards');
    filters = [];
  $cards.isotope({
  itemSelector: '.wholeCard',
  layoutMode: 'fitRows'
});

        
$('.filterLink').click(function(){
    var filterList = $( '.wsm-menu-members' );
    filterList.find('.active_prot_menu').removeClass('active_prot_menu');
    $( this ).parent().addClass('active_prot_menu');
    
    remove(filters, '.workspace');
    remove(filters, '.demoworkspace');
    remove(filters, '.publicdemo');

    var filterValue = $( this ).attr('data-filter-ws');
    
    filters.push(filterValue);
    
    
        var selector = filters.join('');
        $cards.isotope({
            filter: selector
        });

    
     return false;
});

$('.tagFilterLink').click(function(){
    
    var filterValue = $( this ).attr('data-filter-tag');
    
    if($(this).parent().hasClass('active_prot_menu')){
        $(this).parent().removeClass('active_prot_menu');
        remove(filters, filterValue);
    }
    else{
        $( this ).parent().addClass('active_prot_menu'); 
        filters.push(filterValue);
    }
    
    var selector = filters.join(',');
    $cards.isotope({
        filter: selector
    });

    return false;
});

//Collapsible project cards
$(".card__article_collapse").click(function () {
    if ($(this).children("article").text().toLowerCase().indexOf("Actions") >= 0) {
        $(this).children("article").text("Collapse");
    } else {
        $(this).children("article").text("Actions");
    }
    $(this).parent().children(".card__content.card__padding.collapsible").slideToggle({
        duration: 200,
        easing: "easeInOutSine",
        complete: function () {
            var projectsIsotope = $(this).parent().parent().parent().parent();
            projectsIsotope.isotope({
                itemSelector: ".wholeCard",
                layoutMode: "fitRows"
            });
            projectsIsotope.isotope('layout');
        }
    });
});

function remove(arr, item) {
      for(var i = arr.length; i--;) {
          if(arr[i] === item) {
              arr.splice(i, 1);
          }
      }
  }