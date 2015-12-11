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